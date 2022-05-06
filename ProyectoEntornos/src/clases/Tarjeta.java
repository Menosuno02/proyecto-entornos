package clases;

/**
 *
 * @author administrador
 */
public class Tarjeta {

    private String numTarjeta;
    private String fechaCaducidad;
    private int ccv;

    public Tarjeta(String numTarjeta, String fechaCaducidad, int ccv) {
        this.numTarjeta = numTarjeta;
        this.fechaCaducidad = fechaCaducidad;
        this.ccv = ccv;
    }

}
