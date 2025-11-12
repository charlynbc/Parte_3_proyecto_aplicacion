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
        System.out.println("[InscripcionesService] === INICIO INSCRIPCIÓN ===");
        System.out.println("[InscripcionesService] Turista: " + turista);
        System.out.println("[InscripcionesService] Salida: " + salida);
        System.out.println("[InscripcionesService] Cantidad: " + cantidad);
        System.out.println("[InscripcionesService] Fecha: " + fecha);
        
        EntityManager em = null;
        try {
            em = JpaUtil.getEntityManager();
            System.out.println("[InscripcionesService] EntityManager obtenido");
            em.getTransaction().begin();
            System.out.println("[InscripcionesService] Transacción iniciada");
            
            Turista tur = em.find(Turista.class, turista);
            if (tur == null) {
                System.err.println("[InscripcionesService] Turista no encontrado: " + turista);
                em.getTransaction().rollback();
                return false;
            }
            System.out.println("[InscripcionesService] Turista encontrado: " + tur.getNickname());
            
            TypedQuery<Salida> salQuery = em.createQuery(
                "SELECT s FROM Salida s WHERE s.nombre = :nombre", Salida.class);
            salQuery.setParameter("nombre", salida);
            List<Salida> salidas = salQuery.getResultList();
            
            if (salidas.isEmpty()) {
                System.err.println("[InscripcionesService] Salida no encontrada: " + salida);
                em.getTransaction().rollback();
                return false;
            }
            
            Salida sal = salidas.get(0);
            System.out.println("[InscripcionesService] Salida encontrada: " + sal.getNombre() + " (ID en DB: " + sal.getNombre() + ")");
            
            // Verificar cupos disponibles (usar Number para compatibilidad con diferentes tipos)
            TypedQuery<Number> countQuery = em.createQuery(
                "SELECT COALESCE(SUM(i.cantTuristas), 0) FROM Inscripcion i WHERE i.salida = :salida", 
                Number.class);
            countQuery.setParameter("salida", sal);
            Number inscriptosNumber = countQuery.getSingleResult();
            int inscriptos = inscriptosNumber != null ? inscriptosNumber.intValue() : 0;
            
            System.out.println("[InscripcionesService] Cupos - Max: " + sal.getTuristasMax() + ", Inscriptos: " + inscriptos + ", Solicitados: " + cantidad);
            
            if (inscriptos + cantidad > sal.getTuristasMax()) {
                System.err.println("[InscripcionesService] No hay cupos suficientes");
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
            
            System.out.println("[InscripcionesService] Inscripción preparada:");
            System.out.println("[InscripcionesService]   - Turista: " + insc.getTurista().getNickname());
            System.out.println("[InscripcionesService]   - Salida: " + insc.getSalida().getNombre());
            System.out.println("[InscripcionesService]   - Cantidad: " + insc.getCantTuristas());
            System.out.println("[InscripcionesService]   - Costo: " + insc.getCosto());
            
            em.persist(insc);
            System.out.println("[InscripcionesService] Persist ejecutado");
            em.getTransaction().commit();
            System.out.println("[InscripcionesService] Commit exitoso");
            
            System.out.println("[InscripcionesService] Inscripción creada para turista: " + turista);
            return true;
            
        } catch (Exception e) {
            if (em != null && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            System.err.println("[InscripcionesService] Error inscribiendo turista: " + e.getMessage());
            e.printStackTrace();
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
