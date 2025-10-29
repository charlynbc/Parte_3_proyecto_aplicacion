package servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import logica.Fabrica;
import logica.IControladorActividad;
import logica.DataActividad;
import excepciones.ActividadNoExisteException;

@WebServlet("/create-departure")
public class CreateDepartureViewServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String proveedor = (String) request.getSession().getAttribute("username");
        IControladorActividad controlador = Fabrica.getInstance().getIControladorActividad();
        DataActividad[] actividades = new DataActividad[0];
        try {
            actividades = controlador.getActividadesPorProveedor(proveedor);
        } catch (ActividadNoExisteException e) {
            // Si no hay actividades, se deja el array vacío
        }
        // Dejar también el array en el request para que el JSP (si usa scriptlets) lo pueda leer
        request.setAttribute("actividades", actividades);
        // Log para debug: cantidad de actividades y nombres
        if (actividades != null) {
            System.out.println("[CreateDepartureView] Actividades obtenidas: " + actividades.length);
            for (DataActividad a : actividades) {
                System.out.println("[CreateDepartureView] - " + a.getNombre());
            }
        } else {
            System.out.println("[CreateDepartureView] No se obtuvieron actividades (null)");
        }
        // Construir HTML con las <option> para evitar depender de JSTL en el JSP
        StringBuilder opciones = new StringBuilder();
        if (actividades != null) {
            for (DataActividad act : actividades) {
                opciones.append("<option value=\"")
                        .append(act.getNombre())
                        .append("\">")
                        .append(act.getNombre())
                        .append("</option>");
            }
        }
        request.setAttribute("actividadesHtml", opciones.toString());
        request.getRequestDispatcher("/WEB-INF/create-departure.jsp").forward(request, response);
    }
}