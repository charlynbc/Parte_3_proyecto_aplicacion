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
    private EntityManagerFactory emf;

    private ManejadorSalida(){
        emf = Persistence.createEntityManagerFactory("emf");
    }

    public static ManejadorSalida getinstance(){
        if(instancia == null)
            return new ManejadorSalida();
        return instancia;
    }

    public void addSalida(Salida sal){
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.persist(sal);
        em.getTransaction().commit();
        em.close();
    }

    public Salida obtenerSalida(String nombre){
        EntityManager em = emf.createEntityManager();
        Salida Salida = em.find(Salida.class, nombre);
        em.close();
        return Salida;
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