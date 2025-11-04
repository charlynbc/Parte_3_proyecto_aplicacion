package logica;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import jakarta.persistence.*;

@Entity
@Table(name = "proveedores")
@DiscriminatorValue("Proveedor")
public class Proveedor extends Usuario {

    @Column(name = "sitioWeb")
    private String sitioWeb;

    @Column(name = "descripcion")
    private String descripcion;

    @OneToMany(mappedBy = "proveedor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Actividad> actividades = new ArrayList<>();

    public Proveedor() {}

    public Proveedor(String contra, String nickname, String nombre, String apellido,
                     String email, Date fechaNac, String sitioWeb, String descripcion) {
        super(contra, nickname, nombre, apellido, email, fechaNac);
        this.sitioWeb = sitioWeb;
        this.descripcion = descripcion;
    }

    // Getters
    public String getDescripcion() { return descripcion; }
    public String getSitioWeb() { return sitioWeb; }
    public List<Actividad> getActividades() { return this.actividades; }

    // Setters
    public void setSitioWeb(String sitioWeb) { this.sitioWeb = sitioWeb; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public void setActividades(List<Actividad> actividades) { this.actividades = actividades; }

    // 🔑 Método para mantener relación bidireccional
    public void agregarActividad(Actividad act) {
        if (!this.actividades.contains(act)) {
            this.actividades.add(act);
            act.setProveedor(this);
        }
    }
}

