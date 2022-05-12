package clases;

/**
 *
 * @author administrador
 */
public class Tarjeta {

    private String numTarjeta;
    private int ccv;
    private String fechaCaducidad;

    public Tarjeta(String numTarjeta, int ccv, String fechaCaducidad) {
        this.numTarjeta = numTarjeta;
        this.ccv = ccv;
        this.fechaCaducidad = fechaCaducidad;
    }

    public String getNumTarjeta() {
        return numTarjeta;
    }

    public int getCcv() {
        return ccv;
    }

    public String getFechaCaducidad() {
        return fechaCaducidad;
    }

}
