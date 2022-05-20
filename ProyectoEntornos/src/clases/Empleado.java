package clases;

/**
 *
 * @author Alejandro López, Sergio Gago, Marcos Madrid, Alberto Mayo
 */
public class Empleado extends Usuario {

    private boolean repartidor;

    /**
     * Constructor de un empleado
     *
     * @param idUsuario id del usuario
     * @param dni dni del usuario
     * @param tipo tipo de usuario (C - cliente, E - empleado, G - gestor, A - administrador)
     * @param nombreUsuario nombre/username del usuario
     * @param clave clave/contraseña del usuario
     * @param correo correo electrónico del usuario
     * @param nombreApellidos nombre y apellidos del usuario
     * @param direccion dirección del usuario
     * @param repartidor si el empleado puede hacer de repartidor o no
     */
    public Empleado(String idUsuario, String dni, char tipo, String nombreUsuario, String clave, String correo, String nombreApellidos, String direccion, boolean repartidor) {
        super(idUsuario, dni, tipo, nombreUsuario, clave, correo, nombreApellidos, direccion);
        this.repartidor = repartidor;
    }

    /**
     * Getter del atributo repartidor del empleado
     *
     * @return si el empleado puede hacer de repartidor o no
     */
    public boolean isRepartidor() {
        return repartidor;
    }

    @Override
    public String toString() {
        return super.toString() + ", repartidor=" + repartidor;
    }

}
