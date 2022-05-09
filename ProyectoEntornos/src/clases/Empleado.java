package clases;

/**
 *
 * @author administrador
 */
public class Empleado extends Usuario {

    public Empleado(String idUsuario, String dni, char tipo, String nombreUsuario, String clave, String correo, String nombreApellidos, String direccion, boolean repartidor) {
        super(idUsuario, dni, tipo, nombreUsuario, clave, correo, nombreApellidos, direccion, repartidor);
    }

}
