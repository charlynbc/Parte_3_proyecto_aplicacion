package view;

/**
 * Adaptador de vista para exponer las propiedades esperadas por el JSP
 * a partir de un DTO SOAP de Actividad.
 */
public class ActivityView {
    private final String id;
    private final String nombre;
    private final String descripcion;
    private final Integer duracion; // no disponible en SOAP, queda null
    private final float costo;
    private final String ciudad; // mapea desde "lugar" de SOAP
    private final String proveedor; // no disponible en SOAP

    public ActivityView(String id, String descripcion, float costo, String ciudad) {
        this.id = id;
        this.nombre = id;
        this.descripcion = descripcion;
        this.duracion = null;
        this.costo = costo;
        this.ciudad = ciudad;
        this.proveedor = null;
    }

    public String getId() { return id; }
    public String getNombre() { return nombre; }
    public String getDescripcion() { return descripcion; }
    public Integer getDuracion() { return duracion; }
    public float getCosto() { return costo; }
    public String getCiudad() { return ciudad; }
    public String getProveedor() { return proveedor; }
}
