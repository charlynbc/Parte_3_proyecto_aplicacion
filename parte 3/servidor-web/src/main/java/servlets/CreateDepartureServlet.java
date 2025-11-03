package servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;
import logica.DataSalida;
import logica.IControladorSalida;
import logica.Fabrica;
import logica.IControladorActividad;
import logica.DataActividad;
import excepciones.ActividadNoExisteException;

@WebServlet("/CreateDepartureServlet")
@MultipartConfig
public class CreateDepartureServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String activityId = request.getParameter("activityId");
            String fechaStr = request.getParameter("departureDate");
            String horaStr = request.getParameter("departureTime");
            String lugar = request.getParameter("meetingPlace");
            String cupoStr = request.getParameter("maxTourists");
            Part imagenPart = request.getPart("departureImage");

            // Validaciones bÃ¡sicas
            if (activityId == null || fechaStr == null || horaStr == null || lugar == null || cupoStr == null ||
                activityId.isEmpty() || fechaStr.isEmpty() || horaStr.isEmpty() || lugar.isEmpty() || cupoStr.isEmpty()) {
                request.setAttribute("error", "Todos los campos obligatorios deben estar completos.");
                // Repopular la lista de actividades para el JSP antes de forward
                try {
                    String proveedor = (String) request.getSession().getAttribute("username");
                    IControladorActividad ctrlAct = Fabrica.getInstance().getIControladorActividad();
                    DataActividad[] actividades = new DataActividad[0];
                    try {
                        actividades = ctrlAct.getActividadesPorProveedor(proveedor);
                    } catch (ActividadNoExisteException ex) {
                        actividades = new DataActividad[0];
                    }
                    request.setAttribute("actividades", actividades);
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
                } catch (Exception ignore) {
                    // si falla, simplemente no setear las actividades
                }
                request.getRequestDispatcher("/WEB-INF/create-departure.jsp").forward(request, response);
                return;
            }

            LocalDate fecha = LocalDate.parse(fechaStr);
            LocalTime hora = LocalTime.parse(horaStr);
            int cupo = Integer.parseInt(cupoStr);
            Date fechaAlta = new Date();
            String imagen = (imagenPart != null && imagenPart.getSize() > 0) ? imagenPart.getSubmittedFileName() : "";

            DataSalida dataSalida = new DataSalida(
                request.getParameter("nombreSalida"), // nombre (puede generarse o pedirse en el form)
                java.sql.Date.valueOf(fecha),
                hora,
                cupo,
                lugar,
                fechaAlta,
                imagen,
                activityId
            );

            IControladorSalida controlador = Fabrica.getInstance().getIControladorSalida();
            controlador.AltaSalida(dataSalida);

            request.setAttribute("success", "Salida creada exitosamente.");
            response.sendRedirect("ActivityDetailServlet?id=" + activityId);
        } catch (Exception e) {
            request.setAttribute("error", "Error al crear la salida: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/create-departure.jsp").forward(request, response);
        }
    }
}