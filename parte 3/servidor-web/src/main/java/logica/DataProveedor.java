package logica;

import java.util.Date;

public class DataProveedor extends DataUsuario {
    private String sitioWeb;
    private String descripcion;

    public DataProveedor(String contra, String nickname, String nombre, String apellido, String email, Date fechaNac, String sitioWeb, String descripcion){
        super(contra, nickname, nombre, apellido, email, fechaNac);
        this.sitioWeb = sitioWeb;
        this.descripcion = descripcion;
    }

    public String getSitioWeb() { return sitioWeb; }
    public void setSitioWeb(String sitioWeb) { this.sitioWeb = sitioWeb; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    @Override
    public String toString() {
        return "DataProveedor{" +
                "nombre='" + getNombre() + '\'' +
                ", apellido='" + getApellido() + '\'' +
                ", nickname='" + getNickname() + '\'' +
                ", email='" + getEmail() + '\'' +
                ", fechaNac='" + getFechaNac() + '\'' +
                ", sitioWeb='" + getSitioWeb() + '\'' +
                ", descripcion='" + getDescripcion() + '\'' +
                '}';
    }
}
