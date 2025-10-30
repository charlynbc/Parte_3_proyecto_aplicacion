package logica;

import java.util.Date;
import jakarta.persistence.*;

@Entity
@Table(name = "usuarios")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo")
public class Usuario {
    @Id
    private String nickname;

    private String nombre;
    private String apellido;
    private String email;

    @Temporal(TemporalType.DATE)
    private Date fechaNac;

    private String contra;

    public Usuario() {}

    public Usuario(String contra, String nickname, String nombre, String apellido, String email, Date fechaNac) {
        this.contra = contra;
        this.nickname = nickname;
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.fechaNac = fechaNac;
    }

    // Getters
    public String getNickname() { return nickname; }
    public String getNombre() { return nombre; }
    public String getApellido() { return apellido; }
    public String getEmail() { return email; }
    public Date getFechaNac() { return fechaNac; }
    public String getContra() { return contra; }

    // Setters
    public void setNickname(String nickname) { this.nickname = nickname; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setApellido(String apellido) { this.apellido = apellido; }
    public void setEmail(String email) { this.email = email; }
    public void setFechaNac(Date fechaNac) { this.fechaNac = fechaNac; }
    public void setContra(String contra) { this.contra = contra; }
}
