package logica;

import java.util.Date;
import jakarta.persistence.*;

@Entity
@DiscriminatorValue("Turista")
public class Turista extends Usuario {
    private String nacionalidad;

    public Turista() { super(); }

    public Turista(String contra, String nickname, String nombre, String apellido, String email, Date fechaNac, String nacionalidad) {
        super(contra, nickname, nombre, apellido, email, fechaNac);
        this.nacionalidad = nacionalidad;
    }

    public String getNacionalidad() { return nacionalidad; }
    public void setNacionalidad(String nacionalidad) { this.nacionalidad = nacionalidad; }
}
