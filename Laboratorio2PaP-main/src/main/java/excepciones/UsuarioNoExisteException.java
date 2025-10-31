package excepciones;

/**
 * Excepcion utilizada para indicar la inexistencia de un usuario en el sistema.
 
 */
@SuppressWarnings("serial")
public class UsuarioNoExisteException extends Exception {

    public UsuarioNoExisteException(String String) {
        super(String);  // Le pasamos al constructor de la clase exception el mensaje especifico
    }
}
