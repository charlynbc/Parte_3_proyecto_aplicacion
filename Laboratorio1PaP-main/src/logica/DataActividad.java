package logica;

import java.util.Date;

public class DataActividad {
    private String nombre;
    private String descripcion;
    private int duracion;
    private float costo;
    private String ciudad;
    private Date fechaAlta;
    private String estado;
    private String url;
    private String proveedor;

    public DataActividad(String nombre, String descripcion, int duracion, float costo, String ciudad, Date fechaAlta, String estado, String proveedor) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.duracion = duracion;
        this.costo = costo;
        this.ciudad = ciudad;
        this.fechaAlta = fechaAlta;
        this.estado = estado;
        this.proveedor = proveedor;
    }
    public DataActividad(String nombre, String descripcion, int duracion, float costo,
            String ciudad, Date fechaAlta, String proveedor) {
this(nombre, descripcion, duracion, costo, ciudad, fechaAlta, "", proveedor);
}

    public String getNombre() {
        return nombre;
    }
    public String getDescripcion() {
        return descripcion;
    }
    public int getDuracion() {
        return duracion;
    }
    public float getCosto() {
        return costo;
    }
    public String getCiudad() {
        return ciudad;
    }
    public Date getFechaAlta() {
        return fechaAlta;
    }
    public String getEstado() {
        return estado;
    }
    public String getProveedor() {
        return proveedor;
    }
    @Override
    public String toString() {
        return "DataActividad{" +
                "nombre='" + nombre + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", duracion=" + duracion +
                ", costo=" + costo +
                ", ciudad='" + ciudad + '\'' +
                ", fechaAlta=" + fechaAlta +
                ", estado='" + estado + '\'' +
                ", proveedor=" + (proveedor != null ? proveedor.toString() : "null") +
                '}';
    }
}
