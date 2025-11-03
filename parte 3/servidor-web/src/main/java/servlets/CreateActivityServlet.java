package servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

// Import classes from Laboratorio1.jar
import logica.Fabrica;
import logica.IControladorActividad;
import logica.DataActividad;
import excepciones.ActividadRepetidaException;
import excepciones.UsuarioNoExisteException;

@WebServlet("/create-activity")
public class CreateActivityServlet extends HttpServlet {
    
    private IControladorActividad controladorActividad;
    
    @Override
    public void init() throws ServletException {
        super.init();
        try {
            controladorActividad = Fabrica.getInstance().getIControladorActividad();
            System.out.println("CreateActivityServlet initialized - connected to Central Server");
        } catch (Exception e) {
            throw new ServletException("Failed to initialize central server connection", e);
        }
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
        
        System.out.println("\n=== Create Activity Debug Info ===");
        
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
            
            // Create DataActividad object
            Date fechaAlta = new Date(); // Current date
            String estado = "Agregada"; // Initial state as per requirements
            
            DataActividad nuevaActividad = new DataActividad(
                nombre.trim(),
                descripcion.trim(),
                duracion,
                costo,
                ciudad.trim(),
                fechaAlta,
                estado,
                proveedorNickname
            );
            
            System.out.println("3. DataActividad object created with estado: " + estado);
            
            // Try to create the activity using Central Server
            try {
                controladorActividad.altaActividad(nuevaActividad);
                System.out.println("4. Activity created successfully in database");
                
                // Set success message and redirect to dashboard
                session.setAttribute("successMessage", 
                    "¡Actividad '" + nombre + "' creada exitosamente! Estado: " + estado);
                response.sendRedirect(request.getContextPath() + "/dashboard");
                
            } catch (ActividadRepetidaException e) {
                System.out.println("4. Activity creation failed - duplicate name: " + e.getMessage());
                request.setAttribute("error", "Ya existe una actividad con ese nombre. Por favor elija otro nombre.");
                request.getRequestDispatcher("/WEB-INF/create-activity.jsp").forward(request, response);
            } catch (UsuarioNoExisteException e) {
                System.out.println("4. Activity creation failed - provider not found: " + e.getMessage());
                request.setAttribute("error", "Error: Proveedor no encontrado en el sistema.");
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