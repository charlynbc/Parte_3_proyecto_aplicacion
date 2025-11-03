package publicadores;

import javax.xml.ws.Endpoint;

/**
 * Publicador de Web Services
 * Este servidor expone los servicios de negocio a través de SOAP Web Services
 */
public class WebServices {
    
    private static final String BASE_URL = "http://localhost:9128/webservices/";
    
    public static void main(String[] args) {
        System.out.println("Iniciando servidor de Web Services...");
        
        try {
            // Publicar Web Service de Usuario
            Endpoint endpointUsuario = Endpoint.publish(BASE_URL + "usuario", new WSUsuario());
            System.out.println("✓ Web Service Usuario publicado en: " + BASE_URL + "usuario");
            System.out.println("  WSDL disponible en: " + BASE_URL + "usuario?wsdl");
            
            // Publicar Web Service de Actividad
            Endpoint endpointActividad = Endpoint.publish(BASE_URL + "actividad", new WSActividad());
            System.out.println("✓ Web Service Actividad publicado en: " + BASE_URL + "actividad");
            System.out.println("  WSDL disponible en: " + BASE_URL + "actividad?wsdl");
            
            // Publicar Web Service de Salida
            Endpoint endpointSalida = Endpoint.publish(BASE_URL + "salida", new WSSalida());
            System.out.println("✓ Web Service Salida publicado en: " + BASE_URL + "salida");
            System.out.println("  WSDL disponible en: " + BASE_URL + "salida?wsdl");
            
            // Publicar Web Service de Inscripcion
            Endpoint endpointInscripcion = Endpoint.publish(BASE_URL + "inscripcion", new WSInscripcion());
            System.out.println("✓ Web Service Inscripcion publicado en: " + BASE_URL + "inscripcion");
            System.out.println("  WSDL disponible en: " + BASE_URL + "inscripcion?wsdl");
            
            System.out.println("\n===========================================");
            System.out.println("Servidor de Web Services iniciado correctamente");
            System.out.println("Escuchando en puerto 9128");
            System.out.println("Presione Ctrl+C para detener el servidor");
            System.out.println("===========================================\n");
            
            // Mantener el servidor corriendo
            Thread.currentThread().join();
            
        } catch (Exception e) {
            System.err.println("Error al iniciar el servidor de Web Services:");
            e.printStackTrace();
        }
    }
}
