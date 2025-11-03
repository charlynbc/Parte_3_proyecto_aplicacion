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
import java.time.LocalTime;
import java.util.Calendar;
import java.util.Date;

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
        
        EntityManager em = null;

        try {
            System.out.println("🔗 Intentando conectar a Railway...");
            // Usar el mismo EntityManager centralizado (utils.JpaUtil)
            em = JpaUtil.getEntityManager();

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
        }
        
        response.getWriter().println("<div style='text-align: center; margin-top: 30px;'>");
        response.getWriter().println("<a href='" + request.getContextPath() + "' style='background: #007bff; color: white; padding: 10px 20px; text-decoration: none; border-radius: 5px;'>🏠 Aplicación</a>");
        response.getWriter().println("<a href='" + request.getContextPath() + "/login' style='background: #28a745; color: white; padding: 10px 20px; text-decoration: none; border-radius: 5px; margin-left: 10px;'>🔐 Login</a>");
        response.getWriter().println("</div>");
        response.getWriter().println("</body></html>");
    }
    
    private void initializeTestData() {
        System.out.println("🔎 Verificación de conexión a la base de datos completada.");
        EntityManager em = null;
        try {
            em = JpaUtil.getEntityManager();

            em.getTransaction().begin();

            // Utilidades para fechas
            Calendar cal = Calendar.getInstance();
            cal.set(1980, Calendar.JANUARY, 1);
            Date fProv = cal.getTime();
            cal.set(1990, Calendar.MAY, 10);
            Date fTur1 = cal.getTime();
            cal.set(1992, Calendar.JULY, 20);
            Date fTur2 = cal.getTime();
            Date hoy = new Date();

            // Proveedores
            Proveedor prov1 = em.find(Proveedor.class, "prov1");
            if (prov1 == null) {
                prov1 = new Proveedor("prov123", "prov1", "Proveedor", "Uno", "prov1@example.com", fProv, "https://prov1.com", "Proveedor de actividades acuáticas");
                em.persist(prov1);
            }
            Proveedor prov2 = em.find(Proveedor.class, "prov2");
            if (prov2 == null) {
                prov2 = new Proveedor("prov123", "prov2", "Proveedor", "Dos", "prov2@example.com", fProv, "https://prov2.com", "Proveedor de trekking");
                em.persist(prov2);
            }

            // Actividades
            Actividad act1 = em.find(Actividad.class, "Kayak Colonia");
            if (act1 == null) {
                act1 = new Actividad("Kayak Colonia", "Remada por la bahía de Colonia", 120, 1500f, "Colonia", hoy, "ACTIVA", prov1);
                em.persist(act1);
            }
            Actividad act2 = em.find(Actividad.class, "Trekking Pan de Azúcar");
            if (act2 == null) {
                act2 = new Actividad("Trekking Pan de Azúcar", "Ascenso al cerro Pan de Azúcar", 180, 1200f, "Maldonado", hoy, "ACTIVA", prov2);
                em.persist(act2);
            }

            // Salidas
            cal = Calendar.getInstance();
            cal.add(Calendar.DATE, 7);
            Date fechaS1 = cal.getTime();
            cal.add(Calendar.DATE, 3);
            Date fechaS2 = cal.getTime();

            Salida sal1 = em.find(Salida.class, "Salida-Kayak-1");
            if (sal1 == null) {
                sal1 = new Salida("Salida-Kayak-1", fechaS1, LocalTime.of(10, 0), 20, "Puerto de Yates", hoy, "https://images.unsplash.com/photo-1506744038136-46273834b3fb?auto=format&fit=crop&w=800&q=80", act1);
                em.persist(sal1);
            }
            Salida sal2 = em.find(Salida.class, "Salida-Trekking-1");
            if (sal2 == null) {
                sal2 = new Salida("Salida-Trekking-1", fechaS2, LocalTime.of(8, 30), 15, "Base del cerro", hoy, "https://images.unsplash.com/photo-1465101046530-73398c7f28ca?auto=format&fit=crop&w=800&q=80", act2);
                em.persist(sal2);
            }

            // Turistas
            Turista tur1 = em.find(Turista.class, "turi1");
            if (tur1 == null) {
                tur1 = new Turista("turi123", "turi1", "Ana", "García", "ana@example.com", fTur1, "Uruguaya");
                em.persist(tur1);
            }
            Turista tur2 = em.find(Turista.class, "turi2");
            if (tur2 == null) {
                tur2 = new Turista("turi123", "turi2", "Luis", "Pérez", "luis@example.com", fTur2, "Argentina");
                em.persist(tur2);
            }

            // Inscripciones
            Long ins1Count = em.createQuery(
                    "SELECT COUNT(i) FROM Inscripcion i WHERE i.turista.nickname = :t AND i.salida.nombre = :s",
                    Long.class)
                .setParameter("t", "turi1")
                .setParameter("s", "Salida-Kayak-1")
                .getSingleResult();
            if (ins1Count == 0) {
                Inscripcion ins1 = new Inscripcion(hoy, 2, act1.getCosto() * 2, tur1, sal1);
                em.persist(ins1);
            }

            Long ins2Count = em.createQuery(
                    "SELECT COUNT(i) FROM Inscripcion i WHERE i.turista.nickname = :t AND i.salida.nombre = :s",
                    Long.class)
                .setParameter("t", "turi2")
                .setParameter("s", "Salida-Trekking-1")
                .getSingleResult();
            if (ins2Count == 0) {
                Inscripcion ins2 = new Inscripcion(hoy, 1, act2.getCosto(), tur2, sal2);
                em.persist(ins2);
            }

            em.getTransaction().commit();
            System.out.println("✅ Seeding completado (idempotente): proveedores, actividades, salidas, turistas e inscripciones.");
        } catch (Exception ex) {
            if (em != null && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            System.err.println("❌ Error durante seeding de datos: " + ex.getMessage());
            ex.printStackTrace();
        } finally {
            if (em != null) em.close();
        }
    }
}
