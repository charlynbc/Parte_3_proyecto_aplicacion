package uy.edu.pa.central.ws;

import jakarta.jws.WebService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import uy.edu.pa.central.ws.dtos.UserDTO;
import uy.edu.pa.central.persistence.JpaUtil;
import logica.Usuario;
import logica.Turista;
import logica.Proveedor;

import java.util.List;

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

        EntityManager em = null;
        try {
            em = JpaUtil.getEntityManager();
            
            // Buscar usuario por nickname o email
            TypedQuery<Usuario> query = em.createQuery(
                "SELECT u FROM Usuario u WHERE u.nickname = :id OR u.email = :id", Usuario.class);
            query.setParameter("id", identifier.trim());
            List<Usuario> usuarios = query.getResultList();
            
            if (usuarios.isEmpty()) {
                throw new Exception("Usuario no encontrado");
            }
            
            Usuario usuario = usuarios.get(0);
            
            // Validar contraseña (comparación directa - en producción usar hash)
            if (!password.equals(usuario.getContra())) {
                throw new Exception("Contraseña incorrecta");
            }
            
            // Determinar tipo de usuario
            String tipoUsuario;
            if (usuario instanceof Turista) {
                tipoUsuario = "turista";
            } else if (usuario instanceof Proveedor) {
                tipoUsuario = "proveedor";
            } else {
                tipoUsuario = "usuario";
            }
            
            return new UserDTO(
                usuario.getNickname(),
                usuario.getNombre() + " " + usuario.getApellido(),
                usuario.getEmail(),
                tipoUsuario
            );
            
        } catch (Exception e) {
            if (e.getMessage().contains("no encontrado") || e.getMessage().contains("incorrecta")) {
                throw e;
            }
            System.err.println("[AuthService] Error en login: " + e.getMessage());
            throw new Exception("Error al procesar login: " + e.getMessage());
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    @Override
    public boolean registrarTurista(String nickname, String nombre, String apellido, String email, 
                                    String password, String fechaNacimiento, String nacionalidad) throws Exception {
        if (nickname == null || nickname.isBlank() || email == null || email.isBlank()) {
            throw new Exception("Nickname y email son obligatorios");
        }

        EntityManager em = null;
        try {
            em = JpaUtil.getEntityManager();
            
            // Verificar si ya existe
            TypedQuery<Usuario> query = em.createQuery(
                "SELECT u FROM Usuario u WHERE u.nickname = :nick OR u.email = :email", Usuario.class);
            query.setParameter("nick", nickname);
            query.setParameter("email", email);
            
            if (!query.getResultList().isEmpty()) {
                throw new Exception("Ya existe un usuario con ese nickname o email");
            }
            
            // Crear turista
            em.getTransaction().begin();
            
            Turista turista = new Turista();
            turista.setNickname(nickname);
            turista.setNombre(nombre);
            turista.setApellido(apellido);
            turista.setEmail(email);
            turista.setContra(password); // En producción debería hashearse
            turista.setNacionalidad(nacionalidad);
            
            // Parsear y setear fecha de nacimiento
            if (fechaNacimiento != null && !fechaNacimiento.isBlank()) {
                try {
                    java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
                    java.util.Date fechaNac = sdf.parse(fechaNacimiento);
                    turista.setFechaNac(fechaNac);
                    System.out.println("[AuthService] Fecha nacimiento parseada: " + fechaNac);
                } catch (Exception e) {
                    System.err.println("[AuthService] Error parsing date: " + e.getMessage());
                    throw new Exception("Formato de fecha inválido");
                }
            } else {
                throw new Exception("Fecha de nacimiento es obligatoria");
            }
            
            em.persist(turista);
            em.getTransaction().commit();
            
            System.out.println("[AuthService] Turista registrado: " + nickname);
            return true;
            
        } catch (Exception e) {
            if (em != null && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            System.err.println("[AuthService] Error registrando turista: " + e.getMessage());
            throw new Exception("Error al registrar turista: " + e.getMessage());
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    @Override
    public boolean registrarProveedor(String nickname, String nombre, String apellido, String email,
                                      String password, String fechaNacimiento, String descripcion, String link) throws Exception {
        if (nickname == null || nickname.isBlank() || email == null || email.isBlank()) {
            throw new Exception("Nickname y email son obligatorios");
        }

        EntityManager em = null;
        try {
            em = JpaUtil.getEntityManager();
            
            // Verificar si ya existe
            TypedQuery<Usuario> query = em.createQuery(
                "SELECT u FROM Usuario u WHERE u.nickname = :nick OR u.email = :email", Usuario.class);
            query.setParameter("nick", nickname);
            query.setParameter("email", email);
            
            if (!query.getResultList().isEmpty()) {
                throw new Exception("Ya existe un usuario con ese nickname o email");
            }
            
            // Crear proveedor
            em.getTransaction().begin();
            
            Proveedor proveedor = new Proveedor();
            proveedor.setNickname(nickname);
            proveedor.setNombre(nombre);
            proveedor.setApellido(apellido);
            proveedor.setEmail(email);
            proveedor.setContra(password);
            proveedor.setDescripcion(descripcion);
            
            // Parsear y setear fecha de nacimiento
            if (fechaNacimiento != null && !fechaNacimiento.isBlank()) {
                try {
                    java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
                    java.util.Date fechaNac = sdf.parse(fechaNacimiento);
                    proveedor.setFechaNac(fechaNac);
                } catch (Exception e) {
                    System.err.println("[AuthService] Error parsing date: " + e.getMessage());
                    throw new Exception("Formato de fecha inválido");
                }
            } else {
                throw new Exception("Fecha de nacimiento es obligatoria");
            }
            
            em.persist(proveedor);
            em.getTransaction().commit();
            
            System.out.println("[AuthService] Proveedor registrado: " + nickname);
            return true;
            
        } catch (Exception e) {
            if (em != null && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            System.err.println("[AuthService] Error registrando proveedor: " + e.getMessage());
            throw new Exception("Error al registrar proveedor: " + e.getMessage());
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }
}
