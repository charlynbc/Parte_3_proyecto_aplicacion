package logica;

import excepciones.ActividadRepetidaException;
import excepciones.UsuarioNoExisteException;
import excepciones.ActividadNoExisteException;
import jakarta.persistence.*;

public class ControladorActividad implements IControladorActividad {

    public ControladorActividad() {}

    @Override
    public void altaActividad(DataActividad actividad) throws ActividadRepetidaException, UsuarioNoExisteException {
        ManejadorActividad ma = ManejadorActividad.getinstance();
        ManejadorUsuario mu = ManejadorUsuario.getinstance();

        // Verificar si ya existe una actividad con ese nombre
        Actividad act = ma.obtenerActividad(actividad.getNombre());
        if (act != null)
            throw new ActividadRepetidaException("Ya existe una actividad con ese nombre");

        // Buscar proveedor
        Usuario usuario = mu.obtenerUsuario(actividad.getProveedor());
        if (!(usuario instanceof Proveedor))
            throw new UsuarioNoExisteException("El usuario seleccionado no es un proveedor válido");

        Proveedor prov = (Proveedor) usuario;

        // Crear actividad y asociarla al proveedor
        act = new Actividad(
            actividad.getNombre(),
            actividad.getDescripcion(),
            actividad.getDuracion(),
            actividad.getCosto(),
            actividad.getCiudad(),
            actividad.getFechaAlta(),
            actividad.getEstado(),
            prov
        );

        // Guardar en manejador y en proveedor
        ma.addActividad(act);
        prov.agregarActividad(act);
    }

    @Override
    public DataActividad verInfoActividad(String nombre) throws ActividadNoExisteException {
        ManejadorActividad ma = ManejadorActividad.getinstance();
        Actividad act = ma.obtenerActividad(nombre);
        if (act == null)
            throw new ActividadNoExisteException("No existe una actividad con ese nombre");

        String provNombre = act.getProveedor() != null ? act.getProveedor().getNombre() : "(sin proveedor)";
        return new DataActividad(
            act.getNombre(), act.getDescripcion(), act.getDuracion(), act.getCosto(),
            act.getCiudad(), act.getFechaAlta(), act.getEstado(), provNombre
        );
    }

    @Override
    public DataActividad[] getActividades() throws ActividadNoExisteException {
        ManejadorActividad ma = ManejadorActividad.getinstance();
        Actividad[] acts = ma.getActividades();

        if (acts != null && acts.length > 0) {
            DataActividad[] da = new DataActividad[acts.length];
            for (int i = 0; i < acts.length; i++) {
                Actividad act = acts[i];
                String provNombre = act.getProveedor() != null ? act.getProveedor().getNombre() : "(sin proveedor)";
                da[i] = new DataActividad(
                    act.getNombre(), act.getDescripcion(), act.getDuracion(),
                    act.getCosto(), act.getCiudad(), act.getFechaAlta(),
                    act.getEstado(), provNombre
                );
            }
            return da;
        } else {
            throw new ActividadNoExisteException("No existen actividades registradas");
        }
    }

    public DataActividadRanking[] rankingActividades() {
        ManejadorActividad ma = ManejadorActividad.getinstance();
        return ma.rankingActividades();
    }

    @Override
    public void modificarActividad(DataActividad actividad, int opc, String dato) throws ActividadNoExisteException {
        jakarta.persistence.EntityManager em = persistence.JPAUtil.getEntityManager();
        jakarta.persistence.EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Actividad act = em.find(Actividad.class, actividad.getNombre());
            if (act == null) throw new ActividadNoExisteException("Actividad inexistente");
            switch (opc) {
                case 1: // descripcion
                    act.setDescripcion(dato);
                    break;
                case 2: // duracion
                    try { act.setDuracion(Integer.parseInt(dato)); } catch (NumberFormatException ignore) {}
                    break;
                case 3: // costo
                    try { act.setCosto(Float.parseFloat(dato)); } catch (NumberFormatException ignore) {}
                    break;
                case 4: // ciudad
                    act.setCiudad(dato);
                    break;
                case 5: // estado
                    act.setEstado(dato);
                    break;
            }
            tx.commit();
        } catch (RuntimeException e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }
    /**
     * Devuelve las actividades en las que un turista está inscripto.
     * @param nicknameTurista el nickname del turista
     * @return array de DataActividad
     * @throws ActividadNoExisteException si no hay actividades
     */
    public DataActividad[] getActividadesPorTurista(String nicknameTurista) throws ActividadNoExisteException {
        ManejadorActividad ma = ManejadorActividad.getinstance();
        Actividad[] acts = ma.obtenerActividadesPorTurista(nicknameTurista);
        if (acts != null && acts.length > 0) {
            DataActividad[] da = new DataActividad[acts.length];
            for (int i = 0; i < acts.length; i++) {
                Actividad act = acts[i];
                String provNombre = act.getProveedor() != null ? act.getProveedor().getNombre() : "(sin proveedor)";
                da[i] = new DataActividad(
                    act.getNombre(), act.getDescripcion(), act.getDuracion(),
                    act.getCosto(), act.getCiudad(), act.getFechaAlta(),
                    act.getEstado(), provNombre
                );
            }
            return da;
        } else {
            throw new ActividadNoExisteException("El turista no está inscripto en ninguna actividad");
        }
    }

    /**
     * Devuelve las actividades de un proveedor.
     * @param nicknameProveedor el nickname del proveedor
     * @return array de DataActividad
     * @throws ActividadNoExisteException si no hay actividades
     */
    public DataActividad[] getActividadesPorProveedor(String nicknameProveedor) throws ActividadNoExisteException {
        ManejadorActividad ma = ManejadorActividad.getinstance();
        Actividad[] acts = ma.obtenerActividadesPorProveedor(nicknameProveedor);
        if (acts != null && acts.length > 0) {
            DataActividad[] da = new DataActividad[acts.length];
            for (int i = 0; i < acts.length; i++) {
                Actividad act = acts[i];
                String provNombre = act.getProveedor() != null ? act.getProveedor().getNombre() : "(sin proveedor)";
                da[i] = new DataActividad(
                    act.getNombre(), act.getDescripcion(), act.getDuracion(),
                    act.getCosto(), act.getCiudad(), act.getFechaAlta(),
                    act.getEstado(), provNombre
                );
            }
            return da;
        } else {
            throw new ActividadNoExisteException("El proveedor no tiene actividades");
        }
    }
}

