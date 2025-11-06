package excepciones;

@SuppressWarnings("serial")
/*La advertencia "serial" se genera por el compilador cuando una clase implementa Serializable 
 * pero no tiene el campo serialVersionUID. La anotación @SuppressWarnings("serial") se coloca en la clase 
 * para indicar al compilador que ignore esta advertencia específica.*/

public class ActividadRepetidaException extends Exception {

	  public ActividadRepetidaException(String string) {
	        super(string);  // Le pasamos al constructor de la clase que esta heredando ese valor a clase padre
	                        // Esto agrega informacion adicional y especifica al manejador de excepciones
	                        // De otra manera tendria que mandar el programador los mensajes a consola
	    }

}
