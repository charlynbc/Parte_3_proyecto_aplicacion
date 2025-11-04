package filters;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter(urlPatterns = {"/mis-salidas"})
public class TuristaOnlyFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // no-op
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        HttpSession session = req.getSession(false);
        String username = (session != null) ? (String) session.getAttribute("username") : null;
        String tipoUsuario = (session != null) ? (String) session.getAttribute("tipoUsuario") : null;

        if (username == null || username.trim().isEmpty()) {
            res.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        if (tipoUsuario == null || !"turista".equalsIgnoreCase(tipoUsuario)) {
            req.setAttribute("error", "Solo los usuarios Turista pueden ver sus salidas registradas.");
            req.getRequestDispatcher("/WEB-INF/my-registrations.jsp").forward(req, res);
            return;
        }

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        // no-op
    }
}
