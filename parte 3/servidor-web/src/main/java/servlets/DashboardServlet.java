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

import java.io.IOException;
import java.util.List;

import jakarta.persistence.PersistenceException;
import utils.JpaUtil;

@WebServlet("/dashboard")
public class DashboardServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("usuario") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        String tipoUsuario = (String) session.getAttribute("tipoUsuario");
        
        EntityManagerFactory emf = null;
        EntityManager em = null;
        try {
            em = JpaUtil.getEntityManager();
            
            if ("turista".equals(tipoUsuario)) {
                // Dashboard para turista
                loadTuristaDashboard(request, em, (Turista) usuario);
                request.getRequestDispatcher("/WEB-INF/dashboard.jsp").forward(request, response);
            } else if ("proveedor".equals(tipoUsuario)) {
                // Dashboard para proveedor
                loadProveedorDashboard(request, em, (Proveedor) usuario);
                request.getRequestDispatcher("/WEB-INF/dashboard.jsp").forward(request, response);
            } else {
                // Dashboard genérico
                request.getRequestDispatcher("/WEB-INF/dashboard.jsp").forward(request, response);
            }
            
        } catch (PersistenceException e) {
            request.setAttribute("error", "No es posible cargar el dashboard por problemas de conexión con la base de datos.");
            request.getRequestDispatcher("/WEB-INF/dashboard.jsp").forward(request, response);
            return;
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }
    
    private void loadTuristaDashboard(HttpServletRequest request, EntityManager em, Turista turista) {
        // Cargar inscripciones del turista
        TypedQuery<Inscripcion> queryInscripciones = em.createQuery(
            "SELECT i FROM Inscripcion i WHERE i.turista = :turista", Inscripcion.class);
        queryInscripciones.setParameter("turista", turista);
        List<Inscripcion> inscripciones = queryInscripciones.getResultList();
        
        // Cargar actividades disponibles
        TypedQuery<Actividad> queryActividades = em.createQuery(
            "SELECT a FROM Actividad a ORDER BY a.fechaAlta DESC", Actividad.class);
        queryActividades.setMaxResults(5);
        List<Actividad> actividadesRecientes = queryActividades.getResultList();
        
        request.setAttribute("inscripciones", inscripciones);
        request.setAttribute("actividadesRecientes", actividadesRecientes);
    }
    
    private void loadProveedorDashboard(HttpServletRequest request, EntityManager em, Proveedor proveedor) {
        // Cargar actividades del proveedor
        TypedQuery<Actividad> queryActividades = em.createQuery(
            "SELECT a FROM Actividad a WHERE a.proveedor = :proveedor ORDER BY a.fechaAlta DESC", 
            Actividad.class);
        queryActividades.setParameter("proveedor", proveedor);
        List<Actividad> misActividades = queryActividades.getResultList();
        
        // Cargar salidas del proveedor
        TypedQuery<Salida> querySalidas = em.createQuery(
            "SELECT s FROM Salida s WHERE s.actividad.proveedor = :proveedor ORDER BY s.fecha DESC", 
            Salida.class);
        querySalidas.setParameter("proveedor", proveedor);
        List<Salida> misSalidas = querySalidas.getResultList();
        
        request.setAttribute("misActividades", misActividades);
        request.setAttribute("misSalidas", misSalidas);
    }
}