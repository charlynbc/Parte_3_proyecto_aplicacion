package uy.edu.pa.central.ws;

import jakarta.jws.WebService;
import uy.edu.pa.central.ws.dtos.ActividadDTO;
import uy.edu.pa.central.ws.dtos.SalidaDTO;
import uy.edu.pa.central.ws.dtos.InscripcionDTO;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

// JPA / entidades del dominio
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import uy.edu.pa.central.persistence.JpaUtil;
import logica.Turista;
import logica.Salida;
import logica.Inscripcion;

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
                // Para actividad usamos la ciudad como lugar, y no tenemos imagen a este nivel
                ActividadDTO dto = new ActividadDTO(
                    act.getNombre(),
                    act.getDescripcion(),
                    act.getCiudad(),  // Usamos ciudad como lugar
                    act.getCosto(),
                    null  // No hay imagen a nivel de actividad
                );
                
                // Agregar salidas de esta actividad
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
            System.err.println("[CentralWS] Error listando actividades: " + e.getMessage());
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
            
            // Para actividad usamos la ciudad como lugar
            ActividadDTO dto = new ActividadDTO(
                act.getNombre(),
                act.getDescripcion(),
                act.getCiudad(),  // Usamos ciudad como lugar
                act.getCosto(),
                null  // No hay imagen a nivel de actividad
            );
            
            // Agregar salidas
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
            System.err.println("[CentralWS] Error obteniendo actividad: " + e.getMessage());
            return null;
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
            
            return dto;
        } catch (Exception e) {
            System.err.println("[CentralWS] Error obteniendo salida: " + e.getMessage());
            return null;
        } finally {
            if (em != null && em.isOpen()) em.close();
        }
    }

    @Override
    public boolean inscribirTurista(String actividad, String salida, String turista, int cantidad, String fecha, float costo) {
        // Persistencia real: crear Inscripcion si no existe
        if (actividad == null || actividad.isBlank()) return false;
        if (salida == null || salida.isBlank()) return false;
        if (turista == null || turista.isBlank()) return false;
        if (cantidad <= 0) return false;

        EntityManager em = null;
        try {
            em = JpaUtil.getEntityManager();

            // Buscar turista por nickname
            TypedQuery<Turista> qTur = em.createQuery(
                "SELECT t FROM Turista t WHERE t.nickname = :nick", Turista.class);
            qTur.setParameter("nick", turista);
            List<Turista> tList = qTur.getResultList();
            if (tList.isEmpty()) return false;
            Turista tur = tList.get(0);

            // Buscar salida por nombre (se usa el identificador textual)
            TypedQuery<Salida> qSal = em.createQuery(
                "SELECT s FROM Salida s WHERE s.nombre = :nombre", Salida.class);
            qSal.setParameter("nombre", salida);
            List<Salida> sList = qSal.getResultList();
            if (sList.isEmpty()) return false;
            Salida sal = sList.get(0);

            // Verificar duplicado
            TypedQuery<Inscripcion> qDup = em.createQuery(
                "SELECT i FROM Inscripcion i WHERE i.turista = :tur AND i.salida = :sal", Inscripcion.class);
            qDup.setParameter("tur", tur);
            qDup.setParameter("sal", sal);
            if (!qDup.getResultList().isEmpty()) {
                return false; // ya inscrito
            }

            // Crear inscripción
            em.getTransaction().begin();
            Inscripcion ins = new Inscripcion();
            ins.setTurista(tur);
            ins.setSalida(sal);
            ins.setCantTuristas(cantidad);
            try { ins.setCosto(costo); } catch (Exception ignore) { /* costo opcional */ }
            try {
                // Formato esperado dd/MM/yyyy
                java.time.LocalDate ld = java.time.LocalDate.parse(fecha, java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                java.util.Date f = java.util.Date.from(ld.atStartOfDay(java.time.ZoneId.systemDefault()).toInstant());
                ins.setFechaInscripcion(f);
            } catch (Exception ignore) {
                ins.setFechaInscripcion(new java.util.Date());
            }
            em.persist(ins);
            em.getTransaction().commit();
            return true;
        } catch (Exception e) {
            if (em != null && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            System.err.println("[CentralWS] Error al inscribir turista: " + e.getMessage());
            return false;
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    @Override
    public List<InscripcionDTO> listarInscripcionesPorTurista(String turista) {
        List<InscripcionDTO> result = new ArrayList<>();
        if (turista == null || turista.isBlank()) return result;

        EntityManager em = null;
        try {
            em = JpaUtil.getEntityManager();
            TypedQuery<Inscripcion> q = em.createQuery(
                "SELECT i FROM Inscripcion i JOIN FETCH i.salida s JOIN FETCH s.actividad a WHERE i.turista.nickname = :nick ORDER BY s.fecha ASC",
                Inscripcion.class
            );
            q.setParameter("nick", turista);
            List<Inscripcion> list = q.getResultList();

            java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("dd/MM/yyyy");
            for (Inscripcion i : list) {
                InscripcionDTO dto = new InscripcionDTO();
                dto.setSalidaNombre(i.getSalida().getNombre());
                dto.setActividadNombre(i.getSalida().getActividad().getNombre());
                dto.setLugar(i.getSalida().getLugar());
                if (i.getSalida().getFecha() != null) dto.setFechaSalida(df.format(i.getSalida().getFecha()));
                try { dto.setHoraSalida(i.getSalida().getHora().toString()); } catch (Exception ignore) {}
                dto.setCantidad(i.getCantTuristas());
                dto.setCosto(i.getCosto());
                if (i.getFechaInscripcion() != null) dto.setFechaInscripcion(df.format(i.getFechaInscripcion()));
                try { dto.setImagen(i.getSalida().getImagen()); } catch (Exception ignore) {}
                result.add(dto);
            }
        } catch (Exception e) {
            System.err.println("[CentralWS] Error listando inscripciones: " + e.getMessage());
        } finally {
            if (em != null && em.isOpen()) em.close();
        }
        return result;
    }

    @Override
    public List<uy.edu.pa.central.ws.dtos.UserDTO> listarUsuarios() {
        List<uy.edu.pa.central.ws.dtos.UserDTO> result = new ArrayList<>();
        EntityManager em = null;
        try {
            em = JpaUtil.getEntityManager();
            TypedQuery<logica.Usuario> query = em.createQuery(
                "SELECT u FROM Usuario u ORDER BY u.nickname", logica.Usuario.class);
            List<logica.Usuario> usuarios = query.getResultList();
            
            for (logica.Usuario u : usuarios) {
                String tipo = "usuario";
                if (u instanceof logica.Turista) {
                    tipo = "turista";
                } else if (u instanceof logica.Proveedor) {
                    tipo = "proveedor";
                }
                
                uy.edu.pa.central.ws.dtos.UserDTO dto = new uy.edu.pa.central.ws.dtos.UserDTO(
                    u.getNickname(),
                    u.getNombre(),
                    u.getEmail(),
                    tipo
                );
                result.add(dto);
            }
        } catch (Exception e) {
            System.err.println("[CentralWS] Error listando usuarios: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (em != null && em.isOpen()) em.close();
        }
        return result;
    }

    @Override
    public uy.edu.pa.central.ws.dtos.UserDTO obtenerUsuario(String nickname) {
        EntityManager em = null;
        try {
            em = JpaUtil.getEntityManager();
            logica.Usuario u = em.find(logica.Usuario.class, nickname);
            
            if (u == null) {
                System.err.println("[CentralWS] Usuario no encontrado: " + nickname);
                return null;
            }
            
            String tipo = "usuario";
            if (u instanceof logica.Turista) {
                tipo = "turista";
            } else if (u instanceof logica.Proveedor) {
                tipo = "proveedor";
            }
            
            uy.edu.pa.central.ws.dtos.UserDTO dto = new uy.edu.pa.central.ws.dtos.UserDTO(
                u.getNickname(),
                u.getNombre(),
                u.getEmail(),
                tipo
            );
            
            System.out.println("[CentralWS] Usuario obtenido: " + nickname + " (tipo: " + tipo + ")");
            return dto;
            
        } catch (Exception e) {
            System.err.println("[CentralWS] Error obteniendo usuario: " + e.getMessage());
            e.printStackTrace();
            return null;
        } finally {
            if (em != null && em.isOpen()) em.close();
        }
    }

    @Override
    public boolean crearActividad(String nombre, String descripcion, int duracion, 
                                   float costo, String ciudad, String proveedor, String fechaAlta) {
        EntityManager em = null;
        try {
            em = JpaUtil.getEntityManager();
            em.getTransaction().begin();
            
            // Verificar si ya existe
            logica.Actividad existente = em.find(logica.Actividad.class, nombre);
            if (existente != null) {
                System.err.println("[CentralWS] Actividad ya existe: " + nombre);
                return false;
            }
            
            // Buscar proveedor
            logica.Proveedor prov = em.find(logica.Proveedor.class, proveedor);
            if (prov == null) {
                System.err.println("[CentralWS] Proveedor no encontrado: " + proveedor);
                return false;
            }
            
            // Crear actividad
            logica.Actividad act = new logica.Actividad();
            act.setNombre(nombre);
            act.setDescripcion(descripcion);
            act.setDuracion(duracion);
            act.setCosto(costo);
            act.setCiudad(ciudad);
            act.setProveedor(prov);
            act.setEstado("Ingresada"); // Estado inicial
            
            // Parsear fecha
            if (fechaAlta != null && !fechaAlta.isBlank()) {
                java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
                act.setFechaAlta(sdf.parse(fechaAlta));
            }
            
            em.persist(act);
            em.getTransaction().commit();
            
            System.out.println("[CentralWS] Actividad creada: " + nombre);
            return true;
            
        } catch (Exception e) {
            if (em != null && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            System.err.println("[CentralWS] Error creando actividad: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            if (em != null && em.isOpen()) em.close();
        }
    }

    @Override
    public boolean crearSalida(String nombre, String fecha, String hora, String lugar, 
                                int cantMax, String fechaAlta, String actividad) {
        EntityManager em = null;
        try {
            em = JpaUtil.getEntityManager();
            em.getTransaction().begin();
            
            // Buscar actividad
            logica.Actividad act = em.find(logica.Actividad.class, actividad);
            if (act == null) {
                System.err.println("[CentralWS] Actividad no encontrada: " + actividad);
                return false;
            }
            
            // Crear salida
            logica.Salida sal = new logica.Salida();
            sal.setNombre(nombre);
            sal.setLugar(lugar);
            sal.setTuristasMax(cantMax);
            sal.setActividad(act);
            
            // Parsear fecha
            if (fecha != null && !fecha.isBlank()) {
                java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
                sal.setFecha(sdf.parse(fecha));
            }
            
            // Parsear hora (formato HH:mm)
            if (hora != null && !hora.isBlank()) {
                java.time.LocalTime lt = java.time.LocalTime.parse(hora);
                sal.setHora(lt);
            }
            
            // Fecha de alta
            if (fechaAlta != null && !fechaAlta.isBlank()) {
                java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
                sal.setFechaAlta(sdf.parse(fechaAlta));
            }
            
            em.persist(sal);
            em.getTransaction().commit();
            
            System.out.println("[CentralWS] Salida creada: " + nombre + " para actividad " + actividad);
            return true;
            
        } catch (Exception e) {
            if (em != null && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            System.err.println("[CentralWS] Error creando salida: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            if (em != null && em.isOpen()) em.close();
        }
    }

    @Override
    public boolean actualizarUsuario(String nickname, String nombre, String apellido, 
                                      String fechaNacimiento, String nacionalidad, 
                                      String descripcion, String sitioWeb) {
        EntityManager em = null;
        try {
            em = JpaUtil.getEntityManager();
            em.getTransaction().begin();
            
            // Buscar usuario
            logica.Usuario u = em.find(logica.Usuario.class, nickname);
            if (u == null) {
                System.err.println("[CentralWS] Usuario no encontrado: " + nickname);
                return false;
            }
            
            // Actualizar campos comunes
            if (nombre != null && !nombre.isBlank()) u.setNombre(nombre);
            if (apellido != null && !apellido.isBlank()) u.setApellido(apellido);
            
            // Fecha de nacimiento
            if (fechaNacimiento != null && !fechaNacimiento.isBlank()) {
                java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
                u.setFechaNac(sdf.parse(fechaNacimiento));
            }
            
            // Campos específicos por tipo
            if (u instanceof logica.Turista) {
                logica.Turista t = (logica.Turista) u;
                if (nacionalidad != null && !nacionalidad.isBlank()) {
                    t.setNacionalidad(nacionalidad);
                }
            } else if (u instanceof logica.Proveedor) {
                logica.Proveedor p = (logica.Proveedor) u;
                if (descripcion != null) p.setDescripcion(descripcion);
                if (sitioWeb != null) p.setSitioWeb(sitioWeb);
            }
            
            em.merge(u);
            em.getTransaction().commit();
            
            System.out.println("[CentralWS] Usuario actualizado: " + nickname);
            return true;
            
        } catch (Exception e) {
            if (em != null && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            System.err.println("[CentralWS] Error actualizando usuario: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            if (em != null && em.isOpen()) em.close();
        }
    }
}
