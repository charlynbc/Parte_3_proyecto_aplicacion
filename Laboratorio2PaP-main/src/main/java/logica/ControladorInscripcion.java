package logica;

import excepciones.ActividadRepetidaException;
import excepciones.UsuarioNoExisteException;

import java.text.SimpleDateFormat;

import javax.swing.JOptionPane;

import jakarta.persistence.*;


public class ControladorInscripcion implements IControladorInscripcion {
	
	public ControladorInscripcion() {}
	
	public boolean inscribirTurista(String actividad, String salida, String turista, int cantidad, String fecha, float costo){
        
        ManejadorInscripcion mi = ManejadorInscripcion.getinstance();
        ManejadorUsuario mu = ManejadorUsuario.getinstance();
    	ManejadorSalida ms = ManejadorSalida.getinstance();
    	Salida sal = ms.obtenerSalida(salida.split(" - ")[0]);
    	Turista tur = mu.obtenerTurista(turista);
    	
    	if(verificarCuposDisponibles(sal, cantidad) < 0) {
    		JOptionPane.showMessageDialog(null, "Cupos insuficientes, cupos extra: " + (verificarCuposDisponibles(sal, cantidad) * -1), "Error", JOptionPane.ERROR_MESSAGE);
    		return false;
    	}

    	if(!mi.inscripcionExistente(tur.getNickname(), sal.getNombre())) {
        	
	        boolean exito = realizarInscripcion(actividad, salida, turista, cantidad, fecha, costo);
	        if(exito) {
	        	JOptionPane.showMessageDialog(null, "Inscripción realizada con éxito" 
	        + "\nTurista: " + tur.getNickname()
	        + "\nSalida: " + sal.getNombre()
	        + "\nCantidad: " + cantidad
	        + "\nFecha: " + fecha
	        + "\nCosto total: " + costo
	        , "Exito", JOptionPane.INFORMATION_MESSAGE);
	        }
	        return exito;
	        
        }else {
        	JOptionPane.showMessageDialog(null,
                    "Inscripcion ya existente",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        	return false;
        }
    }

	//Esta funcion retorna la cantidad de cupos disponibles, 
	//si el resultado es negativo, 
	//es la cantidad de cupos EXTRA que no se pueden inscribir
	
	private int verificarCuposDisponibles(Salida salida, int cantidadSolicitada) {
	    
		ManejadorInscripcion mi = ManejadorInscripcion.getinstance();
		
		return salida.getTuristasMax() - (mi.obtenerCantidadInscriptos(salida.getNombre()) + cantidadSolicitada);
		
	}


	private boolean realizarInscripcion(String actividad, String salida, String turista, int cantidad, String fecha, float costo) {
	    
		try {
	        ManejadorInscripcion mi = ManejadorInscripcion.getinstance();
	        ManejadorUsuario mu = ManejadorUsuario.getinstance();
	        ManejadorSalida ms = ManejadorSalida.getinstance();
	        Salida sal = ms.obtenerSalida(salida.split(" - ")[0]);
	        Turista tur = mu.obtenerTurista(turista);
	
	        if (tur == null) {
	            JOptionPane.showMessageDialog(null, "Turista no encontrado", "Error", JOptionPane.ERROR_MESSAGE);
	            return false;
	        }
	
	        Inscripcion inscripcion = new Inscripcion(
	            new SimpleDateFormat("dd/MM/yyyy").parse(fecha),
	            cantidad,
	            costo,
	            tur,
	            sal
	        );
	
	        mi.addInscripcion(inscripcion);
	
	        return true;
	
	    } catch (Exception e) {
	        e.printStackTrace();
	        return false;
	    }
	}

}
