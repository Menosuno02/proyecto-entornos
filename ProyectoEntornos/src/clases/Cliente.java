package clases;

/**
 *
 * @author administrador
 */
public class Cliente extends Usuario {

    private Tarjeta tarjeta;

    public Cliente(Tarjeta tarjeta, String idUsuario, String dni, char tipo, String nombreUsuario, String clave, String correo, String nombreApellidos, String direccion, boolean repartidor) {
        super(idUsuario, dni, tipo, nombreUsuario, clave, correo, nombreApellidos, direccion, repartidor);
        this.tarjeta = tarjeta;
    }

    public Tarjeta getTarjeta() {
        return tarjeta;
    }

    public void setTarjeta(Tarjeta tarjeta) {
        this.tarjeta = tarjeta;
    }

}
