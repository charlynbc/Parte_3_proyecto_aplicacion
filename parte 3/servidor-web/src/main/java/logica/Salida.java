package logica;

import jakarta.persistence.*;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

@Entity
@Table(name = "salida")
public class Salida {
    @Id
    private String nombre;

    @Column(name = "fecha")
    @Temporal(TemporalType.DATE)
    private Date fecha;

    private LocalTime hora;

    @Column(name = "turistas_max")
    private int turistasMax;

    private String lugar;

    @Column(name = "fecha_alta")
    @Temporal(TemporalType.DATE)
    private Date fechaAlta;

    private String imagen;

    @ManyToOne
    private Actividad actividad;

    @OneToMany(mappedBy = "salida", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Inscripcion> inscripciones = new ArrayList<>();

    public Salida() {}

    public Salida(String nombre, Date fecha, LocalTime hora, int turistasMax, String lugar, Date fechaAlta, String imagen, Actividad actividad) {
        this.nombre = nombre;
        this.fecha = fecha;
        this.hora = hora;
        this.turistasMax = turistasMax;
        this.lugar = lugar;
        this.fechaAlta = fechaAlta;
        this.imagen = imagen;
        this.actividad = actividad;
    }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public Date getFecha() { return fecha; }
    public void setFecha(Date fecha) { this.fecha = fecha; }

    public LocalTime getHora() { return hora; }
    public void setHora(LocalTime hora) { this.hora = hora; }

    public int getTuristasMax() { return turistasMax; }
    public void setTuristasMax(int turistasMax) { this.turistasMax = turistasMax; }

    public String getLugar() { return lugar; }
    public void setLugar(String lugar) { this.lugar = lugar; }

    public Date getFechaAlta() { return fechaAlta; }
    public void setFechaAlta(Date fechaAlta) { this.fechaAlta = fechaAlta; }

    public String getImagen() { return imagen; }
    public void setImagen(String imagen) { this.imagen = imagen; }

    public Actividad getActividad() { return actividad; }
    public void setActividad(Actividad actividad) { this.actividad = actividad; }

    public List<Inscripcion> getInscripciones() { return inscripciones; }
    public void setInscripciones(List<Inscripcion> inscripciones) { this.inscripciones = inscripciones; }
}
