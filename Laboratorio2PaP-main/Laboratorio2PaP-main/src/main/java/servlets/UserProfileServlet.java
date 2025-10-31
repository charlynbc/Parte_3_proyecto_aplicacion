package servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

import logica.Fabrica;
import logica.IControladorUsuario;
import logica.Inscripcion;
import logica.ManejadorInscripcion;
import logica.IControladorActividad;
import logica.IControladorSalida;
import logica.DataUsuario;
import logica.DataTurista;
import logica.DataProveedor;
import logica.DataActividad;
import logica.DataSalida;
import logica.DataInscripcion;
import excepciones.UsuarioNoExisteException;
import excepciones.ActividadNoExisteException;

@WebServlet(name = "UserProfileServlet", urlPatterns = {"/user-profile"})
public class UserProfileServlet extends HttpServlet {
    
    private IControladorUsuario controladorUsuario;
    private IControladorActividad controladorActividad;
    private IControladorSalida controladorSalida;
    
    @Override
    public void init() throws ServletException {
        super.init();
        try {
            controladorUsuario = Fabrica.getInstance().getIControladorUsuario();
            controladorActividad = Fabrica.getInstance().getIControladorActividad();
            controladorSalida = Fabrica.getInstance().getIControladorSalida();
            System.out.println("UserProfileServlet initialized");
        } catch (Exception e) {
            System.err.println("Error initializing UserProfileServlet: " + e.getMessage());
            e.printStackTrace();
            throw new ServletException("Failed to initialize controller", e);
        }
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        System.out.println("\n=== User Profile Request ===");
        
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
            DataUsuario usuario = controladorUsuario.verInfoUsuario(nickname);
            System.out.println("User found: " + usuario.getNickname());
            
            request.setAttribute("user", usuario);
            
            HttpSession session = request.getSession(false);
            String loggedUsername = (session != null) ? (String) session.getAttribute("username") : null;
            boolean isOwnProfile = nickname.equals(loggedUsername);
            request.setAttribute("isOwnProfile", isOwnProfile);
            System.out.println("Is own profile: " + isOwnProfile);
            
            // Cargar datos según tipo de usuario
            if (usuario instanceof DataProveedor) {
                loadProveedorData(nickname, isOwnProfile, request);
            } else if (usuario instanceof DataTurista) {
                loadTuristaData(nickname, request);
            }
            
            request.getRequestDispatcher("/WEB-INF/user-profile.jsp").forward(request, response);
            
        } catch (UsuarioNoExisteException e) {
            System.out.println("User not found: " + nickname);
            response.sendRedirect(request.getContextPath() + "/users");
        } catch (Exception e) {
            System.err.println("Error retrieving user profile: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("error", "Error al cargar el perfil del usuario");
            request.getRequestDispatcher("/WEB-INF/user-profile.jsp").forward(request, response);
        }
    }
    
    private void loadProveedorData(String nickname, boolean isOwnProfile, HttpServletRequest request) {
        try {
            DataActividad[] todasActividades = controladorActividad.getActividades();
            java.util.List<DataActividad> actividadesProveedor = new java.util.ArrayList<>();
            
            for (DataActividad actividad : todasActividades) {
                if (actividad.getProveedor().equals(nickname)) {
                    if (isOwnProfile) {
                        // Proveedor ve todas sus actividades
                        actividadesProveedor.add(actividad);
                    } else {
                        // Otros usuarios solo ven actividades confirmadas
                        if ("Confirmada".equals(actividad.getEstado())) {
                            actividadesProveedor.add(actividad);
                        }
                    }
                }
            }
            
            request.setAttribute("actividades", actividadesProveedor.toArray(new DataActividad[0]));
            System.out.println("Actividades del proveedor: " + actividadesProveedor.size());
            
            // Cargar salidas turísticas
            java.util.List<DataSalida> todasSalidas = new java.util.ArrayList<>();
            for (DataActividad actividad : actividadesProveedor) {
                try {
                    DataSalida[] salidas = controladorSalida.listarSalidasDeActividad(actividad.getNombre());
                    if (salidas != null) {
                        for (DataSalida salida : salidas) {
                            todasSalidas.add(salida);
                        }
                    }
                } catch (Exception e) {
                    System.err.println("Error getting departures for activity " + actividad.getNombre() + ": " + e.getMessage());
                }
            }
            request.setAttribute("salidas", todasSalidas.toArray(new DataSalida[0]));
            System.out.println("Salidas del proveedor: " + todasSalidas.size());
            
        } catch (Exception e) {
            System.err.println("Error loading provider data: " + e.getMessage());
            request.setAttribute("actividades", new DataActividad[0]);
            request.setAttribute("salidas", new DataSalida[0]);
        }
    }
    
    private void loadTuristaData(String nickname, HttpServletRequest request) {
        try {
            // Obtener inscripciones del turista
            Inscripcion[] inscripciones = ManejadorInscripcion.getinstance().getInscripcionesPorTurista(nickname);
            
            // Convertir a DataInscripcion[] para el JSP
            DataInscripcion[] dataInscripciones = new DataInscripcion[inscripciones != null ? inscripciones.length : 0];
            if (inscripciones != null) {
                for (int i = 0; i < inscripciones.length; i++) {
                    // Crear DataInscripcion a partir de Inscripcion
                    // (asumiendo que tienes un constructor o método para convertir)
                    dataInscripciones[i] = new DataInscripcion(
                        inscripciones[i].getFechaInscripcion(),
                        inscripciones[i].getCantTuristas(),
                        inscripciones[i].getCosto(),
                        inscripciones[i].getTurista().getNickname(),
                        inscripciones[i].getSalida().getNombre()
                    );
                    
                }
            }
            
            request.setAttribute("inscripciones", dataInscripciones);
            System.out.println("Inscripciones del turista: " + dataInscripciones.length);
            
            // Cargar salidas en las que está inscrito
            if (inscripciones != null && inscripciones.length > 0) {
                java.util.List<DataSalida> salidasInscritas = new java.util.ArrayList<>();
                for (Inscripcion insc : inscripciones) {
                    try {
                        String nombreSalida = insc.getSalida().getNombre();
                        // Buscar la salida en todas las actividades
                        DataActividad[] actividades = controladorActividad.getActividades();
                        for (DataActividad act : actividades) {
                            DataSalida[] salidas = controladorSalida.listarSalidasDeActividad(act.getNombre());
                            if (salidas != null) {
                                for (DataSalida sal : salidas) {
                                    if (sal.getNombre().equals(nombreSalida)) {
                                        salidasInscritas.add(sal);
                                        break;
                                    }
                                }
                            }
                        }
                    } catch (Exception e) {
                        System.err.println("Error getting departure info for inscription: " + e.getMessage());
                    }
                }
                request.setAttribute("salidas", salidasInscritas.toArray(new DataSalida[0]));
                System.out.println("Salidas inscritas: " + salidasInscritas.size());
            } else {
                request.setAttribute("salidas", new DataSalida[0]);
            }
            
        } catch (Exception e) {
            System.err.println("Error loading tourist data: " + e.getMessage());
            request.setAttribute("inscripciones", new DataInscripcion[0]);
            request.setAttribute("salidas", new DataSalida[0]);
        }
    }
}