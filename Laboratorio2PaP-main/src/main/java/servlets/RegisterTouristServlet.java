package servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import java.io.IOException;

// Import Web Service client stubs
import uy.edu.pa.central.client.AuthService;
import uy.edu.pa.central.client.AuthService_Service;

@WebServlet("/register-tourist")
@MultipartConfig(
    maxFileSize = 1024 * 1024 * 5, // 5MB
    maxRequestSize = 1024 * 1024 * 10 // 10MB
)
public class RegisterTouristServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/register-tourist.jsp").forward(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        System.out.println("\n=== Register Tourist via SOAP ===");
        
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

            System.out.println("Form data - Nickname: " + nickname + ", Email: " + email);
            
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

            // 4. Register via SOAP
            try {
                System.out.println("Calling AuthService.registrarTurista via SOAP");
                
                AuthService_Service service = new AuthService_Service();
                AuthService port = service.getAuthServicePort();
                
                boolean exito = port.registrarTurista(
                    nickname,
                    firstName,
                    lastName,
                    email,
                    password,
                    birthDateStr,
                    nationality
                );
                
                if (exito) {
                    System.out.println("Tourist registered successfully via SOAP");
                    request.getSession().setAttribute("successMessage", 
                        "¡Registro exitoso! Bienvenido " + firstName + ", por favor inicia sesión");
                    response.sendRedirect(request.getContextPath() + "/login");
                } else {
                    request.setAttribute("error", "No se pudo completar el registro");
                    request.getRequestDispatcher("/WEB-INF/register-tourist.jsp").forward(request, response);
                }
                
            } catch (Exception e) {
                System.err.println("[RegisterTouristServlet] SOAP error: " + e.getMessage());
                e.printStackTrace();
                
                String errorMsg = e.getMessage();
                if (errorMsg != null && errorMsg.contains("ya existe")) {
                    request.setAttribute("error", "El nickname o email ya está registrado");
                } else {
                    request.setAttribute("error", "Error al registrar: " + errorMsg);
                }
                request.getRequestDispatcher("/WEB-INF/register-tourist.jsp").forward(request, response);
            }

        } catch (Exception e) {
            System.err.println("[RegisterTouristServlet] Error: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("error", "Error al procesar el registro");
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
