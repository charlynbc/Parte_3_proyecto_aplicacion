package servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

import uy.edu.pa.central.client.TurismoService;
import uy.edu.pa.central.client.TurismoWebService;
import uy.edu.pa.central.client.InscripcionDTO;

@WebServlet(name = "MyRegistrationsServlet", urlPatterns = {"/my-registrations"})
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
            request.setAttribute("error", "Solo los turistas pueden ver salidas registradas.");
            request.getRequestDispatcher("/WEB-INF/dashboard.jsp").forward(request, response);
            return;
        }

        try {
            TurismoWebService svc = new TurismoService().getTurismoWebServicePort();
            List<InscripcionDTO> inscripciones = svc.listarInscripcionesPorTurista(username);
            request.setAttribute("inscripcionesDto", inscripciones);
        } catch (Exception e) {
            System.err.println("[MyRegistrationsServlet] Error SOAP obteniendo inscripciones: " + e.getMessage());
            request.setAttribute("error", "No se pudieron cargar tus salidas registradas.");
        }
        request.getRequestDispatcher("/WEB-INF/my-registrations.jsp").forward(request, response);
    }
}
