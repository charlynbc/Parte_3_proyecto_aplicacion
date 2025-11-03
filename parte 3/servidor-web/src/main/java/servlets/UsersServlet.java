package servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

// Import Web Service clients
import webserviceclients.WSClientFactory;
import webserviceclients.WSUsuarioClient;
import datatypes.DataUsuario;

@WebServlet(name = "UsersServlet", urlPatterns = {"/users"})
public class UsersServlet extends HttpServlet {
    
    private WSUsuarioClient wsUsuarioClient;
    
    @Override
    public void init() throws ServletException {
        super.init();
        try {
            wsUsuarioClient = WSClientFactory.getInstance().getWSUsuarioClient();
            System.out.println("UsersServlet initialized - connected to Web Services");
        } catch (Exception e) {
            System.err.println("Error initializing UsersServlet: " + e.getMessage());
            e.printStackTrace();
            throw new ServletException("Failed to initialize WS client", e);
        }
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        System.out.println("\n=== Users List Request ===");
        
        try {
            // Get all users from the central server via Web Service
            DataUsuario[] usuarios = wsUsuarioClient.getUsuarios();
            
            System.out.println("Users retrieved: " + usuarios.length);
            
            // Set users as request attribute
            request.setAttribute("users", usuarios);
            
            // Forward to JSP
            request.getRequestDispatcher("/WEB-INF/users.jsp").forward(request, response);
            
        } catch (Exception e) {
            String errorMsg = e.getMessage();
            if (errorMsg != null && errorMsg.contains("NoExiste")) {
                System.out.println("No users found in database");
                request.setAttribute("users", new DataUsuario[0]);
            } else {
                System.err.println("Error retrieving users: " + errorMsg);
                e.printStackTrace();
                request.setAttribute("error", "Error al cargar la lista de usuarios");
            }
            request.getRequestDispatcher("/WEB-INF/users.jsp").forward(request, response);
        }
    }
}

