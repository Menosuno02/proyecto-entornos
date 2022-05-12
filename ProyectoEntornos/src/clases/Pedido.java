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
