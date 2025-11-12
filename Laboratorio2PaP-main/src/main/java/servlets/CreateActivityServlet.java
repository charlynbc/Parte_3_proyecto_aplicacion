package servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

// SOAP stubs
import uy.edu.pa.central.client.TurismoService;
import uy.edu.pa.central.client.TurismoWebService;

@WebServlet("/create-activity")
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
            
            // Fecha de alta (hoy)
            String fechaAlta = new java.text.SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date());
            
            System.out.println("3. Calling SOAP crearActividad");
            
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
                    fechaAlta
                );
                
                if (exito) {
                    System.out.println("4. Activity created successfully via SOAP");
                    
                    // Set success message and redirect to dashboard
                    session.setAttribute("successMessage", 
                        "¡Actividad '" + nombre + "' creada exitosamente!");
                    response.sendRedirect(request.getContextPath() + "/dashboard");
                } else {
                    System.out.println("4. Activity creation failed via SOAP");
                    request.setAttribute("error", "Ya existe una actividad con ese nombre o el proveedor no existe.");
                    request.getRequestDispatcher("/WEB-INF/create-activity.jsp").forward(request, response);
                }
                
            } catch (Exception e) {
                System.out.println("4. Activity creation failed - SOAP error: " + e.getMessage());
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
