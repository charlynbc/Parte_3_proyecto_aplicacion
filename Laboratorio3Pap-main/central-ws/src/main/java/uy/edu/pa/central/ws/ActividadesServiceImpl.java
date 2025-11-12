package uy.edu.pa.central.ws;

import jakarta.jws.WebService;
import uy.edu.pa.central.ws.dtos.ActividadDTO;
import uy.edu.pa.central.ws.dtos.SalidaDTO;
import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import uy.edu.pa.central.persistence.JpaUtil;
import logica.Salida;

@WebService(
    serviceName = "ActividadesService",
    endpointInterface = "uy.edu.pa.central.ws.ActividadesService",
    portName = "ActividadesServicePort",
    targetNamespace = "http://ws.central.pa.edu.uy/"
)
public class ActividadesServiceImpl implements ActividadesService {

    @Override
    public List<ActividadDTO> listarActividades() {
        List<ActividadDTO> result = new ArrayList<>();
        EntityManager em = null;
        try {
            em = JpaUtil.getEntityManager();
            TypedQuery<logica.Actividad> query = em.createQuery(
                "SELECT a FROM Actividad a ORDER BY a.nombre", logica.Actividad.class);
            List<logica.Actividad> actividades = query.getResultList();
            
            java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("dd/MM/yyyy");
            
            for (logica.Actividad act : actividades) {
                ActividadDTO dto = convertirActividadADTO(act);
                
                if (act.getSalidas() != null) {
                    for (Salida sal : act.getSalidas()) {
                        SalidaDTO salidaDto = new SalidaDTO();
                        salidaDto.setId(sal.getNombre());
                        if (sal.getFecha() != null) {
                            salidaDto.setFecha(df.format(sal.getFecha()));
                        }
                        if (sal.getHora() != null) {
                            salidaDto.setHora(sal.getHora().toString());
                        }
                        salidaDto.setLugar(sal.getLugar());
                        dto.getSalidas().add(salidaDto);
                    }
                }
                
                result.add(dto);
            }
        } catch (Exception e) {
            System.err.println("[ActividadesService] Error listando actividades: " + e.getMessage());
        } finally {
            if (em != null && em.isOpen()) em.close();
        }
        return result;
    }

    @Override
    public ActividadDTO obtenerActividad(String id) {
        if (id == null || id.isBlank()) return null;
        
        EntityManager em = null;
        try {
            em = JpaUtil.getEntityManager();
            TypedQuery<logica.Actividad> query = em.createQuery(
                "SELECT a FROM Actividad a WHERE a.nombre = :nombre", logica.Actividad.class);
            query.setParameter("nombre", id);
            List<logica.Actividad> list = query.getResultList();
            
            if (list.isEmpty()) return null;
            
            logica.Actividad act = list.get(0);
            java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("dd/MM/yyyy");
            
            ActividadDTO dto = convertirActividadADTO(act);
            
            if (act.getSalidas() != null) {
                for (Salida sal : act.getSalidas()) {
                    SalidaDTO salidaDto = new SalidaDTO();
                    salidaDto.setId(sal.getNombre());
                    if (sal.getFecha() != null) {
                        salidaDto.setFecha(df.format(sal.getFecha()));
                    }
                    if (sal.getHora() != null) {
                        salidaDto.setHora(sal.getHora().toString());
                    }
                    salidaDto.setLugar(sal.getLugar());
                    dto.getSalidas().add(salidaDto);
                }
            }
            
            return dto;
        } catch (Exception e) {
            System.err.println("[ActividadesService] Error obteniendo actividad: " + e.getMessage());
            return null;
        } finally {
            if (em != null && em.isOpen()) em.close();
        }
    }

