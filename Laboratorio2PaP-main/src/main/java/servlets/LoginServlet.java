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
        
        String identifier = request.getParameter("username");
        String password = request.getParameter("password");

        String trimmedIdentifier = identifier == null ? null : identifier.trim();
        String trimmedPassword = password == null ? null : password.trim();

        if (trimmedIdentifier == null || trimmedIdentifier.isEmpty() ||
            trimmedPassword == null || trimmedPassword.isEmpty()) {
            request.setAttribute("error", "Debes ingresar usuario y contraseña");
            request.setAttribute("username", trimmedIdentifier);
            request.getRequestDispatcher("/WEB-INF/login.jsp").forward(request, response);
            return;
        }

        EntityManager em = null;
        try {
            em = JpaUtil.getEntityManager();

            Usuario usuario = null;
            TypedQuery<Usuario> query;

            if (trimmedIdentifier.contains("@")) {
                query = em.createQuery("SELECT u FROM Usuario u WHERE LOWER(u.email) = :email", Usuario.class);
                query.setParameter("email", trimmedIdentifier.toLowerCase());
            } else {
                query = em.createQuery("SELECT u FROM Usuario u WHERE u.nickname = :nickname", Usuario.class);
                query.setParameter("nickname", trimmedIdentifier);
            }

            List<Usuario> usuarios = query.setMaxResults(1).getResultList();
            if (!usuarios.isEmpty()) {
                usuario = usuarios.get(0);
            }

            if (usuario == null) {
                request.setAttribute("error", "Usuario no encontrado");
                request.setAttribute("username", trimmedIdentifier);
                request.getRequestDispatcher("/WEB-INF/login.jsp").forward(request, response);
                return;
            }

            String storedPassword = usuario.getContra();
            if (storedPassword == null || !storedPassword.equals(trimmedPassword)) {
                request.setAttribute("error", "Credenciales inválidas");
                request.setAttribute("username", trimmedIdentifier);
                request.getRequestDispatcher("/WEB-INF/login.jsp").forward(request, response);
                return;
            }

            HttpSession session = request.getSession(true);
            session.setAttribute("usuario", usuario);
            session.setAttribute("usuarioId", usuario.getNickname());
            session.setAttribute("usuarioNombre", usuario.getNombre());

            if (usuario instanceof Turista) {
                session.setAttribute("tipoUsuario", "turista");
                session.setAttribute("userType", "Turista");
            } else if (usuario instanceof Proveedor) {
                session.setAttribute("tipoUsuario", "proveedor");
                session.setAttribute("userType", "Proveedor");
            } else {
                session.setAttribute("userType", "Usuario");
            }

            response.sendRedirect(request.getContextPath() + "/dashboard");

        } catch (PersistenceException e) {
            request.setAttribute("error", "No se pudo conectar a la base de datos. Intenta nuevamente en unos minutos.");
            request.setAttribute("username", trimmedIdentifier);
            request.getRequestDispatcher("/WEB-INF/login.jsp").forward(request, response);
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }
}