package excepciones;

@SuppressWarnings("serial")
public class SalidaNoExisteException extends Exception

{

    public SalidaNoExisteException (String string) {
        super(string);  // Le pasamos al constructor de la clase exception el mensaje especifico
    }
	

}
