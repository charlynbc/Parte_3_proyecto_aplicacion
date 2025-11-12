package servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import uy.edu.pa.central.client.TurismoService;
import uy.edu.pa.central.client.TurismoWebService;
import uy.edu.pa.central.client.UserDTO;

@WebServlet("/dashboard")
public class DashboardServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        // Validación de sesión basada en las claves establecidas por el Login via SOAP
        String username = (session != null) ? (String) session.getAttribute("username") : null;
        if (username == null || username.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        // Obtener imagen del usuario via SOAP si no está en sesión
        if (session.getAttribute("userImage") == null) {
            try {
                TurismoWebService svc = new TurismoService().getTurismoWebServicePort();
                UserDTO user = svc.obtenerUsuario(username);
                if (user != null && user.getImagen() != null && !user.getImagen().trim().isEmpty()) {
                    session.setAttribute("userImage", user.getImagen());
                }
            } catch (Exception e) {
                System.err.println("[DashboardServlet] Error obteniendo imagen de usuario: " + e.getMessage());
            }
        }

        // Simplificado según "la letra": el dashboard no debe depender de JPA en el web.
        // Mostramos el dashboard usando los datos de sesión (username, userType, etc.).
        request.getRequestDispatcher("/WEB-INF/dashboard.jsp").forward(request, response);
    }
}