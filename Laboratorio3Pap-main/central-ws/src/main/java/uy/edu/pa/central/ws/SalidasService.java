package uy.edu.pa.central.ws;

import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebResult;
import jakarta.jws.WebService;
import uy.edu.pa.central.ws.dtos.SalidaDTO;
import uy.edu.pa.central.ws.dtos.InscripcionDTO;
import java.util.List;

@WebService(name = "SalidasService", targetNamespace = "http://ws.central.pa.edu.uy/")
public interface SalidasService {

    @WebMethod(operationName = "CrearSalida")
    @WebResult(name = "exito")
    boolean crearSalida(
        @WebParam(name = "nombre") String nombre,
        @WebParam(name = "fecha") String fecha,
        @WebParam(name = "hora") String hora,
        @WebParam(name = "lugar") String lugar,
        @WebParam(name = "cantMax") int cantMax,
        @WebParam(name = "fechaAlta") String fechaAlta,
        @WebParam(name = "actividad") String actividad
    );

    @WebMethod(operationName = "ObtenerSalida")
    @WebResult(name = "salida")
    SalidaDTO obtenerSalida(@WebParam(name = "id") String id);

    @WebMethod(operationName = "ListarSalidasDeActividad")
    @WebResult(name = "salidas")
    List<SalidaDTO> listarSalidasDeActividad(
        @WebParam(name = "actividad") String actividad
    );

    @WebMethod(operationName = "ModificarSalida")
    @WebResult(name = "exito")
    boolean modificarSalida(
        @WebParam(name = "nombre") String nombre,
        @WebParam(name = "fecha") String fecha,
        @WebParam(name = "hora") String hora,
        @WebParam(name = "lugar") String lugar,
        @WebParam(name = "cantMax") int cantMax,
        @WebParam(name = "imagenBase64") String imagenBase64
    );

    @WebMethod(operationName = "ListarInscripcionesDeSalida")
    @WebResult(name = "inscripciones")
    List<InscripcionDTO> listarInscripcionesDeSalida(
        @WebParam(name = "salida") String salida
    );
}
