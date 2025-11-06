package excepciones;

@SuppressWarnings("serial")
public class ActividadNoExisteException extends Exception

{

    public ActividadNoExisteException (String string) {
        super(string);  // Le pasamos al constructor de la clase exception el mensaje especifico
    }
	

}
