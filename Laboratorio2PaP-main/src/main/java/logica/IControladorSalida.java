package logica;
import excepciones.SalidaNoExisteException;
import excepciones.SalidaRepetidaException;
import excepciones.ActividadNoExisteException;
public interface IControladorSalida {
    public abstract void AltaSalida(DataSalida salida) throws SalidaRepetidaException;
    public abstract DataSalida verSalida(String nombre) throws SalidaNoExisteException;
    public abstract DataInscripcion[] listarInscripcionesDeSalida(String nombreSalida) throws SalidaNoExisteException;
    public abstract DataSalida[] listarSalidasDeActividad(String nombreActividad) throws ActividadNoExisteException;
}
