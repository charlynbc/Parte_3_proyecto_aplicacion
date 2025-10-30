package utils;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class JpaUtil {
    private static final String UNIT_NAME = "emf";
    private static final int MAX_RETRIES = 3;
    private static final long RETRY_DELAY_MS = Duration.ofSeconds(5).toMillis();
    private static final EntityManagerFactory EMF = buildEmf();

    private JpaUtil() {}

    private static EntityManagerFactory buildEmf() {
        Map<String, Object> overrides = new HashMap<>();
        String jdbcUrl = env("DB_URL", "jdbc:mysql://autorack.proxy.rlwy.net:13046/railway?serverTimezone=UTC&autoReconnect=true&allowPublicKeyRetrieval=true&useSSL=true&connectTimeout=30000&socketTimeout=60000");
        String jdbcUser = env("DB_USER", "root");
        String jdbcPassword = env("DB_PASSWORD", "OqlJKLJHzKixCFCeqgVSKjNJzpAXspeQ");
        overrides.put("jakarta.persistence.jdbc.url", jdbcUrl);
        overrides.put("jakarta.persistence.jdbc.user", jdbcUser);
        overrides.put("jakarta.persistence.jdbc.password", jdbcPassword);
        overrides.put("eclipselink.connection-pool.default.url", jdbcUrl);
        overrides.put("eclipselink.connection-pool.default.login-timeout", env("DB_LOGIN_TIMEOUT", "30"));
        overrides.put("eclipselink.connection-pool.default.max", env("DB_MAX_POOL", "20"));
        overrides.put("eclipselink.connection-pool.default.failover", "true");
        overrides.put("eclipselink.logging.level", env("DB_LOG_LEVEL", "INFO"));

        int attempt = 0;
        while (true) {
            try {
                // Validar conexión antes de inicializar EMF
                testConnection(jdbcUrl, jdbcUser, jdbcPassword);
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

    private static String env(String key, String fallback) {
        String value = System.getenv(key);
        return (value == null || value.isBlank()) ? fallback : value.trim();
    }

    // Cambiar la firma: no propagar SQLException
    private static void testConnection(String url, String user, String password) {
        try (Connection ignored = DriverManager.getConnection(url, user, password)) {
            // conexión OK
        } catch (SQLException ex) {
            // Convertir a RuntimeException para que el compilador no exija throws
            throw new RuntimeException("Fallo de conexión JDBC: " + ex.getMessage(), ex);
        }
    }

    public static EntityManager getEntityManager() {
        return EMF.createEntityManager();
    }
}
