package logica;

import java.util.Date;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class MainPersistencia {
    public static void main(String[] args) {
        // Al crear el EMF, EclipseLink ejecuta drop-and-create-tables
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("emf");

        // Crear un proveedor (implementa Usuario)
        Proveedor proveedor = new Proveedor(
            "clave123",            // contra
            "nickproveedor",       // nickname (PK)
            "NombreProv",          // nombre
            "ApellidoProv",        // apellido
            "prov@email.com",      // email
            new Date(),            // fecha nacimiento
            "https://miweb.com",   // sitio web
            "Proveedor de excursiones" // descripcion
        );

        ManejadorUsuario manejador = ManejadorUsuario.getinstance();
        manejador.addUsuario(proveedor);
        System.out.println("Proveedor persistido correctamente.");

        // Recuperar por nickname
        Usuario recuperado = manejador.obtenerUsuario("nickproveedor");
        if (recuperado != null) {
            System.out.println("Usuario recuperado: " +
                               recuperado.getNombre() + " " +
                               recuperado.getApellido());
        } else {
            System.out.println("No se encontró el usuario.");
        }

        emf.close();
    }
}


