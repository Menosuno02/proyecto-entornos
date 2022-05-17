package clases;

/**
 *
 * @author administrador
 */
public class ProductoCesta {

    private String idCliente;
    private String codProducto;
    private int cantidad;

    /**
     * Constructor de un producto de una cesta
     *
     * @param idCliente id del cliente de la cesta
     * @param codProducto código del producto
     * @param cantidad cantidad de platos del producto
     */
    public ProductoCesta(String idCliente, String codProducto, int cantidad) {
        this.idCliente = idCliente;
        this.codProducto = codProducto;
        this.cantidad = cantidad;
    }

    @Override
    public String toString() {
        return "ProductoCesta{" + "idCliente=" + idCliente + ", codProducto=" + codProducto + ", cantidad=" + cantidad + '}';
    }

}
