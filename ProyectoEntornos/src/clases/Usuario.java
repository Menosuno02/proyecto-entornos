package clases;

/**
 *
 * @author administrador
 */
public class Usuario {

    private String idUsuario;
    private String dni;
    private char tipo;
    private String nombreUsuario;
    private String clave;
    private String correo;
    private String nombreApellidos;
    private String direccion;

    /**
     * Constructor de un usuario
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
    public Usuario(String idUsuario, String dni, char tipo, String nombreUsuario, String clave, String correo, String nombreApellidos, String direccion) {
        this.idUsuario = idUsuario;
        this.dni = dni;
        this.tipo = tipo;
        this.nombreUsuario = nombreUsuario;
        this.clave = clave;
        this.correo = correo;
        this.nombreApellidos = nombreApellidos;
        this.direccion = direccion;
    }

    /**
     * Getter del ID del usuario
     *
     * @return id del usuario
     */
    public String getIdUsuario() {
        return idUsuario;
    }

    /**
     * Getter del DNI del usuario
     *
     * @return dni del usuario
     */
    public String getDni() {
        return dni;
    }

    /**
     * Getter del tipo del usuario
     *
     * @return tipo del usuario
     */
    public char getTipo() {
        return tipo;
    }

    /**
     * Getter del nombre del usuario
     *
     * @return nombre del usuario
     */
    public String getNombreUsuario() {
        return nombreUsuario;
    }

    /**
     * Getter de la clave del usuario
     *
     * @return clave del usuario
     */
    public String getClave() {
        return clave;
    }

    /**
     * Getter del correo electrónico del usuario
     *
     * @return correo del usuario
     */
    public String getCorreo() {
        return correo;
    }

    /**
     * Getter del nombre y apellidos del usuario
     *
     * @return nombre y apellidos del usuario
     */
    public String getNombreApellidos() {
        return nombreApellidos;
    }

    /**
     * Getter de la dirección del usuario
     *
     * @return dirección del usuario
     */
    public String getDireccion() {
        return direccion;
    }

    @Override
    public String toString() {
        return "Usuario{" + "idUsuario=" + idUsuario + ", dni=" + dni + ", tipo=" + tipo + ", nombreUsuario=" + nombreUsuario + ", clave=" + clave + ", correo=" + correo + ", nombreApellidos=" + nombreApellidos + ", direccion=" + direccion + '}';
    }

}
