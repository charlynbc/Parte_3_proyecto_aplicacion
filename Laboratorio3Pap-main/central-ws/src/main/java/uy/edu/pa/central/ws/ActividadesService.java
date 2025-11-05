package uy.edu.pa.central.ws;

import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebResult;
import jakarta.jws.WebService;
import uy.edu.pa.central.ws.dtos.ActividadDTO;
import uy.edu.pa.central.ws.dtos.SalidaDTO;
import uy.edu.pa.central.ws.dtos.InscripcionDTO;
import java.util.List;

@WebService(name = "ActividadesService", targetNamespace = "http://ws.central.pa.edu.uy/")
public interface ActividadesService {

    @WebMethod(operationName = "ListarActividades")
    @WebResult(name = "actividades")
    List<ActividadDTO> listarActividades();

    @WebMethod(operationName = "ObtenerActividad")
    @WebResult(name = "actividad")
    ActividadDTO obtenerActividad(@WebParam(name = "id") String id);

    @WebMethod(operationName = "ObtenerSalida")
    @WebResult(name = "salida")
    SalidaDTO obtenerSalida(@WebParam(name = "id") String id);

    @WebMethod(operationName = "InscribirTurista")
    @WebResult(name = "exito")
    boolean inscribirTurista(
        @WebParam(name = "actividad") String actividad,
        @WebParam(name = "salida") String salida,
        @WebParam(name = "turista") String turista,
        @WebParam(name = "cantidad") int cantidad,
        @WebParam(name = "fecha") String fecha,
        @WebParam(name = "costo") float costo
    );

    @WebMethod(operationName = "ListarInscripcionesPorTurista")
    @WebResult(name = "inscripciones")
    List<InscripcionDTO> listarInscripcionesPorTurista(
        @WebParam(name = "turista") String turista
    );
}
