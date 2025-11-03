package servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import logica.Fabrica;
import logica.IControladorActividad;
import logica.IControladorSalida;
import logica.DataActividad;
import logica.DataSalida;
import excepciones.ActividadNoExisteException;

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

		IControladorActividad ctrlAct = Fabrica.getInstance().getIControladorActividad();
		IControladorSalida ctrlSal = Fabrica.getInstance().getIControladorSalida();

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

			request.getRequestDispatcher("/WEB-INF/activity-detail.jsp").forward(request, response);
		} catch (ActividadNoExisteException ex) {
			// Si la actividad no existe, redirigimos a la lista con un mensaje de error
			request.getSession().removeAttribute("isActivityOwner");
			request.setAttribute("error", "La actividad especificada no existe: " + nombre);
			response.sendRedirect(request.getContextPath() + "/activities");
		}
	}

}