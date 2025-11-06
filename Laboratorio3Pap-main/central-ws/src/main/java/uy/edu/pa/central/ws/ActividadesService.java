package uy.edu.pa.central.ws;

import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebResult;
import jakarta.jws.WebService;
import uy.edu.pa.central.ws.dtos.ActividadDTO;
import uy.edu.pa.central.ws.dtos.SalidaDTO;
import uy.edu.pa.central.ws.dtos.InscripcionDTO;
import uy.edu.pa.central.ws.dtos.UserDTO;
import java.util.List;

@WebService(name = "ActividadesService", targetNamespace = "http://ws.central.pa.edu.uy/")
public interface ActividadesService {

    @WebMethod(operationName = "ListarActividades")
    @WebResult(name = "actividades")
    List<ActividadDTO> listarActividades();

    @WebMethod(operationName = "ObtenerActividad")
    @WebResult(name = "actividad")
    ActividadDTO obtenerActividad(@WebParam(name = "id") String id);

    @WebMethod(operationName = "ObtenerSalida")
    @WebResult(name = "salida")
    SalidaDTO obtenerSalida(@WebParam(name = "id") String id);

    @WebMethod(operationName = "InscribirTurista")
    @WebResult(name = "exito")
    boolean inscribirTurista(
        @WebParam(name = "actividad") String actividad,
        @WebParam(name = "salida") String salida,
        @WebParam(name = "turista") String turista,
        @WebParam(name = "cantidad") int cantidad,
        @WebParam(name = "fecha") String fecha,
        @WebParam(name = "costo") float costo
    );

    @WebMethod(operationName = "ListarInscripcionesPorTurista")
    @WebResult(name = "inscripciones")
    List<InscripcionDTO> listarInscripcionesPorTurista(
        @WebParam(name = "turista") String turista
    );

    @WebMethod(operationName = "ListarUsuarios")
    @WebResult(name = "usuarios")
    List<UserDTO> listarUsuarios();

    @WebMethod(operationName = "ObtenerUsuario")
    @WebResult(name = "usuario")
    UserDTO obtenerUsuario(@WebParam(name = "nickname") String nickname);

    @WebMethod(operationName = "CrearActividad")
    @WebResult(name = "exito")
    boolean crearActividad(
        @WebParam(name = "nombre") String nombre,
        @WebParam(name = "descripcion") String descripcion,
        @WebParam(name = "duracion") int duracion,
        @WebParam(name = "costo") float costo,
        @WebParam(name = "ciudad") String ciudad,
        @WebParam(name = "proveedor") String proveedor,
        @WebParam(name = "fechaAlta") String fechaAlta
    );

    @WebMethod(operationName = "CrearSalida")
    @WebResult(name = "exito")
    boolean crearSalida(
        @WebParam(name = "nombre") String nombre,
        @WebParam(name = "fecha") String fecha,
        @WebParam(name = "hora") String hora,
        @WebParam(name = "lugar") String lugar,
        @WebParam(name = "cantMax") int cantMax,
        @WebParam(name = "fechaAlta") String fechaAlta,
        @WebParam(name = "actividad") String actividad
    );

    @WebMethod(operationName = "ActualizarUsuario")
    @WebResult(name = "exito")
    boolean actualizarUsuario(
        @WebParam(name = "nickname") String nickname,
        @WebParam(name = "nombre") String nombre,
        @WebParam(name = "apellido") String apellido,
        @WebParam(name = "fechaNacimiento") String fechaNacimiento,
        @WebParam(name = "nacionalidad") String nacionalidad,
        @WebParam(name = "descripcion") String descripcion,
        @WebParam(name = "sitioWeb") String sitioWeb
    );
}
