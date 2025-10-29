package logica;

import excepciones.ActividadNoExisteException;
import excepciones.ActividadRepetidaException;
import excepciones.UsuarioNoExisteException;


public interface IControladorActividad{

    public abstract DataActividad[] getActividadesPorProveedor(String nicknameProveedor) throws ActividadNoExisteException;
    public abstract void altaActividad(DataActividad actividad) throws ActividadRepetidaException, UsuarioNoExisteException;

    public abstract DataActividad verInfoActividad(String nombre) throws ActividadNoExisteException;
    
    public abstract DataActividad[] getActividades() throws ActividadNoExisteException;
    public abstract DataActividadRanking[] rankingActividades();

    public abstract DataActividad[] getActividadesPorTurista(String nicknameTurista) throws ActividadNoExisteException;

    public abstract void modificarActividad(DataActividad actividad, int opc, String dato) throws ActividadNoExisteException;
}