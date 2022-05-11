package clases;

/**
 *
 * @author administrador
 */
public class Empleado extends Usuario {

    private boolean repartidor;

    public Empleado(String idUsuario, String dni, char tipo, String nombreUsuario, String clave, String correo, String nombreApellidos, String direccion, boolean repartidor) {
        super(idUsuario, dni, tipo, nombreUsuario, clave, correo, nombreApellidos, direccion);
        this.repartidor = repartidor;
    }

    public boolean isRepartidor() {
        return repartidor;
    }

    @Override
    public String toString() {
        return "Empleado{" + super.toString() + ",repartidor=" + repartidor + '}';
    }

}
