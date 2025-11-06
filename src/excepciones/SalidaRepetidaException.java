package excepciones;

@SuppressWarnings("serial")
public class SalidaRepetidaException extends Exception{
	
	  public SalidaRepetidaException(String string)  {
	        super(string);  // Le pasamos al constructor de la clase que esta heredando ese valor a clase padre
	                        // Esto agrega informacion adicional y especifica al manejador de excepciones
	                        // De otra manera tendria que mandar el programador los mensajes a consola
	    }

}
