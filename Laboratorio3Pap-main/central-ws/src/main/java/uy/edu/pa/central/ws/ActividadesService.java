package uy.edu.pa.central.ws;

import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebResult;
import jakarta.jws.WebService;
import uy.edu.pa.central.ws.dtos.ActividadDTO;
import java.util.List;

@WebService(name = "ActividadesService", targetNamespace = "http://ws.central.pa.edu.uy/")
public interface ActividadesService {

    @WebMethod(operationName = "ListarActividades")
    @WebResult(name = "actividades")
    List<ActividadDTO> listarActividades();

    @WebMethod(operationName = "ObtenerActividad")
    @WebResult(name = "actividad")
    ActividadDTO obtenerActividad(@WebParam(name = "id") String id);

    @WebMethod(operationName = "CrearActividad")
    @WebResult(name = "exito")
    boolean crearActividad(
        @WebParam(name = "nombre") String nombre,
        @WebParam(name = "descripcion") String descripcion,
        @WebParam(name = "duracion") int duracion,
        @WebParam(name = "costo") float costo,
        @WebParam(name = "ciudad") String ciudad,
        @WebParam(name = "proveedor") String proveedor,
        @WebParam(name = "fechaAlta") String fechaAlta,
        @WebParam(name = "imagenBase64") String imagenBase64
    );

    @WebMethod(operationName = "ListarActividadesPorProveedor")
    @WebResult(name = "actividades")
    List<ActividadDTO> listarActividadesPorProveedor(
        @WebParam(name = "proveedor") String proveedor
    );

    @WebMethod(operationName = "RankingActividades")
    @WebResult(name = "ranking")
    List<ActividadDTO> rankingActividades();

    @WebMethod(operationName = "ListarActividadesPorTurista")
    @WebResult(name = "actividades")
    List<ActividadDTO> listarActividadesPorTurista(
        @WebParam(name = "turista") String turista
    );
}
