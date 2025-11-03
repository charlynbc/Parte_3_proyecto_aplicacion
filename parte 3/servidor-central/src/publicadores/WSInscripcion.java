package publicadores;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

import logica.Fabrica;
import logica.IControladorInscripcion;

@WebService
@SOAPBinding(style = SOAPBinding.Style.RPC)
public class WSInscripcion {
    
    private IControladorInscripcion controlador;
    
    public WSInscripcion() {
        this.controlador = Fabrica.getInstance().getIControladorInscripcion();
    }
    
    @WebMethod
    public boolean inscribirTurista(String actividad, String salida, String turista, int cantidad, String fecha, float costo) {
        return controlador.inscribirTurista(actividad, salida, turista, cantidad, fecha, costo);
    }
}
