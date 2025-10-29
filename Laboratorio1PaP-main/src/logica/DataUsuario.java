package logica;

import java.util.Date;

public class DataUsuario {
    private String nickname;
    private String nombre;
    private String apellido;
    private String email;
    private Date fechaNac; 
    private String contra;

    public DataUsuario(String contra, String nickname, String nombre, String apellido, String email, Date fechaNac) {
        this.nickname = nickname;
        this.email = email;
        this.fechaNac = fechaNac;
        this.nombre = nombre;
        this.apellido = apellido;
        this.contra = contra;
    }
    
    public DataUsuario(Usuario usu) {
        this.nickname = usu.getNickname();
        this.email = usu.getEmail();
        this.fechaNac = usu.getFechaNac();
        this.nombre = usu.getNombre();
        this.apellido = usu.getApellido();
        this.contra = usu.getContra();
    }

    public String getNickname() { return nickname; }
    public void setNickname(String nickname) { this.nickname = nickname; }

    public String getNombre(){ return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getApellido(){ return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public Date getFechaNac() { return fechaNac; }
    public void setFechaNac(Date fechaNac) { this.fechaNac = fechaNac; }

    public String getContra(){ return contra; }
    public void setContra(String contra){ this.contra = contra; }

    @Override
    public String toString() {
        return "DataUsuario{" +
                "nombre='" + getNombre() + '\'' +
                ", apellido='" + getApellido() + '\'' +
                ", nickname='" + getNickname() + '\'' +
                ", email='" + getEmail() + '\'' +
                ", fechaNac='" + getFechaNac() + '\'' +
                '}';
    }
}

