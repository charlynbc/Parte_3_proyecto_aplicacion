package servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

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

        // Simplificado según "la letra": el dashboard no debe depender de JPA en el web.
        // Mostramos el dashboard usando los datos de sesión (username, userType, etc.).
        request.getRequestDispatcher("/WEB-INF/dashboard.jsp").forward(request, response);
    }
}