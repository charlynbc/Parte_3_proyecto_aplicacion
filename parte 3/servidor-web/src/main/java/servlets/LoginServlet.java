package servlets;
import exceptions.*;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import datatypes.DataUsuario;
import webserviceclients.WSUsuarioClient;

import java.io.IOException;

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

        try {
            WSUsuarioClient usuarioClient = new WSUsuarioClient();
            DataUsuario usuario = null;

            // Intentar obtener usuario por email o nickname
            if (trimmedIdentifier.contains("@")) {
                usuario = usuarioClient.obtenerUsuarioPorEmail(trimmedIdentifier.toLowerCase());
            } else {
                usuario = usuarioClient.obtenerUsuario(trimmedIdentifier);
            }

            if (usuario == null) {
                request.setAttribute("error", "Usuario no encontrado");
                request.setAttribute("username", trimmedIdentifier);
                request.getRequestDispatcher("/WEB-INF/login.jsp").forward(request, response);
                return;
            }

            // Verificar contraseña
            if (usuario.getContra() == null || !usuario.getContra().equals(trimmedPassword)) {
                request.setAttribute("error", "Credenciales inválidas");
                request.setAttribute("username", trimmedIdentifier);
                request.getRequestDispatcher("/WEB-INF/login.jsp").forward(request, response);
                return;
            }

            // Crear sesión
            HttpSession session = request.getSession(true);
            session.setAttribute("usuario", usuario);
            session.setAttribute("usuarioId", usuario.getNickname());
            session.setAttribute("usuarioNombre", usuario.getNombre());
            session.setAttribute("username", usuario.getNickname());
            session.setAttribute("email", usuario.getEmail());

            // Determinar tipo de usuario usando Web Service
            String tipoUsuario = usuarioClient.obtenerTipoUsuario(usuario.getNickname());
            if ("proveedor".equals(tipoUsuario)) {
                session.setAttribute("tipoUsuario", "proveedor");
                session.setAttribute("userType", "Proveedor");
            } else if ("turista".equals(tipoUsuario)) {
                session.setAttribute("tipoUsuario", "turista");
                session.setAttribute("userType", "Turista");
            } else {
                session.setAttribute("tipoUsuario", "usuario");
                session.setAttribute("userType", "Usuario");
            }

            response.sendRedirect(request.getContextPath() + "/dashboard");

        } catch (Exception e) {
            request.setAttribute("error", "No se pudo conectar al servidor. Intenta nuevamente en unos minutos.");
            request.setAttribute("username", trimmedIdentifier);
            request.getRequestDispatcher("/WEB-INF/login.jsp").forward(request, response);
        }
    }
}