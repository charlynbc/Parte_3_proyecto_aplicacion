package servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import logica.DataProveedor;
import logica.DataUsuario;
import logica.Fabrica;
import logica.IControladorUsuario;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import excepciones.UsuarioRepetidoException;

@WebServlet("/register-provider")
@MultipartConfig(
    maxFileSize = 5 * 1024 * 1024,      // 5MB máximo
    maxRequestSize = 10 * 1024 * 1024,  // 10MB máximo total
    fileSizeThreshold = 1024 * 1024     // 1MB en memoria
)
public class RegisterProviderServlet extends HttpServlet {
    
    private IControladorUsuario controladorUsuario;
    
    @Override
    public void init() throws ServletException {
        super.init();
        // Initialize the business logic controller from Laboratorio1.jar
        controladorUsuario = Fabrica.getInstance().getIControladorUsuario();
        System.out.println("RegisterProviderServlet initialized - connected to Laboratorio1.jar persistence");
    }
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException{
        request.getRequestDispatcher("/WEB-INF/register-provider.jsp").forward(request, response);
    }
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException{
        
        System.out.println("\n=== Register Provider Debug Info ===");
        
        try {
            // Validación de datos con tu método existente
            Map<String, String> errores = validarDatos(request);
            
            if (!errores.isEmpty()) {
                // Convertir el mapa de errores en un mensaje único
                StringBuilder mensajeError = new StringBuilder("Errores en el formulario: ");
                for (Map.Entry<String, String> error : errores.entrySet()) {
                    mensajeError.append(error.getValue()).append(". ");
                }
                request.setAttribute("error", mensajeError.toString());
                
                // Mantener los valores ingresados
                request.setAttribute("valoresFormulario", obtenerValoresFormulario(request));
                request.getRequestDispatcher("/WEB-INF/register-provider.jsp").forward(request, response);
                return;
            }
            
            Map<String, String> datos = obtenerValoresFormulario(request);
            
            String birthDateStr = getParameterSafe(request, "birthDate");
            Date birthDate;
            
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            birthDate = sdf.parse(birthDateStr);
            
            // Process image if provided
            byte[] imageData = null;
            Part imagePart = request.getPart("profileImage");
            if (imagePart != null && imagePart.getSize() > 0) {
                try (InputStream is = imagePart.getInputStream()) {
                    imageData = is.readAllBytes();
                    System.out.println("3. Image uploaded: " + imageData.length + " bytes");
                }
            }
            
            // Handle optional website field
            String sitioWeb = (datos.get("website") != null && !datos.get("website").trim().isEmpty()) ? datos.get("website").trim() : null;
            
            DataUsuario usu;
            usu = new DataProveedor(
                getParameterSafe(request, "password"),
                datos.get("nickname"),
                datos.get("firstName"),
                datos.get("lastName"),
                datos.get("email"),
                birthDate,
                sitioWeb,
                datos.get("description")
            );
            
            System.out.println("4. DataProveedor object created");

            try {
                controladorUsuario.registrarUsuario(usu);
                System.out.println("5. Provider registered successfully in database");
                
                // Set success message and redirect to login
                request.getSession().setAttribute("successMessage", 
                    "¡Registro exitoso! Bienvenido " + datos.get("firstName") + ", por favor inicia sesión");
                response.sendRedirect(request.getContextPath() + "/login");
                
            } catch (UsuarioRepetidoException ex) {
                System.out.println("5. Registration failed - user already exists: " + ex.getMessage());
                request.setAttribute("error", "El nickname o email ya está registrado. Por favor elija otro.");
                request.getRequestDispatcher("/WEB-INF/register-provider.jsp").forward(request, response);
            }
            
        } catch (Exception e) {
            System.err.println("Error during provider registration: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("error", "Error al registrar: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/register-provider.jsp").forward(request, response);
        }
    }
    

    private Map<String, String> validarDatos(HttpServletRequest request) {
        Map<String, String> errores = new HashMap<>();
        
        try {
            
            String nickname = getParameterSafe(request, "nickname");
            if (nickname == null || nickname.trim().isEmpty()) {
                errores.put("nickname", "El nickname es obligatorio");
            } else if (nickname.length() < 3) {
                errores.put("nickname", "El nickname debe tener al menos 3 caracteres");
            } else if (nickname.length() > 50) {
                errores.put("nickname", "El nickname no puede exceder 50 caracteres");
            }
            
            String email = getParameterSafe(request, "email");
            if (email == null || email.trim().isEmpty()) {
                errores.put("email", "El email es obligatorio");
            } else if (!isValidEmail(email)) {
                errores.put("email", "El formato de email no es válido");
            } else if (email.length() > 100) {
                errores.put("email", "El email no puede exceder 100 caracteres");
            }
            
            String firstName = getParameterSafe(request, "firstName");
            if (firstName == null || firstName.trim().isEmpty()) {
                errores.put("firstName", "El nombre es obligatorio");
            } else if (firstName.length() > 50) {
                errores.put("firstName", "El nombre no puede exceder 50 caracteres");
            }
            
            String lastName = getParameterSafe(request, "lastName");
            if (lastName == null || lastName.trim().isEmpty()) {
                errores.put("lastName", "El apellido es obligatorio");
            } else if (lastName.length() > 50) {
                errores.put("lastName", "El apellido no puede exceder 50 caracteres");
            }
            
            String password = getParameterSafe(request, "password");
            String confirmPassword = getParameterSafe(request, "confirmPassword");
            
            if (password == null || password.trim().isEmpty()) {
                errores.put("password", "La contraseña es obligatoria");
            } else if (password.length() < 6) {
                errores.put("password", "La contraseña debe tener al menos 6 caracteres");
            } else if (password.length() > 100) {
                errores.put("password", "La contraseña no puede exceder 100 caracteres");
            } else if (!isPasswordSecure(password)) {
                errores.put("password", "La contraseña debe contener letras y números");
            }
            
            if (confirmPassword == null || confirmPassword.trim().isEmpty()) {
                errores.put("confirmPassword", "Debe confirmar la contraseña");
            } else if (!password.equals(confirmPassword)) {
                errores.put("confirmPassword", "Las contraseñas no coinciden");
            }
            
            String description = getParameterSafe(request, "description");
            if (description == null || description.trim().isEmpty()) {
                errores.put("description", "La descripción es obligatoria");
            } else if (description.length() < 10) {
                errores.put("description", "La descripción debe tener al menos 10 caracteres");
            } else if (description.length() > 500) {
                errores.put("description", "La descripción no puede exceder 500 caracteres");
            }
            
            String website = getParameterSafe(request, "website");
            if (website != null && !website.trim().isEmpty()) {
                if (!isValidUrl(website)) {
                    errores.put("website", "El formato de URL no es válido");
                } else if (website.length() > 200) {
                    errores.put("website", "La URL no puede exceder 200 caracteres");
                }
            }
            
            Part profileImagePart = request.getPart("profileImage");
            if (profileImagePart != null && profileImagePart.getSize() > 0) {
                if (!validarImagen(profileImagePart)) {
                    errores.put("profileImage", "La imagen debe ser JPG/PNG y menor a 5MB");
                }
            }
            
            String birthDateStr = getParameterSafe(request, "birthDate");
            if (birthDateStr == null || birthDateStr.trim().isEmpty()) {
                errores.put("birthDate", "La fecha de nacimiento es obligatoria");
            }
            
        } catch (Exception e) {
            errores.put("general", "Error en la validación de datos: " + e.getMessage());
        }
        
        return errores;
    }
    
    private Map<String, String> obtenerValoresFormulario(HttpServletRequest request) {
        Map<String, String> valores = new HashMap<>();
        valores.put("nickname", getParameterSafe(request, "nickname"));
        valores.put("email", getParameterSafe(request, "email"));
        valores.put("firstName", getParameterSafe(request, "firstName"));
        valores.put("lastName", getParameterSafe(request, "lastName"));
        valores.put("description", getParameterSafe(request, "description"));
        valores.put("website", getParameterSafe(request, "website"));
        return valores;
    }

    // METODOS AUXILIARES:

    private String getParameterSafe(HttpServletRequest request, String paramName) {
        try {
            String value = request.getParameter(paramName);
            return value != null ? value.trim() : null;
        } catch (Exception e) {
            return null;
        }
    }

    private boolean isValidEmail(String email) {
        if (email == null) return false;
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        return email.matches(emailRegex);
    }

    private boolean isValidUrl(String url) {
        if (url == null || url.trim().isEmpty()) return true;
        try {
            // Aceptar URLs sin http:// (agregarlo temporalmente para validación)
            String testUrl = url.toLowerCase();
            if (!testUrl.startsWith("http://") && !testUrl.startsWith("https://")) {
                testUrl = "https://" + url;
            }
            new java.net.URL(testUrl);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private boolean isPasswordSecure(String password) {
        // Al menos una letra y un número
        boolean hasLetter = password.matches(".*[a-zA-Z].*");
        boolean hasDigit = password.matches(".*\\d.*");
        return hasLetter && hasDigit;
    }

    private boolean validarImagen(Part filePart) {
        if (filePart == null || filePart.getSize() == 0) {
            return true; // No hay imagen, no es error
        }
        
        // Validar tamaño (máximo 5MB)
        if (filePart.getSize() > 5 * 1024 * 1024) {
            return false;
        }
        
        // Validar tipo de archivo
        String mimeType = filePart.getContentType();
        if (mimeType == null || 
            (!mimeType.equals("image/jpeg") && 
             !mimeType.equals("image/jpg") && 
             !mimeType.equals("image/png") &&
             !mimeType.equals("image/gif"))) {
            return false;
        }
        
        // Validar nombre de archivo
        String fileName = getFileName(filePart);
        if (fileName != null) {
            String extension = fileName.toLowerCase();
            if (!extension.endsWith(".jpg") && 
                !extension.endsWith(".jpeg") && 
                !extension.endsWith(".png") &&
                !extension.endsWith(".gif")) {
                return false;
            }
        }
        
        return true;
    }

    private String getFileName(Part part) {
        String contentDisposition = part.getHeader("content-disposition");
        String[] tokens = contentDisposition.split(";");
        for (String token : tokens) {
            if (token.trim().startsWith("filename")) {
                return token.substring(token.indexOf('=') + 1).trim().replace("\"", "");
            }
        }
        return null;
    }
}