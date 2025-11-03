package servlets;
import exceptions.*;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import webserviceclients.WSClientFactory;
import webserviceclients.WSActividadClient;
import webserviceclients.WSSalidaClient;
import datatypes.DataActividad;
import datatypes.DataSalida;

@WebServlet("/activity-detail")
public class ActivityDetailServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String nombre = request.getParameter("name");
		if (nombre == null || nombre.trim().isEmpty()) {
			// Parámetro faltante: redirigir a la lista de actividades
			response.sendRedirect(request.getContextPath() + "/activities");
			return;
		}

		WSActividadClient ctrlAct = WSClientFactory.getInstance().getWSActividadClient();
		WSSalidaClient ctrlSal = WSClientFactory.getInstance().getWSSalidaClient();

		try {
			DataActividad actividad = ctrlAct.verInfoActividad(nombre);
			DataSalida[] salidas = new DataSalida[0];
			try {
				salidas = ctrlSal.listarSalidasDeActividad(nombre);
			} catch (Exception ex) {
				// Si no hay salidas o hay error, dejamos el array vacío
				salidas = new DataSalida[0];
			}

			request.setAttribute("activity", actividad);
			request.setAttribute("departures", salidas);

			// Marcar si el usuario logueado es el proveedor de la actividad
			String sessionUser = (String) request.getSession().getAttribute("username");
			boolean isOwner = sessionUser != null && sessionUser.equals(actividad.getProveedor());
			request.getSession().setAttribute("isActivityOwner", isOwner);

        } catch (Exception ex) {
			request.setAttribute("error", "La actividad especificada no existe: " + nombre);
			response.sendRedirect(request.getContextPath() + "/activities");
		}
	}

}