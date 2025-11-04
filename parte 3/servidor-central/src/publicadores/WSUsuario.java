package publicadores;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

import logica.DataUsuario;
import logica.Fabrica;
import logica.IControladorUsuario;

@WebService
@SOAPBinding(style = SOAPBinding.Style.RPC)
public class WSUsuario {
    
    private IControladorUsuario controlador;
    
    public WSUsuario() {
        this.controlador = Fabrica.getInstance().getIControladorUsuario();
    }
    
    @WebMethod
    public void registrarUsuario(DataUsuario usuario) throws Exception {
        try {
            controlador.registrarUsuario(usuario);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }
    
    @WebMethod
    public DataUsuario verInfoUsuario(String nickname) throws Exception {
        try {
            return controlador.verInfoUsuario(nickname);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }
    
    @WebMethod
    public DataUsuario[] getUsuarios() throws Exception {
        try {
            return controlador.getUsuarios();
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }
    
    @WebMethod
    public void modificarUsuario(DataUsuario u, int opc, String dato) throws Exception {
        try {
            controlador.modificarUsuario(u, opc, dato);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }
    
    @WebMethod
    public DataUsuario[] getProveedores() throws Exception {
        try {
            return controlador.getProveedores();
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }
    
    @WebMethod
    public DataUsuario obtenerUsuario(String nickname) throws Exception {
        try {
            return controlador.verInfoUsuario(nickname);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }
    
    @WebMethod
    public DataUsuario obtenerUsuarioPorEmail(String email) throws Exception {
        try {
            // Buscar usuario por email - necesitamos implementar este método en el controlador
            DataUsuario[] usuarios = controlador.getUsuarios();
            for (DataUsuario usuario : usuarios) {
                if (usuario.getEmail() != null && usuario.getEmail().toLowerCase().equals(email.toLowerCase())) {
                    return usuario;
                }
            }
            return null;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }
    
    @WebMethod
    public String obtenerTipoUsuario(String nickname) throws Exception {
        try {
            DataUsuario usuario = controlador.verInfoUsuario(nickname);
            if (usuario == null) {
                return null;
            }
            
            // Verificar si es turista o proveedor basado en campos específicos
            DataUsuario[] proveedores = controlador.getProveedores();
            for (DataUsuario proveedor : proveedores) {
                if (proveedor.getNickname().equals(nickname)) {
                    return "proveedor";
                }
            }
            return "turista";
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }
}
