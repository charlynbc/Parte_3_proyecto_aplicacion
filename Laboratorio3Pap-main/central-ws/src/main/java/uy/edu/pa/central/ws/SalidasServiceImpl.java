package uy.edu.pa.central.ws;

import jakarta.jws.WebService;
import uy.edu.pa.central.ws.dtos.SalidaDTO;
import uy.edu.pa.central.ws.dtos.InscripcionDTO;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import uy.edu.pa.central.persistence.JpaUtil;
import logica.Salida;
import logica.Inscripcion;

@WebService(
    serviceName = "SalidasService",
    endpointInterface = "uy.edu.pa.central.ws.SalidasService",
    portName = "SalidasServicePort",
    targetNamespace = "http://ws.central.pa.edu.uy/"
)
public class SalidasServiceImpl implements SalidasService {

    @Override
    public boolean crearSalida(String nombre, String fecha, String hora, String lugar, 
                                int cantMax, String fechaAlta, String actividad) {
        EntityManager em = null;
        try {
            em = JpaUtil.getEntityManager();
            em.getTransaction().begin();
            
            logica.Actividad act = em.find(logica.Actividad.class, actividad);
            if (act == null) {
                System.err.println("[SalidasService] Actividad no encontrada: " + actividad);
                return false;
            }
            
            logica.Salida sal = new logica.Salida();
            sal.setNombre(nombre);
            sal.setLugar(lugar);
            sal.setTuristasMax(cantMax);
            sal.setActividad(act);
            
            if (fecha != null && !fecha.isBlank()) {
                java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
                sal.setFecha(sdf.parse(fecha));
            }
            
            if (hora != null && !hora.isBlank()) {
                java.time.LocalTime lt = java.time.LocalTime.parse(hora);
                sal.setHora(lt);
            }
            
            if (fechaAlta != null && !fechaAlta.isBlank()) {
                java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
                sal.setFechaAlta(sdf.parse(fechaAlta));
            }
            
            em.persist(sal);
            em.getTransaction().commit();
            
            System.out.println("[SalidasService] Salida creada: " + nombre);
            return true;
            
        } catch (Exception e) {
            if (em != null && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            System.err.println("[SalidasService] Error creando salida: " + e.getMessage());
            return false;
        } finally {
            if (em != null && em.isOpen()) em.close();
        }
    }

    @Override
    public SalidaDTO obtenerSalida(String id) {
        if (id == null || id.isBlank()) return null;
        
        EntityManager em = null;
        try {
            em = JpaUtil.getEntityManager();
            TypedQuery<Salida> query = em.createQuery(
                "SELECT s FROM Salida s WHERE s.nombre = :nombre", Salida.class);
            query.setParameter("nombre", id);
            List<Salida> list = query.getResultList();
            
            if (list.isEmpty()) return null;
            
            Salida sal = list.get(0);
            java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("dd/MM/yyyy");
            
            SalidaDTO dto = new SalidaDTO();
            dto.setId(sal.getNombre());
            if (sal.getFecha() != null) {
                dto.setFecha(df.format(sal.getFecha()));
            }
            if (sal.getHora() != null) {
                dto.setHora(sal.getHora().toString());
            }
            dto.setLugar(sal.getLugar());
            dto.setTuristasMax(sal.getTuristasMax());
            dto.setImagen(sal.getImagen());
            if (sal.getActividad() != null) {
                dto.setActividadNombre(sal.getActividad().getNombre());
            }
            
            return dto;
        } catch (Exception e) {
            System.err.println("[SalidasService] Error obteniendo salida: " + e.getMessage());
            return null;
        } finally {
            if (em != null && em.isOpen()) em.close();
        }
    }

    @Override
    public List<SalidaDTO> listarSalidasDeActividad(String actividad) {
        List<SalidaDTO> result = new ArrayList<>();
        if (actividad == null || actividad.isBlank()) return result;
        
        EntityManager em = null;
        try {
            em = JpaUtil.getEntityManager();
            TypedQuery<Salida> query = em.createQuery(
                "SELECT s FROM Salida s WHERE s.actividad.nombre = :nombre ORDER BY s.fecha DESC", 
                Salida.class);
            query.setParameter("nombre", actividad);
            List<Salida> salidas = query.getResultList();
            
            java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("dd/MM/yyyy");
            
            for (Salida s : salidas) {
                SalidaDTO dto = new SalidaDTO();
                dto.setId(s.getNombre());
                if (s.getFecha() != null) {
                    dto.setFecha(df.format(s.getFecha()));
                }
                if (s.getHora() != null) {
                    dto.setHora(s.getHora().toString());
                }
                dto.setLugar(s.getLugar());
                dto.setTuristasMax(s.getTuristasMax());
                dto.setImagen(s.getImagen());
                if (s.getActividad() != null) {
                    dto.setActividadNombre(s.getActividad().getNombre());
                }
                result.add(dto);
            }
        } catch (Exception e) {
            System.err.println("[SalidasService] Error listando salidas: " + e.getMessage());
        } finally {
            if (em != null && em.isOpen()) em.close();
        }
        return result;
    }

    @Override
    public boolean modificarSalida(String nombre, String fecha, String hora, String lugar, 
                                  int cantMax, String imagenBase64) {
        if (nombre == null || nombre.isBlank()) return false;
        
        EntityManager em = null;
        try {
            em = JpaUtil.getEntityManager();
            em.getTransaction().begin();
            
            TypedQuery<Salida> query = em.createQuery(
                "SELECT s FROM Salida s WHERE s.nombre = :nombre", Salida.class);
            query.setParameter("nombre", nombre);
            List<Salida> list = query.getResultList();
            
            if (list.isEmpty()) {
                em.getTransaction().rollback();
                return false;
            }
            
            Salida salida = list.get(0);
            
            // Actualizar campos
            if (fecha != null && !fecha.isBlank()) {
                try {
                    java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("dd/MM/yyyy");
                    salida.setFecha(df.parse(fecha));
                } catch (Exception e) {
                    System.err.println("[SalidasService] Error parseando fecha: " + e.getMessage());
                }
            }
            
            if (hora != null && !hora.isBlank()) {
                try {
                    salida.setHora(java.time.LocalTime.parse(hora));
                } catch (Exception e) {
                    System.err.println("[SalidasService] Error parseando hora: " + e.getMessage());
                }
            }
            
            if (lugar != null && !lugar.isBlank()) {
                salida.setLugar(lugar);
            }
            
            if (cantMax > 0) {
                salida.setTuristasMax(cantMax);
            }
            
            // Actualizar imagen si se proporciona
            if (imagenBase64 != null && !imagenBase64.isBlank()) {
                String imagenDataUri;
                if (imagenBase64.startsWith("data:image/")) {
                    imagenDataUri = imagenBase64;
                } else {
                    imagenDataUri = "data:image/jpeg;base64," + imagenBase64;
                }
                salida.setImagen(imagenDataUri);
            }
            
            em.merge(salida);
            em.getTransaction().commit();
            return true;
        } catch (Exception e) {
            System.err.println("[SalidasService] Error modificando salida: " + e.getMessage());
            if (em != null && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            return false;
        } finally {
            if (em != null && em.isOpen()) em.close();
        }
    }

    @Override
    public List<InscripcionDTO> listarInscripcionesDeSalida(String salida) {
        List<InscripcionDTO> result = new ArrayList<>();
        if (salida == null || salida.isBlank()) return result;
        
        EntityManager em = null;
        try {
            em = JpaUtil.getEntityManager();
            TypedQuery<Inscripcion> query = em.createQuery(
                "SELECT i FROM Inscripcion i WHERE i.salida.nombre = :nombre ORDER BY i.fechaInscripcion DESC", 
                Inscripcion.class);
            query.setParameter("nombre", salida);
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
            System.err.println("[SalidasService] Error listando inscripciones: " + e.getMessage());
        } finally {
            if (em != null && em.isOpen()) em.close();
        }
        return result;
    }
}
