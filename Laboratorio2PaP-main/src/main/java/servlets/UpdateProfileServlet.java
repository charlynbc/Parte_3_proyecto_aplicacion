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
import java.text.SimpleDateFormat;
import java.util.Date;

// Import classes from Laboratorio1.jar
import logica.Fabrica;
import logica.IControladorUsuario;
import logica.DataUsuario;
import logica.DataTurista;
import logica.DataProveedor;
import excepciones.UsuarioNoExisteException;




@WebServlet(name = "UpdateProfileServlet", urlPatterns = {"/edit-profile"})
@MultipartConfig(
    maxFileSize = 1024 * 1024 * 5,      // 5MB max file size
    maxRequestSize = 1024 * 1024 * 10,  // 10MB max request size
    fileSizeThreshold = 1024 * 1024     // 1MB size threshold
)
public class UpdateProfileServlet extends HttpServlet {

    private IControladorUsuario controladorUsuario;
    
    @Override
    public void init() throws ServletException {
        super.init();
        try {
            controladorUsuario = Fabrica.getInstance().getIControladorUsuario();
            System.out.println("UpdateProfileServlet initialized");
        } catch (Exception e) {
            System.err.println("Error initializing UpdateProfileServlet: " + e.getMessage());
            e.printStackTrace();
            throw new ServletException("Failed to initialize controller", e);
        }
    }

