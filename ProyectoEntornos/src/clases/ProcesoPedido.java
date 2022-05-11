package clases;

import java.time.LocalDateTime;

/**
 *
 * @author administrador
 */
public class ProcesoPedido {

    private String codPedido;
    private String idEmple;
    private String codProducto;
    private LocalDateTime fechaPrep;
    private int cantidad;

    public ProcesoPedido(String codPedido, String idEmple, String codProducto, LocalDateTime fechaPrep, int cantidad) {
        this.codPedido = codPedido;
        this.idEmple = idEmple;
        this.codProducto = codProducto;
        this.fechaPrep = fechaPrep;
        this.cantidad = cantidad;
    }

    @Override
    public String toString() {
        return "ProcesoPedido{" + "codPedido=" + codPedido + ", idEmple=" + idEmple + ", codProducto=" + codProducto + ", fechaPrep=" + fechaPrep + ", cantidad=" + cantidad + '}';
    }

}
