package logica;

import java.util.Date;

public class DataTurista extends DataUsuario {
    private String nacionalidad;

    public DataTurista(String contra, String nickname, String nombre, String apellido, String email, Date fechaNac, String nacionalidad){
        super(contra, nickname, nombre, apellido, email, fechaNac);
        this.nacionalidad = nacionalidad;
    }

    public String getNacionalidad() { return nacionalidad; }
    public void setNacionalidad(String nacionalidad) { this.nacionalidad = nacionalidad; }

    @Override
    public String toString() {
        return "DataTurista{" +
                "nombre='" + getNombre() + '\'' +
                ", apellido='" + getApellido() + '\'' +
                ", nickname='" + getNickname() + '\'' +
                ", email='" + getEmail() + '\'' +
                ", fechaNac='" + getFechaNac() + '\'' +
                ", nacionalidad='" + getNacionalidad() + '\'' +
                '}';
    }
}
