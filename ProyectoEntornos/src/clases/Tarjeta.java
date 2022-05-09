package clases;

/**
 *
 * @author administrador
 */
public class Tarjeta {

    private int numTarjeta;
    private int ccv;
    private String fechaCaducidad;

    public Tarjeta(int numTarjeta, int ccv, String fechaCaducidad) {
        this.numTarjeta = numTarjeta;
        this.ccv = ccv;
        this.fechaCaducidad = fechaCaducidad;
    }
}
