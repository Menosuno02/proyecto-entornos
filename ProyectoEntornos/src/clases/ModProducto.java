package clases;

import java.time.LocalDateTime;

/**
 *
 * @author hiper
 */
public class ModProducto {

    private String idUsuario;
    private String codProducto;
    private LocalDateTime fecha;
    private String accion;
    private String descripcion;

    /**
     * Constructor de una nodificación de un producto
     *
     * @param idUsuario id del usuario que ha realizado la modificación
     * @param codProducto código del producto modificado
     * @param fecha fecha de la modificación
     * @param accion acción realizada sobre el producto (alta, baja, modificación)
     * @param descripcion descripción de la modificación
     */
    public ModProducto(String idUsuario, String codProducto, LocalDateTime fecha, String accion, String descripcion) {
        this.idUsuario = idUsuario;
        this.codProducto = codProducto;
        this.fecha = fecha;
        this.accion = accion;
        this.descripcion = descripcion;
    }

    @Override
    public String toString() {
        return "ModProducto{" + "idUsuario=" + idUsuario + ", codProducto=" + codProducto + ", fecha=" + fecha + ", accion=" + accion + ", descripcion=" + descripcion + '}';
    }

}
