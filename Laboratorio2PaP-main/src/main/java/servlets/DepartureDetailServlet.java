package servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

import uy.edu.pa.central.client.TurismoService;
import uy.edu.pa.central.client.TurismoWebService;
import uy.edu.pa.central.client.SalidaDTO;
import view.DepartureView;

@WebServlet(name = "DepartureDetailServlet", urlPatterns = {"/departure-detail"})
public class DepartureDetailServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String id = req.getParameter("id");
		if (id == null || id.isBlank()) {
			resp.sendRedirect(req.getContextPath() + "/activities");
			return;
		}

		TurismoWebService svc = new TurismoService().getTurismoWebServicePort();
		try {
			SalidaDTO s = svc.obtenerSalida(id);
			if (s == null || s.getId() == null) {
				resp.sendRedirect(req.getContextPath() + "/activities");
				return;
			}
			DepartureView view = new DepartureView(s.getId(), s.getFecha(), s.getHora(), s.getLugar());
			req.setAttribute("departure", view);
			req.getRequestDispatcher("/WEB-INF/departure-detail.jsp").forward(req, resp);
		} catch (Exception e) {
			resp.sendRedirect(req.getContextPath() + "/activities");
		}
	}
}
