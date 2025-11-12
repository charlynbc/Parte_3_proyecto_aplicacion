package uy.edu.pa.central.ws.dtos;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

@XmlRootElement(name = "Salida")
@XmlAccessorType(XmlAccessType.FIELD)
public class SalidaDTO implements Serializable {
    private String id; // nombre de salida
    private String fecha; // ISO yyyy-MM-dd
    private String hora;  // HH:mm
    private String lugar;
    private int turistasMax;
    private String imagen;
    private String actividadNombre;

    public SalidaDTO() {}

    public SalidaDTO(String id, String fecha, String hora, String lugar) {
        this.id = id;
        this.fecha = fecha;
        this.hora = hora;
        this.lugar = lugar;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getFecha() { return fecha; }
    public void setFecha(String fecha) { this.fecha = fecha; }

    public String getHora() { return hora; }
    public void setHora(String hora) { this.hora = hora; }

    public String getLugar() { return lugar; }
    public void setLugar(String lugar) { this.lugar = lugar; }

    public int getTuristasMax() { return turistasMax; }
    public void setTuristasMax(int turistasMax) { this.turistasMax = turistasMax; }

    public String getImagen() { return imagen; }
    public void setImagen(String imagen) { this.imagen = imagen; }

    public String getActividadNombre() { return actividadNombre; }
    public void setActividadNombre(String actividadNombre) { this.actividadNombre = actividadNombre; }
}
