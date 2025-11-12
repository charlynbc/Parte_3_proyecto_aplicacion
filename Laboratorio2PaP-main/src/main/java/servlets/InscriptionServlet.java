package servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.time.format.DateTimeFormatter;

import uy.edu.pa.central.client.TurismoService;
import uy.edu.pa.central.client.TurismoWebService;
import uy.edu.pa.central.client.ActividadDTO;
import uy.edu.pa.central.client.SalidaDTO;

@WebServlet("/inscripcion")
public class InscriptionServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        mostrarFormulario(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Verificar si es un submit del formulario (tiene cantidad) o solo cambio de actividad
        String cantidadStr = request.getParameter("cantidad");
        
        if (cantidadStr != null && !cantidadStr.trim().isEmpty()) {
            // Es un intento de inscripción
            procesarInscripcion(request, response);
        } else {
            // Solo está recargando para ver salidas de otra actividad
            mostrarFormulario(request, response);
        }
    }
    
    private void mostrarFormulario(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("username") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String tipoUsuario = (String) session.getAttribute("tipoUsuario");
        if (tipoUsuario == null || !"turista".equalsIgnoreCase(tipoUsuario)) {
            request.setAttribute("error", "Solo los turistas pueden inscribirse a salidas turisticas.");
            request.getRequestDispatcher("/WEB-INF/dashboard.jsp").forward(request, response);
            return;
        }

        // Obtener parámetros de actividad y salida (pueden venir del link o del formulario)
        String actividadSeleccionada = request.getParameter("actividad");
        String salidaSeleccionada = request.getParameter("salida");
        
        // Cargar todas las actividades vía SOAP
        TurismoWebService svc = new TurismoService().getTurismoWebServicePort();
        java.util.List<ActividadDTO> actividades = java.util.Collections.emptyList();
        try {
            actividades = svc.listarActividades();
        } catch (Exception e) {
            System.err.println("[InscriptionServlet] Error SOAP listarActividades: " + e.getMessage());
        }

        StringBuilder actividadesHtml = new StringBuilder();
        if (actividades != null) {
            for (ActividadDTO a : actividades) {
                actividadesHtml.append("<option value=\"")
                        .append(a.getId())
                        .append("\">")
                        .append(a.getId())
                        .append("</option>");
            }
        }
        request.setAttribute("actividadesHtml", actividadesHtml.toString());
        
        // Pasar los parámetros seleccionados al JSP para que los pre-seleccione
        if (actividadSeleccionada != null) {
            request.setAttribute("actividadSeleccionada", actividadSeleccionada);
        }
        if (salidaSeleccionada != null) {
            request.setAttribute("salidaSeleccionada", salidaSeleccionada);
        }
        
        // Si se recibió una actividad, cargar sus salidas
        StringBuilder salidasHtml = new StringBuilder();
        salidasHtml.append("<option value=\"\">-- Seleccione una salida --</option>");
        if (actividadSeleccionada != null && !actividadSeleccionada.trim().isEmpty()) {
            try {
                // Buscar la actividad seleccionada en el listado y usar sus salidas
                ActividadDTO actSel = null;
                for (ActividadDTO a : actividades) {
                    if (actividadSeleccionada.equals(a.getId())) { actSel = a; break; }
                }
                if (actSel == null) { actSel = svc.obtenerDetalleActividad(actividadSeleccionada); }
                if (actSel != null && actSel.getSalidas() != null) {
                    for (SalidaDTO s : actSel.getSalidas()) {
                        salidasHtml.append("<option value=\"")
                                .append(s.getId())
                                .append("\">")
                                .append(s.getId())
                                .append(" - ")
                                .append(s.getFecha())
                                .append("</option>");
                    }
                }
            } catch (Exception e) {
                System.err.println("[InscriptionServlet] Error SOAP obteniendo salidas: " + e.getMessage());
            }
        }
        request.setAttribute("salidasHtml", salidasHtml.toString());
        
        request.getRequestDispatcher("/WEB-INF/inscripcion.jsp").forward(request, response);
    }
    
    private void procesarInscripcion(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("username") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String tipoUsuario = (String) session.getAttribute("tipoUsuario");
        if (tipoUsuario == null || !"turista".equalsIgnoreCase(tipoUsuario)) {
            request.setAttribute("error", "Solo los turistas pueden inscribirse a salidas turisticas.");
            request.getRequestDispatcher("/WEB-INF/dashboard.jsp").forward(request, response);
            return;
        }

        String turista = (String) session.getAttribute("username");

        // Cliente SOAP
        TurismoWebService svc = new TurismoService().getTurismoWebServicePort();

        String actividad = request.getParameter("actividad");
        String salida = request.getParameter("salida");
        String cantidadStr = request.getParameter("cantidad");

        if (actividad == null || actividad.trim().isEmpty() || salida == null || salida.trim().isEmpty()) {
            request.setAttribute("error", "Complete todos los campos requeridos.");
            mostrarFormulario(request, response);
            return;
        }

        int cantidad = 1;
        try {
            cantidad = Integer.parseInt(cantidadStr);
            if (cantidad <= 0) cantidad = 1;
        } catch (NumberFormatException e) {
            cantidad = 1;
        }

        // Fecha actual en formato simple (yyyy-MM-dd)
        java.time.LocalDate hoy = java.time.LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String fecha = hoy.format(formatter);
        
        // Obtener costo aproximado desde la actividad vía SOAP
        float costo = 0.0f;
        try {
            ActividadDTO da = svc.obtenerDetalleActividad(actividad);
            if (da != null) costo = da.getCosto();
        } catch (Exception e) {
            System.err.println("[InscriptionServlet] Error SOAP obteniendo costo de actividad: " + e.getMessage());
        }

        // Llamar al servicio SOAP de inscripciones (inscribirTurista)
        boolean exito = false;
        try {
            exito = svc.inscribirTurista(turista, salida, cantidad, fecha);
        } catch (Exception e) {
            System.err.println("[InscriptionServlet] Error SOAP al inscribir: " + e.getMessage());
            exito = false;
        }

        if (exito) {
            request.setAttribute("success", "Inscripción realizada correctamente.");
            request.getRequestDispatcher("/WEB-INF/inscripcion-result.jsp").forward(request, response);
        } else {
            request.setAttribute("error", "No se pudo realizar la inscripción. Puede que ya esté inscrito o no haya cupo.");
            mostrarFormulario(request, response);
        }
    }
}
