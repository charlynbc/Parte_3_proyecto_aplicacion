package servlets;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import logica.*;
import jakarta.persistence.PersistenceException;
import utils.JpaUtil;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // Si ya está logueado, redirigir al dashboard
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("usuario") != null) {
            response.sendRedirect(request.getContextPath() + "/dashboard");
            return;
        }
        
        // Mostrar página de registro
        request.getRequestDispatcher("/WEB-INF/jsp/register.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String tipoUsuario = request.getParameter("tipoUsuario");
        String nickname = request.getParameter("nickname");
        String email = request.getParameter("email");
        String nombre = request.getParameter("nombre");
        String apellido = request.getParameter("apellido");
        String fechaNacimientoStr = request.getParameter("fechaNacimiento");
        
        // Campos específicos para turista
        String nacionalidad = request.getParameter("nacionalidad");
        
        // Campos específicos para proveedor
        String descripcion = request.getParameter("descripcion");
        String link = request.getParameter("link");
        
        // Validaciones básicas
        if (nickname == null || nickname.trim().isEmpty() ||
            email == null || email.trim().isEmpty() ||
            nombre == null || nombre.trim().isEmpty() ||
            apellido == null || apellido.trim().isEmpty() ||
            fechaNacimientoStr == null || fechaNacimientoStr.trim().isEmpty() ||
            tipoUsuario == null || tipoUsuario.trim().isEmpty()) {
            
            request.setAttribute("error", "Todos los campos obligatorios deben ser completados");
            preserveFormData(request, nickname, email, nombre, apellido, fechaNacimientoStr, 
                           tipoUsuario, nacionalidad, descripcion, link);
            request.getRequestDispatcher("/WEB-INF/jsp/register.jsp").forward(request, response);
            return;
        }
        
        // Validaciones específicas por tipo
        if ("turista".equals(tipoUsuario) && (nacionalidad == null || nacionalidad.trim().isEmpty())) {
            request.setAttribute("error", "La nacionalidad es obligatoria para turistas");
            preserveFormData(request, nickname, email, nombre, apellido, fechaNacimientoStr, 
                           tipoUsuario, nacionalidad, descripcion, link);
            request.getRequestDispatcher("/WEB-INF/jsp/register.jsp").forward(request, response);
            return;
        }
        
        if ("proveedor".equals(tipoUsuario) && (descripcion == null || descripcion.trim().isEmpty())) {
            request.setAttribute("error", "La descripción es obligatoria para proveedores");
            preserveFormData(request, nickname, email, nombre, apellido, fechaNacimientoStr, 
                           tipoUsuario, nacionalidad, descripcion, link);
            request.getRequestDispatcher("/WEB-INF/jsp/register.jsp").forward(request, response);
            return;
        }
        
        try (EntityManager em = JpaUtil.getEntityManager()) {
            LocalDate fechaNacimiento = LocalDate.parse(fechaNacimientoStr);
            
            // Verificar si ya existe el nickname o email
            TypedQuery<Usuario> queryNickname = em.createQuery(
                "SELECT u FROM Usuario u WHERE u.nickname = :nickname", Usuario.class);
            queryNickname.setParameter("nickname", nickname.trim());
            
            TypedQuery<Usuario> queryEmail = em.createQuery(
                "SELECT u FROM Usuario u WHERE u.email = :email", Usuario.class);
            queryEmail.setParameter("email", email.trim().toLowerCase());
            
            if (!queryNickname.getResultList().isEmpty()) {
                request.setAttribute("error", "El nickname ya está en uso");
                preserveFormData(request, nickname, email, nombre, apellido, fechaNacimientoStr, 
                               tipoUsuario, nacionalidad, descripcion, link);
                request.getRequestDispatcher("/WEB-INF/jsp/register.jsp").forward(request, response);
                return;
            }
            
            if (!queryEmail.getResultList().isEmpty()) {
                request.setAttribute("error", "El email ya está registrado");
                preserveFormData(request, nickname, email, nombre, apellido, fechaNacimientoStr, 
                               tipoUsuario, nacionalidad, descripcion, link);
                request.getRequestDispatcher("/WEB-INF/jsp/register.jsp").forward(request, response);
                return;
            }
            
            em.getTransaction().begin();
            
            Usuario usuario = null;
            
            if ("turista".equals(tipoUsuario)) {
                Turista turista = new Turista();
                turista.setNickname(nickname.trim());
                turista.setEmail(email.trim().toLowerCase());
                turista.setNombre(nombre.trim());
                turista.setApellido(apellido.trim());
                turista.setNacionalidad(nacionalidad.trim());
                usuario = turista;
                
            } else if ("proveedor".equals(tipoUsuario)) {
                Proveedor proveedor = new Proveedor();
                proveedor.setNickname(nickname.trim());
                proveedor.setEmail(email.trim().toLowerCase());
                proveedor.setNombre(nombre.trim());
                proveedor.setApellido(apellido.trim());
                proveedor.setDescripcion(descripcion.trim());
                usuario = proveedor;
            }
            
            if (usuario != null) {
                em.persist(usuario);
                em.getTransaction().commit();
                
                // Crear sesión automáticamente después del registro
                HttpSession session = request.getSession(true);
                session.setAttribute("usuario", usuario);
                session.setAttribute("usuarioId", usuario.getNickname());
                session.setAttribute("usuarioNombre", usuario.getNombre());
                session.setAttribute("tipoUsuario", tipoUsuario);
                
                // Redirigir al dashboard con mensaje de éxito
                response.sendRedirect(request.getContextPath() + "/dashboard?registered=true");
                
            } else {
                request.setAttribute("error", "Tipo de usuario no válido");
                preserveFormData(request, nickname, email, nombre, apellido, fechaNacimientoStr, 
                               tipoUsuario, nacionalidad, descripcion, link);
                request.getRequestDispatcher("/WEB-INF/jsp/register.jsp").forward(request, response);
            }
            
        } catch (PersistenceException e) {
            request.setAttribute("error", "No se pudo registrar el usuario porque la base de datos no responde.");
            preserveFormData(request, nickname, email, nombre, apellido, fechaNacimientoStr, tipoUsuario, nacionalidad, descripcion, link);
            request.getRequestDispatcher("/WEB-INF/jsp/register.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Error del sistema: " + e.getMessage());
            preserveFormData(request, nickname, email, nombre, apellido, fechaNacimientoStr, 
                           tipoUsuario, nacionalidad, descripcion, link);
            request.getRequestDispatcher("/WEB-INF/jsp/register.jsp").forward(request, response);
        }
    }
    
    private void preserveFormData(HttpServletRequest request, String nickname, String email, 
                                String nombre, String apellido, String fechaNacimiento, 
                                String tipoUsuario, String nacionalidad, String descripcion, String link) {
        request.setAttribute("nickname", nickname);
        request.setAttribute("email", email);
        request.setAttribute("nombre", nombre);
        request.setAttribute("apellido", apellido);
        request.setAttribute("fechaNacimiento", fechaNacimiento);
        request.setAttribute("tipoUsuario", tipoUsuario);
        request.setAttribute("nacionalidad", nacionalidad);
        request.setAttribute("descripcion", descripcion);
        request.setAttribute("link", link);
    }
}
