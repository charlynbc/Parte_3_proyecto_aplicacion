package servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

// Import classes from Laboratorio1.jar
import logica.Fabrica;
import logica.IControladorUsuario;
import logica.DataUsuario;
import logica.DataTurista;
import logica.DataProveedor;
import excepciones.UsuarioNoExisteException;

@WebServlet(name = "LoginServlet", urlPatterns = {"/login"})
public class LoginServlet extends HttpServlet {
    
    private IControladorUsuario controladorUsuario;
    
    @Override
    public void init() throws ServletException {
        super.init();
        try {
            // Initialize the business logic controller from Laboratorio1.jar
            controladorUsuario = Fabrica.getInstance().getIControladorUsuario();
            System.out.println("LoginServlet initialized - connected to Laboratorio1.jar persistence");
        } catch (Exception e) {
            System.err.println("Error initializing LoginServlet: " + e.getMessage());
            e.printStackTrace();
            throw new ServletException("Failed to initialize central server connection", e);
        }
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        System.out.println("=== LOGIN SERVLET HIT ===");
        
        // Check if user is already logged in
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("username") != null) {
            response.sendRedirect(request.getContextPath() + "/dashboard");
            return;
        }
        
        request.getRequestDispatcher("/WEB-INF/login.jsp").forward(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        System.out.println("\n=== Login Debug Info (Central Server Integration) ===");
        
        // Get form parameters
        String usernameOrEmail = request.getParameter("username");
        String password = request.getParameter("password");
        
        System.out.println("1. Login attempt:");
        System.out.println("- Username/Email: '" + usernameOrEmail + "'");
        System.out.println("- Password: [PROTECTED]");
        
        // Basic validation
        if (usernameOrEmail == null || usernameOrEmail.trim().isEmpty() || 
            password == null || password.trim().isEmpty()) {
            System.out.println("2. Validation failed - empty fields");
            request.setAttribute("error", "Por favor ingrese todos los campos requeridos");
            request.getRequestDispatcher("/WEB-INF/login.jsp").forward(request, response);
            return;
        }

        try {
            // Clean input
            usernameOrEmail = usernameOrEmail.trim();
            
            System.out.println("2. Attempting authentication with Central Server...");
            
            // Try to authenticate using the central server logic
            DataUsuario usuario = authenticateUser(usernameOrEmail, password);
            
            if (usuario != null) {
                System.out.println("3. Authentication successful:");
                System.out.println("- User: " + usuario.getNickname());
                System.out.println("- Email: " + usuario.getEmail());
                System.out.println("- Type: " + (usuario instanceof DataTurista ? "Turista" : "Proveedor"));
                
                // Create session with user data from database
                HttpSession session = request.getSession(true);
                session.setAttribute("username", usuario.getNickname());
                session.setAttribute("userType", usuario instanceof DataTurista ? "Turista" : "Proveedor");
                session.setAttribute("email", usuario.getEmail());
                session.setAttribute("fullName", usuario.getNombre() + " " + usuario.getApellido());
                
                // Add specific data based on user type
                if (usuario instanceof DataTurista) {
                    session.setAttribute("nacionalidad", ((DataTurista) usuario).getNacionalidad());
                    System.out.println("- Nacionalidad: " + ((DataTurista) usuario).getNacionalidad());
                } else if (usuario instanceof DataProveedor) {
                    session.setAttribute("descripcion", ((DataProveedor) usuario).getDescripcion());
                    session.setAttribute("sitioWeb", ((DataProveedor) usuario).getSitioWeb());
                    System.out.println("- Descripcion: " + ((DataProveedor) usuario).getDescripcion());
                }
                
                // Set success message and redirect to dashboard
                session.setAttribute("successMessage", "¡Bienvenido " + usuario.getNombre() + "!");
                System.out.println("4. Session created successfully - redirecting to dashboard");
                response.sendRedirect(request.getContextPath() + "/dashboard");
            } else {
                System.out.println("3. Authentication failed - invalid credentials");
                request.setAttribute("error", "Las credenciales ingresadas no son válidas");
                request.getRequestDispatcher("/WEB-INF/login.jsp").forward(request, response);
            }
            
        } catch (Exception e) {
            System.err.println("Error during authentication: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("error", "Error al iniciar sesión. Por favor intente nuevamente.");
            request.getRequestDispatcher("/WEB-INF/login.jsp").forward(request, response);
        }
    }
    
    /**
     * Authenticate user using the Central Server logic
     * This method implements the authentication logic since it's not directly available in IControladorUsuario
     */
    private DataUsuario authenticateUser(String usernameOrEmail, String password) {
        try {
            // First, try to find user by nickname
            DataUsuario usuario = null;
            
            try {
                usuario = controladorUsuario.verInfoUsuario(usernameOrEmail);
                System.out.println("   - User found by nickname: " + usuario.getNickname());
            } catch (UsuarioNoExisteException e) {
                // If not found by nickname, search by email
                System.out.println("   - Not found by nickname, searching by email...");
                usuario = findUserByEmail(usernameOrEmail);
            }
            
            // If user found, verify password
            if (usuario != null) {
                System.out.println("   - Verifying password for user: " + usuario.getNickname());
                if (password.equals(usuario.getContra())) {
                    System.out.println("   - Password verified successfully");
                    return usuario;
                } else {
                    System.out.println("   - Password verification failed");
                    return null;
                }
            } else {
                System.out.println("   - User not found in database");
                return null;
            }
            
        } catch (Exception e) {
            System.err.println("   - Error during authentication: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Helper method to find user by email since the controller doesn't have direct email lookup
     */
    private DataUsuario findUserByEmail(String email) {
        try {
            System.out.println("   - Searching all users for email: " + email);
            DataUsuario[] allUsers = controladorUsuario.getUsuarios();
            System.out.println("   - Found " + allUsers.length + " users in database");
            
            for (DataUsuario user : allUsers) {
                if (email.equalsIgnoreCase(user.getEmail())) {
                    System.out.println("   - User found by email: " + user.getNickname());
                    return user;
                }
            }
            System.out.println("   - No user found with email: " + email);
        } catch (UsuarioNoExisteException e) {
            System.out.println("   - No users found in database");
        } catch (Exception e) {
            System.err.println("   - Error searching users: " + e.getMessage());
        }
        return null;
    }
}