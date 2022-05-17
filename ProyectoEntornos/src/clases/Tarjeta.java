package clases;

/**
 *
 * @author administrador
 */
public class Tarjeta {

    private String numTarjeta;
    private int ccv;
    private String fechaCaducidad;

    /**
     * Constructor de una tarjeta
     *
     * @param numTarjeta número de la tarjeta (16 números)
     * @param ccv ccv de la tarjeta (3 números)
     * @param fechaCaducidad fecha de expiración de la tarjeta (MM/yy)
     */
    public Tarjeta(String numTarjeta, int ccv, String fechaCaducidad) {
        this.numTarjeta = numTarjeta;
        this.ccv = ccv;
        this.fechaCaducidad = fechaCaducidad;
    }

    /**
     * Getter del número de la tarjeta
     *
     * @return número de la tarjeta
     */
    public String getNumTarjeta() {
        return numTarjeta;
    }

    /**
     * Getter del CCV de la tarjeta
     *
     * @return ccv de la tarjeta
     */
    public int getCcv() {
        return ccv;
    }

    /**
     * Getter de la fecha de expiración de la tarjeta
     *
     * @return fecha de expiración de la tarjeta
     */
    public String getFechaCaducidad() {
        return fechaCaducidad;
    }

}
