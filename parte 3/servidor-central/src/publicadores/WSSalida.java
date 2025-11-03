package publicadores;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

import logica.DataSalida;
import logica.DataInscripcion;
import logica.Fabrica;
import logica.IControladorSalida;

@WebService
@SOAPBinding(style = SOAPBinding.Style.RPC)
public class WSSalida {
    
    private IControladorSalida controlador;
    
    public WSSalida() {
        this.controlador = Fabrica.getInstance().getIControladorSalida();
    }
    
    @WebMethod
    public void altaSalida(DataSalida salida) throws Exception {
        try {
            controlador.AltaSalida(salida);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }
    
    @WebMethod
    public DataSalida verSalida(String nombre) throws Exception {
        try {
            return controlador.verSalida(nombre);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }
    
    @WebMethod
    public DataInscripcion[] listarInscripcionesDeSalida(String nombreSalida) throws Exception {
        try {
            return controlador.listarInscripcionesDeSalida(nombreSalida);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }
    
    @WebMethod
    public DataSalida[] listarSalidasDeActividad(String nombreActividad) throws Exception {
        try {
            return controlador.listarSalidasDeActividad(nombreActividad);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }
}
