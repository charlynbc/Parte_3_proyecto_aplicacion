package uy.edu.pa.central.ws;

import jakarta.jws.WebService;
import uy.edu.pa.central.ws.dtos.ActividadDTO;
import uy.edu.pa.central.ws.dtos.SalidaDTO;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@WebService(
    serviceName = "ActividadesService",
    endpointInterface = "uy.edu.pa.central.ws.ActividadesService",
    portName = "ActividadesServicePort",
    targetNamespace = "http://ws.central.pa.edu.uy/"
)
public class ActividadesServiceImpl implements ActividadesService {

    @Override
    public List<ActividadDTO> listarActividades() {
        // Placeholder: Mock hasta conectar a DB Central
        List<ActividadDTO> list = new ArrayList<>();
        ActividadDTO a1 = new ActividadDTO("Kayak Colonia", "Remada por la bahía de Colonia", "Colonia", 1500f, "https://images.unsplash.com/photo-1506744038136-46273834b3fb?auto=format&fit=crop&w=800&q=80");
        a1.getSalidas().add(new SalidaDTO("Salida-Kayak-1", LocalDate.now().plusDays(7).toString(), "10:00", "Puerto de Yates"));
        list.add(a1);
        return list;
    }

    @Override
    public ActividadDTO obtenerActividad(String id) {
        // Simulación simple
        if (id == null || id.isBlank()) return null;
        ActividadDTO a = new ActividadDTO(id, "Descripción de " + id, "Lugar", 1200f, null);
        a.getSalidas().add(new SalidaDTO("Salida-" + id + "-1", LocalDate.now().plusDays(3).toString(), "09:00", "Punto de encuentro"));
        return a;
    }

    @Override
    public SalidaDTO obtenerSalida(String id) {
        if (id == null || id.isBlank()) return null;
        return new SalidaDTO(id, LocalDate.now().plusDays(1).toString(), "08:30", "Lugar de salida");
    }
}
