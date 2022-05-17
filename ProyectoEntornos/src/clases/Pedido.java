package clases;

import java.time.LocalDateTime;

/**
 *
 * @author administrador
 */
public class Pedido {

    private String codPedido;
    private String idCliente;
    private String idRepartidor;
    private LocalDateTime fechaAlta;
    private LocalDateTime fechaEntrega;
    private double precio;

    /**
     * Constructor de un pedido
     *
     * @param codPedido código del pedido
     * @param idCliente id del cliente que ha realizado el pedido
     * @param idRepartidor id del empleado repartidor que reparte el pedido
     * @param fechaAlta fecha en la que el pedido fue dado de alta
     * @param fechaEntrega fecha en el que el pedido fue entregado
     * @param precio importe total del pedido
     */
    public Pedido(String codPedido, String idCliente, String idRepartidor, LocalDateTime fechaAlta, LocalDateTime fechaEntrega, double precio) {
        this.codPedido = codPedido;
        this.idCliente = idCliente;
        this.idRepartidor = idRepartidor;
        this.fechaAlta = fechaAlta;
        this.fechaEntrega = fechaEntrega;
        this.precio = precio;
    }

    @Override
    public String toString() {
        return "Pedido{" + "codPedido=" + codPedido + ", idCliente=" + idCliente + ", idRepartidor=" + idRepartidor + ", fechaAlta=" + fechaAlta + ", fechaEntrega=" + fechaEntrega + ", precio=" + precio + '}';
    }

}
