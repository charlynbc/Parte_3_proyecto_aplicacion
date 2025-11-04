package logica;

import java.util.Date;
import java.time.LocalDate;
import java.time.LocalTime;

public class DataInscripcion{
    Date fechaniscripcion;
    int cantturistas;
    float costo;
    private String turista;
    private String salida;


    public DataInscripcion() {
    }

    public DataInscripcion(Date fechaniscripcion, int cantturistas, float costo, String turista, String salida) {
        this.fechaniscripcion = fechaniscripcion;
        this.cantturistas = cantturistas;
        this.costo = costo;
        this.turista = turista;
        this.salida = salida;
    }


    public Date getFechaniscripcion() {
        return this.fechaniscripcion;
    }

    public void setFechaniscripcion(Date fechaniscripcion) {
        this.fechaniscripcion = fechaniscripcion;
    }

    public int getCantturistas() {
        return this.cantturistas;
    }

    public void setCantturistas(int cantturistas) {
        this.cantturistas = cantturistas;
    }

    public float getCosto() {
        return this.costo;
    }

    public void setCosto(float costo) {
        this.costo = costo;
    }

    public String getTurista() {
        return this.turista;
    }

    public void setTurista(String turista) {
        this.turista = turista;
    }

    public String getSalida() {
        return this.salida;
    }

    public void setSalida(String salida) {
        this.salida = salida;
    }
     
    @Override
    public String toString() {
        return "{" +
            " fechaniscripcion='" + getFechaniscripcion() + "'" +
            ", cantturistas='" + getCantturistas() + "'" +
            ", costo='" + getCosto() + "'" +
            ", turista='" + getTurista() + "'" +
            ", salida='" + getSalida() + "'" +
            "}";
    }

}