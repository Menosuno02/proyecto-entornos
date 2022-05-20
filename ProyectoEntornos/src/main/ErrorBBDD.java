package main;

/**
 * Error al acceder a la base de datos
 *
 * @author Alejandro López, Sergio Gago, Marcos Madrid, Alberto Mayo
 */
public class ErrorBBDD extends Exception {

    public ErrorBBDD(String arg0) {
        super(arg0);
    }
}
