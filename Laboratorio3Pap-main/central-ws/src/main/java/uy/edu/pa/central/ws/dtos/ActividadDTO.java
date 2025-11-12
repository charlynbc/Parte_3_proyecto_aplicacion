package uy.edu.pa.central.ws.dtos;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "Actividad")
@XmlAccessorType(XmlAccessType.FIELD)
public class ActividadDTO implements Serializable {
    private String id; // nombre de actividad
    private String descripcion;
    private String lugar;
    private int duracion; // horas
    private float costo;
    private String imagen;
    private List<SalidaDTO> salidas = new ArrayList<>();

    public ActividadDTO() {}

    public ActividadDTO(String id, String descripcion, String lugar, float costo, String imagen) {
        this.id = id;
        this.descripcion = descripcion;
        this.lugar = lugar;
        this.costo = costo;
        this.imagen = imagen;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getLugar() { return lugar; }
    public void setLugar(String lugar) { this.lugar = lugar; }
    
    public int getDuracion() { return duracion; }
    public void setDuracion(int duracion) { this.duracion = duracion; }

    public float getCosto() { return costo; }
    public void setCosto(float costo) { this.costo = costo; }

    public String getImagen() { return imagen; }
    public void setImagen(String imagen) { this.imagen = imagen; }

    public List<SalidaDTO> getSalidas() { return salidas; }
    public void setSalidas(List<SalidaDTO> salidas) { this.salidas = salidas; }
}
