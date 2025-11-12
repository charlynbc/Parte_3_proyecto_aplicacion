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
import uy.edu.pa.central.client.ActividadDTO;

@WebServlet("/my-activities")
public class MyActivitiesServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("username") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        String userType = (String) session.getAttribute("userType");
        if (!"Proveedor".equals(userType)) {
            response.sendRedirect(request.getContextPath() + "/dashboard");
            return;
        }
        
        String proveedorNickname = (String) session.getAttribute("username");
        
        try {
            TurismoService service = new TurismoService();
            TurismoWebService port = service.getTurismoWebServicePort();
            
            List<ActividadDTO> misActividades = port.listarActividadesPorProveedor(proveedorNickname);
            request.setAttribute("activities", misActividades);
            
        } catch (Exception e) {
            System.err.println("Error loading provider activities: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("error", "Error al cargar las actividades");
        }
        
        request.getRequestDispatcher("/WEB-INF/my-activities.jsp").forward(request, response);
    }
}
