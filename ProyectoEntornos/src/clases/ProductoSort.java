package clases;

import java.util.Comparator;

/**
 *
 * @author Alejandro López, Sergio Gago, Marcos Madrid, Alberto Mayo
 */
public class ProductoSort implements Comparator<Producto> {

    @Override
    public int compare(Producto o1, Producto o2) {
        return o1.getNomProducto().compareTo(o2.getNomProducto());
    }
}
