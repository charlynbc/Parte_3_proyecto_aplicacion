package servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import uy.edu.pa.central.client.TurismoService;
import uy.edu.pa.central.client.TurismoWebService;
import uy.edu.pa.central.client.ActividadDTO;
import uy.edu.pa.central.client.SalidaDTO;
import view.ActivityView;
import view.DepartureView;

@WebServlet("/activity-detail")
public class ActivityDetailServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String nombre = request.getParameter("name");
		if (nombre == null || nombre.trim().isEmpty()) {
			response.sendRedirect(request.getContextPath() + "/activities");
			return;
		}

		// Consumir el servicio SOAP del Central
		TurismoWebService service = new TurismoService().getTurismoWebServicePort();

		try {
			ActividadDTO act = service.obtenerDetalleActividad(nombre);
			if (act == null || act.getId() == null) {
				request.getSession().removeAttribute("isActivityOwner");
				response.sendRedirect(request.getContextPath() + "/activities");
				return;
			}

			// Adaptar a las propiedades que el JSP espera
			ActivityView activityView = new ActivityView(
				act.getId(),
				act.getDescripcion() != null ? act.getDescripcion() : "",
				act.getCosto(),
				act.getLugar() != null ? act.getLugar() : "-"
			);

			List<DepartureView> departures = new ArrayList<>();
			if (act.getSalidas() != null) {
				for (SalidaDTO s : act.getSalidas()) {
					departures.add(new DepartureView(
						s.getId(),
						s.getFecha(),
						s.getHora(),
						s.getLugar()
					));
				}
			}

			request.setAttribute("activity", activityView);
			request.setAttribute("departures", departures);

			// Sin proveedor en SOAP de ejemplo -> dueña false
			request.getSession().setAttribute("isActivityOwner", false);

			request.getRequestDispatcher("/WEB-INF/activity-detail.jsp").forward(request, response);
		} catch (Exception ex) {
			request.getSession().removeAttribute("isActivityOwner");
			request.setAttribute("error", "No se pudo obtener la actividad desde el Servidor Central (SOAP).");
			response.sendRedirect(request.getContextPath() + "/activities");
		}
	}

}