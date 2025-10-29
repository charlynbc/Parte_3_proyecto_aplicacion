package logica;

import java.util.List;
import jakarta.persistence.*;
import persistence.JPAUtil;


public class ManejadorActividad {
    private static ManejadorActividad instancia = null;
    private EntityManagerFactory emf;

    private ManejadorActividad() {
        emf = Persistence.createEntityManagerFactory("emf");
    }

    public static ManejadorActividad getinstance() {
        if (instancia == null)
            instancia = new ManejadorActividad();
        return instancia;
    }

    public void addActividad(Actividad act) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        
        try {
            tx.begin();
            em.persist(act);
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }
    
    public Actividad obtenerActividad(String nombre) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            Actividad actividad = em.find(Actividad.class, nombre);
            return actividad;
        } finally {
            em.close();
        }
    }
    
    public Actividad[] obtenerActividadesPorProveedor(String nickname) {
    	
    	EntityManager em = JPAUtil.getEntityManager();
    	try {
			String jpql = "SELECT a FROM Actividad a WHERE a.proveedor.nickname = :nickname";
			List<Actividad> actividades = em.createQuery(jpql, Actividad.class)
					.setParameter("nickname", nickname)
					.getResultList();
			return actividades.toArray(new Actividad[0]);
		} finally {
			em.close();
		}
    	
    }

    public Actividad[] getActividades() {
        EntityManager em = JPAUtil.getEntityManager();
        
        try {
            List<Actividad> actividades = em.createQuery("SELECT a FROM Actividad a", Actividad.class).getResultList();
            return actividades.toArray(new Actividad[0]);
        } finally {
            em.close();
        }
    }

    // Agregar salida a una actividad
    public void addSalidaToActividad(String nombreActividad, Salida salida) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        
        try {
            tx.begin();
            Actividad act = em.find(Actividad.class, nombreActividad);
            if (act != null) {
                act.agregarSalida(salida);   // este método lo tenés que definir en Actividad
                em.persist(salida);          // persistir la nueva salida
                em.merge(act);               // actualizar la actividad con la salida asociada
            }
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    public DataActividadRanking[] rankingActividades(){
        EntityManager em = JPAUtil.getEntityManager();
        try {
            List<Object[]> rows = em.createQuery(
                "SELECT a.nombre, COUNT(s) FROM Actividad a LEFT JOIN a.salidas s GROUP BY a.nombre ORDER BY COUNT(s) DESC",
                Object[].class).getResultList();
            DataActividadRanking[] res = new DataActividadRanking[rows.size()];
            for(int i=0;i<rows.size();i++){
                Object[] r = rows.get(i);
                String nombre = (String) r[0];
                Long cant = (Long) r[1];
                res[i] = new DataActividadRanking(nombre, cant==null?0L:cant.longValue());
            }
            return res;
        } finally {
            em.close();
        }
    }

    // --- Métodos de modificación de campos de Actividad ---
    private void modificarActividad(String nombre, java.util.function.Consumer<Actividad> setter) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Actividad act = em.find(Actividad.class, nombre);
            if (act != null) setter.accept(act);
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    public void modDescripcion(Actividad act, String descripcion) { modificarActividad(act.getNombre(), a -> a.setDescripcion(descripcion)); }
    public void modDuracion(Actividad act, int duracion) { modificarActividad(act.getNombre(), a -> a.setDuracion(duracion)); }
    public void modCosto(Actividad act, float costo) { modificarActividad(act.getNombre(), a -> a.setCosto(costo)); }
    public void modCiudad(Actividad act, String ciudad) { modificarActividad(act.getNombre(), a -> a.setCiudad(ciudad)); }
    public void modEstado(Actividad act, String estado) { modificarActividad(act.getNombre(), a -> a.setEstado(estado)); }

    public Actividad[] obtenerActividadesPorTurista(String nicknameTurista) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            String jpql = "SELECT DISTINCT a FROM Actividad a JOIN a.salidas s JOIN s.inscripciones i WHERE i.turista.nickname = :nickname";
            List<Actividad> actividades = em.createQuery(jpql, Actividad.class)
                .setParameter("nickname", nicknameTurista)
                .getResultList();
            return actividades.toArray(new Actividad[0]);
        } finally {
            em.close();
        }
    }
}