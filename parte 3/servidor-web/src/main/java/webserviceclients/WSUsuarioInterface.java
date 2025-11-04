package webserviceclients;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

@WebService
@SOAPBinding(style = SOAPBinding.Style.RPC)
public interface WSUsuarioInterface {
    
    @WebMethod
    void registrarUsuario(datatypes.DataUsuario usuario) throws Exception;
    
    @WebMethod
    datatypes.DataUsuario verInfoUsuario(String nickname) throws Exception;
    
    @WebMethod
    datatypes.DataUsuario[] getUsuarios() throws Exception;
    
    @WebMethod
    void modificarUsuario(datatypes.DataUsuario u, int opc, String dato) throws Exception;
    
    @WebMethod
    datatypes.DataUsuario[] getProveedores() throws Exception;
    
    @WebMethod
    datatypes.DataUsuario obtenerUsuario(String nickname) throws Exception;
    
    @WebMethod
    datatypes.DataUsuario obtenerUsuarioPorEmail(String email) throws Exception;
    
    @WebMethod
    String obtenerTipoUsuario(String nickname) throws Exception;
}
