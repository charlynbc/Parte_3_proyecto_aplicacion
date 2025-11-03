package webserviceclients;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import java.net.URL;

/**
 * Cliente para el Web Service de Inscripcion
 */
public class WSInscripcionClient {
    
    private static final String WSDL_URL = "http://localhost:9128/webservices/inscripcion?wsdl";
    private static final String NAMESPACE = "http://publicadores/";
    private static final String SERVICE_NAME = "WSInscripcionService";
    
    private WSInscripcionInterface port;
    
    public WSInscripcionClient() {
        try {
            URL url = new URL(WSDL_URL);
            QName qname = new QName(NAMESPACE, SERVICE_NAME);
            Service service = Service.create(url, qname);
            port = service.getPort(WSInscripcionInterface.class);
        } catch (Exception e) {
            throw new RuntimeException("Error al conectar con el Web Service de Inscripcion: " + e.getMessage(), e);
        }
    }
    
    public boolean inscribirTurista(String actividad, String salida, String turista, int cantidad, String fecha, float costo) {
        return port.inscribirTurista(actividad, salida, turista, cantidad, fecha, costo);
    }
}
