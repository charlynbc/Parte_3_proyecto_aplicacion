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
import java.util.Date;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

@WebServlet("/actividad")
public class ActividadServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String action = request.getParameter("action");
        if (action == null) action = "list";
        
        switch (action) {
            case "list":
                listActividades(request, response);
                break;
            case "view":
                viewActividad(request, response);
                break;
            case "create":
                showCreateForm(request, response);
                break;
            default:
                listActividades(request, response);
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String action = request.getParameter("action");
        
        if ("create".equals(action)) {
            createActividad(request, response);
        }
    }
    
    private void listActividades(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        EntityManagerFactory emf = null;
        EntityManager em = null;
        
        try {
            emf = Persistence.createEntityManagerFactory("emf");
            em = emf.createEntityManager();
            
            TypedQuery<Actividad> query = em.createQuery(
                "SELECT a FROM Actividad a ORDER BY a.fechaAlta DESC", Actividad.class);
            List<Actividad> actividades = query.getResultList();
            
            request.setAttribute("actividades", actividades);
            request.getRequestDispatcher("/WEB-INF/jsp/actividades.jsp").forward(request, response);
            
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Error cargando actividades");
            request.getRequestDispatcher("/WEB-INF/jsp/error.jsp").forward(request, response);
        } finally {
            if (em != null) em.close();
            if (emf != null) emf.close();
        }
    }
    
    private void viewActividad(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String nombreActividad = request.getParameter("nombre");
        if (nombreActividad == null) {
            response.sendRedirect(request.getContextPath() + "/actividad");
            return;
        }
        
        EntityManagerFactory emf = null;
        EntityManager em = null;
        
        try {
            emf = Persistence.createEntityManagerFactory("emf");
            em = emf.createEntityManager();
            
            TypedQuery<Actividad> query = em.createQuery(
                "SELECT a FROM Actividad a WHERE a.nombre = :nombre", Actividad.class);
            query.setParameter("nombre", nombreActividad);
            List<Actividad> actividades = query.getResultList();
            
            if (!actividades.isEmpty()) {
                Actividad actividad = actividades.get(0);
                
                // Cargar salidas de la actividad
                TypedQuery<Salida> querySalidas = em.createQuery(
                    "SELECT s FROM Salida s WHERE s.actividad = :actividad ORDER BY s.fecha", 
                    Salida.class);
                querySalidas.setParameter("actividad", actividad);
                List<Salida> salidas = querySalidas.getResultList();
                
                request.setAttribute("actividad", actividad);
                request.setAttribute("salidas", salidas);
                request.getRequestDispatcher("/WEB-INF/jsp/actividad-detalle.jsp").forward(request, response);
            } else {
                response.sendRedirect(request.getContextPath() + "/actividad");
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Error cargando actividad");
            request.getRequestDispatcher("/WEB-INF/jsp/error.jsp").forward(request, response);
        } finally {
            if (em != null) em.close();
            if (emf != null) emf.close();
        }
    }
    
    private void showCreateForm(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        if (session == null || !"proveedor".equals(session.getAttribute("tipoUsuario"))) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        request.getRequestDispatcher("/WEB-INF/jsp/crear-actividad.jsp").forward(request, response);
    }
    
    private void createActividad(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        if (session == null || !"proveedor".equals(session.getAttribute("tipoUsuario"))) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        String nombre = request.getParameter("nombre");
        String descripcion = request.getParameter("descripcion");
        String duracionStr = request.getParameter("duracion");
        String costoStr = request.getParameter("costo");
        String ciudad = request.getParameter("ciudad");
        
        // Validaciones
        if (nombre == null || nombre.trim().isEmpty() ||
            descripcion == null || descripcion.trim().isEmpty()) {
            request.setAttribute("error", "Nombre y descripción son obligatorios");
            request.getRequestDispatcher("/WEB-INF/jsp/crear-actividad.jsp").forward(request, response);
            return;
        }
        
        EntityManagerFactory emf = null;
        EntityManager em = null;
        
        try {
            int duracion = Integer.parseInt(duracionStr);
            float costo = Float.parseFloat(costoStr);
            Date fechaAlta = Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant());
            
            emf = Persistence.createEntityManagerFactory("emf");
            em = emf.createEntityManager();
            
            Proveedor proveedor = (Proveedor) session.getAttribute("usuario");
            
            em.getTransaction().begin();
            
            Actividad actividad = new Actividad();
            actividad.setNombre(nombre.trim());
            actividad.setDescripcion(descripcion.trim());
            actividad.setDuracion(duracion);
            actividad.setCosto(costo);
            actividad.setFechaAlta(fechaAlta);
            actividad.setProveedor(proveedor);
            
            em.persist(actividad);
            em.getTransaction().commit();
            
            response.sendRedirect(request.getContextPath() + "/actividad?success=created");
            
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Duración y costo deben ser números válidos");
            request.getRequestDispatcher("/WEB-INF/jsp/crear-actividad.jsp").forward(request, response);
        } catch (Exception e) {
            if (em != null && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
            request.setAttribute("error", "Error creando actividad");
            request.getRequestDispatcher("/WEB-INF/jsp/crear-actividad.jsp").forward(request, response);
        } finally {
            if (em != null) em.close();
            if (emf != null) emf.close();
        }
    }
}
