package main;

/**
 * Error al acceder a la base de datos
 *
 * @author administrador
 */
public class ErrorBBDD extends Exception {

    public ErrorBBDD(String arg0) {
        super(arg0);
    }
}
