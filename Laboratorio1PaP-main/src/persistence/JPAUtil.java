package persistence;

import jakarta.persistence.EntityManager;

/**
 * Stub JPAUtil usado solo para permitir compilación local.
 * En producción debe devolver un EntityManager real desde un EntityManagerFactory.
 */
public class JPAUtil {
    public static EntityManager getEntityManager() {
        // Retornamos null como stub; el código que use esto en runtime puede fallar,
        // pero permite compilar las clases que llaman a JPAUtil.getEntityManager().
        return null;
    }
}
