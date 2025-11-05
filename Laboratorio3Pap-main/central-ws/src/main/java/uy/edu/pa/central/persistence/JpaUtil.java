package uy.edu.pa.central.persistence;

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
    private static final String UNIT_NAME = "emf"; // Debe coincidir con el override de persistence.xml
    private static final int MAX_RETRIES = 3;
    private static final long RETRY_DELAY_MS = Duration.ofSeconds(5).toMillis();
    private static volatile EntityManagerFactory EMF;

    private JpaUtil() {}

    public static EntityManager getEntityManager() {
        if (EMF == null) {
            synchronized (JpaUtil.class) {
                if (EMF == null) {
                    EMF = buildEmf();
                }
            }
        }
        return EMF.createEntityManager();
    }

    private static EntityManagerFactory buildEmf() {
        String directUrl = System.getenv("DB_URL");
        String directUser = System.getenv("DB_USER");
        String directPass = System.getenv("DB_PASSWORD");

        DataSource jndiDs = lookupDataSource("java:comp/env/jdbc/railway");
        DataSource effectiveDs = (jndiDs == null) ? null : new SafeDataSource(jndiDs);

        Map<String, Object> overrides = new HashMap<>();
        overrides.put("eclipselink.logging.level", env("DB_LOG_LEVEL", "INFO"));

        int attempt = 0;
        while (true) {
            try {
                if (effectiveDs != null) {
                    testConnection(effectiveDs);
                    overrides.put("jakarta.persistence.nonJtaDataSource", effectiveDs);
                    overrides.put("eclipselink.jdbc.datasource", effectiveDs);
                } else if (directUrl != null && !directUrl.isBlank()) {
                    String url = directUrl.trim();
                    String user = (directUser == null) ? "" : directUser.trim();
                    String pass = (directPass == null) ? "" : directPass.trim();
                    overrides.put("jakarta.persistence.jdbc.url", url);
                    if (!user.isEmpty()) overrides.put("jakarta.persistence.jdbc.user", user);
                    if (!pass.isEmpty()) overrides.put("jakarta.persistence.jdbc.password", pass);
                    testConnection(url, user, pass);
                }
                return Persistence.createEntityManagerFactory(UNIT_NAME, overrides);
            } catch (Exception ex) {
                attempt++;
                if (attempt >= MAX_RETRIES) throw ex;
                try { Thread.sleep(RETRY_DELAY_MS * attempt); } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    throw new IllegalStateException("Inicialización de JPA interrumpida", ie);
                }
            }
        }
    }

    private static void testConnection(DataSource ds) {
        try (Connection ignored = ds.getConnection()) {
            // OK
        } catch (SQLException ex) {
            throw new RuntimeException("Fallo de conexión (JNDI DS): " + ex.getMessage(), ex);
        }
    }

    private static void testConnection(String url, String user, String password) {
        try (Connection ignored = DriverManager.getConnection(url, user, password)) {
            // OK
        } catch (SQLException ex) {
            throw new RuntimeException("Fallo de conexión JDBC: " + ex.getMessage(), ex);
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
}
