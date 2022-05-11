package clases;

/**
 *
 * @author administrador
 */
public class Cliente extends Usuario {

    private Tarjeta tarjeta;

    public Cliente(Tarjeta tarjeta, String idUsuario, String dni, char tipo, String nombreUsuario, String clave, String correo, String nombreApellidos, String direccion) {
        super(idUsuario, dni, tipo, nombreUsuario, clave, correo, nombreApellidos, direccion);
        this.tarjeta = tarjeta;
    }

    public Tarjeta getTarjeta() {
        return tarjeta;
    }

    public void setTarjeta(Tarjeta tarjeta) {
        this.tarjeta = tarjeta;
    }

}
