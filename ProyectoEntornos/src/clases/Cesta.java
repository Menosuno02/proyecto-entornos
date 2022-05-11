package clases;

import java.util.Vector;

/**
 *
 * @author administrador
 */
public class Cesta {

    private String idCliente;
    private Vector<Producto> productos;

    public Cesta(String idCliente, Vector<Producto> productos) {
        this.idCliente = idCliente;
        this.productos = productos;
    }

}
