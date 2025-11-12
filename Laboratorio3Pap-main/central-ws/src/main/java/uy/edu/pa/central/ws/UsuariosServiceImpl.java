package uy.edu.pa.central.ws;

import jakarta.jws.WebService;
import uy.edu.pa.central.ws.dtos.UserDTO;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import uy.edu.pa.central.persistence.JpaUtil;

@WebService(
    serviceName = "UsuariosService",
    endpointInterface = "uy.edu.pa.central.ws.UsuariosService",
    portName = "UsuariosServicePort",
    targetNamespace = "http://ws.central.pa.edu.uy/"
)
public class UsuariosServiceImpl implements UsuariosService {

    @Override
    public List<UserDTO> listarUsuarios() {
        List<UserDTO> result = new ArrayList<>();
        EntityManager em = null;
        try {
            em = JpaUtil.getEntityManager();
            TypedQuery<logica.Usuario> query = em.createQuery(
                "SELECT u FROM Usuario u ORDER BY u.nickname", logica.Usuario.class);
            List<logica.Usuario> usuarios = query.getResultList();
            
            for (logica.Usuario u : usuarios) {
                String tipo = "usuario";
                if (u instanceof logica.Turista) {
                    tipo = "turista";
                } else if (u instanceof logica.Proveedor) {
                    tipo = "proveedor";
                }
                
                UserDTO dto = new UserDTO(
                    u.getNickname(),
                    u.getNombre(),
                    u.getEmail(),
                    tipo
                );
                result.add(dto);
            }
        } catch (Exception e) {
            System.err.println("[UsuariosService] Error listando usuarios: " + e.getMessage());
        } finally {
            if (em != null && em.isOpen()) em.close();
        }
        return result;
    }

    @Override
    public List<UserDTO> listarProveedores() {
        List<UserDTO> result = new ArrayList<>();
        EntityManager em = null;
        try {
            em = JpaUtil.getEntityManager();
            TypedQuery<logica.Proveedor> query = em.createQuery(
                "SELECT p FROM Proveedor p ORDER BY p.nickname", logica.Proveedor.class);
            List<logica.Proveedor> proveedores = query.getResultList();
            
            for (logica.Proveedor p : proveedores) {
                UserDTO dto = new UserDTO(
                    p.getNickname(),
                    p.getNombre(),
                    p.getEmail(),
                    "proveedor"
                );
                result.add(dto);
            }
        } catch (Exception e) {
            System.err.println("[UsuariosService] Error listando proveedores: " + e.getMessage());
        } finally {
            if (em != null && em.isOpen()) em.close();
        }
        return result;
    }

    @Override
    public List<UserDTO> listarTuristas() {
        List<UserDTO> result = new ArrayList<>();
        EntityManager em = null;
        try {
            em = JpaUtil.getEntityManager();
            TypedQuery<logica.Turista> query = em.createQuery(
                "SELECT t FROM Turista t ORDER BY t.nickname", logica.Turista.class);
            List<logica.Turista> turistas = query.getResultList();
            
            for (logica.Turista t : turistas) {
                UserDTO dto = new UserDTO(
                    t.getNickname(),
                    t.getNombre(),
                    t.getEmail(),
                    "turista"
                );
                result.add(dto);
            }
        } catch (Exception e) {
            System.err.println("[UsuariosService] Error listando turistas: " + e.getMessage());
        } finally {
            if (em != null && em.isOpen()) em.close();
        }
        return result;
    }

    @Override
    public UserDTO obtenerUsuario(String nickname) {
        EntityManager em = null;
        try {
            em = JpaUtil.getEntityManager();
            logica.Usuario u = em.find(logica.Usuario.class, nickname);
            
            if (u == null) {
                System.err.println("[UsuariosService] Usuario no encontrado: " + nickname);
                return null;
            }
            
            String tipo = "usuario";
            if (u instanceof logica.Turista) {
                tipo = "turista";
            } else if (u instanceof logica.Proveedor) {
                tipo = "proveedor";
            }
            
            UserDTO dto = new UserDTO(
                u.getNickname(),
                u.getNombre(),
                u.getEmail(),
                tipo
            );
            
            System.out.println("[UsuariosService] Usuario obtenido: " + nickname + " (tipo: " + tipo + ")");
            return dto;
            
        } catch (Exception e) {
            System.err.println("[UsuariosService] Error obteniendo usuario: " + e.getMessage());
            return null;
        } finally {
            if (em != null && em.isOpen()) em.close();
        }
    }

    @Override
    public boolean actualizarUsuario(String nickname, String nombre, String apellido, 
                                      String fechaNacimiento, String nacionalidad, 
                                      String descripcion, String sitioWeb) {
        EntityManager em = null;
        try {
            em = JpaUtil.getEntityManager();
            em.getTransaction().begin();
            
            logica.Usuario u = em.find(logica.Usuario.class, nickname);
            if (u == null) {
                System.err.println("[UsuariosService] Usuario no encontrado: " + nickname);
                return false;
            }
            
            if (nombre != null && !nombre.isBlank()) u.setNombre(nombre);
            if (apellido != null && !apellido.isBlank()) u.setApellido(apellido);
            
            if (fechaNacimiento != null && !fechaNacimiento.isBlank()) {
                java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
                u.setFechaNac(sdf.parse(fechaNacimiento));
            }
            
            if (u instanceof logica.Turista) {
                logica.Turista t = (logica.Turista) u;
                if (nacionalidad != null && !nacionalidad.isBlank()) {
                    t.setNacionalidad(nacionalidad);
                }
            } else if (u instanceof logica.Proveedor) {
                logica.Proveedor p = (logica.Proveedor) u;
                if (descripcion != null) p.setDescripcion(descripcion);
                if (sitioWeb != null) p.setSitioWeb(sitioWeb);
            }
            
            em.merge(u);
            em.getTransaction().commit();
            
            System.out.println("[UsuariosService] Usuario actualizado: " + nickname);
            return true;
            
        } catch (Exception e) {
            if (em != null && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            System.err.println("[UsuariosService] Error actualizando usuario: " + e.getMessage());
            return false;
        } finally {
            if (em != null && em.isOpen()) em.close();
        }
    }
}
