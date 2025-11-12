package servlets;

import uy.edu.pa.central.client.TurismoService;
import uy.edu.pa.central.client.TurismoWebService;
import uy.edu.pa.central.client.UserDTO;
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

        try {
            // Consumir SOAP del Central
            TurismoService svc = new TurismoService();
            TurismoWebService port = svc.getTurismoWebServicePort();
            UserDTO user = port.iniciarSesion(trimmedIdentifier, trimmedPassword);

            if (user == null || user.getNickname() == null) {
                request.setAttribute("error", "Credenciales inválidas");
                request.setAttribute("username", trimmedIdentifier);
                request.getRequestDispatcher("/WEB-INF/login.jsp").forward(request, response);
                return;
            }

            HttpSession session = request.getSession(true);
            session.setAttribute("username", user.getNickname());
            session.setAttribute("usuarioNombre", user.getNombre());
            session.setAttribute("email", user.getEmail());
            session.setAttribute("tipoUsuario", user.getTipoUsuario());
            session.setAttribute("userType",
                    "turista".equalsIgnoreCase(user.getTipoUsuario()) ? "Turista" :
                    ("proveedor".equalsIgnoreCase(user.getTipoUsuario()) ? "Proveedor" : "Usuario"));

            response.sendRedirect(request.getContextPath() + "/dashboard");

        } catch (Exception e) {
            request.setAttribute("error", "No se pudo autenticar contra el Servidor Central (SOAP).");
            request.setAttribute("username", trimmedIdentifier);
            request.getRequestDispatcher("/WEB-INF/login.jsp").forward(request, response);
        }
    }
}