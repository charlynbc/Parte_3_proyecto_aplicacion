package uy.edu.pa.central.ws;

import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebResult;
import jakarta.jws.WebService;
import uy.edu.pa.central.ws.dtos.UserDTO;
import java.util.List;

@WebService(name = "UsuariosService", targetNamespace = "http://ws.central.pa.edu.uy/")
public interface UsuariosService {

    @WebMethod(operationName = "ListarUsuarios")
    @WebResult(name = "usuarios")
    List<UserDTO> listarUsuarios();

    @WebMethod(operationName = "ListarProveedores")
    @WebResult(name = "proveedores")
    List<UserDTO> listarProveedores();

    @WebMethod(operationName = "ListarTuristas")
    @WebResult(name = "turistas")
    List<UserDTO> listarTuristas();

    @WebMethod(operationName = "ObtenerUsuario")
    @WebResult(name = "usuario")
    UserDTO obtenerUsuario(@WebParam(name = "nickname") String nickname);

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
