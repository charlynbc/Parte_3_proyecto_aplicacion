package uy.edu.pa.central.ws;

import jakarta.jws.WebService;
import uy.edu.pa.central.ws.dtos.UserDTO;

@WebService(
    serviceName = "AuthService",
    endpointInterface = "uy.edu.pa.central.ws.AuthService",
    portName = "AuthServicePort",
    targetNamespace = "http://ws.central.pa.edu.uy/"
)
public class AuthServiceImpl implements AuthService {

    @Override
    public String ping() {
        return "pong";
    }

    @Override
    public UserDTO login(String identifier, String password) throws Exception {
        // Placeholder: en la versión final se valida contra la DB del Central
        if (identifier == null || identifier.isBlank() || password == null || password.isBlank()) {
            throw new Exception("Credenciales inválidas");
        }
        // Simulación básica de rol por conveniencia
        String tipo = identifier.toLowerCase().startsWith("turi") ? "turista" : "proveedor";
        return new UserDTO(identifier, identifier, identifier + "@example.com", tipo);
    }
}
