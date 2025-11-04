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
}
