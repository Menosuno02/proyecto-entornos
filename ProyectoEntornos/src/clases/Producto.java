package clases;

/**
 *
 * @author administrador
 */
public class Producto {

    private String codProducto;
    private String nomProducto;
    private String ingredientes;
    private String alergenos;
    private double precio;
    private int minPrep;

    /**
     * Constructor de un producto
     *
     * @param codProducto código del producto
     * @param nomProducto nombre del producto
     * @param ingredientes ingredientes del producto (seguidos y por comas)
     * @param alergenos alérgenos del producto (seguidos y por comas)
     * @param precio precio del producto
     * @param minPrep minutos de preparación mínimos del producto
     */
    public Producto(String codProducto, String nomProducto, String ingredientes, String alergenos, double precio, int minPrep) {
        this.codProducto = codProducto;
        this.nomProducto = nomProducto;
        this.ingredientes = ingredientes;
        this.alergenos = alergenos;
        this.precio = precio;
        this.minPrep = minPrep;
    }

    /**
     * Getter del código del producto
     *
     * @return código del producto
     */
    public String getCodProducto() {
        return codProducto;
    }

    /**
     * Getter del nombre del producto
     *
     * @return nombre del producto
     */
    public String getNomProducto() {
        return nomProducto;
    }

    /**
     * Getter de los ingredientes del producto
     *
     * @return ingredientes del producto
     */
    public String getIngredientes() {
        return ingredientes;
    }

    /**
     * Getter de los alérgenos del producto
     *
     * @return alérgenos del producto
     */
    public String getAlergenos() {
        return alergenos;
    }

    /**
     * Getter del precio del producto
     *
     * @return precio del producto
     */
    public double getPrecio() {
        return precio;
    }

    /**
     * Getter de los minutos de preparación del producto
     *
     * @return minutos de preparación del producto
     */
    public int getMinPrep() {
        return minPrep;
    }

    @Override
    public String toString() {
        return "Producto{" + "codProducto=" + codProducto + ", nomProducto=" + nomProducto + ", ingredientes=" + ingredientes + ", alergenos=" + alergenos + ", precio=" + precio + ", minPrep=" + minPrep + '}';
    }

}
