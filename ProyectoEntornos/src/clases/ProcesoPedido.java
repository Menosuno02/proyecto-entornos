package clases;

import java.time.LocalDateTime;

/**
 *
 * @author Alejandro López, Sergio Gago, Marcos Madrid, Alberto Mayo
 */
public class ProcesoPedido {

    private String codPedido;
    private String idEmple;
    private String codProducto;
    private LocalDateTime fechaPrep;
    private int cantidad;

    /**
     * Constructor de un proceso de un pedido (divisiones de un pedido)
     *
     * @param codPedido código del pedido al que pertenece el proceso
     * @param idEmple id del empleado encargado de realizar el proceso
     * @param codProducto código del producto del proceso
     * @param fechaPrep fecha de preparación
     * @param cantidad cantidad de platos del producto
     */
    public ProcesoPedido(String codPedido, String idEmple, String codProducto, LocalDateTime fechaPrep, int cantidad) {
        this.codPedido = codPedido;
        this.idEmple = idEmple;
        this.codProducto = codProducto;
        this.fechaPrep = fechaPrep;
        this.cantidad = cantidad;
    }

    @Override
    public String toString() {
        return "codPedido=" + codPedido + ", idEmple=" + idEmple + ", codProducto=" + codProducto + ", fechaPrep=" + fechaPrep + ", cantidad=" + cantidad;
    }

}
