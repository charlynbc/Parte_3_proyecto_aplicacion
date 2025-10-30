package servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import logica.Fabrica;
import logica.IControladorActividad;
import logica.DataActividad;
import excepciones.ActividadNoExisteException;

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
        
        List<java.util.Map<String, Object>> actividades = new ArrayList<>();
        try {
            DataActividad[] resultado = controladorActividad.getActividades();
            if (resultado != null) {
                for (DataActividad act : resultado) {
                    java.util.Map<String, Object> map = new java.util.HashMap<>();
                    map.put("nombre", act.getNombre());
                    map.put("descripcion", act.getDescripcion());
                    map.put("duracion", act.getDuracion());
                    map.put("costo", act.getCosto());
                    map.put("ciudad", act.getCiudad());
                    map.put("estado", act.getEstado());
                    map.put("proveedor", act.getProveedor());
                    actividades.add(map);
                }
            }
            request.setAttribute("activities", actividades);
        } catch (ActividadNoExisteException ex) {
            request.setAttribute("activities", Collections.emptyList());
        } catch (Exception e) {
            request.setAttribute("error", "No se pudieron cargar las actividades. Verifica la conexión a la base de datos.");
        }

        request.getRequestDispatcher("/WEB-INF/activities.jsp").forward(request, response);
    }
}