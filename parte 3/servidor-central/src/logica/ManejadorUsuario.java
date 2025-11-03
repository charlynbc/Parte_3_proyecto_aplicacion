package logica;

import jakarta.persistence.*;
import persistence.JPAUtil;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class ManejadorUsuario {
    private static ManejadorUsuario instancia = null;
    private EntityManagerFactory emf;

    private ManejadorUsuario() {
        emf = Persistence.createEntityManagerFactory("emf");
    }

    public static ManejadorUsuario getinstance() {
        if (instancia == null)
            instancia = new ManejadorUsuario();
        return instancia;
    }

    public void addUsuario(Usuario usu) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(usu);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw new RuntimeException("Error al agregar usuario");
        } finally {
            em.close();
        }
    }

    // Obtener usuario por nickname (PK)
    public Usuario obtenerUsuario(String nickname) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.find(Usuario.class, nickname);
        } finally {
            em.close();
        }
    }

    public Usuario[] getUsuarios() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            // Prueba con una consulta más específica
            List<Usuario> usuarios = em.createQuery("SELECT u FROM Usuario u WHERE u.nickname IS NOT NULL", Usuario.class)
                                      .getResultList();
            return usuarios.toArray(new Usuario[0]);
        } catch (Exception e) {
            System.out.println("Error en consulta: " + e.getMessage());
            return new Usuario[0]; // Retorna array vacío en caso de error
        } finally {
            em.close();
        }
    }

    // Obtener usuario por nickname con query (redundante, pero útil si querés validaciones más complejas)
    public Usuario obtenerUsuarioXNick(String nick) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Usuario> query = em.createQuery("SELECT u FROM Usuario u WHERE u.nickname = :nick", Usuario.class);
            query.setParameter("nick", nick);
            List<Usuario> usuarios = query.getResultList();
            return usuarios.isEmpty() ? null : usuarios.get(0);
        } finally {
            em.close();
        }
    }

    // --- Métodos de modificación ---
    public void modNombre(Usuario u, String nombre) {
        modificarCampo(u.getNickname(), usuario -> usuario.setNombre(nombre));
    }

    public void modApellido(Usuario u, String apellido) {
        modificarCampo(u.getNickname(), usuario -> usuario.setApellido(apellido));
    }

    public void modFechaNac(Usuario u, Date fechaNac) {
        modificarCampo(u.getNickname(), usuario -> usuario.setFechaNac(fechaNac));
    }

    public void modContra(Usuario u, String contra) {
        modificarCampo(u.getNickname(), usuario -> usuario.setContra(contra));
    }

    public void modNacionalidad(Usuario u, String nacionalidad) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            Turista usuario = em.find(Turista.class, u.getNickname());
            usuario.setNacionalidad(nacionalidad);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    public void modSitioWeb(Usuario u, String sitioWeb) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            Proveedor usuario = em.find(Proveedor.class, u.getNickname());
            usuario.setSitioWeb(sitioWeb);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    public void modDescripcion(Usuario u, String descripcion) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            Proveedor usuario = em.find(Proveedor.class, u.getNickname());
            usuario.setDescripcion(descripcion);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    // --- Auxiliar para simplificar modificaciones ---
    private void modificarCampo(String nickname, java.util.function.Consumer<Usuario> setter) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            Usuario usuario = em.find(Usuario.class, nickname);
            if (usuario != null) {
                setter.accept(usuario);
            }
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    // Turistas
    public Turista[] getTuristas() {
        return Arrays.stream(getUsuarios())
                .filter(usuario -> usuario instanceof Turista)
                .map(Turista.class::cast)
                .toArray(Turista[]::new);
    }
    
    public Turista obtenerTurista(String nickname) {
        
    	EntityManager em = JPAUtil.getEntityManager();
    	
    	try {
            return em.find(Turista.class, nickname);
        } finally {
            if (em != null) {
                em.close();
            }
        }
    	
    }
}

