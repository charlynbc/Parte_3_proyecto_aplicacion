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

// SOAP stubs
import uy.edu.pa.central.client.TurismoService;
import uy.edu.pa.central.client.TurismoWebService;

@WebServlet("/create-activity")
@MultipartConfig(
    maxFileSize = 1024 * 1024 * 5,      // 5MB max
    maxRequestSize = 1024 * 1024 * 10,  // 10MB max
    fileSizeThreshold = 1024 * 1024     // 1MB threshold
)
public class CreateActivityServlet extends HttpServlet {
    
    @Override
    public void init() throws ServletException {
        super.init();
        System.out.println("CreateActivityServlet initialized (SOAP mode)");
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Check if user is logged in and is a provider
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("username") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        String userType = (String) session.getAttribute("userType");
        if (!"Proveedor".equals(userType)) {
            request.setAttribute("error", "Solo los proveedores pueden crear actividades");
            request.getRequestDispatcher("/WEB-INF/dashboard.jsp").forward(request, response);
            return;
        }
        
        request.getRequestDispatcher("/WEB-INF/create-activity.jsp").forward(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        System.out.println("\n=== Create Activity (SOAP) ===");
        
        // Check authentication
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("username") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        String proveedorNickname = (String) session.getAttribute("username");
        String userType = (String) session.getAttribute("userType");
        
        if (!"Proveedor".equals(userType)) {
            request.setAttribute("error", "Solo los proveedores pueden crear actividades");
            request.getRequestDispatcher("/WEB-INF/dashboard.jsp").forward(request, response);
            return;
        }
        
        try {
            // Get form parameters
            String nombre = request.getParameter("nombre");
            String descripcion = request.getParameter("descripcion");
            String duracionStr = request.getParameter("duracion");
            String costoStr = request.getParameter("costo");
            String ciudad = request.getParameter("ciudad");
            
            System.out.println("1. Activity data received:");
            System.out.println("- Nombre: " + nombre);
            System.out.println("- Ciudad: " + ciudad);
            System.out.println("- Proveedor: " + proveedorNickname);
            
            // Basic validation
            if (nombre == null || nombre.trim().isEmpty() ||
                descripcion == null || descripcion.trim().isEmpty() ||
                ciudad == null || ciudad.trim().isEmpty() ||
                duracionStr == null || duracionStr.trim().isEmpty() ||
                costoStr == null || costoStr.trim().isEmpty()) {
                
                request.setAttribute("error", "Todos los campos son obligatorios");
                request.getRequestDispatcher("/WEB-INF/create-activity.jsp").forward(request, response);
                return;
            }
            
            // Parse numeric fields
            int duracion;
            float costo;
            try {
                duracion = Integer.parseInt(duracionStr.trim());
                costo = Float.parseFloat(costoStr.trim());
                
                if (duracion <= 0 || costo <= 0) {
                    throw new NumberFormatException("Values must be positive");
                }
            } catch (NumberFormatException e) {
                request.setAttribute("error", "Duración y costo deben ser números válidos y positivos");
                request.getRequestDispatcher("/WEB-INF/create-activity.jsp").forward(request, response);
                return;
            }
            
            System.out.println("2. Parsed data:");
            System.out.println("- Duracion: " + duracion + " horas");
            System.out.println("- Costo: $" + costo);
            
            // Procesar imagen si se subió
            String imagenBase64 = null;
            Part filePart = request.getPart("activityImage");
            if (filePart != null && filePart.getSize() > 0) {
                System.out.println("3. Processing image - size: " + filePart.getSize());
                try (InputStream fileContent = filePart.getInputStream()) {
                    byte[] imageBytes = fileContent.readAllBytes();
                    imagenBase64 = Base64.getEncoder().encodeToString(imageBytes);
                    System.out.println("3. Image encoded to Base64 (length: " + imagenBase64.length() + ")");
                }
            }
            
            // Fecha de alta (hoy)
            String fechaAlta = new java.text.SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date());
            
            System.out.println("4. Calling SOAP crearActividad");
            
            // Try to create the activity using SOAP
            try {
                TurismoService service = new TurismoService();
                TurismoWebService port = service.getTurismoWebServicePort();
                
                boolean exito = port.crearActividad(
                    nombre.trim(),
                    descripcion.trim(),
                    duracion,
                    costo,
                    ciudad.trim(),
                    proveedorNickname,
                    fechaAlta,
                    imagenBase64
                );
                
                if (exito) {
                    System.out.println("5. Activity created successfully via SOAP");
                    
                    // Set success message and redirect to dashboard
                    session.setAttribute("successMessage", 
                        "¡Actividad '" + nombre + "' creada exitosamente!");
                    response.sendRedirect(request.getContextPath() + "/dashboard");
                } else {
                    System.out.println("5. Activity creation failed via SOAP");
                    request.setAttribute("error", "Ya existe una actividad con ese nombre o el proveedor no existe.");
                    request.getRequestDispatcher("/WEB-INF/create-activity.jsp").forward(request, response);
                }
                
            } catch (Exception e) {
                System.out.println("5. Activity creation failed - SOAP error: " + e.getMessage());
                e.printStackTrace();
                request.setAttribute("error", "Error al crear la actividad: " + e.getMessage());
                request.getRequestDispatcher("/WEB-INF/create-activity.jsp").forward(request, response);
            }
            
        } catch (Exception e) {
            System.err.println("Error during activity creation: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("error", "Error al crear la actividad: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/create-activity.jsp").forward(request, response);
        }
    }
}
