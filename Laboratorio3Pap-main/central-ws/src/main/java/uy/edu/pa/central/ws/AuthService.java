package uy.edu.pa.central.ws;

import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebResult;
import jakarta.jws.WebService;
import uy.edu.pa.central.ws.dtos.UserDTO;

@WebService(name = "AuthService", targetNamespace = "http://ws.central.pa.edu.uy/")
public interface AuthService {

    @WebMethod(operationName = "Ping")
    @WebResult(name = "pong")
    String ping();

    @WebMethod(operationName = "Login")
    @WebResult(name = "user")
    UserDTO login(@WebParam(name = "identifier") String identifier,
                  @WebParam(name = "password") String password) throws Exception;

    @WebMethod(operationName = "RegistrarTurista")
    @WebResult(name = "exito")
    boolean registrarTurista(
        @WebParam(name = "nickname") String nickname,
        @WebParam(name = "nombre") String nombre,
        @WebParam(name = "apellido") String apellido,
        @WebParam(name = "email") String email,
        @WebParam(name = "password") String password,
        @WebParam(name = "fechaNacimiento") String fechaNacimiento,
        @WebParam(name = "nacionalidad") String nacionalidad
    ) throws Exception;

    @WebMethod(operationName = "RegistrarProveedor")
    @WebResult(name = "exito")
    boolean registrarProveedor(
        @WebParam(name = "nickname") String nickname,
        @WebParam(name = "nombre") String nombre,
        @WebParam(name = "apellido") String apellido,
        @WebParam(name = "email") String email,
        @WebParam(name = "password") String password,
        @WebParam(name = "fechaNacimiento") String fechaNacimiento,
        @WebParam(name = "descripcion") String descripcion,
        @WebParam(name = "link") String link
    ) throws Exception;
}
