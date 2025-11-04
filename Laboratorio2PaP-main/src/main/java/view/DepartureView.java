package view;

/**
 * Adaptador de vista para exponer las propiedades esperadas por el JSP
 * a partir de un DTO SOAP de Salida.
 */
public class DepartureView {
    private final String nombre; // mapea desde id
    private final String fecha;
    private final String hora;
    private final String lugar;
    private final Integer turistasmax; // no disponible en SOAP

    public DepartureView(String nombre, String fecha, String hora, String lugar) {
        this.nombre = nombre;
        this.fecha = fecha;
        this.hora = hora;
        this.lugar = lugar;
        this.turistasmax = null;
    }

    public String getNombre() { return nombre; }
    public String getFecha() { return fecha; }
    public String getHora() { return hora; }
    public String getLugar() { return lugar; }
    public Integer getTuristasmax() { return turistasmax; }
}
