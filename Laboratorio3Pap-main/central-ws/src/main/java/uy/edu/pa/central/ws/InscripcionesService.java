package uy.edu.pa.central.ws;

import jakarta.jws.WebService;
import jakarta.jws.WebMethod;
import java.util.List;
import uy.edu.pa.central.ws.dtos.InscripcionDTO;

@WebService
public interface InscripcionesService {
    
    @WebMethod
    boolean inscribirTurista(String turista, String salida, int cantidad, String fecha);
    
    @WebMethod
    List<InscripcionDTO> listarInscripcionesPorTurista(String turista);
    
}
