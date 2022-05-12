package clases;

/**
 *
 * @author administrador
 */
public class ProductoCesta {

    private String idCliente;
    private String codProducto;
    private int cantidad;

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
