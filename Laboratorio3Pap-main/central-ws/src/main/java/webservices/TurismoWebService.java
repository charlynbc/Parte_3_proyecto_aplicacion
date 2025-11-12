package webservices;

import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebService;
import uy.edu.pa.central.ws.dtos.UserDTO;
import uy.edu.pa.central.ws.dtos.ActividadDTO;
import uy.edu.pa.central.ws.dtos.SalidaDTO;
import uy.edu.pa.central.ws.dtos.InscripcionDTO;
import java.util.List;

@WebService(name = "TurismoWebService", targetNamespace = "http://webservices/")
public interface TurismoWebService {

    // Autenticación
    @WebMethod
    UserDTO iniciarSesion(@WebParam(name = "nickname") String nickname, 
                          @WebParam(name = "password") String password);
    
    @WebMethod
    String registrarUsuario(@WebParam(name = "nickname") String nickname,
                            @WebParam(name = "nombre") String nombre,
                            @WebParam(name = "apellido") String apellido,
                            @WebParam(name = "email") String email,
                            @WebParam(name = "password") String password,
                            @WebParam(name = "fechaNacimiento") String fechaNacimiento,
                            @WebParam(name = "nacionalidad") String nacionalidad,
                            @WebParam(name = "tipo") String tipo,
                            @WebParam(name = "descripcion") String descripcion,
                            @WebParam(name = "sitioWeb") String sitioWeb,
                            @WebParam(name = "imagenBase64") String imagenBase64);
    
    @WebMethod
    String cerrarSesion(@WebParam(name = "nickname") String nickname);

    // Usuarios
    @WebMethod
    List<UserDTO> listarUsuarios();
    
    @WebMethod
    UserDTO obtenerUsuario(@WebParam(name = "nickname") String nickname);
    
    @WebMethod
    boolean editarUsuario(@WebParam(name = "nickname") String nickname,
                         @WebParam(name = "nombre") String nombre,
                         @WebParam(name = "apellido") String apellido,
                         @WebParam(name = "fechaNacimiento") String fechaNacimiento,
                         @WebParam(name = "nacionalidad") String nacionalidad,
                         @WebParam(name = "descripcion") String descripcion,
                         @WebParam(name = "sitioWeb") String sitioWeb,
                         @WebParam(name = "imagenBase64") String imagenBase64);

    // Actividades

    // Actividades
    @WebMethod
    List<ActividadDTO> listarActividades();
    
    @WebMethod
    ActividadDTO obtenerDetalleActividad(@WebParam(name = "id") String id);
    
    @WebMethod
    boolean crearActividad(@WebParam(name = "nombre") String nombre,
                          @WebParam(name = "descripcion") String descripcion,
                          @WebParam(name = "duracion") int duracion,
                          @WebParam(name = "costo") float costo,
                          @WebParam(name = "ciudad") String ciudad,
                          @WebParam(name = "proveedor") String proveedor,
                          @WebParam(name = "fechaAlta") String fechaAlta,
                          @WebParam(name = "imagenBase64") String imagenBase64);
    
    @WebMethod
    boolean modificarActividad(@WebParam(name = "nombre") String nombre,
                              @WebParam(name = "descripcion") String descripcion,
                              @WebParam(name = "duracion") int duracion,
                              @WebParam(name = "costo") float costo,
                              @WebParam(name = "ciudad") String ciudad,
                              @WebParam(name = "imagenBase64") String imagenBase64);

    // Salidas
    @WebMethod
    boolean crearSalida(@WebParam(name = "nombre") String nombre,
                       @WebParam(name = "fecha") String fecha,
                       @WebParam(name = "hora") String hora,
                       @WebParam(name = "lugar") String lugar,
                       @WebParam(name = "cantMax") int cantMax,
                       @WebParam(name = "fechaAlta") String fechaAlta,
                       @WebParam(name = "actividad") String actividad);
    
    @WebMethod
    SalidaDTO obtenerSalida(@WebParam(name = "id") String id);
    
    @WebMethod
    List<SalidaDTO> listarSalidasDeActividad(@WebParam(name = "actividad") String actividad);

    @WebMethod
    boolean modificarSalida(@WebParam(name = "nombre") String nombre,
                           @WebParam(name = "fecha") String fecha,
                           @WebParam(name = "hora") String hora,
                           @WebParam(name = "lugar") String lugar,
                           @WebParam(name = "cantMax") int cantMax,
                           @WebParam(name = "imagenBase64") String imagenBase64);

    // Inscripciones
    @WebMethod
    boolean inscribirTurista(@WebParam(name = "turista") String turista,
                            @WebParam(name = "salida") String salida,
                            @WebParam(name = "cantidad") int cantidad,
                            @WebParam(name = "fecha") String fecha);
    
    @WebMethod
    List<InscripcionDTO> listarInscripcionesPorTurista(@WebParam(name = "turista") String turista);
    
    @WebMethod
    List<InscripcionDTO> listarInscripcionesDeSalida(@WebParam(name = "salida") String salida);
    
    @WebMethod
    int obtenerCuposDisponibles(@WebParam(name = "salida") String salida);

    // Métodos adicionales
    @WebMethod
    List<UserDTO> listarProveedores();
    
    @WebMethod
    List<UserDTO> listarTuristas();
    
    @WebMethod
    List<ActividadDTO> listarActividadesPorProveedor(@WebParam(name = "proveedor") String proveedor);
    
    @WebMethod
    List<ActividadDTO> rankingActividades();
    
    @WebMethod
    List<ActividadDTO> listarActividadesPorTurista(@WebParam(name = "turista") String turista);
}
