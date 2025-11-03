package webserviceclients;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import java.net.URL;

/**
 * Cliente para el Web Service de Actividad
 */
public class WSActividadClient {
    
    private static final String WSDL_URL = "http://localhost:9128/webservices/actividad?wsdl";
    private static final String NAMESPACE = "http://publicadores/";
    private static final String SERVICE_NAME = "WSActividadService";
    
    private WSActividadInterface port;
    
    public WSActividadClient() {
        try {
            URL url = new URL(WSDL_URL);
            QName qname = new QName(NAMESPACE, SERVICE_NAME);
            Service service = Service.create(url, qname);
            port = service.getPort(WSActividadInterface.class);
        } catch (Exception e) {
            throw new RuntimeException("Error al conectar con el Web Service de Actividad: " + e.getMessage(), e);
        }
    }
    
    public datatypes.DataActividad[] getActividadesPorProveedor(String nicknameProveedor) throws Exception {
        return port.getActividadesPorProveedor(nicknameProveedor);
    }
    
    public void altaActividad(datatypes.DataActividad actividad) throws Exception {
        port.altaActividad(actividad);
    }
    
    public datatypes.DataActividad verInfoActividad(String nombre) throws Exception {
        return port.verInfoActividad(nombre);
    }
    
    public datatypes.DataActividad[] getActividades() throws Exception {
        return port.getActividades();
    }
    
    public datatypes.DataActividadRanking[] rankingActividades() {
        return port.rankingActividades();
    }
    
    public datatypes.DataActividad[] getActividadesPorTurista(String nicknameTurista) throws Exception {
        return port.getActividadesPorTurista(nicknameTurista);
    }
    
    public void modificarActividad(datatypes.DataActividad actividad, int opc, String dato) throws Exception {
        port.modificarActividad(actividad, opc, dato);
    }
}
