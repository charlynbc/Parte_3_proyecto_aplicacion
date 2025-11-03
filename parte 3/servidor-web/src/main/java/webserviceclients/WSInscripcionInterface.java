package webserviceclients;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

@WebService
@SOAPBinding(style = SOAPBinding.Style.RPC)
public interface WSInscripcionInterface {
    
    @WebMethod
    boolean inscribirTurista(String actividad, String salida, String turista, int cantidad, String fecha, float costo);
}
