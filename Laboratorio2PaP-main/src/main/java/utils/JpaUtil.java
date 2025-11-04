package utils;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

public final class JpaUtil {
    private static final String UNIT_NAME = "emf";
    private static final int MAX_RETRIES = 3;
    private static final long RETRY_DELAY_MS = Duration.ofSeconds(5).toMillis();
    private static volatile EntityManagerFactory EMF;

    private JpaUtil() {}

    private static EntityManagerFactory buildEmf() {
    // 1) Si hay DB_URL en variables de entorno, usarlo con DB_USER/DB_PASSWORD
    String directUrl = System.getenv("DB_URL");
    String directUser = System.getenv("DB_USER");
    String directPass = System.getenv("DB_PASSWORD");
    // Soporte Railway: convertir MYSQL_PUBLIC_URL -> JDBC si DB_URL no está definido
    if ((directUrl == null || directUrl.isBlank())) {
        String publicUrl = System.getenv("MYSQL_PUBLIC_URL");
        if (publicUrl != null && !publicUrl.isBlank()) {
            try {
                MysqlConn c = parseMysqlUrl(publicUrl.trim());
                directUrl = c.toJdbcUrl();
                if (directUser == null || directUser.isBlank()) directUser = c.user;
                if (directPass == null || directPass.isBlank()) directPass = c.pass;
                System.out.println("[JpaUtil] Derivado DB_URL desde MYSQL_PUBLIC_URL");
            } catch (Exception ignore) {
                // continúa con otras rutas
            }
        }
    }
    String prefer = env("DB_PREFER_SOURCE", "jndi"); // jndi | env | auto

    // 2) Intentar usar el DataSource JNDI de Tomcat si está disponible
    DataSource jndiDs = lookupDataSource("java:comp/env/jdbc/railway");

        // Config común de EclipseLink
        Map<String, Object> overrides = new HashMap<>();
        overrides.put("eclipselink.connection-pool.default.login-timeout", env("DB_LOGIN_TIMEOUT", "30"));
        overrides.put("eclipselink.connection-pool.default.max", env("DB_MAX_POOL", "20"));
        overrides.put("eclipselink.connection-pool.default.failover", "true");
        overrides.put("eclipselink.logging.level", env("DB_LOG_LEVEL", "INFO"));

        // Opcional: permitir activar DDL por variable de entorno (e.g. create-or-extend-tables)
        String ddl = env("DB_DDL", "none");
        if (!"none".equalsIgnoreCase(ddl)) {
            System.out.println("[JpaUtil] Activando DDL '" + ddl + "' por variable DB_DDL");
            System.out.println("[JpaUtil] Aviso: desactiva DB_DDL (none) tras crear el esquema para evitar ALTER duplicados en cada arranque.");
            overrides.put("eclipselink.ddl-generation", ddl);
            overrides.put("eclipselink.ddl-generation.output-mode", "database");
        }

        int attempt = 0;
        while (true) {
            try {
                // Preferencia: usar JNDI si está disponible (por defecto), salvo que DB_PREFER_SOURCE=env
                if (jndiDs != null && !"env".equalsIgnoreCase(prefer)) {
                    System.out.println("[JpaUtil] Usando DataSource JNDI 'jdbc/railway'. Probando conexión...");
                    testConnection(jndiDs);
                    overrides.put("jakarta.persistence.nonJtaDataSource", "java:comp/env/jdbc/railway");
                    overrides.put("eclipselink.jdbc.datasource", "java:comp/env/jdbc/railway");
                } else if (directUrl != null && !directUrl.isBlank()) {
                    System.out.println("[JpaUtil] Usando DB_URL de variables de entorno");
                    String url = directUrl.trim();
                    String user = (directUser == null) ? "" : directUser.trim();
                    String pass = (directPass == null) ? "" : directPass.trim();

                    overrides.put("jakarta.persistence.jdbc.url", url);
                    if (!user.isEmpty()) overrides.put("jakarta.persistence.jdbc.user", user);
                    if (!pass.isEmpty()) overrides.put("jakarta.persistence.jdbc.password", pass);
                    overrides.put("eclipselink.connection-pool.default.url", url);

                    testConnection(url, user, pass);
                } else {
                    // 2) Fallback: construir URL con variables de entorno
                    String host = env("RAILWAY_TCP_PROXY_DOMAIN",
                            env("RAILWAY_PRIVATE_DOMAIN",
                            env("MYSQLHOST", "localhost")));
                    String port = env("RAILWAY_TCP_PROXY_PORT", env("MYSQLPORT", "3306"));
                    String db   = env("MYSQL_DATABASE", env("MYSQLDATABASE", "railway"));
                    String user = env("MYSQLUSER", env("DB_USER", "root"));
                    String pass = env("MYSQL_ROOT_PASSWORD", env("MYSQLPASSWORD", env("DB_PASSWORD", "")));

                    String jdbcUrl = "jdbc:mysql://" + host + ":" + port + "/" + db
                            + "?serverTimezone=UTC"
                            + "&allowPublicKeyRetrieval=true"
                            + "&useSSL=false"
                            + "&characterEncoding=UTF-8"
                            + "&useUnicode=true"
                            + "&zeroDateTimeBehavior=convertToNull"
                            + "&connectTimeout=30000"
                            + "&socketTimeout=60000";

                    System.out.println("[JpaUtil] Usando JDBC por variables de entorno: " + host + ":" + port + "/" + db);

                    overrides.put("jakarta.persistence.jdbc.url", jdbcUrl);
                    overrides.put("jakarta.persistence.jdbc.user", user);
                    overrides.put("jakarta.persistence.jdbc.password", pass);
                    overrides.put("eclipselink.connection-pool.default.url", jdbcUrl);

                    // Validar conexión antes de inicializar EMF
                    testConnection(jdbcUrl, user, pass);
                }

                return Persistence.createEntityManagerFactory(UNIT_NAME, overrides);
            } catch (Exception ex) {
                attempt++;
                if (attempt >= MAX_RETRIES) {
                    throw ex;
                }
                try {
                    Thread.sleep(RETRY_DELAY_MS * attempt);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    throw new IllegalStateException("Inicialización de JPA interrumpida", ie);
                }
            }
        }
    }

