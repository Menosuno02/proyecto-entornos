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
    private boolean repartidor;

    public Usuario(String idUsuario, String dni, char tipo, String nombreUsuario, String clave, String correo, String nombreApellidos, String direccion, boolean repartidor) {
        this.idUsuario = idUsuario;
        this.dni = dni;
        this.tipo = tipo;
        this.nombreUsuario = nombreUsuario;
        this.clave = clave;
        this.correo = correo;
        this.nombreApellidos = nombreApellidos;
        this.direccion = direccion;
        this.repartidor = repartidor;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public char getTipo() {
        return tipo;
    }

    public void setTipo(char tipo) {
        this.tipo = tipo;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getNombreApellidos() {
        return nombreApellidos;
    }

    public void setNombreApellidos(String nombreApellidos) {
        this.nombreApellidos = nombreApellidos;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public boolean isRepartidor() {
        return repartidor;
    }

}
