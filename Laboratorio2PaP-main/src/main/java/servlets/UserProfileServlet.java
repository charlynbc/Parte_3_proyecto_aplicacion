package servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

// SOAP stubs
import uy.edu.pa.central.client.ActividadesService_Service;
import uy.edu.pa.central.client.ActividadesService;
import uy.edu.pa.central.client.UserDTO;
import uy.edu.pa.central.client.ActividadDTO;
import uy.edu.pa.central.client.InscripcionDTO;

@WebServlet(name = "UserProfileServlet", urlPatterns = {"/user-profile"})
public class UserProfileServlet extends HttpServlet {
    
    @Override
    public void init() throws ServletException {
        super.init();
        System.out.println("UserProfileServlet initialized (SOAP mode)");
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        System.out.println("\n=== User Profile Request (SOAP) ===");
        
        // Obtener nickname: primero de parámetro, luego de sesión
        String nickname = request.getParameter("nickname");
        
        // Si no viene por parámetro, usar el usuario logueado
        if (nickname == null || nickname.trim().isEmpty()) {
            HttpSession session = request.getSession(false);
            if (session != null) {
                nickname = (String) session.getAttribute("username");
                System.out.println("Using logged user: " + nickname);
            }
        }
        
        // Si todavía no hay nickname, redirigir
        if (nickname == null || nickname.trim().isEmpty()) {
            System.out.println("No nickname provided and no user logged in");
            response.sendRedirect(request.getContextPath() + "/users");
            return;
        }
        
        System.out.println("Loading profile for: " + nickname);
        
        try {
            // Obtener usuario via SOAP
            ActividadesService_Service service = new ActividadesService_Service();
            ActividadesService port = service.getActividadesServicePort();
            
            UserDTO usuario = port.obtenerUsuario(nickname);
            
            if (usuario == null) {
                System.out.println("User not found: " + nickname);
                response.sendRedirect(request.getContextPath() + "/users");
                return;
            }
            
            System.out.println("User found: " + usuario.getNickname() + " (tipo: " + usuario.getTipoUsuario() + ")");
            
            request.setAttribute("user", usuario);
            
            HttpSession session = request.getSession(false);
            String loggedUsername = (session != null) ? (String) session.getAttribute("username") : null;
            boolean isOwnProfile = nickname.equals(loggedUsername);
            request.setAttribute("isOwnProfile", isOwnProfile);
            System.out.println("Is own profile: " + isOwnProfile);
            
            // Cargar datos según tipo de usuario
            if ("proveedor".equalsIgnoreCase(usuario.getTipoUsuario())) {
                loadProveedorData(nickname, isOwnProfile, request, port);
            } else if ("turista".equalsIgnoreCase(usuario.getTipoUsuario())) {
                loadTuristaData(nickname, request, port);
            }
            
            request.getRequestDispatcher("/WEB-INF/user-profile.jsp").forward(request, response);
            
        } catch (Exception e) {
            System.err.println("Error retrieving user profile: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("error", "Error al cargar el perfil del usuario");
            request.getRequestDispatcher("/WEB-INF/user-profile.jsp").forward(request, response);
        }
    }
    
    private void loadProveedorData(String nickname, boolean isOwnProfile, HttpServletRequest request, ActividadesService port) {
        try {
            List<ActividadDTO> todasActividades = port.listarActividades();
            List<ActividadDTO> actividadesProveedor = new ArrayList<>();
            
            // Filtrar actividades del proveedor
            if (todasActividades != null) {
                for (ActividadDTO actividad : todasActividades) {
                    // Las actividades en el DTO deberían tener el proveedor
                    // Por ahora, todas las actividades (simplificación)
                    actividadesProveedor.add(actividad);
                }
            }
            
            request.setAttribute("actividades", actividadesProveedor);
            System.out.println("Actividades cargadas: " + actividadesProveedor.size());
            
        } catch (Exception e) {
            System.err.println("Error loading provider data: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("actividades", new ArrayList<ActividadDTO>());
        }
    }
    
    private void loadTuristaData(String nickname, HttpServletRequest request, ActividadesService port) {
        try {
            // Obtener inscripciones del turista via SOAP
            List<InscripcionDTO> inscripciones = port.listarInscripcionesPorTurista(nickname);
            
            if (inscripciones == null) {
                inscripciones = new ArrayList<>();
            }
            
            request.setAttribute("inscripciones", inscripciones);
            System.out.println("Inscripciones del turista: " + inscripciones.size());
            
        } catch (Exception e) {
            System.err.println("Error loading tourist data: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("inscripciones", new ArrayList<InscripcionDTO>());
        }
    }
}
