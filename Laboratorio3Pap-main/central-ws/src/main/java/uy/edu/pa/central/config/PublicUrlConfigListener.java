package uy.edu.pa.central.config;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

/**
 * Configura la URL pública para los WSDLs cuando se despliega en Codespaces
 */
@WebListener
public class PublicUrlConfigListener implements ServletContextListener {
    
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        // Detectar si estamos en Codespaces
        String codespaceName = System.getenv("CODESPACE_NAME");
        String domain = System.getenv("GITHUB_CODESPACES_PORT_FORWARDING_DOMAIN");
        
        if (codespaceName != null && !codespaceName.isEmpty() && domain != null) {
            // Construir la URL pública de Codespaces
            String publicUrl = "https://" + codespaceName + "-9128." + domain + "/central-ws/services";
            
            // Establecer la propiedad del sistema para JAX-WS Metro
            System.setProperty("com.sun.xml.ws.transport.http.servlet.publishedEndpointUrl", publicUrl);
            
            // También establecer para JAX-WS RI
            System.setProperty("jakarta.xml.ws.wsdl.publish.location", publicUrl);
            
            System.out.println("=========================================");
            System.out.println("🌐 ENDPOINT PÚBLICO CONFIGURADO");
            System.out.println("=========================================");
            System.out.println("URL: " + publicUrl);
            System.out.println("Codespace: " + codespaceName);
            System.out.println("=========================================");
            
            // Guardar en el contexto de la aplicación
            sce.getServletContext().setAttribute("publicEndpointUrl", publicUrl);
        } else {
            System.out.println("🏠 Ejecutando en localhost (no Codespaces)");
        }
    }
    
    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        // Limpiar configuración si es necesario
    }
}
