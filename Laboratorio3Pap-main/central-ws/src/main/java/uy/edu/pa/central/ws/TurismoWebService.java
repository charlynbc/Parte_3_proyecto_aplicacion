package uy.edu.pa.central.ws;

import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebService;
import uy.edu.pa.central.ws.dtos.*;
import java.util.List;

@WebService(name = "TurismoWebService", targetNamespace = "http://webservices/")
public interface TurismoWebService {

    // Operaciones de Autenticación
    @WebMethod
    UserDTO iniciarSesion(@WebParam(name = "nickname") String nickname, 
                          @WebParam(name = "password") String password);
    
    @WebMethod
    boolean registrarUsuario(@WebParam(name = "nickname") String nickname,
                            @WebParam(name = "nombre") String nombre,
                            @WebParam(name = "apellido") String apellido,
                            @WebParam(name = "email") String email,
                            @WebParam(name = "password") String password,
                            @WebParam(name = "fechaNacimiento") String fechaNacimiento,
                            @WebParam(name = "nacionalidad") String nacionalidad,
                            @WebParam(name = "tipo") String tipo,
                            @WebParam(name = "descripcion") String descripcion,
                            @WebParam(name = "sitioWeb") String sitioWeb);
    
    @WebMethod
    boolean cerrarSesion(@WebParam(name = "nickname") String nickname);

    // Operaciones de Usuarios
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
                         @WebParam(name = "sitioWeb") String sitioWeb);

    // Operaciones de Actividades
    @WebMethod
    List<ActividadDTO> listarActividades();
    
    @WebMethod
    ActividadDTO obtenerDetalleActividad(@WebParam(name = "nombre") String nombre);
    
    @WebMethod
    boolean crearActividad(@WebParam(name = "nombre") String nombre,
                          @WebParam(name = "descripcion") String descripcion,
                          @WebParam(name = "duracion") int duracion,
                          @WebParam(name = "costo") float costo,
                          @WebParam(name = "ciudad") String ciudad,
                          @WebParam(name = "proveedor") String proveedor,
                          @WebParam(name = "fechaAlta") String fechaAlta);

    // Operaciones de Salidas
    @WebMethod
    boolean crearSalida(@WebParam(name = "nombre") String nombre,
                       @WebParam(name = "fecha") String fecha,
                       @WebParam(name = "hora") String hora,
                       @WebParam(name = "lugar") String lugar,
                       @WebParam(name = "cantMax") int cantMax,
                       @WebParam(name = "fechaAlta") String fechaAlta,
                       @WebParam(name = "actividad") String actividad);
}
