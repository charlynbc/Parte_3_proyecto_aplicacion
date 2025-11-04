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
import uy.edu.pa.central.client.ActividadesService;
import uy.edu.pa.central.client.ActividadesService_Service;
import uy.edu.pa.central.client.ActividadDTO;

@WebServlet(name = "ActivitiesServlet", urlPatterns = {"/activities"})
public class ActivitiesServlet extends HttpServlet {
    private ActividadesService service;

    @Override
    public void init() throws ServletException {
        super.init();
        // Inicializar stub SOAP apuntando al endpoint del central en el mismo Tomcat
        ActividadesService_Service svc = new ActividadesService_Service();
        service = svc.getActividadesServicePort();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        try {
            java.util.List<ActividadDTO> resultado = service.listarActividades();
            request.setAttribute("activities", resultado);
        } catch (Exception e) {
            request.setAttribute("activities", java.util.Collections.emptyList());
            request.setAttribute("error", "No se pudieron cargar las actividades desde el Servidor Central (SOAP).");
        }

        request.getRequestDispatcher("/WEB-INF/activities.jsp").forward(request, response);
    }
}