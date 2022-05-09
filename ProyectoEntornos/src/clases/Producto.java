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

    public Producto(String codProducto, String nomProducto, String ingredientes, String alergenos, double precio, int minPrep) {
        this.codProducto = codProducto;
        this.nomProducto = nomProducto;
        this.ingredientes = ingredientes;
        this.alergenos = alergenos;
        this.precio = precio;
        this.minPrep = minPrep;
    }

    public String getCodProducto() {
        return codProducto;
    }

    public String getNomProducto() {
        return nomProducto;
    }

    public String getIngredientes() {
        return ingredientes;
    }

    public String getAlergenos() {
        return alergenos;
    }

    public double getPrecio() {
        return precio;
    }

    public int getMinPrep() {
        return minPrep;
    }

    @Override
    public String toString() {
        return "Producto{" + "codProducto=" + codProducto + ", nomProducto=" + nomProducto + ", ingredientes=" + ingredientes + ", alergenos=" + alergenos + ", precio=" + precio + ", minPrep=" + minPrep + '}';
    }

}
