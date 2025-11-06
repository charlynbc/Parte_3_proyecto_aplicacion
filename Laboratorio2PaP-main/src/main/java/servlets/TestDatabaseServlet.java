package servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

// SOAP stubs
import uy.edu.pa.central.client.ActividadesService_Service;
import uy.edu.pa.central.client.ActividadesService;
import uy.edu.pa.central.client.UserDTO;

@WebServlet("/test-db")
public class TestDatabaseServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        
        out.println("<!DOCTYPE html>");
        out.println("<html><head><title>Database Connection Test (SOAP)</title>");
        out.println("<style>");
        out.println("body { font-family: Arial, sans-serif; margin: 40px; }");
        out.println("h1 { color: #333; }");
        out.println(".success { color: green; }");
        out.println(".error { color: red; }");
        out.println("ul { list-style-type: none; padding: 0; }");
        out.println("li { padding: 8px; margin: 4px 0; background: #f5f5f5; border-radius: 4px; }");
        out.println(".badge { padding: 4px 8px; border-radius: 3px; font-size: 12px; margin-left: 10px; }");
        out.println(".turista { background: #e3f2fd; color: #1976d2; }");
        out.println(".proveedor { background: #fff3e0; color: #f57c00; }");
        out.println("</style>");
        out.println("</head><body>");
        out.println("<h1>🧪 Database Connection Test via SOAP</h1>");
        
        try {
            // Test SOAP connection
            ActividadesService_Service service = new ActividadesService_Service();
            ActividadesService port = service.getActividadesServicePort();
            
            out.println("<p class='success'><strong>✅ SOAP Service initialized successfully</strong></p>");
            out.println("<p><em>Connecting to: http://localhost:9128/central-ws/services/ActividadesService</em></p>");
            
            // Try to get all users via SOAP
            List<UserDTO> usuarios = port.listarUsuarios();
            
            if (usuarios != null && !usuarios.isEmpty()) {
                out.println("<p class='success'><strong>✅ Database connection successful via SOAP</strong></p>");
                out.println("<p>Found <strong>" + usuarios.size() + "</strong> users in Railway MySQL database:</p>");
                out.println("<ul>");
                for (UserDTO usuario : usuarios) {
                    String tipo = usuario.getTipoUsuario();
                    String badgeClass = "turista";
                    if ("proveedor".equalsIgnoreCase(tipo)) {
                        badgeClass = "proveedor";
                    }
                    out.println("<li>");
                    out.println("<strong>" + usuario.getNickname() + "</strong> - " + usuario.getNombre());
                    out.println(" (" + usuario.getEmail() + ")");
                    out.println("<span class='badge " + badgeClass + "'>" + tipo + "</span>");
                    out.println("</li>");
                }
                out.println("</ul>");
            } else {
                out.println("<p><strong>ℹ️ SOAP connection successful but no users found</strong></p>");
                out.println("<p>This is normal for a new database. You can now register users.</p>");
            }
            
            // Test Ping
            out.println("<hr>");
            out.println("<h2>🏓 SOAP Ping Test</h2>");
            try {
                uy.edu.pa.central.client.AuthService_Service authService = new uy.edu.pa.central.client.AuthService_Service();
                uy.edu.pa.central.client.AuthService authPort = authService.getAuthServicePort();
                String pong = authPort.ping();
                out.println("<p class='success'><strong>✅ Ping successful:</strong> " + pong + "</p>");
            } catch (Exception e) {
                out.println("<p class='error'><strong>❌ Ping failed:</strong> " + e.getMessage() + "</p>");
            }
            
        } catch (Exception e) {
            out.println("<p class='error'><strong>❌ Error connecting to SOAP service:</strong></p>");
            out.println("<pre>" + e.getMessage() + "</pre>");
            out.println("<p><em>Make sure central-ws is running on port 9128</em></p>");
            e.printStackTrace(out);
        }
        
        out.println("<hr>");
        out.println("<p><a href='" + request.getContextPath() + "/login'>← Back to Login</a></p>");
        out.println("<p><small>Using 100% Web Services (SOAP) - No direct JAR access</small></p>");
        out.println("</body></html>");
    }
}
