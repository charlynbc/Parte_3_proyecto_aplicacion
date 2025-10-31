package logica;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;

import excepciones.SalidaNoExisteException;
import excepciones.SalidaRepetidaException;
import excepciones.ActividadNoExisteException;

public class ControladorSalida implements IControladorSalida {
    public ControladorSalida() {}

    @Override
    public void AltaSalida(DataSalida dataSalida) throws SalidaRepetidaException {
        ManejadorSalida ms = ManejadorSalida.getinstance();
        ManejadorActividad ma = ManejadorActividad.getinstance();
        Actividad ac = ma.obtenerActividad(dataSalida.getActividad());
        
        if (ac == null) {
            throw new IllegalArgumentException("No existe la actividad: " + dataSalida.getActividad());
        }

        Salida existente = ms.obtenerSalida(dataSalida.getNombre());
        if (existente != null) {
            throw new SalidaRepetidaException("La salida " + dataSalida.getNombre() + " ya existe");
        }

        // Crear nueva salida
        Salida salida = new Salida(
            dataSalida.getNombre(),
            dataSalida.getFecha(),
            dataSalida.getHora(),
            dataSalida.getTuristasmax(),
            dataSalida.getLugar(),
            dataSalida.getFechaalta(),
            dataSalida.getImagen(),
            ac
        );

        // Guardar salida
        ms.addSalida(salida);

        // Asociar a la actividad
        ac.agregarSalida(salida);
    }

    @Override
    public DataSalida verSalida(String nombre) throws SalidaNoExisteException {
        ManejadorSalida ms = ManejadorSalida.getinstance();
        Salida salida = ms.obtenerSalida(nombre);

        if (salida == null) {
            throw new SalidaNoExisteException("No existe la salida: " + nombre);
        }

        return new DataSalida(
            salida.getNombre(),
            salida.getFecha(),
            salida.getHora(),
            salida.getTuristasMax(),
            salida.getLugar(),
            salida.getFechaAlta(),
            salida.getImagen(),
            salida.getActividad().getNombre()
        );
    }

    @Override
    public DataInscripcion[] listarInscripcionesDeSalida(String nombreSalida) throws SalidaNoExisteException {
        ManejadorSalida ms = ManejadorSalida.getinstance();
        Salida salida = ms.obtenerSalida(nombreSalida);

        if (salida == null) {
            throw new SalidaNoExisteException("No existe la salida: " + nombreSalida);
        }

        List<DataInscripcion> result = new ArrayList<>();
        for (Inscripcion ins : salida.getInscripciones()) {
            result.add(new DataInscripcion(
                ins.getFechaInscripcion(),
                ins.getCantTuristas(),
                ins.getCosto(),
                ins.getTurista().getNickname(),
                salida.getNombre()
            ));
        }

        return result.toArray(new DataInscripcion[0]);
    }

    @Override
    public DataSalida[] listarSalidasDeActividad(String nombreActividad) throws ActividadNoExisteException {
        
    	ManejadorSalida ms = ManejadorSalida.getinstance();
        
        Salida[] salidas = ms.getSalidasDeActividad(nombreActividad);
        
        List<DataSalida> dataSalidas = new ArrayList<>();
        for(Salida s : salidas) {
        	DataSalida newSalida = new DataSalida(
        			s.getNombre(),
        			s.getFecha(),
        			s.getHora(),
        			s.getTuristasMax(),
        			s.getLugar(),
        			s.getFechaAlta(),
        			s.getImagen(),
        			s.getImagen()
        			);
        	dataSalidas.add(newSalida);
        }
        
        return dataSalidas.toArray(new DataSalida[0]);
    }
}