package servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;

import uy.edu.pa.central.client.TurismoService;
import uy.edu.pa.central.client.TurismoWebService;
import uy.edu.pa.central.client.ActividadDTO;

@WebServlet("/edit-activity")
@MultipartConfig(
    maxFileSize = 1024 * 1024 * 5,
    maxRequestSize = 1024 * 1024 * 10,
    fileSizeThreshold = 1024 * 1024
)
public class EditActivityServlet extends HttpServlet {
    
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
        
        String activityName = request.getParameter("name");
        if (activityName == null || activityName.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/my-activities");
            return;
        }
        
        try {
            TurismoService service = new TurismoService();
            TurismoWebService port = service.getTurismoWebServicePort();
            
            ActividadDTO actividad = port.obtenerDetalleActividad(activityName);
            
            if (actividad == null) {
                request.setAttribute("error", "Actividad no encontrada");
                response.sendRedirect(request.getContextPath() + "/my-activities");
                return;
            }
            
            request.setAttribute("actividad", actividad);
            request.getRequestDispatcher("/WEB-INF/edit-activity.jsp").forward(request, response);
            
        } catch (Exception e) {
            System.err.println("Error loading activity: " + e.getMessage());
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/my-activities");
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
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
        
        try {
            String nombre = request.getParameter("nombre");
            String descripcion = request.getParameter("descripcion");
            String duracionStr = request.getParameter("duracion");
            String costoStr = request.getParameter("costo");
            String ciudad = request.getParameter("ciudad");
            
            // Validación básica
            if (nombre == null || nombre.trim().isEmpty() ||
                descripcion == null || descripcion.trim().isEmpty() ||
                ciudad == null || ciudad.trim().isEmpty() ||
                duracionStr == null || duracionStr.trim().isEmpty() ||
                costoStr == null || costoStr.trim().isEmpty()) {
                
                request.setAttribute("error", "Todos los campos son obligatorios");
                doGet(request, response);
                return;
            }
            
            int duracion = Integer.parseInt(duracionStr.trim());
            float costo = Float.parseFloat(costoStr.trim());
            
            // Procesar imagen si se subió una nueva
            String imagenBase64 = null;
            Part filePart = request.getPart("activityImage");
            if (filePart != null && filePart.getSize() > 0) {
                try (InputStream fileContent = filePart.getInputStream()) {
                    byte[] imageBytes = fileContent.readAllBytes();
                    imagenBase64 = Base64.getEncoder().encodeToString(imageBytes);
                }
            }
            
            TurismoService service = new TurismoService();
            TurismoWebService port = service.getTurismoWebServicePort();
            
            boolean exito = port.modificarActividad(nombre, descripcion, duracion, costo, ciudad, imagenBase64);
            
            if (exito) {
                session.setAttribute("successMessage", "Actividad actualizada exitosamente");
                response.sendRedirect(request.getContextPath() + "/my-activities");
            } else {
                request.setAttribute("error", "No se pudo actualizar la actividad");
                doGet(request, response);
            }
            
        } catch (Exception e) {
            System.err.println("Error updating activity: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("error", "Error al actualizar la actividad: " + e.getMessage());
            doGet(request, response);
        }
    }
}
