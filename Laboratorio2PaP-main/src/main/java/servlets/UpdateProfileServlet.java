package servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

// SOAP stubs
import uy.edu.pa.central.client.TurismoService;
import uy.edu.pa.central.client.TurismoWebService;
import uy.edu.pa.central.client.UserDTO;

@WebServlet(name = "UpdateProfileServlet", urlPatterns = {"/edit-profile"})
@MultipartConfig
public class UpdateProfileServlet extends HttpServlet {

    @Override 
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
        throws ServletException, IOException {
        
        System.out.println("\n=== Edit Profile Form (SOAP) ===");
        
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("username") == null) {
            System.out.println("User not logged in, redirecting to login");
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        String username = (String) session.getAttribute("username");
        System.out.println("Loading edit form for user: " + username);
        
        try {
            // Obtener usuario via SOAP
            TurismoService service = new TurismoService();
            TurismoWebService port = service.getTurismoWebServicePort();
            
            UserDTO usuario = port.obtenerUsuario(username);
            
            if (usuario == null) {
                session.setAttribute("error", "Usuario no encontrado");
                response.sendRedirect(request.getContextPath() + "/user-profile");
                return;
            }
            
            request.setAttribute("currentUser", usuario);
            request.getRequestDispatcher("/WEB-INF/edit-profile.jsp").forward(request, response);
            
        } catch (Exception e) {
            System.err.println("Error loading user data for editing: " + e.getMessage());
            e.printStackTrace();
            session.setAttribute("error", "Error al cargar datos del usuario");
            response.sendRedirect(request.getContextPath() + "/user-profile");
        }
    }
    
    @Override 
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
        throws ServletException, IOException {
        
        System.out.println("\n=== Update Profile (SOAP) ===");
        
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("username") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        String username = (String) session.getAttribute("username");
        
        try {
            // Obtener parámetros del formulario
            String nombre = request.getParameter("nombre");
            String apellido = request.getParameter("apellido");
            String fechaNacimiento = request.getParameter("fechaNacimiento");
            String nacionalidad = request.getParameter("nacionalidad");
            String descripcion = request.getParameter("descripcion");
            String sitioWeb = request.getParameter("sitioWeb");
            
            // Procesar imagen si se proporciona
            String imagenBase64 = "";
            jakarta.servlet.http.Part imagePart = request.getPart("profileImage");
            if (imagePart != null && imagePart.getSize() > 0) {
                java.io.InputStream imageStream = imagePart.getInputStream();
                java.io.ByteArrayOutputStream buffer = new java.io.ByteArrayOutputStream();
                byte[] data = new byte[1024];
                int nRead;
                while ((nRead = imageStream.read(data, 0, data.length)) != -1) {
                    buffer.write(data, 0, nRead);
                }
                buffer.flush();
                byte[] imageBytes = buffer.toByteArray();
                imagenBase64 = java.util.Base64.getEncoder().encodeToString(imageBytes);
            }
            
            System.out.println("Updating user: " + username);
            
            // Actualizar via SOAP
            TurismoService service = new TurismoService();
            TurismoWebService port = service.getTurismoWebServicePort();
            
            boolean exito = port.editarUsuario(
                username,
                nombre,
                apellido,
                fechaNacimiento,
                nacionalidad != null ? nacionalidad : "",
                descripcion != null ? descripcion : "",
                sitioWeb != null ? sitioWeb : "",
                imagenBase64 // Enviar imagen Base64
            );
            
            if (exito) {
                System.out.println("Profile updated successfully via SOAP");
                session.setAttribute("successMessage", "Perfil actualizado exitosamente");
                response.sendRedirect(request.getContextPath() + "/user-profile");
            } else {
                System.out.println("Profile update failed via SOAP");
                request.setAttribute("error", "Error al actualizar el perfil");
                request.getRequestDispatcher("/WEB-INF/edit-profile.jsp").forward(request, response);
            }
            
        } catch (Exception e) {
            System.err.println("Error updating profile: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("error", "Error al actualizar: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/edit-profile.jsp").forward(request, response);
        }
    }
}
