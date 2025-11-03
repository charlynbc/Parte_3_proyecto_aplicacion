package servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.time.format.DateTimeFormatter;

import logica.Fabrica;
import logica.IControladorActividad;
import logica.IControladorInscripcion;
import logica.IControladorSalida;
import logica.DataActividad;
import logica.DataSalida;
import excepciones.ActividadNoExisteException;

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
        // Obtener parámetros de actividad y salida (pueden venir del link o del formulario)
        String actividadSeleccionada = request.getParameter("actividad");
        String salidaSeleccionada = request.getParameter("salida");
        
        // Cargar todas las actividades
        IControladorActividad ctrlAct = Fabrica.getInstance().getIControladorActividad();
        DataActividad[] actividades = new DataActividad[0];
        try {
            actividades = ctrlAct.getActividades();
        } catch (ActividadNoExisteException e) {
            // Si no hay actividades, dejamos el array vacío
        } catch (Exception e) {
            System.err.println("[InscripcionServlet] Error obteniendo actividades: " + e.getMessage());
        }

        StringBuilder actividadesHtml = new StringBuilder();
        if (actividades != null) {
            for (DataActividad a : actividades) {
                actividadesHtml.append("<option value=\"")
                        .append(a.getNombre())
                        .append("\">")
                        .append(a.getNombre())
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
                IControladorSalida ctrlSal = Fabrica.getInstance().getIControladorSalida();
                DataSalida[] salidas = ctrlSal.listarSalidasDeActividad(actividadSeleccionada);
                if (salidas != null) {
                    for (DataSalida s : salidas) {
                        salidasHtml.append("<option value=\"")
                                .append(s.getNombre())
                                .append("\">")
                                .append(s.getNombre())
                                .append(" - ")
                                .append(s.getFecha())
                                .append("</option>");
                    }
                }
            } catch (ActividadNoExisteException e) {
                // dejar la lista con la opción por defecto
            } catch (Exception e) {
                System.err.println("[InscripcionServlet] Error obteniendo salidas: " + e.getMessage());
            }
        }
        request.setAttribute("salidasHtml", salidasHtml.toString());
        
        request.getRequestDispatcher("/WEB-INF/inscripcion.jsp").forward(request, response);
    }
    
    private void procesarInscripcion(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        String turista = null;
        if (session != null) {
            turista = (String) session.getAttribute("username");
        }

        String actividad = request.getParameter("actividad");
        String salida = request.getParameter("salida");
        String cantidadStr = request.getParameter("cantidad");

        if (turista == null) {
            request.setAttribute("error", "Debe iniciar sesión como turista para inscribirse.");
            request.getRequestDispatcher("/WEB-INF/login.jsp").forward(request, response);
            return;
        }

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
        
        // Obtener costo aproximado desde la actividad
        float costo = 0.0f;
        try {
            IControladorActividad ctrlAct = Fabrica.getInstance().getIControladorActividad();
            DataActividad da = ctrlAct.verInfoActividad(actividad);
            if (da != null) {
                costo = da.getCosto();
            }
        } catch (ActividadNoExisteException e) {
            // Actividad no encontrada: costo queda en 0
        } catch (Exception e) {
            System.err.println("[InscripcionServlet] Error obteniendo costo de actividad: " + e.getMessage());
        }

        // Llamar al controlador de inscripciones
        IControladorInscripcion ctrlIns = Fabrica.getInstance().getIControladorInscripcion();
        boolean exito = false;
        try {
            exito = ctrlIns.inscribirTurista(actividad, salida, turista, cantidad, fecha, costo);
        } catch (Exception e) {
            System.err.println("[InscripcionServlet] Error al inscribir: " + e.getMessage());
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
