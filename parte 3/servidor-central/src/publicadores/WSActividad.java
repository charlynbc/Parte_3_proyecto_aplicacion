package publicadores;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

import logica.DataActividad;
import logica.DataActividadRanking;
import logica.Fabrica;
import logica.IControladorActividad;

@WebService
@SOAPBinding(style = SOAPBinding.Style.RPC)
public class WSActividad {
    
    private IControladorActividad controlador;
    
    public WSActividad() {
        this.controlador = Fabrica.getInstance().getIControladorActividad();
    }
    
    @WebMethod
    public DataActividad[] getActividadesPorProveedor(String nicknameProveedor) throws Exception {
        try {
            return controlador.getActividadesPorProveedor(nicknameProveedor);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }
    
    @WebMethod
    public void altaActividad(DataActividad actividad) throws Exception {
        try {
            controlador.altaActividad(actividad);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }
    
    @WebMethod
    public DataActividad verInfoActividad(String nombre) throws Exception {
        try {
            return controlador.verInfoActividad(nombre);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }
    
    @WebMethod
    public DataActividad[] getActividades() throws Exception {
        try {
            return controlador.getActividades();
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }
    
    @WebMethod
    public DataActividadRanking[] rankingActividades() {
        return controlador.rankingActividades();
    }
    
    @WebMethod
    public DataActividad[] getActividadesPorTurista(String nicknameTurista) throws Exception {
        try {
            return controlador.getActividadesPorTurista(nicknameTurista);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }
    
    @WebMethod
    public void modificarActividad(DataActividad actividad, int opc, String dato) throws Exception {
        try {
            controlador.modificarActividad(actividad, opc, dato);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }
}
