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
        // Placeholder: Mock hasta conectar a DB Central
        List<ActividadDTO> list = new ArrayList<>();
        ActividadDTO a1 = new ActividadDTO("Kayak Colonia", "Remada por la bahía de Colonia", "Colonia", 1500f, "https://images.unsplash.com/photo-1506744038136-46273834b3fb?auto=format&fit=crop&w=800&q=80");
        a1.getSalidas().add(new SalidaDTO("Salida-Kayak-1", LocalDate.now().plusDays(7).toString(), "10:00", "Puerto de Yates"));
        list.add(a1);
        return list;
    }

    @Override
    public ActividadDTO obtenerActividad(String id) {
        // Simulación simple
        if (id == null || id.isBlank()) return null;
        ActividadDTO a = new ActividadDTO(id, "Descripción de " + id, "Lugar", 1200f, null);
        a.getSalidas().add(new SalidaDTO("Salida-" + id + "-1", LocalDate.now().plusDays(3).toString(), "09:00", "Punto de encuentro"));
        return a;
    }

    @Override
    public SalidaDTO obtenerSalida(String id) {
        if (id == null || id.isBlank()) return null;
        return new SalidaDTO(id, LocalDate.now().plusDays(1).toString(), "08:30", "Lugar de salida");
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
}
