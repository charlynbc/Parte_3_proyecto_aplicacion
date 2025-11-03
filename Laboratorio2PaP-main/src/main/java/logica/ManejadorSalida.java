package logica;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Date;
import jakarta.persistence.*;
import persistence.JPAUtil;

public class ManejadorSalida{
    private static ManejadorSalida instancia = null;
    // JPA manejado centralmente a través de `persistence.JPAUtil` (delegando a utils.JpaUtil)
    private ManejadorSalida(){
        // constructor vacío
    }

    public static ManejadorSalida getinstance(){
        if(instancia == null)
            return new ManejadorSalida();
        return instancia;
    }

    public void addSalida(Salida sal){
        EntityManager em = persistence.JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(sal);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    public Salida obtenerSalida(String nombre){
        EntityManager em = persistence.JPAUtil.getEntityManager();
        try {
            return em.find(Salida.class, nombre);
        } finally {
            em.close();
        }
    }

    public Salida[] getSalida() { // todas las salidas
        EntityManager em = JPAUtil.getEntityManager();
        try {
            List<Salida> salidas = em.createQuery("SELECT s FROM Salida s", Salida.class).getResultList();
            return salidas.toArray(new Salida[0]);
        } finally {
            em.close();
        }
    }

    public Salida[] getSalidasDeActividad(String nombreActividad) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            List<Salida> salidas = em.createQuery(
                    "SELECT s FROM Salida s WHERE s.actividad.nombre = :nombre", Salida.class)
                    .setParameter("nombre", nombreActividad)
                    .getResultList();
            return salidas.toArray(new Salida[0]);
        } finally {
            em.close();
        }
    }


    
}