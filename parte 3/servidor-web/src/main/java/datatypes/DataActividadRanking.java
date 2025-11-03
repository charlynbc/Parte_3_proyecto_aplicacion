package datatypes;

public class DataActividadRanking {
    private String nombre;
    private long cantidadSalidas;

    // No-arg constructor for JAXB/Web Services
    public DataActividadRanking() {
    }

    public DataActividadRanking(String nombre, long cantidadSalidas) {
        this.nombre = nombre;
        this.cantidadSalidas = cantidadSalidas;
    }
    public String getNombre() { return nombre; }
    public long getCantidadSalidas() { return cantidadSalidas; }
    @Override
    public String toString() { return nombre + " (" + cantidadSalidas + ")"; }
}

