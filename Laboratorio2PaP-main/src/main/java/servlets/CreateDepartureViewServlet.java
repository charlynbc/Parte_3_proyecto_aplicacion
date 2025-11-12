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
import uy.edu.pa.central.client.TurismoService;
import uy.edu.pa.central.client.TurismoWebService;
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
            TurismoService service = new TurismoService();
            TurismoWebService port = service.getTurismoWebServicePort();
            
            // Obtener solo las actividades del proveedor logueado
            List<ActividadDTO> actividadesProveedor = port.listarActividadesPorProveedor(proveedor);
            
            request.setAttribute("actividades", actividadesProveedor);
            
            // Generar HTML para el select
            StringBuilder actividadesHtml = new StringBuilder();
            if (actividadesProveedor != null && !actividadesProveedor.isEmpty()) {
                actividadesHtml.append("<option value=\"\">Seleccione una actividad</option>");
                for (ActividadDTO act : actividadesProveedor) {
                    String id = act.getId() != null ? act.getId() : "";
                    actividadesHtml.append("<option value=\"")
                                  .append(id)
                                  .append("\">")
                                  .append(id)
                                  .append("</option>");
                }
            }
            request.setAttribute("actividadesHtml", actividadesHtml.toString());
            
            System.out.println("[CreateDepartureView SOAP] Actividades del proveedor " + proveedor + ": " + 
                (actividadesProveedor != null ? actividadesProveedor.size() : 0));
            
        } catch (Exception e) {
            System.err.println("Error loading activities: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("actividades", new java.util.ArrayList<ActividadDTO>());
            request.setAttribute("actividadesHtml", "");
        }

        request.getRequestDispatcher("/WEB-INF/create-departure.jsp").forward(request, response);
    }
}
