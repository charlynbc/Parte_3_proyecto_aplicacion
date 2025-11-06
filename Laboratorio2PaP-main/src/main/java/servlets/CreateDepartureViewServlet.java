package servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

// SOAP stubs
import uy.edu.pa.central.client.ActividadesService_Service;
import uy.edu.pa.central.client.ActividadesService;
import uy.edu.pa.central.client.ActividadDTO;

@WebServlet("/create-departure")
public class CreateDepartureViewServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("username") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String tipoUsuario = (String) session.getAttribute("tipoUsuario");
        if (tipoUsuario == null || !"proveedor".equalsIgnoreCase(tipoUsuario)) {
            request.setAttribute("error", "Solo los proveedores pueden gestionar salidas turísticas.");
            request.getRequestDispatcher("/WEB-INF/dashboard.jsp").forward(request, response);
            return;
        }

        String proveedor = (String) session.getAttribute("username");
        
        try {
            // Obtener actividades via SOAP
            ActividadesService_Service service = new ActividadesService_Service();
            ActividadesService port = service.getActividadesServicePort();
            
            List<ActividadDTO> todasActividades = port.listarActividades();
            
            // Filtrar solo las del proveedor (simplificado - todas por ahora)
            request.setAttribute("actividades", todasActividades);
            
            System.out.println("[CreateDepartureView SOAP] Actividades obtenidas: " + 
                (todasActividades != null ? todasActividades.size() : 0));
            
        } catch (Exception e) {
            System.err.println("Error loading activities: " + e.getMessage());
            request.setAttribute("actividades", new java.util.ArrayList<ActividadDTO>());
        }

        request.getRequestDispatcher("/WEB-INF/create-departure.jsp").forward(request, response);
    }
}
