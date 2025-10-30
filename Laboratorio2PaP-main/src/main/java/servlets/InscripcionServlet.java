package servlets;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import logica.*;
import jakarta.persistence.PersistenceException;
import utils.JpaUtil;

import java.io.IOException;
import java.util.Date;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

@WebServlet("/inscripcion-jpa")
public class InscripcionServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        if (session == null || !"turista".equals(session.getAttribute("tipoUsuario"))) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        String nombreSalida = request.getParameter("salida");
        if (nombreSalida == null) {
            response.sendRedirect(request.getContextPath() + "/actividad");
            return;
        }
        
        EntityManagerFactory emf = null;
        EntityManager em = null;
        
        try {
            emf = Persistence.createEntityManagerFactory("emf");
            em = emf.createEntityManager();
            
            TypedQuery<Salida> query = em.createQuery(
                "SELECT s FROM Salida s WHERE s.nombre = :nombre", Salida.class);
            query.setParameter("nombre", nombreSalida);
            List<Salida> salidas = query.getResultList();
            
            if (!salidas.isEmpty()) {
                Salida salida = salidas.get(0);
                request.setAttribute("salida", salida);
                request.getRequestDispatcher("/WEB-INF/inscripcion.jsp").forward(request, response);
            } else {
                response.sendRedirect(request.getContextPath() + "/actividad");
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/actividad");
        } finally {
            if (em != null) em.close();
            if (emf != null) emf.close();
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        if (session == null || !"turista".equals(session.getAttribute("tipoUsuario"))) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        String nombreSalida = request.getParameter("nombreSalida");
        String fechaInscripcionStr = request.getParameter("fechaInscripcion");
        
        EntityManagerFactory emf = null;
        EntityManager em = null;
        
        try {
            emf = Persistence.createEntityManagerFactory("emf");
            em = emf.createEntityManager();
            
            // Buscar salida
            TypedQuery<Salida> querySalida = em.createQuery(
                "SELECT s FROM Salida s WHERE s.nombre = :nombre", Salida.class);
            querySalida.setParameter("nombre", nombreSalida);
            List<Salida> salidas = querySalida.getResultList();
            
            if (salidas.isEmpty()) {
                request.setAttribute("error", "Salida no encontrada");
                response.sendRedirect(request.getContextPath() + "/actividad");
                return;
            }
            
            Salida salida = salidas.get(0);
            Turista turista = (Turista) session.getAttribute("usuario");
            
            // Verificar si ya está inscrito
            TypedQuery<Inscripcion> queryInscripcion = em.createQuery(
                "SELECT i FROM Inscripcion i WHERE i.turista = :turista AND i.salida = :salida", 
                Inscripcion.class);
            queryInscripcion.setParameter("turista", turista);
            queryInscripcion.setParameter("salida", salida);
            
            if (!queryInscripcion.getResultList().isEmpty()) {
                request.setAttribute("error", "Ya está inscrito en esta salida");
                request.setAttribute("salida", salida);
                request.getRequestDispatcher("/WEB-INF/inscripcion.jsp").forward(request, response);
                return;
            }
            
            em.getTransaction().begin();
            
            Inscripcion inscripcion = new Inscripcion();
            inscripcion.setTurista(turista);
            inscripcion.setSalida(salida);
            Date fechaInscripcion = Date.from(
                LocalDate.parse(fechaInscripcionStr)
                         .atStartOfDay(ZoneId.systemDefault())
                         .toInstant()
            );
            inscripcion.setFechaInscripcion(fechaInscripcion);
            
            em.persist(inscripcion);
            em.getTransaction().commit();
            
            request.setAttribute("success", "Inscripción realizada exitosamente");
            request.setAttribute("inscripcion", inscripcion);
            request.getRequestDispatcher("/WEB-INF/inscripcion-result.jsp").forward(request, response);
            
        } catch (PersistenceException e) {
            request.setAttribute("error", "No fue posible contactar a la base de datos para registrar la inscripción.");
            response.sendRedirect(request.getContextPath() + "/actividad");
            return;
        } catch (Exception e) {
            if (em != null && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
            request.setAttribute("error", "Error procesando inscripción");
            response.sendRedirect(request.getContextPath() + "/actividad");
        } finally {
            if (em != null) em.close();
            if (emf != null) emf.close();
        }
    }
}