    // Estructura simple para parsear mysql://user:pass@host:port/db
    private static class MysqlConn {
        final String user, pass, host, port, db;
        MysqlConn(String u, String p, String h, String po, String d){ user=u; pass=p; host=h; port=po; db=d; }
        String toJdbcUrl() {
            String p = (port==null||port.isBlank())?"3306":port;
            return "jdbc:mysql://"+host+":"+p+"/"+db
                    +"?serverTimezone=UTC&allowPublicKeyRetrieval=true&sslMode=PREFERRED&useSSL=true&characterEncoding=UTF-8&useUnicode=true";
        }
    }

    private static MysqlConn parseMysqlUrl(String url) {
        // Formato esperado: mysql://user:pass@host:port/db
        // Usamos java.net.URI para extraer componentes
        try {
            java.net.URI u = new java.net.URI(url.replaceFirst("^mysql://","http://"));
            String userInfo = u.getUserInfo();
            String user = userInfo != null && userInfo.contains(":") ? userInfo.substring(0, userInfo.indexOf(':')) : userInfo;
            String pass = userInfo != null && userInfo.contains(":") ? userInfo.substring(userInfo.indexOf(':')+1) : "";
            String host = u.getHost();
            int p = u.getPort();
            String port = p > 0 ? Integer.toString(p) : "3306";
            String path = u.getPath();
            String db = (path != null && path.length() > 1) ? path.substring(1) : "railway";
            return new MysqlConn(user, pass, host, port, db);
        } catch (Exception e) {
            throw new IllegalArgumentException("URL MySQL inválida: " + url, e);
        }
    }

    private static void testConnection(String url, String user, String password) {
        try (Connection ignored = DriverManager.getConnection(url, user, password)) {
            // OK
        } catch (SQLException ex) {
            throw new RuntimeException("Fallo de conexión JDBC: " + ex.getMessage(), ex);
        }
    }

    private static void testConnection(DataSource ds) {
        try (Connection ignored = ds.getConnection()) {
            // OK
        } catch (SQLException ex) {
            throw new RuntimeException("Fallo de conexión (JNDI DS): " + ex.getMessage(), ex);
        }
    }

    private static DataSource lookupDataSource(String jndiName) {
        try {
            InitialContext ctx = new InitialContext();
            Object obj = ctx.lookup(jndiName);
            if (obj instanceof DataSource) {
                return (DataSource) obj;
            }
            return null;
        } catch (NamingException e) {
            return null;
        }
    }

    private static String env(String key, String fallback) {
        String v = System.getenv(key);
        return (v == null || v.isBlank()) ? fallback : v.trim();
    }

    public static EntityManager getEntityManager() {
        // Inicialización perezosa y tolerante a fallos transitorios
        if (EMF == null) {
            synchronized (JpaUtil.class) {
                if (EMF == null) {
                    EMF = buildEmf();
                }
            }
        }
        return EMF.createEntityManager();
    }
}
