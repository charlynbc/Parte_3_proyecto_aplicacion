package webserviceclients;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import java.net.URL;

/**
 * Cliente para el Web Service de Salida
 */
public class WSSalidaClient {
    
    private static final String WSDL_URL = "http://localhost:9128/webservices/salida?wsdl";
    private static final String NAMESPACE = "http://publicadores/";
    private static final String SERVICE_NAME = "WSSalidaService";
    
    private WSSalidaInterface port;
    
    public WSSalidaClient() {
        try {
            URL url = new URL(WSDL_URL);
            QName qname = new QName(NAMESPACE, SERVICE_NAME);
            Service service = Service.create(url, qname);
            port = service.getPort(WSSalidaInterface.class);
        } catch (Exception e) {
            throw new RuntimeException("Error al conectar con el Web Service de Salida: " + e.getMessage(), e);
        }
    }
    
    public void altaSalida(datatypes.DataSalida salida) throws Exception {
        port.altaSalida(salida);
    }
    
    public datatypes.DataSalida verSalida(String nombre) throws Exception {
        return port.verSalida(nombre);
    }
    
    public datatypes.DataInscripcion[] listarInscripcionesDeSalida(String nombreSalida) throws Exception {
        return port.listarInscripcionesDeSalida(nombreSalida);
    }
    
    public datatypes.DataSalida[] listarSalidasDeActividad(String nombreActividad) throws Exception {
        return port.listarSalidasDeActividad(nombreActividad);
    }
}
