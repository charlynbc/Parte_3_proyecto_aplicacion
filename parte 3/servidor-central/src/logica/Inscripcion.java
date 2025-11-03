package logica;

import java.util.Date;
import jakarta.persistence.*;

@Entity
@Table(name = "inscripcion")
public class Inscripcion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id", unique=true)
    private int id;

    @Column(name="fecha_inscripcion", nullable=false)
    @Temporal(TemporalType.DATE)
    private Date fechaInscripcion;

    @Column(name="cant_turistas", nullable=false)
    private int cantTuristas;

    @Column(name="costo", nullable=false)
    private float costo;

    @ManyToOne
    @JoinColumn(name = "turista", nullable = false, referencedColumnName = "nickname")
    private Turista turista;

    @ManyToOne
    @JoinColumn(name = "salida", nullable = false, referencedColumnName = "nombre")
    private Salida salida;

    // --- Constructores ---
    public Inscripcion() {}

    public Inscripcion(Date fechaInscripcion, int cantTuristas, float costo, Turista turista, Salida salida) {
        this.fechaInscripcion = fechaInscripcion;
        this.cantTuristas = cantTuristas;
        this.costo = costo;
        this.turista = turista;
        this.salida = salida;
    }

    // --- Getters & Setters ---
    public int getId() { return id; }

    public Date getFechaInscripcion() { return fechaInscripcion; }
    public void setFechaInscripcion(Date fechaInscripcion) { this.fechaInscripcion = fechaInscripcion; }

    public int getCantTuristas() { return cantTuristas; }
    public void setCantTuristas(int cantTuristas) { this.cantTuristas = cantTuristas; }

    public float getCosto() { return costo; }
    public void setCosto(float costo) { this.costo = costo; }

    public Turista getTurista() { return turista; }
    public void setTurista(Turista turista) { this.turista = turista; }

    public Salida getSalida() { return salida; }
    public void setSalida(Salida salida) { this.salida = salida; }
}

