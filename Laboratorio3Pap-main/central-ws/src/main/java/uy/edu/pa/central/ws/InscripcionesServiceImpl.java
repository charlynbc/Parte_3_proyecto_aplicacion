package uy.edu.pa.central.ws;

import jakarta.jws.WebService;
import uy.edu.pa.central.ws.dtos.InscripcionDTO;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import uy.edu.pa.central.persistence.JpaUtil;
import logica.Inscripcion;
import logica.Turista;
import logica.Salida;

@WebService(
    serviceName = "InscripcionesService",
    endpointInterface = "uy.edu.pa.central.ws.InscripcionesService",
    portName = "InscripcionesServicePort",
    targetNamespace = "http://ws.central.pa.edu.uy/"
)
public class InscripcionesServiceImpl implements InscripcionesService {

    @Override
    public boolean inscribirTurista(String turista, String salida, int cantidad, String fecha) {
        EntityManager em = null;
        try {
            em = JpaUtil.getEntityManager();
            em.getTransaction().begin();
            
            Turista tur = em.find(Turista.class, turista);
            if (tur == null) {
                em.getTransaction().rollback();
                return false;
            }
            
            TypedQuery<Salida> salQuery = em.createQuery(
                "SELECT s FROM Salida s WHERE s.nombre = :nombre", Salida.class);
            salQuery.setParameter("nombre", salida);
            List<Salida> salidas = salQuery.getResultList();
            
            if (salidas.isEmpty()) {
                em.getTransaction().rollback();
                return false;
            }
            
            Salida sal = salidas.get(0);
            
            // Verificar cupos disponibles
            TypedQuery<Number> countQuery = em.createQuery(
                "SELECT COALESCE(SUM(i.cantTuristas), 0) FROM Inscripcion i WHERE i.salida = :salida", 
                Number.class);
            countQuery.setParameter("salida", sal);
            Number inscriptosNumber = countQuery.getSingleResult();
            int inscriptos = inscriptosNumber != null ? inscriptosNumber.intValue() : 0;
            
            if (inscriptos + cantidad > sal.getTuristasMax()) {
                em.getTransaction().rollback();
                return false;
            }
            
            Inscripcion insc = new Inscripcion();
            insc.setTurista(tur);
            insc.setSalida(sal);
            insc.setCantTuristas(cantidad);
            
            if (fecha != null && !fecha.isBlank()) {
                java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
                insc.setFechaInscripcion(sdf.parse(fecha));
            } else {
                insc.setFechaInscripcion(new java.util.Date());
            }
            
            // Calcular costo
            float costoActividad = sal.getActividad().getCosto();
            insc.setCosto(costoActividad * cantidad);
            
            em.persist(insc);
            em.getTransaction().commit();
            
            return true;
            
        } catch (Exception e) {
            if (em != null && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            return false;
        } finally {
            if (em != null && em.isOpen()) em.close();
        }
    }

    @Override
    public List<InscripcionDTO> listarInscripcionesPorTurista(String turista) {
        List<InscripcionDTO> result = new ArrayList<>();
        if (turista == null || turista.isBlank()) return result;
        
        EntityManager em = null;
        try {
            em = JpaUtil.getEntityManager();
            TypedQuery<Inscripcion> query = em.createQuery(
                "SELECT i FROM Inscripcion i WHERE i.turista.nickname = :nickname ORDER BY i.fechaInscripcion DESC", 
                Inscripcion.class);
            query.setParameter("nickname", turista);
            List<Inscripcion> inscripciones = query.getResultList();
            
            java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("dd/MM/yyyy");
            
            for (Inscripcion i : inscripciones) {
                InscripcionDTO dto = new InscripcionDTO();
                dto.setSalidaNombre(i.getSalida().getNombre());
                dto.setActividadNombre(i.getSalida().getActividad().getNombre());
                dto.setLugar(i.getSalida().getLugar());
                if (i.getFechaInscripcion() != null) {
                    dto.setFechaInscripcion(df.format(i.getFechaInscripcion()));
                }
                dto.setCantidad(i.getCantTuristas());
                dto.setCosto(i.getCosto());
                result.add(dto);
            }
        } catch (Exception e) {
            System.err.println("[InscripcionesService] Error listando inscripciones: " + e.getMessage());
        } finally {
            if (em != null && em.isOpen()) em.close();
        }
        return result;
    }
}
