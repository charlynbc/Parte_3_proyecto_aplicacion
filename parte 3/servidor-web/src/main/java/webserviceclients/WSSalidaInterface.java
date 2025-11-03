package webserviceclients;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

@WebService
@SOAPBinding(style = SOAPBinding.Style.RPC)
public interface WSSalidaInterface {
    
    @WebMethod
    void altaSalida(datatypes.DataSalida salida) throws Exception;
    
    @WebMethod
    datatypes.DataSalida verSalida(String nombre) throws Exception;
    
    @WebMethod
    datatypes.DataInscripcion[] listarInscripcionesDeSalida(String nombreSalida) throws Exception;
    
    @WebMethod
    datatypes.DataSalida[] listarSalidasDeActividad(String nombreActividad) throws Exception;
}
