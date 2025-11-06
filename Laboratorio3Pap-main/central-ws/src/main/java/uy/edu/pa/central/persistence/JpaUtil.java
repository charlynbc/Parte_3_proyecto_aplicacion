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
        // Usar directamente el persistence.xml sin overrides
        Map<String, Object> overrides = new HashMap<>();
        overrides.put("eclipselink.logging.level", "INFO");
        
        try {
            return Persistence.createEntityManagerFactory(UNIT_NAME, overrides);
        } catch (Exception ex) {
            System.err.println("[JpaUtil] Error creando EntityManagerFactory: " + ex.getMessage());
            ex.printStackTrace();
            throw new RuntimeException("No se pudo crear el EntityManagerFactory", ex);
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
