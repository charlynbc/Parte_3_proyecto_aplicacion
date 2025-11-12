package servlets;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import webservices.*;

@WebServlet("/edit-departure")
@MultipartConfig
public class EditDepartureServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("usuario") == null) {
            response.sendRedirect("login.jsp");
            return;
        }
        
        String departureId = request.getParameter("id");
        if (departureId == null || departureId.isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID de salida requerido");
            return;
        }
        
        try {
            TurismoWebServiceImplService service = new TurismoWebServiceImplService();
            TurismoWebService port = service.getTurismoWebServiceImplPort();
            
            SalidaDTO salida = port.obtenerSalida(departureId);
            if (salida == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Salida no encontrada");
                return;
            }
            
            // Verificar que el usuario es proveedor y dueño de la actividad
            String usuario = (String) session.getAttribute("usuario");
            UserDTO user = port.obtenerUsuario(usuario);
            if (!"PROVEEDOR".equals(user.getTipoUsuario())) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Solo proveedores pueden editar salidas");
                return;
            }
            
            request.setAttribute("salida", salida);
            request.getRequestDispatcher("/WEB-INF/views/edit-departure.jsp").forward(request, response);
            
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, 
                "Error al cargar la salida: " + e.getMessage());
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("usuario") == null) {
            response.sendRedirect("login.jsp");
            return;
        }
        
        String nombre = request.getParameter("nombre");
        String fecha = request.getParameter("fecha");
        String hora = request.getParameter("hora");
        String lugar = request.getParameter("lugar");
        String cantMaxStr = request.getParameter("cantMax");
        
        if (nombre == null || fecha == null || hora == null || lugar == null || cantMaxStr == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Todos los campos son requeridos");
            return;
        }
        
        try {
            int cantMax = Integer.parseInt(cantMaxStr);
            
            // Manejar la imagen opcional
            String imagenBase64 = null;
            Part imagePart = request.getPart("imagen");
            if (imagePart != null && imagePart.getSize() > 0) {
                InputStream imageStream = imagePart.getInputStream();
                ByteArrayOutputStream buffer = new ByteArrayOutputStream();
                byte[] data = new byte[1024];
                int nRead;
                while ((nRead = imageStream.read(data, 0, data.length)) != -1) {
                    buffer.write(data, 0, nRead);
                }
                buffer.flush();
                byte[] imageBytes = buffer.toByteArray();
                imagenBase64 = Base64.getEncoder().encodeToString(imageBytes);
            }
            
            TurismoWebServiceImplService service = new TurismoWebServiceImplService();
            TurismoWebService port = service.getTurismoWebServiceImplPort();
            
            // Convertir fecha de yyyy-MM-dd a dd/MM/yyyy
            String[] partes = fecha.split("-");
            String fechaFormateada = partes[2] + "/" + partes[1] + "/" + partes[0];
            
            boolean exito = port.modificarSalida(nombre, fechaFormateada, hora, lugar, cantMax, 
                                                imagenBase64 == null ? "" : imagenBase64);
            
            if (exito) {
                response.sendRedirect("my-activities.jsp?success=departure-updated");
            } else {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, 
                    "Error al actualizar la salida");
            }
            
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Cantidad máxima inválida");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, 
                "Error al procesar la solicitud: " + e.getMessage());
        }
    }
}
