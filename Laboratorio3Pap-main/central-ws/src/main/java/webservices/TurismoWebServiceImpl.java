package webservices;

import jakarta.jws.WebService;
import uy.edu.pa.central.ws.dtos.UserDTO;
import uy.edu.pa.central.ws.dtos.ActividadDTO;
import uy.edu.pa.central.ws.dtos.SalidaDTO;
import uy.edu.pa.central.ws.dtos.InscripcionDTO;
import java.util.List;

import uy.edu.pa.central.ws.AuthServiceImpl;
import uy.edu.pa.central.ws.UsuariosServiceImpl;
import uy.edu.pa.central.ws.ActividadesServiceImpl;
import uy.edu.pa.central.ws.SalidasServiceImpl;
import uy.edu.pa.central.ws.InscripcionesServiceImpl;

@WebService(
    serviceName = "TurismoService",
    portName = "TurismoWebServicePort",
    endpointInterface = "webservices.TurismoWebService",
    targetNamespace = "http://webservices/"
)
public class TurismoWebServiceImpl implements TurismoWebService {

    private final AuthServiceImpl authService = new AuthServiceImpl();
    private final UsuariosServiceImpl usuariosService = new UsuariosServiceImpl();
    private final ActividadesServiceImpl actividadesService = new ActividadesServiceImpl();
    private final SalidasServiceImpl salidasService = new SalidasServiceImpl();
    private final InscripcionesServiceImpl inscripcionesService = new InscripcionesServiceImpl();

    @Override
    public UserDTO iniciarSesion(String nickname, String password) {
        try {
            return authService.login(nickname, password);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public String registrarUsuario(String nickname, String nombre, String apellido, String email,
                                   String password, String fechaNacimiento, String nacionalidad,
                                   String tipo, String descripcion, String sitioWeb, String imagenBase64) {
        try {
            if ("turista".equalsIgnoreCase(tipo)) {
                return authService.registrarTurista(nickname, nombre, apellido, email, password, 
                                                   fechaNacimiento, nacionalidad, imagenBase64);
            } else if ("proveedor".equalsIgnoreCase(tipo)) {
                return authService.registrarProveedor(nickname, nombre, apellido, email, password, 
                                                     fechaNacimiento, descripcion, sitioWeb, imagenBase64);
            }
        } catch (Exception e) {
            return "ERROR: " + e.getMessage();
        }
        return "ERROR: Tipo de usuario inválido";
    }

    @Override
    public String cerrarSesion(String nickname) {
        return "Sesión cerrada para: " + nickname;
    }

    @Override
    public List<UserDTO> listarUsuarios() {
        return usuariosService.listarUsuarios();
    }

    @Override
    public UserDTO obtenerUsuario(String nickname) {
        return usuariosService.obtenerUsuario(nickname);
    }

    @Override
    public boolean editarUsuario(String nickname, String nombre, String apellido,
                                String fechaNacimiento, String nacionalidad,
                                String descripcion, String sitioWeb, String imagenBase64) {
        return usuariosService.actualizarUsuario(nickname, nombre, apellido, fechaNacimiento,
                                                nacionalidad, descripcion, sitioWeb, imagenBase64);
    }

    @Override
    public List<ActividadDTO> listarActividades() {
        return actividadesService.listarActividades();
    }

    @Override
    public ActividadDTO obtenerDetalleActividad(String id) {
        return actividadesService.obtenerActividad(id);
    }

    @Override
    public boolean crearActividad(String nombre, String descripcion, int duracion,
                                 float costo, String ciudad, String proveedor, String fechaAlta, String imagenBase64) {
        return actividadesService.crearActividad(nombre, descripcion, duracion, costo,
                                                ciudad, proveedor, fechaAlta, imagenBase64);
    }
    
    @Override
    public boolean modificarActividad(String nombre, String descripcion, int duracion,
                                     float costo, String ciudad, String imagenBase64) {
        return actividadesService.modificarActividad(nombre, descripcion, duracion, costo, ciudad, imagenBase64);
    }

    @Override
    public boolean crearSalida(String nombre, String fecha, String hora, String lugar,
                              int cantMax, String fechaAlta, String actividad) {
        return salidasService.crearSalida(nombre, fecha, hora, lugar, cantMax, fechaAlta, actividad);
    }

    @Override
    public SalidaDTO obtenerSalida(String id) {
        return salidasService.obtenerSalida(id);
    }

    @Override
    public List<SalidaDTO> listarSalidasDeActividad(String actividad) {
        return salidasService.listarSalidasDeActividad(actividad);
    }

    @Override
    public boolean modificarSalida(String nombre, String fecha, String hora, String lugar,
                                  int cantMax, String imagenBase64) {
        return salidasService.modificarSalida(nombre, fecha, hora, lugar, cantMax, imagenBase64);
    }

    @Override
    public boolean inscribirTurista(String turista, String salida, int cantidad, String fecha) {
        return inscripcionesService.inscribirTurista(turista, salida, cantidad, fecha);
    }

    @Override
    public List<InscripcionDTO> listarInscripcionesPorTurista(String turista) {
        return inscripcionesService.listarInscripcionesPorTurista(turista);
    }

    @Override
    public List<InscripcionDTO> listarInscripcionesDeSalida(String salida) {
        return salidasService.listarInscripcionesDeSalida(salida);
    }

    @Override
    public List<UserDTO> listarProveedores() {
        return usuariosService.listarProveedores();
    }

    @Override
    public List<UserDTO> listarTuristas() {
        return usuariosService.listarTuristas();
    }

    @Override
    public List<ActividadDTO> listarActividadesPorProveedor(String proveedor) {
        return actividadesService.listarActividadesPorProveedor(proveedor);
    }

    @Override
    public List<ActividadDTO> rankingActividades() {
        return actividadesService.rankingActividades();
    }

    @Override
    public List<ActividadDTO> listarActividadesPorTurista(String turista) {
        return actividadesService.listarActividadesPorTurista(turista);
    }
}