    @Override
    public boolean crearActividad(String nombre, String descripcion, int duracion, 
                                   float costo, String ciudad, String proveedor, String fechaAlta, String imagenBase64) {
        EntityManager em = null;
        try {
            em = JpaUtil.getEntityManager();
            em.getTransaction().begin();
            
            logica.Actividad existente = em.find(logica.Actividad.class, nombre);
            if (existente != null) {
                System.err.println("[ActividadesService] Actividad ya existe: " + nombre);
                return false;
            }
            
            logica.Proveedor prov = em.find(logica.Proveedor.class, proveedor);
            if (prov == null) {
                System.err.println("[ActividadesService] Proveedor no encontrado: " + proveedor);
                return false;
            }
            
            logica.Actividad act = new logica.Actividad();
            act.setNombre(nombre);
            act.setDescripcion(descripcion);
            act.setDuracion(duracion);
            act.setCosto(costo);
            act.setCiudad(ciudad);
            act.setProveedor(prov);
            act.setEstado("Ingresada");
            
            // Procesar imagen si se envió
            if (imagenBase64 != null && !imagenBase64.isBlank()) {
                // Guardar imagen como data URI
                act.setImagen("data:image/jpeg;base64," + imagenBase64);
                System.out.println("[ActividadesService] Imagen guardada para actividad: " + nombre);
            }
            
            if (fechaAlta != null && !fechaAlta.isBlank()) {
                java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
                act.setFechaAlta(sdf.parse(fechaAlta));
            }
            
            em.persist(act);
            em.getTransaction().commit();
            
            System.out.println("[ActividadesService] Actividad creada: " + nombre);
            return true;
            
        } catch (Exception e) {
            if (em != null && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            System.err.println("[ActividadesService] Error creando actividad: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            if (em != null && em.isOpen()) em.close();
        }
    }
    
    public boolean modificarActividad(String nombre, String descripcion, int duracion,
                                      float costo, String ciudad, String imagenBase64) {
        EntityManager em = null;
        try {
            em = JpaUtil.getEntityManager();
            em.getTransaction().begin();
            
            logica.Actividad act = em.find(logica.Actividad.class, nombre);
            if (act == null) {
                System.err.println("[ActividadesService] Actividad no encontrada: " + nombre);
                return false;
            }
            
            // Actualizar campos
            act.setDescripcion(descripcion);
            act.setDuracion(duracion);
            act.setCosto(costo);
            act.setCiudad(ciudad);
            
            // Actualizar imagen solo si se envió una nueva
            if (imagenBase64 != null && !imagenBase64.isBlank()) {
                act.setImagen("data:image/jpeg;base64," + imagenBase64);
                System.out.println("[ActividadesService] Imagen actualizada para: " + nombre);
            }
            
            em.merge(act);
            em.getTransaction().commit();
            
            System.out.println("[ActividadesService] Actividad modificada: " + nombre);
            return true;
            
        } catch (Exception e) {
            if (em != null && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            System.err.println("[ActividadesService] Error modificando actividad: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            if (em != null && em.isOpen()) em.close();
        }
    }

    @Override
    public List<ActividadDTO> listarActividadesPorProveedor(String proveedor) {
        List<ActividadDTO> result = new ArrayList<>();
        if (proveedor == null || proveedor.isBlank()) return result;
        
        EntityManager em = null;
        try {
            em = JpaUtil.getEntityManager();
            TypedQuery<logica.Actividad> query = em.createQuery(
                "SELECT a FROM Actividad a WHERE a.proveedor.nickname = :nick ORDER BY a.fechaAlta DESC", 
                logica.Actividad.class);
            query.setParameter("nick", proveedor);
            List<logica.Actividad> actividades = query.getResultList();
            
            java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("dd/MM/yyyy");
            
            for (logica.Actividad act : actividades) {
                ActividadDTO dto = new ActividadDTO(
                    act.getNombre(),
                    act.getDescripcion(),
                    act.getCiudad(),
                    act.getCosto(),
                    act.getImagen()
                );
                result.add(dto);
            }
        } catch (Exception e) {
            System.err.println("[ActividadesService] Error listando actividades por proveedor: " + e.getMessage());
        } finally {
            if (em != null && em.isOpen()) em.close();
        }
        return result;
    }

    @Override
    public List<ActividadDTO> rankingActividades() {
        List<ActividadDTO> result = new ArrayList<>();
        EntityManager em = null;
        try {
            em = JpaUtil.getEntityManager();
            TypedQuery<logica.Actividad> query = em.createQuery(
                "SELECT a FROM Actividad a LEFT JOIN a.salidas s LEFT JOIN s.inscripciones i " +
                "GROUP BY a ORDER BY COUNT(i) DESC", 
                logica.Actividad.class);
            List<logica.Actividad> actividades = query.getResultList();
            
            for (logica.Actividad act : actividades) {
                ActividadDTO dto = new ActividadDTO(
                    act.getNombre(),
                    act.getDescripcion(),
                    act.getCiudad(),
                    act.getCosto(),
                    act.getImagen()
                );
                result.add(dto);
            }
        } catch (Exception e) {
            System.err.println("[ActividadesService] Error generando ranking: " + e.getMessage());
        } finally {
            if (em != null && em.isOpen()) em.close();
        }
        return result;
    }

    @Override
    public List<ActividadDTO> listarActividadesPorTurista(String turista) {
        List<ActividadDTO> result = new ArrayList<>();
        if (turista == null || turista.isBlank()) return result;
        
        EntityManager em = null;
        try {
            em = JpaUtil.getEntityManager();
            TypedQuery<logica.Actividad> query = em.createQuery(
                "SELECT DISTINCT a FROM Actividad a JOIN a.salidas s JOIN s.inscripciones i " +
                "WHERE i.turista.nickname = :nick ORDER BY a.nombre", 
                logica.Actividad.class);
            query.setParameter("nick", turista);
            List<logica.Actividad> actividades = query.getResultList();
            
            for (logica.Actividad act : actividades) {
                ActividadDTO dto = new ActividadDTO(
                    act.getNombre(),
                    act.getDescripcion(),
                    act.getCiudad(),
                    act.getCosto(),
                    act.getImagen()
                );
                result.add(dto);
            }
        } catch (Exception e) {
            System.err.println("[ActividadesService] Error listando actividades por turista: " + e.getMessage());
        } finally {
            if (em != null && em.isOpen()) em.close();
        }
        return result;
    }
    
    // Método helper para convertir Actividad a DTO
    private ActividadDTO convertirActividadADTO(logica.Actividad act) {
        ActividadDTO dto = new ActividadDTO(
            act.getNombre(),
            act.getDescripcion(),
            act.getCiudad(),
            act.getCosto(),
            act.getImagen()
        );
        dto.setDuracion(act.getDuracion());
        return dto;
    }
}
