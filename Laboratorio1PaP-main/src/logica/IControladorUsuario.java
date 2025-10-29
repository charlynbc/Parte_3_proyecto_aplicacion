package logica;

import excepciones.UsuarioNoExisteException;
import excepciones.UsuarioRepetidoException;

public interface IControladorUsuario {

    /**
     * Registra al usuario en el sistema.
     * Valida que el nickname y el email sean únicos.
     * @param usuario DataUsuario con los datos del nuevo usuario.
     * @throws UsuarioRepetidoException Si el nickname o email ya están en uso.
     */
    public abstract void registrarUsuario(DataUsuario usuario) throws UsuarioRepetidoException;

    /**
     * Retorna la información de un usuario dado su nickname.
     * @param nickname Nickname del usuario.
     * @return Información del usuario.
     * @throws UsuarioNoExisteException Si el usuario no existe en el sistema.
     */
    public abstract DataUsuario verInfoUsuario(String nickname) throws UsuarioNoExisteException;

    /**
     * Retorna la información de todos los usuarios registrados en el sistema.
     * @return Arreglo de DataUsuario.
     * @throws UsuarioNoExisteException Si no existen usuarios registrados.
     */
    public abstract DataUsuario[] getUsuarios() throws UsuarioNoExisteException;

    /**
     * Modifica un usuario identificado por su nickname.
     * @param u DataUsuario a modificar.
     * @param opc Opción de modificación.
     * @param dato Valor nuevo.
     * @throws UsuarioNoExisteException Si el usuario no existe.
     */
    public abstract void modificarUsuario(DataUsuario u, int opc, String dato) throws UsuarioNoExisteException;

    /**
     * Retorna todos los usuarios de tipo Proveedor.
     * @return Arreglo de DataUsuario (DataProveedor).
     * @throws UsuarioNoExisteException Si no existen proveedores registrados.
     */
    public abstract DataUsuario[] getProveedores() throws UsuarioNoExisteException;
}

