package logica;

import java.util.List;
import jakarta.persistence.*;
import persistence.JPAUtil;

public class ManejadorInscripcion {
    private static ManejadorInscripcion instancia = null;

    private ManejadorInscripcion() {}

    public static ManejadorInscripcion getinstance() {
        if (instancia == null) {
            instancia = new ManejadorInscripcion();
        }
        return instancia;
    }

    public void addInscripcion(Inscripcion inscripcion) {
    	
    	EntityManager em = JPAUtil.getEntityManager();
    	
    	try {
    		em.getTransaction().begin();
    		em.persist(inscripcion);
    		em.getTransaction().commit();
    	}catch(Exception e){
    		if(em.getTransaction().isActive()) em.getTransaction().rollback();
    		e.printStackTrace(); // Mostrar el stack trace para diagnosticar
            throw new RuntimeException("Error al incribir usuario: " + e.getMessage());
    	}finally {
    		em.close();
    	}

    }

    public Inscripcion[] getInscripcionesDeSalida(String nombreSalida) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            Salida salida = em.find(Salida.class, nombreSalida);
            if (salida == null) {
                return new Inscripcion[0];
            }
            List<Inscripcion> ins = em.createQuery(
                "SELECT i FROM Inscripcion i WHERE i.salida = :salida",
                Inscripcion.class
            ).setParameter("salida", salida).getResultList();
            return ins.toArray(new Inscripcion[0]);
        } finally {
            em.close();
        }
    }

    
    public Inscripcion[] getInscripcionesPorTurista(String nickname) {
    	
    	EntityManager em = JPAUtil.getEntityManager();
    	
    	try {
    		String jpql = "SELECT i FROM Inscripcion i WHERE i.turista.nickname = :nickname";
    		List<Inscripcion> inscripciones = em.createQuery(jpql, Inscripcion.class)
    				.setParameter("nickname", nickname)
    				.getResultList();
    		return inscripciones.toArray(new Inscripcion[0]);
    	}finally {
    		em.close();
    	}
    }
	
    public Inscripcion[] getInscripciones() {
    	
    	EntityManager em = JPAUtil.getEntityManager();
    	
    	try {
    		List<Inscripcion> inscripciones = em.createQuery("SELECT i FROM Inscripcion i", Inscripcion.class).getResultList();
    		return inscripciones.toArray(new Inscripcion[0]);
    	}finally {
    		em.close();
    	}
    }
    
    public boolean inscripcionExistente(String turista, String salida) {
    	
    	
		Inscripcion[] inscripciones = getInscripciones();
		
		for(Inscripcion inscripcion : inscripciones) {
			if(inscripcion.getTurista().getNickname() == turista && inscripcion.getSalida().getNombre() == salida) {
				return true;
			}
		}
		
		return false;
	
    	
    }
    
    public int obtenerCantidadInscriptos(String nombreSalida) {
    		
    	Inscripcion[] inscripciones = getInscripciones();
    	
    	int toRtn = 0;
    	
    	for(Inscripcion inscripcion : inscripciones) {
    		if(inscripcion.getSalida().getNombre().equals(nombreSalida)) {
    			toRtn += inscripcion.getCantTuristas();
    		}
    	}
    	
    	return toRtn;
    	
    }
    
}