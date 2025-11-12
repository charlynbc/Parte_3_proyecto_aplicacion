package uy.edu.pa.central.ws;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.persistence.EntityManager;
import uy.edu.pa.central.persistence.JpaUtil;

@WebServlet(name = "DatabaseMigrationServlet", urlPatterns = {"/migrate"}, loadOnStartup = 1)
public class DatabaseMigrationServlet extends HttpServlet {
    
    @Override
    public void init() throws ServletException {
        super.init();
        System.out.println("[DatabaseMigration] Iniciando migración de base de datos...");
        
        EntityManager em = null;
        try {
            em = JpaUtil.getEntityManager();
            
            // Verificar y agregar columna imagen a tabla actividad
            if (!columnExists(em, "actividad", "imagen")) {
                em.getTransaction().begin();
                try {
                    em.createNativeQuery("ALTER TABLE actividad ADD COLUMN imagen VARCHAR(5000) NULL").executeUpdate();
                    em.getTransaction().commit();
                    System.out.println("[DatabaseMigration] ✅ Columna 'imagen' agregada a 'actividad' exitosamente");
                } catch (Exception e) {
                    em.getTransaction().rollback();
                    System.out.println("[DatabaseMigration] ❌ Error al agregar columna 'imagen' a 'actividad': " + e.getMessage());
                }
            } else {
                System.out.println("[DatabaseMigration] ℹ️ La columna 'imagen' ya existe en 'actividad'");
            }
            
            // Verificar y agregar columna imagen a tabla usuarios
            if (!columnExists(em, "usuarios", "imagen")) {
                em.getTransaction().begin();
                try {
                    em.createNativeQuery("ALTER TABLE usuarios ADD COLUMN imagen VARCHAR(5000) NULL").executeUpdate();
                    em.getTransaction().commit();
                    System.out.println("[DatabaseMigration] ✅ Columna 'imagen' agregada a 'usuarios' exitosamente");
                } catch (Exception e) {
                    em.getTransaction().rollback();
                    System.out.println("[DatabaseMigration] ❌ Error al agregar columna 'imagen' a 'usuarios': " + e.getMessage());
                }
            } else {
                System.out.println("[DatabaseMigration] ℹ️ La columna 'imagen' ya existe en 'usuarios'");
            }
            
            System.out.println("[DatabaseMigration] Migración completada");
            
        } catch (Exception e) {
            System.err.println("[DatabaseMigration] ❌ Error durante la migración: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }
    
    private boolean columnExists(EntityManager em, String tableName, String columnName) {
        try {
            // Consulta para verificar si la columna existe en MySQL
            String query = "SELECT COUNT(*) FROM information_schema.COLUMNS " +
                          "WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = ? AND COLUMN_NAME = ?";
            Object result = em.createNativeQuery(query)
                             .setParameter(1, tableName)
                             .setParameter(2, columnName)
                             .getSingleResult();
            return ((Number) result).intValue() > 0;
        } catch (Exception e) {
            System.out.println("[DatabaseMigration] Error al verificar columna " + columnName + " en " + tableName + ": " + e.getMessage());
            return false;
        }
    }
}
