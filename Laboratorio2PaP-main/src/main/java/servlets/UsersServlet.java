package servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

// Import classes from Laboratorio1.jar
import logica.Fabrica;
import logica.IControladorUsuario;
import logica.DataUsuario;
import excepciones.UsuarioNoExisteException;

@WebServlet(name = "UsersServlet", urlPatterns = {"/users"})
public class UsersServlet extends HttpServlet {
    
    private IControladorUsuario controladorUsuario;
    
    @Override
    public void init() throws ServletException {
        super.init();
        try {
            controladorUsuario = Fabrica.getInstance().getIControladorUsuario();
            System.out.println("UsersServlet initialized - connected to Laboratorio1.jar");
        } catch (Exception e) {
            System.err.println("Error initializing UsersServlet: " + e.getMessage());
            e.printStackTrace();
            throw new ServletException("Failed to initialize controller", e);
        }
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        System.out.println("\n=== Users List Request ===");
        
        try {
            // Get all users from the central server
            DataUsuario[] usuarios = controladorUsuario.getUsuarios();
            
            System.out.println("Users retrieved: " + usuarios.length);
            
            // Set users as request attribute
            request.setAttribute("users", usuarios);
            
            // Forward to JSP
            request.getRequestDispatcher("/WEB-INF/users.jsp").forward(request, response);
            
        } catch (UsuarioNoExisteException e) {
            System.out.println("No users found in database");
            request.setAttribute("users", new DataUsuario[0]);
            request.getRequestDispatcher("/WEB-INF/users.jsp").forward(request, response);
        } catch (Exception e) {
            System.err.println("Error retrieving users: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("error", "Error al cargar la lista de usuarios");
            request.getRequestDispatcher("/WEB-INF/users.jsp").forward(request, response);
        }
    }
}

