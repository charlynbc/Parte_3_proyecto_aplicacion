package logica;

public class Fabrica {

	private static Fabrica instancia;

    private Fabrica() {  /* Constructor privado para evitar que otras clases puedan instanciarlo */
    };

    /*La primera vez que se llama a getInstance(), la instancia se crea y almacenada en la variable instance. 
     En llamadas posteriores, simplemente se devuelve la instancia ya creada.*/
    
    public static Fabrica getInstance() {
        if (instancia == null) {
            instancia = new Fabrica();   // SOlo de aca se puede llamar al contructor
        }
        return instancia;
    }
    /*Metodo publico para devolver el controlador*/
    public IControladorUsuario getIControladorUsuario() {
        return new ControladorUsuario();
    }

    public IControladorActividad getIControladorActividad() {
        return new ControladorActividad();
    }

    public IControladorSalida getIControladorSalida(){
        return new ControladorSalida();
    }
    
    public IControladorInscripcion getIControladorInscripcion(){
		return new ControladorInscripcion();
	}
}
