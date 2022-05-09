package clases;

/**
 *
 * @author administrador
 */
public class Cliente extends Usuario {

    private Tarjeta tarjeta;

    public Cliente(String idUsuario, String dni, char tipo, String nombreUsuario, String clave, String correo, String nombreApellidos, String direccion, boolean repartidor) {
        super(idUsuario, dni, tipo, nombreUsuario, clave, correo, nombreApellidos, direccion, repartidor);
    }

}
