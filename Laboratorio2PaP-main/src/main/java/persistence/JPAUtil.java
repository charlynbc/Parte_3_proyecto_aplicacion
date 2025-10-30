package persistence;

import jakarta.persistence.EntityManager;

/**
 * Implementación que anula el stub de Laboratorio1.jar.
 * Delegamos en utils.JpaUtil del webapp para obtener un EntityManager real.
 */
public final class JPAUtil {
    private JPAUtil() {}

    public static EntityManager getEntityManager() {
        return utils.JpaUtil.getEntityManager();
    }
}
