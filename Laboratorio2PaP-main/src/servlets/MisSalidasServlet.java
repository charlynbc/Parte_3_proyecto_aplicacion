package servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import uy.edu.pa.central.client.TurismoService;
import uy.edu.pa.central.client.TurismoWebService;
import uy.edu.pa.central.client.ActividadDTO;
import uy.edu.pa.central.client.SalidaDTO;

@WebServlet(name = "MisSalidasServlet", urlPatterns = {"/mis-salidas"})
public class MisSalidasServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        String username = (session != null) ? (String) session.getAttribute("username") : null;
        String tipoUsuario = (session != null) ? (String) session.getAttribute("tipoUsuario") : null;

        if (username == null || username.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        if (tipoUsuario == null || !"proveedor".equalsIgnoreCase(tipoUsuario)) {
            request.setAttribute("error", "Solo los proveedores pueden ver sus salidas.");
            request.getRequestDispatcher("/WEB-INF/dashboard.jsp").forward(request, response);
            return;
        }

        try {
            TurismoWebService svc = new TurismoService().getTurismoWebServicePort();
            // Obtener todas las actividades del proveedor
            List<ActividadDTO> actividades = svc.listarActividadesPorProveedor(username);
            
            // Recolectar todas las salidas de todas las actividades
            List<SalidaDTO> todasLasSalidas = new ArrayList<>();
            if (actividades != null) {
                for (ActividadDTO act : actividades) {
                    if (act.getSalidas() != null) {
                        for (SalidaDTO salida : act.getSalidas()) {
                            // Agregar nombre de actividad para contexto
                            salida.setActividadNombre(act.getId());
                            todasLasSalidas.add(salida);
                        }
                    }
                }
            }
            
            request.setAttribute("salidas", todasLasSalidas);
        } catch (Exception e) {
            System.err.println("[MisSalidasServlet] Error SOAP obteniendo salidas: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("error", "No se pudieron cargar tus salidas.");
        }
        request.getRequestDispatcher("/WEB-INF/mis-salidas.jsp").forward(request, response);
    }
}
