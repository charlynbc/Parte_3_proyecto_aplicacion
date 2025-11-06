package servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

// Import Web Service client stubs
import uy.edu.pa.central.client.ActividadesService;
import uy.edu.pa.central.client.ActividadesService_Service;
import uy.edu.pa.central.client.UserDTO;

@WebServlet(name = "UsersServlet", urlPatterns = {"/users"})
public class UsersServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        System.out.println("\n=== Users List Request via Web Services ===");
        
        try {
            // Get Web Service client
            ActividadesService_Service service = new ActividadesService_Service();
            ActividadesService port = service.getActividadesServicePort();
            
            // Get all users from SOAP service
            List<UserDTO> usuarios = port.listarUsuarios();
            
            System.out.println("Users retrieved via WS: " + usuarios.size());
            
            // Set users as request attribute
            request.setAttribute("users", usuarios);
            
            // Forward to JSP
            request.getRequestDispatcher("/WEB-INF/users.jsp").forward(request, response);
            
        } catch (Exception e) {
            System.err.println("Error retrieving users via Web Services: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("users", new ArrayList<UserDTO>());
            request.setAttribute("error", "Error al cargar la lista de usuarios desde el servidor central");
            request.getRequestDispatcher("/WEB-INF/users.jsp").forward(request, response);
        }
    }
}

