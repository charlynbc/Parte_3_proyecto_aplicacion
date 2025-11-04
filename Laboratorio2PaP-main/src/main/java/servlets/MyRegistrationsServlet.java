package servlets;

import jakarta.persistence.EntityManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

import logica.Inscripcion;
import utils.JpaUtil;

@WebServlet(name = "MyRegistrationsServlet", urlPatterns = {"/mis-salidas"})
public class MyRegistrationsServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        String username = (session != null) ? (String) session.getAttribute("username") : null;
        String tipoUsuario = (session != null) ? (String) session.getAttribute("tipoUsuario") : null;

        if (username == null || username.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        if (tipoUsuario == null || !"turista".equalsIgnoreCase(tipoUsuario)) {
            request.setAttribute("error", "Solo los usuarios Turista pueden ver sus salidas registradas.");
            request.getRequestDispatcher("/WEB-INF/my-registrations.jsp").forward(request, response);
            return;
        }

        EntityManager em = null;
        try {
            em = JpaUtil.getEntityManager();
            List<Inscripcion> inscripciones = em.createQuery(
                    "SELECT i FROM Inscripcion i " +
                    "JOIN FETCH i.salida s " +
                    "JOIN FETCH s.actividad a " +
                    "WHERE i.turista.nickname = :nick " +
                    "ORDER BY s.fecha ASC",
                    Inscripcion.class)
                .setParameter("nick", username)
                .getResultList();

            request.setAttribute("inscripciones", inscripciones);
            request.getRequestDispatcher("/WEB-INF/my-registrations.jsp").forward(request, response);
        } catch (Exception e) {
            System.err.println("[MyRegistrationsServlet] Error obteniendo inscripciones: " + e.getMessage());
            request.setAttribute("error", "No se pudieron cargar tus salidas registradas.");
            request.getRequestDispatcher("/WEB-INF/my-registrations.jsp").forward(request, response);
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }
}
