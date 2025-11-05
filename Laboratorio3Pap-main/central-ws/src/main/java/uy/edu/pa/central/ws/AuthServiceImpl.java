package uy.edu.pa.central.ws;

import jakarta.jws.WebService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;
import logica.Proveedor;
import logica.Turista;
import logica.Usuario;
import uy.edu.pa.central.persistence.JpaUtil;
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
        if (identifier == null || identifier.isBlank() || password == null || password.isBlank()) {
            throw new Exception("Credenciales inválidas");
        }

    String normalizedId = identifier.trim().toLowerCase();
    String normalizedPass = password.trim();
    System.err.println("[AuthService] Intento de login con identificador=" + normalizedId);

        EntityManager em = null;
        try {
            em = JpaUtil.getEntityManager();
            Long totalUsuarios = em.createQuery("SELECT COUNT(u) FROM Usuario u", Long.class).getSingleResult();
            System.err.println("[AuthService] Total usuarios registrados: " + totalUsuarios);

            TypedQuery<Usuario> query = em.createQuery(
                "SELECT u FROM Usuario u WHERE LOWER(u.nickname) = :ident OR LOWER(u.email) = :ident",
                Usuario.class
            );
            query.setParameter("ident", normalizedId);
            query.setMaxResults(1);

            Usuario usuario = null;
            java.util.List<Usuario> resultados = query.getResultList();
            System.err.println("[AuthService] Resultados obtenidos=" + resultados.size());
            if (!resultados.isEmpty()) {
                usuario = resultados.get(0);
                System.err.println("[AuthService] Usuario encontrado=" + usuario.getNickname() + " tipo=" + usuario.getClass().getSimpleName());
            }
            if (usuario == null) {
                System.err.println("[AuthService] Usuario no encontrado: " + normalizedId);
                throw new Exception("Credenciales inválidas");
            }

            String storedPass = usuario.getContra() == null ? "" : usuario.getContra();
            System.err.println("[AuthService] Password recibido='" + normalizedPass + "' (len=" + normalizedPass.length() + ") almacenado='" + storedPass + "' (len=" + storedPass.length() + ")");
            if (!storedPass.equals(normalizedPass)) {
                System.err.println("[AuthService] Contraseña incorrecta para usuario: " + usuario.getNickname());
                throw new Exception("Credenciales inválidas");
            }

            String tipo;
            if (usuario instanceof Turista) {
                tipo = "turista";
            } else if (usuario instanceof Proveedor) {
                tipo = "proveedor";
            } else {
                tipo = "usuario";
            }

            String nombre = usuario.getNombre() == null ? "" : usuario.getNombre().trim();
            String apellido = usuario.getApellido() == null ? "" : usuario.getApellido().trim();
            String nombreCompleto = (nombre + " " + apellido).trim();
            if (nombreCompleto.isEmpty()) {
                nombreCompleto = usuario.getNickname();
            }

            System.err.println("[AuthService] Login exitoso para=" + usuario.getNickname() + " tipo=" + tipo);
            return new UserDTO(usuario.getNickname(), nombreCompleto, usuario.getEmail(), tipo);
        } catch (Exception ex) {
            if ("Credenciales inválidas".equals(ex.getMessage())) {
                System.err.println("[AuthService] Credenciales inválidas para identificador=" + normalizedId);
                throw ex;
            }
            System.err.println("[AuthService] Error autenticando usuario: " + ex.getMessage());
            throw new Exception("Error interno al autenticar");
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }
}
