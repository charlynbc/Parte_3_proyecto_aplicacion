package servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

// Import classes from Laboratorio1.jar
import logica.Fabrica;
import logica.IControladorUsuario;
import logica.DataTurista;
import excepciones.UsuarioRepetidoException;

@WebServlet("/register-tourist")
@MultipartConfig(
    maxFileSize = 1024 * 1024 * 5, // 5MB
    maxRequestSize = 1024 * 1024 * 10 // 10MB
)
public class RegisterTouristServlet extends HttpServlet {
    
    private IControladorUsuario controladorUsuario;
    
    @Override
    public void init() throws ServletException {
        super.init();
        // Initialize the business logic controller from Laboratorio1.jar
        controladorUsuario = Fabrica.getInstance().getIControladorUsuario();
        System.out.println("RegisterTouristServlet initialized - connected to Laboratorio1.jar persistence");
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/register-tourist.jsp").forward(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        System.out.println("\n=== Register Tourist Debug Info ===");
        
        try {
            // 1. Get form parameters
            String nickname = request.getParameter("nickname");
            String firstName = request.getParameter("firstName");
            String lastName = request.getParameter("lastName");
            String email = request.getParameter("email");
            String password = request.getParameter("password");
            String confirmPassword = request.getParameter("confirmPassword");
            String birthDateStr = request.getParameter("birthDate");
            String nationality = request.getParameter("nationality");
            Part imagePart = request.getPart("profileImage");

            System.out.println("1. Form data received:");
            System.out.println("- Nickname: " + nickname);
            System.out.println("- Email: " + email);
            System.out.println("- Nationality: " + nationality);
            
            // 2. Basic validation
            if (!validateRequiredFields(nickname, firstName, lastName, email, password, birthDateStr, nationality)) {
                request.setAttribute("error", "Todos los campos marcados con * son obligatorios");
                request.getRequestDispatcher("/WEB-INF/register-tourist.jsp").forward(request, response);
                return;
            }

            // 3. Validate passwords match
            if (!password.equals(confirmPassword)) {
                request.setAttribute("error", "Las contraseñas no coinciden");
                request.getRequestDispatcher("/WEB-INF/register-tourist.jsp").forward(request, response);
                return;
            }

            // 4. Validate date format and convert
            Date birthDate;
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                birthDate = sdf.parse(birthDateStr);
                System.out.println("2. Birth date parsed: " + birthDate);
            } catch (Exception e) {
                request.setAttribute("error", "Formato de fecha inválido");
                request.getRequestDispatcher("/WEB-INF/register-tourist.jsp").forward(request, response);
                return;
            }

            // 5. Process image if provided (for future enhancement)
            byte[] imageData = null;
            if (imagePart != null && imagePart.getSize() > 0) {
                try (InputStream is = imagePart.getInputStream()) {
                    imageData = is.readAllBytes();
                    System.out.println("3. Image uploaded: " + imageData.length + " bytes");
                }
            }

            // 6. Create DataTurista object
            DataTurista newTurista = new DataTurista(
                password,      // contra
                nickname,      // nickname
                firstName,     // nombre
                lastName,      // apellido
                email,         // email
                birthDate,     // fechaNac
                nationality    // nacionalidad
            );
            
            System.out.println("4. DataTurista object created");

            // 7. Try to register the tourist using Central Server
            try {
                controladorUsuario.registrarUsuario(newTurista);
                System.out.println("5. Tourist registered successfully in database");
                
                // 8. Set success message and redirect to login
                request.getSession().setAttribute("successMessage", 
                    "¡Registro exitoso! Bienvenido " + firstName + ", por favor inicia sesión");
                response.sendRedirect(request.getContextPath() + "/login");
                
            } catch (UsuarioRepetidoException e) {
                System.out.println("5. Registration failed - user already exists: " + e.getMessage());
                request.setAttribute("error", "El nickname o email ya está registrado. Por favor elija otro.");
                request.getRequestDispatcher("/WEB-INF/register-tourist.jsp").forward(request, response);
            }

        } catch (Exception e) {
            System.err.println("Error during tourist registration: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("error", "Error al registrar: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/register-tourist.jsp").forward(request, response);
        }
    }

    private boolean validateRequiredFields(String... fields) {
        for (String field : fields) {
            if (field == null || field.trim().isEmpty()) {
                return false;
            }
        }
        return true;
    }
}