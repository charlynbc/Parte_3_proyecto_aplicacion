package webserviceclients;

/**
 * Fabrica de clientes de Web Services
 * Reemplaza la antigua Fabrica que devolvía controladores locales
 */
public class WSClientFactory {
    
    private static WSClientFactory instancia;
    
    private WSClientFactory() {
        // Constructor privado para patrón Singleton
    }
    
    public static WSClientFactory getInstance() {
        if (instancia == null) {
            instancia = new WSClientFactory();
        }
        return instancia;
    }
    
    public WSUsuarioClient getWSUsuarioClient() {
        return new WSUsuarioClient();
    }
    
    public WSActividadClient getWSActividadClient() {
        return new WSActividadClient();
    }
    
    public WSSalidaClient getWSSalidaClient() {
        return new WSSalidaClient();
    }
    
    public WSInscripcionClient getWSInscripcionClient() {
        return new WSInscripcionClient();
    }
}
