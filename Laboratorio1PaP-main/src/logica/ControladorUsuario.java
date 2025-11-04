package logica;

import excepciones.UsuarioNoExisteException;
import excepciones.UsuarioRepetidoException;
import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class ControladorUsuario implements IControladorUsuario {

    public ControladorUsuario() {}

    // Alta de usuario con validación por nickname
    public void registrarUsuario(DataUsuario usuario) throws UsuarioRepetidoException {
        ManejadorUsuario mu = ManejadorUsuario.getinstance();
        Usuario u = mu.obtenerUsuarioXNick(usuario.getNickname());  
        if (u != null) {  
            throw new UsuarioRepetidoException("El usuario con nickname " + usuario.getNickname() + " ya está registrado");
        }
        if (usuario instanceof DataTurista) {
            u = new Turista(
                usuario.getContra(),
                usuario.getNickname(),
                usuario.getNombre(),
                usuario.getApellido(),
                usuario.getEmail(),
                usuario.getFechaNac(),
                ((DataTurista) usuario).getNacionalidad()
            );
        } else {
            u = new Proveedor(
                usuario.getContra(),
                usuario.getNickname(),
                usuario.getNombre(),
                usuario.getApellido(),
                usuario.getEmail(),
                usuario.getFechaNac(),
                ((DataProveedor) usuario).getSitioWeb(),
                ((DataProveedor) usuario).getDescripcion()
            );
        }
        mu.addUsuario(u);
    }

    // Consulta por nickname
    public DataUsuario verInfoUsuario(String nickname) throws UsuarioNoExisteException {
        ManejadorUsuario mu = ManejadorUsuario.getinstance();
        Usuario u = mu.obtenerUsuarioXNick(nickname);
        if (u == null) {
            throw new UsuarioNoExisteException("El usuario con nickname " + nickname + " no existe");
        }
        if (u instanceof Turista) {
            return new DataTurista(
                u.getContra(), u.getNickname(), u.getNombre(),
                u.getApellido(), u.getEmail(), u.getFechaNac(),
                ((Turista) u).getNacionalidad()
            );
        } else {
            return new DataProveedor(
                u.getContra(), u.getNickname(), u.getNombre(),
                u.getApellido(), u.getEmail(), u.getFechaNac(),
                ((Proveedor) u).getSitioWeb(), ((Proveedor) u).getDescripcion()
            );
        }
    }

    // Get de todos los usuarios
    public DataUsuario[] getUsuarios() throws UsuarioNoExisteException {
        ManejadorUsuario mu = ManejadorUsuario.getinstance();
        Usuario[] usrs = mu.getUsuarios();

        if (usrs == null || usrs.length == 0) {
            throw new UsuarioNoExisteException("No existen usuarios registrados");
        }

        DataUsuario[] du = new DataUsuario[usrs.length];
        for (int i = 0; i < usrs.length; i++) {
            Usuario usuario = usrs[i];
            if (usuario instanceof Turista) {
                du[i] = new DataTurista(
                    usuario.getContra(), usuario.getNickname(), usuario.getNombre(),
                    usuario.getApellido(), usuario.getEmail(), usuario.getFechaNac(),
                    ((Turista) usuario).getNacionalidad()
                );
            } else if (usuario instanceof Proveedor) {
                du[i] = new DataProveedor(
                    usuario.getContra(), usuario.getNickname(), usuario.getNombre(),
                    usuario.getApellido(), usuario.getEmail(), usuario.getFechaNac(),
                    ((Proveedor) usuario).getSitioWeb(), ((Proveedor) usuario).getDescripcion()
                );
            }
        }
        return du;
    }

    // Modificaciones por nickname
    public void modificarUsuario(DataUsuario u, int opc, String dato) throws UsuarioNoExisteException {
        ManejadorUsuario mu = ManejadorUsuario.getinstance();
        Usuario us = mu.obtenerUsuarioXNick(u.getNickname());
        if (us == null) {
            throw new UsuarioNoExisteException("Usuario inexistente");
        }

        if (us instanceof Turista) {
            switch (opc) {
                case 1: mu.modNombre(us, dato); break;
                case 2: mu.modApellido(us, dato); break;
                case 3:
                    SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy"); 
                    try {
                        Date fechaNacimiento = formato.parse(dato);
                        mu.modFechaNac(us, fechaNacimiento);
                    } catch (ParseException e) {
                        System.out.println("Formato inválido. Use dd/MM/yyyy.");
                    }
                    break;
                case 4: mu.modNacionalidad(us, dato); break;
                case 5: mu.modContra(us, dato); break;
            }
        } else {
            switch (opc) {
                case 1: mu.modNombre(us, dato); break;
                case 2: mu.modApellido(us, dato); break;
                case 3:
                    SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy"); 
                    try {
                        Date fechaNacimiento = formato.parse(dato);
                        mu.modFechaNac(us, fechaNacimiento);
                    } catch (ParseException e) {
                        System.out.println("Formato inválido. Use dd/MM/yyyy.");
                    }
                    break;
                case 4: mu.modSitioWeb(us, dato); break;
                case 5: mu.modDescripcion(us, dato); break;
                case 6: mu.modContra(us, dato); break;
            }
        }
    }

    // Solo proveedores
    public DataUsuario[] getProveedores() throws UsuarioNoExisteException {
        ManejadorUsuario mu = ManejadorUsuario.getinstance();
        Usuario[] usrs = mu.getUsuarios();

        if (usrs == null || usrs.length == 0) {
            throw new UsuarioNoExisteException("No existen proveedores registrados");
        }

        return java.util.Arrays.stream(usrs)
            .filter(u -> u instanceof Proveedor)
            .map(u -> new DataProveedor(
                u.getContra(), u.getNickname(), u.getNombre(),
                u.getApellido(), u.getEmail(), u.getFechaNac(),
                ((Proveedor) u).getSitioWeb(), ((Proveedor) u).getDescripcion()
            ))
            .toArray(DataUsuario[]::new);
    }
}

