package servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

// Import classes from Laboratorio1.jar
import logica.Fabrica;
import logica.IControladorUsuario;
import logica.DataUsuario;
import excepciones.UsuarioNoExisteException;

@WebServlet("/test-db")
public class TestDatabaseServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        
        out.println("<!DOCTYPE html>");
        out.println("<html><head><title>Database Connection Test</title></head><body>");
        out.println("<h1>Database Connection Test</h1>");
        
        try {
            // Test connection to Laboratorio1.jar
            IControladorUsuario controlador = Fabrica.getInstance().getIControladorUsuario();
            out.println("<p><strong>✅ Fabrica initialized successfully</strong></p>");
            
            // Try to get all users
            try {
                DataUsuario[] usuarios = controlador.getUsuarios();
                out.println("<p><strong>✅ Database connection successful</strong></p>");
                out.println("<p>Found " + usuarios.length + " users in database:</p>");
                out.println("<ul>");
                for (DataUsuario usuario : usuarios) {
                    out.println("<li>" + usuario.getNickname() + " (" + usuario.getEmail() + ") - " + 
                               (usuario instanceof logica.DataTurista ? "Turista" : "Proveedor") + "</li>");
                }
                out.println("</ul>");
            } catch (UsuarioNoExisteException e) {
                out.println("<p><strong>ℹ️ Database connected but no users found</strong></p>");
                out.println("<p>This is normal for a new database. You can now register users.</p>");
            }
            
        } catch (Exception e) {
            out.println("<p><strong>❌ Error connecting to database:</strong></p>");
            out.println("<pre>" + e.getMessage() + "</pre>");
            e.printStackTrace(out);
        }
        
        out.println("<br><a href='" + request.getContextPath() + "/'>← Back to Login</a>");
        out.println("</body></html>");
    }
}