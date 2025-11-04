package servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

// Importar clases del servidor central
import logica.Fabrica;
import logica.IControladorActividad;
import logica.DataActividad;

@WebServlet(name = "ActivitiesServlet", urlPatterns = {"/activities"})
public class ActivitiesServlet extends HttpServlet {
    private IControladorActividad controladorActividad;

    @Override
    public void init() throws ServletException {
        super.init();
        Fabrica fab = Fabrica.getInstance();
        controladorActividad = fab.getIControladorActividad();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        DataActividad[] actividadesArray;

        try {
            
            actividadesArray = controladorActividad.getActividades();
            request.setAttribute("activities", actividadesArray);

        } catch (Exception e) {
            request.setAttribute("error", "No se pudieron cargar las actividades: " + e.getMessage());
        }

        request.getRequestDispatcher("/WEB-INF/activities.jsp").forward(request, response);
    }
}