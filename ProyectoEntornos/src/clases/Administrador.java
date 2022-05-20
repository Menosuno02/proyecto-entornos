package clases;

/**
 *
 * @author Alejandro López, Sergio Gago, Marcos Madrid, Alberto Mayo
 */
public class Administrador extends Usuario {

    /**
     * Constructor de un administrador
     *
     * @param idUsuario id del usuario
     * @param dni dni del usuario
     * @param tipo tipo de usuario (C - cliente, E - empleado, G - gestor, A - administrador)
     * @param nombreUsuario nombre/username del usuario
     * @param clave clave/contraseña del usuario
     * @param correo correo electrónico del usuario
     * @param nombreApellidos nombre y apellidos del usuario
     * @param direccion dirección del usuario
     */
    public Administrador(String idUsuario, String dni, char tipo, String nombreUsuario, String clave, String correo, String nombreApellidos, String direccion) {
        super(idUsuario, dni, tipo, nombreUsuario, clave, correo, nombreApellidos, direccion);
    }

}
