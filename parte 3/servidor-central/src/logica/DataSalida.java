package logica;

import java.util.Date;
import java.time.LocalDate;
import java.time.LocalTime;

public class DataSalida{
    String nombre;
    Date fecha;
    LocalTime hora;
    int turistasmax;
    String lugar;
    Date fechaalta;
    String imagen;
    String actividad;

    public DataSalida() {}

    public DataSalida(String nombre,Date fecha,LocalTime hora,int turistasmax,String lugar,Date fechaalta,String imagen,String actividad){
        this.nombre = nombre;
        this.fecha = fecha;
        this.hora = hora;
        this.turistasmax = turistasmax;
        this.lugar = lugar;
        this.fechaalta = fechaalta;
        this.imagen = imagen;
        this.actividad = actividad;
    }

    public String getNombre() {
        return this.nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Date getFecha() {
        return this.fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public LocalTime getHora() {
        return this.hora;
    }

    public void setHora(LocalTime hora) {
        this.hora = hora;
    }

    public int getTuristasmax() {
        return this.turistasmax;
    }

    public void setTuristasmax(int turistasmax) {
        this.turistasmax = turistasmax;
    }

    public String getLugar() {
        return this.lugar;
    }

    public void setLugar(String lugar) {
        this.lugar = lugar;
    }

    public Date getFechaalta() {
        return this.fechaalta;
    }

    public void setFechaalta(Date fechaalta) {
        this.fechaalta = fechaalta;
    }

    public String getImagen() {
        return this.imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public void setActividad(String actividad){
        this.actividad = actividad;
    }

    public String getActividad(){
        return this.actividad;
    }

    @Override
    public String toString() {
        return "{" +
            " nombre='" + getNombre() + "'" +
            ", fecha='" + getFecha() + "'" +
            ", hora='" + getHora() + "'" +
            ", turistasmax='" + getTuristasmax() + "'" +
            ", lugar='" + getLugar() + "'" +
            ", fechaalta='" + getFechaalta() + "'" +
            ", imagen='" + getImagen() + "'" +
            "}";
    }

}