package webserviceclients;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

@WebService
@SOAPBinding(style = SOAPBinding.Style.RPC)
public interface WSActividadInterface {
    
    @WebMethod
    datatypes.DataActividad[] getActividadesPorProveedor(String nicknameProveedor) throws Exception;
    
    @WebMethod
    void altaActividad(datatypes.DataActividad actividad) throws Exception;
    
    @WebMethod
    datatypes.DataActividad verInfoActividad(String nombre) throws Exception;
    
    @WebMethod
    datatypes.DataActividad[] getActividades() throws Exception;
    
    @WebMethod
    datatypes.DataActividadRanking[] rankingActividades();
    
    @WebMethod
    datatypes.DataActividad[] getActividadesPorTurista(String nicknameTurista) throws Exception;
    
    @WebMethod
    void modificarActividad(datatypes.DataActividad actividad, int opc, String dato) throws Exception;
}
