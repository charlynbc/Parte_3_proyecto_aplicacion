package logica;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import jakarta.persistence.*;

@Entity
@Table(name = "actividad")
public class Actividad {
    @Id
    @Column(name = "nombre", nullable = false, unique = true)
    private String nombre;

    @Column(name = "descripcion", nullable = false)
    private String descripcion;

    @Column(name = "duracion", nullable = false)
    private int duracion;

    @Column(name = "costo", nullable = false)
    private float costo;

    @Column(name = "ciudad", nullable = false)
    private String ciudad;

    @Column(name = "fechaAlta", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date fechaAlta;

    @Column(name = "estado", nullable = false)
    private String estado;

    @ManyToOne
    @JoinColumn(name = "proveedor", nullable = false, referencedColumnName = "nickname")
    private Proveedor proveedor;

    // Relación 1 actividad -> * salidas
    @OneToMany(mappedBy = "actividad", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Salida> salidas = new ArrayList<>();

    public Actividad() {}

    public Actividad(String nombre, String descripcion, int duracion, float costo,
                     String ciudad, Date fechaAlta, String estado, Proveedor proveedor) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.duracion = duracion;
        this.costo = costo;
        this.ciudad = ciudad;
        this.fechaAlta = fechaAlta;
        this.estado = estado;
        this.proveedor = proveedor;
    }

    // --- Métodos para manejar relación con Salida ---
    public void agregarSalida(Salida salida) {
        salidas.add(salida);
        salida.setActividad(this);
    }

    public List<Salida> getSalidas() {
        return salidas;
    }

    public void setSalidas(List<Salida> salidas) {
        this.salidas = salidas;
    }

    // --- Getters y setters ---
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public int getDuracion() { return duracion; }
    public void setDuracion(int duracion) { this.duracion = duracion; }

    public float getCosto() { return costo; }
    public void setCosto(float costo) { this.costo = costo; }

    public String getCiudad() { return ciudad; }
    public void setCiudad(String ciudad) { this.ciudad = ciudad; }

    public Date getFechaAlta() { return fechaAlta; }
    public void setFechaAlta(Date fechaAlta) { this.fechaAlta = fechaAlta; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public Proveedor getProveedor() { return proveedor; }
    public void setProveedor(Proveedor proveedor) { this.proveedor = proveedor; }
}

