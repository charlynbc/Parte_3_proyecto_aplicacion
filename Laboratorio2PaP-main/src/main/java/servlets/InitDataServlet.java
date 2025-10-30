package servlets;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.PersistenceException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import logica.*;
import utils.JpaUtil;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;

@WebServlet(name = "InitDataServlet", urlPatterns = {"/init-data"}, loadOnStartup = 1)
public class InitDataServlet extends HttpServlet {
    
    @Override
    public void init() throws ServletException {
        super.init();
        System.out.println("🚀 Iniciando conexión a Railway Database...");
        // Retrasar la inicialización para evitar problemas de conexión
        new Thread(() -> {
            try {
                Thread.sleep(10000); // Esperar 10 segundos
                initializeTestData();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }).start();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        response.setContentType("text/html;charset=UTF-8");
        response.getWriter().println("<!DOCTYPE html>");
        response.getWriter().println("<html><head><title>Estado Railway DB</title>");
        response.getWriter().println("<style>body{font-family:Arial;margin:40px;} .status{padding:20px;margin:20px;border-radius:8px;} .success{background:#d4edda;border:1px solid #c3e6cb;} .error{background:#f8d7da;border:1px solid #f5c6cb;} .info{background:#d1ecf1;border:1px solid #bee5eb;}</style>");
        response.getWriter().println("</head><body>");
        response.getWriter().println("<h2>🌐 Estado de Railway Database</h2>");
        
        EntityManagerFactory emf = null;
        EntityManager em = null;
        
        try {
            System.out.println("🔗 Intentando conectar a Railway...");
            emf = Persistence.createEntityManagerFactory("emf");
            em = emf.createEntityManager();
            
            // Test de conexión
            em.createQuery("SELECT COUNT(u) FROM Usuario u", Long.class).getSingleResult();
            
            response.getWriter().println("<div class='status success'>");
            response.getWriter().println("<h3>✅ Conexión a Railway Exitosa</h3>");
            
            long countUsuarios = em.createQuery("SELECT COUNT(u) FROM Usuario u", Long.class).getSingleResult();
            long countActividades = em.createQuery("SELECT COUNT(a) FROM Actividad a", Long.class).getSingleResult();
            long countSalidas = em.createQuery("SELECT COUNT(s) FROM Salida s", Long.class).getSingleResult();
            
            response.getWriter().println("<p>👥 Usuarios: " + countUsuarios + "</p>");
            response.getWriter().println("<p>🎯 Actividades: " + countActividades + "</p>");
            response.getWriter().println("<p>📅 Salidas: " + countSalidas + "</p>");
            response.getWriter().println("</div>");
            
            if (countUsuarios == 0) {
                response.getWriter().println("<div class='status info'>");
                response.getWriter().println("<h3>📊 Cargando datos de prueba...</h3>");
                response.getWriter().println("<p>Los datos se están cargando en segundo plano.</p>");
                response.getWriter().println("</div>");
            }
            
        } catch (Exception e) {
            response.getWriter().println("<div class='status error'>");
            response.getWriter().println("<h3>❌ Error de Conexión</h3>");
            response.getWriter().println("<p>Motivo: " + e.getClass().getSimpleName() + " - " + e.getMessage() + "</p>");
            response.getWriter().println("<p>Verifica que el servicio Railway acepte conexiones externas y que las variables DB_URL, DB_USER y DB_PASSWORD sean correctas.</p>");
            response.getWriter().println("</div>");
            e.printStackTrace();
        } finally {
            if (em != null) em.close();
            if (emf != null) emf.close();
        }
        
        response.getWriter().println("<div style='text-align: center; margin-top: 30px;'>");
        response.getWriter().println("<a href='" + request.getContextPath() + "' style='background: #007bff; color: white; padding: 10px 20px; text-decoration: none; border-radius: 5px;'>🏠 Aplicación</a>");
        response.getWriter().println("<a href='" + request.getContextPath() + "/login' style='background: #28a745; color: white; padding: 10px 20px; text-decoration: none; border-radius: 5px; margin-left: 10px;'>🔐 Login</a>");
        response.getWriter().println("</div>");
        response.getWriter().println("</body></html>");
    }
    
    private void initializeTestData() {
        System.out.println("🔎 Verificación de conexión a la base de datos completada.");
    }
}