    @Override 
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
        throws ServletException, IOException {
        
        System.out.println("\n=== Edit Profile Form Request ===");
        
        // Verificar que el usuario esté logueado
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("username") == null) {
            System.out.println("User not logged in, redirecting to login");
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        String username = (String) session.getAttribute("username");
        System.out.println("Loading edit form for user: " + username);
        
        try {
            // Obtener datos actuales del usuario desde el sistema
            DataUsuario usuario = controladorUsuario.verInfoUsuario(username);
            
            // Pasar datos actuales al formulario
            request.setAttribute("currentUser", usuario);
            
            // Cargar datos específicos según tipo de usuario
            if (usuario instanceof DataTurista) {
                DataTurista turista = (DataTurista) usuario;
                request.setAttribute("nacionalidad", turista.getNacionalidad());
            } else if (usuario instanceof DataProveedor) {
                DataProveedor proveedor = (DataProveedor) usuario;
                request.setAttribute("descripcion", proveedor.getDescripcion());
                request.setAttribute("sitioWeb", proveedor.getSitioWeb());
            }
            
            // Forward al JSP de edición
            request.getRequestDispatcher("/WEB-INF/edit-profile.jsp").forward(request, response);
            
        } catch (UsuarioNoExisteException e) {
            System.err.println("User not found for editing: " + username);
            session.setAttribute("error", "Usuario no encontrado");
            response.sendRedirect(request.getContextPath() + "/user-profile");
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
        
        System.out.println("\n=== Update Profile Request ===");
        
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("username") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        String username = (String) session.getAttribute("username");
        String userType = (String) session.getAttribute("userType");
        
        System.out.println("Updating profile for: " + username + " (" + userType + ")");
        
        try {
            // Verificar que sea multipart
            if (!request.getContentType().startsWith("multipart/form-data")) {
                throw new ServletException("Form must be multipart/form-data");
            }
            
            // Obtener parámetros desde partes
            String nombre = getPartValue(request, "firstName");
            String apellido = getPartValue(request, "lastName");
            String fechaNacimiento = getPartValue(request, "birthDate");
            
            System.out.println("=== DEBUG FORM DATA ===");
            System.out.println("firstName: " + nombre);
            System.out.println("lastName: " + apellido);
            System.out.println("birthDate: " + fechaNacimiento);
            System.out.println("userType: " + userType);
            
            // Validaciones básicas
            if (nombre == null || nombre.trim().isEmpty() || 
                apellido == null || apellido.trim().isEmpty() ||
                fechaNacimiento == null || fechaNacimiento.trim().isEmpty()) {
                
                System.out.println("Validation failed - required fields missing");
                request.setAttribute("error", "Todos los campos obligatorios deben ser completados");
                doGet(request, response);
                return;
            }
            
            // Obtener usuario actual
            DataUsuario usuarioActual = controladorUsuario.verInfoUsuario(username);
            
            // Procesar según tipo de usuario
            if ("Turista".equals(userType)) {
                String nacionalidad = getPartValue(request, "nationality");
                System.out.println("nationality: " + nacionalidad);
                
                if (nacionalidad == null || nacionalidad.trim().isEmpty()) {
                    request.setAttribute("error", "La nacionalidad es obligatoria para turistas");
                    doGet(request, response);
                    return;
                }
                
                // Actualizar campos
                controladorUsuario.modificarUsuario(usuarioActual, 1, nombre);
                controladorUsuario.modificarUsuario(usuarioActual, 2, apellido);
                
                String fechaFormateada = convertDateFormat(fechaNacimiento);
                controladorUsuario.modificarUsuario(usuarioActual, 3, fechaFormateada);
                
                controladorUsuario.modificarUsuario(usuarioActual, 4, nacionalidad);
                
                // Actualizar sesión
                session.setAttribute("fullName", nombre + " " + apellido);
                session.setAttribute("nacionalidad", nacionalidad);
                
            } else if ("Proveedor".equals(userType)) {
                String descripcion = getPartValue(request, "description");
                String sitioWeb = getPartValue(request, "website");
                
                System.out.println("description: " + descripcion);
                System.out.println("website: " + sitioWeb);
                
                if (descripcion == null || descripcion.trim().isEmpty()) {
                    request.setAttribute("error", "La descripción es obligatoria para proveedores");
                    doGet(request, response);
                    return;
                }
                
                // Actualizar campos
                controladorUsuario.modificarUsuario(usuarioActual, 1, nombre);
                controladorUsuario.modificarUsuario(usuarioActual, 2, apellido);
                
                String fechaFormateada = convertDateFormat(fechaNacimiento);
                controladorUsuario.modificarUsuario(usuarioActual, 3, fechaFormateada);
                
                controladorUsuario.modificarUsuario(usuarioActual, 4, sitioWeb != null ? sitioWeb : "");
                controladorUsuario.modificarUsuario(usuarioActual, 5, descripcion);
                
                // Actualizar sesión
                session.setAttribute("fullName", nombre + " " + apellido);
                session.setAttribute("descripcion", descripcion);
                session.setAttribute("sitioWeb", sitioWeb);
            }
            
            // Procesar imagen si se subió
            Part filePart = request.getPart("profileImage");
            if (filePart != null && filePart.getSize() > 0) {
                System.out.println("Processing uploaded image - size: " + filePart.getSize());
                // Aquí va tu lógica para guardar la imagen
            }
            
            // Actualizar datos básicos en sesión
            session.setAttribute("firstName", nombre);
            session.setAttribute("lastName", apellido);
            session.setAttribute("birthDate", fechaNacimiento);
            
            System.out.println("Profile updated successfully for: " + username);
            session.setAttribute("successMessage", "Perfil actualizado correctamente");
            
            response.sendRedirect(request.getContextPath() + "/user-profile");
            
        } catch (Exception e) {
            System.err.println("Error updating profile: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("error", "Error al actualizar el perfil: " + e.getMessage());
            doGet(request, response);
        }
    }

    /**
     * Obtiene el valor de un part como String
     */
    private String getPartValue(HttpServletRequest request, String partName) {
        try {
            Part part = request.getPart(partName);
            if (part != null && part.getSize() > 0) {
                InputStream is = part.getInputStream();
                byte[] bytes = new byte[(int) part.getSize()];
                is.read(bytes);
                return new String(bytes, "UTF-8").trim();
            }
        } catch (Exception e) {
            System.err.println("Error getting part '" + partName + "': " + e.getMessage());
        }
        return null;
    }

    /**
     * Convierte formato de fecha de yyyy-MM-dd a dd/MM/yyyy
     */
    private String convertDateFormat(String dateString) {
        try {
            if (dateString == null || dateString.trim().isEmpty()) {
                return "";
            }
            java.text.SimpleDateFormat inputFormat = new java.text.SimpleDateFormat("yyyy-MM-dd");
            java.util.Date date = inputFormat.parse(dateString);
            
            java.text.SimpleDateFormat outputFormat = new java.text.SimpleDateFormat("dd/MM/yyyy");
            return outputFormat.format(date);
        } catch (Exception e) {
            System.err.println("Error converting date format: " + dateString + " - " + e.getMessage());
            return "";
        }
    }
    
    /**
     * Procesa la imagen subida y devuelve el nombre del archivo
     */
    private String processUploadedImage(Part filePart, String username) throws IOException {
        String fileName = getFileName(filePart);
        if (fileName != null && !fileName.trim().isEmpty()) {
            // Generar nombre único para evitar conflictos
            String fileExtension = fileName.substring(fileName.lastIndexOf("."));
            String uniqueFileName = username + "_profile" + fileExtension;
            
            // Guardar archivo en el servidor (implementación dependiente de tu setup)
            // InputStream fileContent = filePart.getInputStream();
            // ... lógica para guardar archivo ...
            
            System.out.println("Image uploaded: " + uniqueFileName);
            return uniqueFileName;
        }
        return null;
    }
    
    /**
     * Extrae el nombre del archivo del Part
     */
    private String getFileName(Part part) {
        String contentDisp = part.getHeader("content-disposition");
        String[] tokens = contentDisp.split(";");
        for (String token : tokens) {
            if (token.trim().startsWith("filename")) {
                return token.substring(token.indexOf("=") + 2, token.length() - 1);
            }
        }
        return null;
    }
    
    /**
     * Convierte String en formato "yyyy-MM-dd" a java.util.Date
     */
    private Date convertStringToDate(String dateString) {
        try {
            if (dateString == null || dateString.trim().isEmpty()) {
                return null;
            }
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            return dateFormat.parse(dateString);
        } catch (Exception e) {
            System.err.println("Error converting date: " + dateString + " - " + e.getMessage());
            return null;
        }
    }
    
 
}