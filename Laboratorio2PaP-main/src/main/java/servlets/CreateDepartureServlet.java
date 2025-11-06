package servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;

// SOAP stubs
import uy.edu.pa.central.client.ActividadesService_Service;
import uy.edu.pa.central.client.ActividadesService;

@WebServlet("/CreateDepartureServlet")
@MultipartConfig
public class CreateDepartureServlet extends HttpServlet {
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        System.out.println("\n=== Create Departure (SOAP) ===");
        
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("username") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String tipoUsuario = (String) session.getAttribute("tipoUsuario");
        if (tipoUsuario == null || !"proveedor".equalsIgnoreCase(tipoUsuario)) {
            request.setAttribute("error", "Solo los proveedores pueden crear salidas turísticas.");
            request.getRequestDispatcher("/WEB-INF/dashboard.jsp").forward(request, response);
            return;
        }

        String proveedor = (String) session.getAttribute("username");
        
        try {
            String activityId = request.getParameter("activityId");
            String fechaStr = request.getParameter("departureDate");
            String horaStr = request.getParameter("departureTime");
            String lugar = request.getParameter("meetingPlace");
            String cupoStr = request.getParameter("maxTourists");
            String nombreSalida = request.getParameter("nombreSalida");
            Part imagenPart = request.getPart("departureImage");

            System.out.println("1. Departure data received:");
            System.out.println("- Activity: " + activityId);
            System.out.println("- Fecha: " + fechaStr);
            System.out.println("- Lugar: " + lugar);

            // Validaciones básicas
            if (activityId == null || fechaStr == null || horaStr == null || 
                lugar == null || cupoStr == null ||
                activityId.isEmpty() || fechaStr.isEmpty() || horaStr.isEmpty() || 
                lugar.isEmpty() || cupoStr.isEmpty()) {
                
                request.setAttribute("error", "Todos los campos obligatorios deben estar completos.");
                request.getRequestDispatcher("/WEB-INF/create-departure.jsp").forward(request, response);
                return;
            }

            // Validar y parsear
            LocalDate fecha = LocalDate.parse(fechaStr);
            LocalTime hora = LocalTime.parse(horaStr);
            int cupo = Integer.parseInt(cupoStr);
            
            // Generar nombre si no se proporciona
            if (nombreSalida == null || nombreSalida.trim().isEmpty()) {
                nombreSalida = activityId + "_" + fechaStr + "_" + horaStr.replace(":", "");
            }
            
            String fechaAlta = new java.text.SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date());

            System.out.println("2. Parsed data:");
            System.out.println("- Nombre salida: " + nombreSalida);
            System.out.println("- Cupo: " + cupo);
            
            System.out.println("3. Calling SOAP crearSalida");

            // Crear salida via SOAP
            ActividadesService_Service service = new ActividadesService_Service();
            ActividadesService port = service.getActividadesServicePort();
            
            boolean exito = port.crearSalida(
                nombreSalida,
                fechaStr,
                horaStr,
                lugar,
                cupo,
                fechaAlta,
                activityId
            );

            if (exito) {
                System.out.println("4. Departure created successfully via SOAP");
                session.setAttribute("successMessage", "Salida turística creada exitosamente.");
                response.sendRedirect(request.getContextPath() + "/dashboard");
            } else {
                System.out.println("4. Departure creation failed via SOAP");
                request.setAttribute("error", "Error al crear la salida. Verifique que la actividad existe.");
                request.getRequestDispatcher("/WEB-INF/create-departure.jsp").forward(request, response);
            }

        } catch (NumberFormatException e) {
            System.err.println("Invalid number format: " + e.getMessage());
            request.setAttribute("error", "El cupo debe ser un número válido.");
            request.getRequestDispatcher("/WEB-INF/create-departure.jsp").forward(request, response);
        } catch (Exception e) {
            System.err.println("Error creating departure: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("error", "Error al crear la salida: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/create-departure.jsp").forward(request, response);
        }
    }
}
