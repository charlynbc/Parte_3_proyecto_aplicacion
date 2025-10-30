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
import logica.Usuario;
import logica.Turista;
import logica.Proveedor;

import java.io.IOException;
import java.util.List;

import jakarta.persistence.PersistenceException;
import utils.JpaUtil;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
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
        
        // Mostrar página de login
    request.getRequestDispatcher("/WEB-INF/login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String email = request.getParameter("email");
        String nickname = request.getParameter("nickname");
        
        // Validación básica
        if ((email == null || email.trim().isEmpty()) && 
            (nickname == null || nickname.trim().isEmpty())) {
            request.setAttribute("error", "Email o nickname son requeridos");
            request.getRequestDispatcher("/WEB-INF/login.jsp").forward(request, response);
            return;
        }
        
        EntityManager em = null;
        try {
            em = JpaUtil.getEntityManager();
            
            Usuario usuario = null;
            
            // Buscar por email o nickname
            if (email != null && !email.trim().isEmpty()) {
                TypedQuery<Usuario> query = em.createQuery(
                    "SELECT u FROM Usuario u WHERE u.email = :email", Usuario.class);
                query.setParameter("email", email.trim());
                List<Usuario> usuarios = query.getResultList();
                if (!usuarios.isEmpty()) {
                    usuario = usuarios.get(0);
                }
            } else if (nickname != null && !nickname.trim().isEmpty()) {
                TypedQuery<Usuario> query = em.createQuery(
                    "SELECT u FROM Usuario u WHERE u.nickname = :nickname", Usuario.class);
                query.setParameter("nickname", nickname.trim());
                List<Usuario> usuarios = query.getResultList();
                if (!usuarios.isEmpty()) {
                    usuario = usuarios.get(0);
                }
            }
            
            if (usuario != null) {
                // Login exitoso
                HttpSession session = request.getSession(true);
                session.setAttribute("usuario", usuario);
                session.setAttribute("usuarioId", usuario.getNickname());
                session.setAttribute("usuarioNombre", usuario.getNombre());
                
                // Determinar tipo de usuario
                if (usuario instanceof Turista) {
                    session.setAttribute("tipoUsuario", "turista");
                } else if (usuario instanceof Proveedor) {
                    session.setAttribute("tipoUsuario", "proveedor");
                }
                
                // Redirigir al dashboard
                response.sendRedirect(request.getContextPath() + "/dashboard");
                
            } else {
                // Login fallido
                request.setAttribute("error", "Usuario no encontrado");
                request.setAttribute("email", email);
                request.setAttribute("nickname", nickname);
                request.getRequestDispatcher("/WEB-INF/login.jsp").forward(request, response);
            }
            
        } catch (PersistenceException e) {
            request.setAttribute("error", "No se pudo conectar a la base de datos. Intenta nuevamente en unos minutos.");
            request.getRequestDispatcher("/WEB-INF/login.jsp").forward(request, response);
            return;
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }
}