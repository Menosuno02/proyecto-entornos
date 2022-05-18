package clases;

import java.util.Vector;
import main.ErrorBBDD;
import static main.Restaurante.bd;
import static main.Restaurante.sc;
import static main.Restaurante.usuLog;

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

    /**
     * Método para añadir un producto a la cesta del usuario, verificando que el producto existe y que no ha sido añadido ya a la cesta
     *
     * @return true si se ha añadido un producto a la cesta con éxito
     */
    public static boolean addProductoCesta() {
        int cantidad;
        boolean valProd;
        String codProducto;
        Vector<Producto> productos = new Vector<Producto>();
        Vector<String> codigosCesta = new Vector<String>();
        try {
            productos = bd.listadoProductos();
        } catch (ErrorBBDD ex) {
            System.out.println("Error -> " + ex);
            return false;
        }
        do {
            valProd = false;
            System.out.println("Introduce código producto");
            codProducto = sc.nextLine();
            for (Producto p : productos) {
                if (p.getCodProducto().equalsIgnoreCase(codProducto)) {
                    valProd = true;
                    break;
                }
            }
            try {
                codigosCesta = bd.getCodProductosCesta(usuLog.getIdUsuario());
            } catch (ErrorBBDD ex) {
                System.out.println("Error -> " + ex);
                break;
            }
            for (int i = 0; i <= codigosCesta.size() - 1; i++) {
                if (codigosCesta.get(i).equalsIgnoreCase(codProducto)) {
                    valProd = false;
                    break;
                }
            }
        } while (!valProd);
        do {
            System.out.println("Introduce cantidad");
            cantidad = sc.nextInt();
        } while (cantidad < 1 || cantidad > 10);
        sc.nextLine();
        try {
            bd.addProductoCesta(usuLog.getIdUsuario(), codProducto, cantidad);
            System.out.println("Producto añadido");
        } catch (ErrorBBDD ex) {
            System.out.println("Error -> " + ex);
            return false;
        }
        return true;
    }

    /**
     * Método para borrar un proceso de la cesta del usuario verificando que exista en la cesta
     *
     * @return true si se ha borrado un producto de la cesta con éxito
     */
    public static boolean deleteProductoCesta() {
        boolean valProd;
        String codProducto;
        Vector<String> productosCesta = new Vector<String>();
        do {
            valProd = false;
            System.out.println("Introduce código producto");
            codProducto = sc.nextLine();
            try {
                productosCesta = bd.getCodProductosCesta(usuLog.getIdUsuario());
            } catch (ErrorBBDD ex) {
                System.out.println("Error -> " + ex);
                break;
            }
            for (int i = 0; i <= productosCesta.size() - 1; i++) {
                if (productosCesta.get(i).equalsIgnoreCase(codProducto)) {
                    valProd = true;
                }
            }
        } while (!valProd);
        try {
            bd.quitarProductoCesta(usuLog.getIdUsuario(), codProducto);
            System.out.println("Producto borrado");
        } catch (ErrorBBDD ex) {
            System.out.println("Error -> " + ex);
            return false;
        }
        return true;
    }

}
