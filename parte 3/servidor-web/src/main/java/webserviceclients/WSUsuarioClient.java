package webserviceclients;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import java.net.URL;

/**
 * Cliente para el Web Service de Usuario
 */
public class WSUsuarioClient {
    
    private static final String WSDL_URL = "http://localhost:9128/webservices/usuario?wsdl";
    private static final String NAMESPACE = "http://publicadores/";
    private static final String SERVICE_NAME = "WSUsuarioService";
    
    private WSUsuarioInterface port;
    
    public WSUsuarioClient() {
        try {
            URL url = new URL(WSDL_URL);
            QName qname = new QName(NAMESPACE, SERVICE_NAME);
            Service service = Service.create(url, qname);
            port = service.getPort(WSUsuarioInterface.class);
        } catch (Exception e) {
            throw new RuntimeException("Error al conectar con el Web Service de Usuario: " + e.getMessage(), e);
        }
    }
    
    public datatypes.DataUsuario[] getUsuarios() throws Exception {
        return port.getUsuarios();
    }
    
    public datatypes.DataUsuario verInfoUsuario(String nickname) throws Exception {
        return port.verInfoUsuario(nickname);
    }
    
    public void registrarUsuario(datatypes.DataUsuario usuario) throws Exception {
        port.registrarUsuario(usuario);
    }
    
    public void modificarUsuario(datatypes.DataUsuario u, int opc, String dato) throws Exception {
        port.modificarUsuario(u, opc, dato);
    }
    
    public datatypes.DataUsuario[] getProveedores() throws Exception {
        return port.getProveedores();
    }
    
    public datatypes.DataUsuario obtenerUsuario(String nickname) throws Exception {
        return port.obtenerUsuario(nickname);
    }
    
    public datatypes.DataUsuario obtenerUsuarioPorEmail(String email) throws Exception {
        return port.obtenerUsuarioPorEmail(email);
    }
    
    public String obtenerTipoUsuario(String nickname) throws Exception {
        return port.obtenerTipoUsuario(nickname);
    }
}
