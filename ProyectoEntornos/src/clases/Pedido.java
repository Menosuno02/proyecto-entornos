package clases;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Vector;
import proyectoentornos.ErrorBBDD;
import static proyectoentornos.ProyectoEntornos.bd;
import static proyectoentornos.ProyectoEntornos.sc;
import static proyectoentornos.ProyectoEntornos.usuLog;

/**
 *
 * @author administrador
 */
public class Pedido {

    private String codPedido;
    private String idCliente;
    private String idRepartidor;
    private LocalDateTime fechaAlta;
    private LocalDateTime fechaEntrega;
    private double precio;

    /**
     * Constructor de un pedido
     *
     * @param codPedido código del pedido
     * @param idCliente id del cliente que ha realizado el pedido
     * @param idRepartidor id del empleado repartidor que reparte el pedido
     * @param fechaAlta fecha en la que el pedido fue dado de alta
     * @param fechaEntrega fecha en el que el pedido fue entregado
     * @param precio importe total del pedido
     */
    public Pedido(String codPedido, String idCliente, String idRepartidor, LocalDateTime fechaAlta, LocalDateTime fechaEntrega, double precio) {
        this.codPedido = codPedido;
        this.idCliente = idCliente;
        this.idRepartidor = idRepartidor;
        this.fechaAlta = fechaAlta;
        this.fechaEntrega = fechaEntrega;
        this.precio = precio;
    }

    @Override
    public String toString() {
        return "Pedido{" + "codPedido=" + codPedido + ", idCliente=" + idCliente + ", idRepartidor=" + idRepartidor + ", fechaAlta=" + fechaAlta + ", fechaEntrega=" + fechaEntrega + ", precio=" + precio + '}';
    }

    /**
     * Método que tramita un pedido con los productos de la cesta del usuario, creando el pedido y sus procesos y una tarjeta si el usuario no tuviese una y vaciando la cesta
     *
     * @return true si se ha tramitado el pedido con éxito
     */
    public static boolean tramitarPedido() {
        int cantidad, ccv;
        double importe = 0, importeTotal = 0;
        String numTarjeta, fechaExp, codPedido = null;
        Tarjeta tarjeta;
        Vector<String> productosCesta = new Vector<String>();
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("MM/yy");
        tarjeta = null;
        try {
            tarjeta = bd.buscarTarjeta(usuLog.getIdUsuario());
        } catch (ErrorBBDD ex) {
            System.out.println("Error -> " + ex);
            return false;
        }
        if (tarjeta == null) {
            String ccvS;
            LocalDate fechaExpLD = null;
            boolean valFecha;
            do {
                System.out.println("El usuario no posee tarjeta");
                System.out.println("Introduce num tarjeta (16 números seguidos)");
                numTarjeta = sc.nextLine();
            } while (!numTarjeta.matches("[0-9]{16}$"));
            do {
                System.out.println("Introduce CCV (3 números)");
                ccvS = sc.nextLine();
            } while (!ccvS.matches("[0-9]{3}$"));
            ccv = Integer.parseInt(ccvS);
            do {
                valFecha = false;
                try {
                    System.out.println("Introduce fecha expiración (MM/YY)");
                    fechaExpLD = LocalDate.parse(sc.nextLine(), formato);
                    valFecha = true;
                } catch (DateTimeParseException dtpex) {
                    System.out.println("Error -> " + dtpex);
                } catch (DateTimeException dtex) {
                    System.out.println("Error -> " + dtex);
                }
            } while (!valFecha);
            fechaExp = formato.format(fechaExpLD);
            tarjeta = new Tarjeta(numTarjeta, ccv, fechaExp);
            try {
                bd.addTarjeta(usuLog.getIdUsuario(), tarjeta);
            } catch (ErrorBBDD ex) {
                System.out.println("Error -> " + ex);
                return false;
            }
        }
        ((Cliente) usuLog).setTarjeta(tarjeta);
        try {
            codPedido = bd.getCodPedido();
        } catch (ErrorBBDD ex) {
            System.out.println("Error -> " + ex);
            return false;
        }
        try {
            productosCesta = bd.getCodProductosCesta(usuLog.getIdUsuario());
            for (int i = 0; i <= productosCesta.size() - 1; i++) {
                cantidad = bd.getCantidadCesta(usuLog.getIdUsuario(), productosCesta.get(i));
                importe = bd.addProcesoPedido(codPedido, productosCesta.get(i), cantidad);
                importeTotal += importe;
                bd.quitarProductoCesta(usuLog.getIdUsuario(), productosCesta.get(i));
            }
        } catch (ErrorBBDD ex) {
            System.out.println("Error -> " + ex);
            return false;
        }
        try {
            bd.addPedido(usuLog.getIdUsuario(), importeTotal, codPedido);
        } catch (ErrorBBDD ex) {
            System.out.println("Error -> " + ex);
            return false;
        }
        return true;
    }

}
