package proyectoentornos;

import bbdd.*;
import clases.*;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;
import java.util.regex.*;
import java.util.Scanner;
import java.util.Vector;

/**
 *
 * @author administrador
 */
public class ProyectoEntornos {

    static BD_Restaurante bd = new BD_Restaurante("restaurante");
    static Cliente usuLog = null;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        BD_Restaurante bd = new BD_Restaurante("restaurante");
        Scanner sc = new Scanner(System.in);
        sc.useLocale(Locale.ENGLISH);

        String usuario, clave, dni, correo, nombreApellidos, direccion, idUsu;
        boolean usuCon, usuExiste;
        Vector<Usuario> usuarios;
        int menúInicio;
        System.out.println("\n1. Crear usuario\n2. Iniciar sesión\n3. Salir");
        menúInicio = sc.nextInt();
        sc.nextLine();
        do {
            switch (menúInicio) {
                case 1:
                    do {
                        System.out.println("Introduce DNI");
                        dni = sc.nextLine();
                    } while (!dni.matches("[0-9]{8}[A-Z]{1}$"));
                    do {
                        usuExiste = false;
                        System.out.println("Introduce username (nombre usuario)");
                        usuario = sc.nextLine();
                        try {
                            usuarios = bd.listadoUsuarios();
                        } catch (ErrorBBDD ex) {
                            System.out.println("Error -> " + ex);
                            continue;
                        }
                        for (Usuario u : usuarios) {
                            if (u.getNombreUsuario().equalsIgnoreCase(usuario)) {
                                usuExiste = true;
                                break;
                            }
                        }
                    } while (usuExiste);
                    do {
                        System.out.println("Clave debe tener entre 6 y 30 caracteres, tener mínimo una mayúscula, una minúscula, un número y un caracter especial y no tener espacios");
                        System.out.println("Introduce clave");
                        clave = sc.nextLine();
                    } while (!valClave(clave));
                    do {
                        System.out.println("Introduce correo");
                        correo = sc.nextLine();
                    } while (!correo.contains("@"));
                    System.out.println("Introduce nombre y apellidos");
                    nombreApellidos = sc.nextLine();
                    System.out.println("Introduce dirección");
                    direccion = sc.nextLine();
                    try {
                        idUsu = bd.getCodUsu("C");
                    } catch (ErrorBBDD ex) {
                        System.out.println("Error -> " + ex);
                        break;
                    }
                    try {
                        bd.addCliente(new Usuario(idUsu, dni, 'C', usuario, clave, correo, nombreApellidos, direccion, false));
                    } catch (ErrorBBDD ex) {
                        System.out.println("Error -> " + ex);
                        break;
                    }
                    break;
                case 2:
                    do {
                        usuCon = false;
                        System.out.println("Introduce nombre usuario");
                        usuario = sc.nextLine();
                        System.out.println("Introduce contraseña");
                        clave = sc.nextLine();
                        try {
                            usuarios = bd.listadoUsuarios();
                        } catch (ErrorBBDD ex) {
                            System.out.println("Error -> " + ex);
                            continue;
                        }
                        for (Usuario u : usuarios) {
                            if (u.getNombreUsuario().equalsIgnoreCase(usuario)) {
                                if (u.getClave().equalsIgnoreCase(clave)) {
                                    usuLog = (Cliente) u;
                                    usuCon = true;
                                    break;
                                } else {
                                    break;
                                }
                            }
                        }
                        if (!usuCon) {
                            System.out.println("Sesión no iniciada (no existe usuario o contraseña inválida)");
                        }
                    } while (!usuCon);
                    System.out.println("Sesión iniciada");
                    break;
                case 3:
                    System.out.println("Menú cerrado");
                    break;
                default:
                    break;
            }
        } while (menúInicio != 2 && menúInicio != 3);

        if (menúInicio == 3) {
            System.exit(0);
        } else {
            switch (usuLog.getTipo()) {
                case 'C':
                    menuCliente();
                    break;
                case 'E':
                    
                    break;
                case 'G':
                    break;
                case 'A':
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * Método que comprueba si una contraseña:
     * - Tiene entre 6 y 30 caracteres
     * - Posee al menos un número, una minúscula, una mayúscula y un caracter especial
     * - No posee espacios
     *
     * @param clave la contraseña
     * @return true si la contraseña cumple la condición
     */
    public static boolean valClave(String clave) {
        String regex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{6,30}$";
        Pattern p = Pattern.compile(regex);
        if (clave == null) {
            return false;
        }
        Matcher m = p.matcher(clave);
        return m.matches();
    }

    /**
     * Menú de los clientes
     */
    public static void menuCliente() {
        Scanner sc = new Scanner(System.in);
        int menuCliente, cantidad, numTarjeta = 0, ccv;
        double importe = 0, importeTotal = 0;
        boolean valProd;
        String codProducto, fechaExp, codPedido = null;
        Tarjeta tarjeta;
        Vector<Producto> productos = null;
        Vector<String> productosCesta = null;
        do {
            System.out.println("\n1. Lista productos\n2. Añadir producto a cesta\n3. Quitar producto de cesta\n4. Listar productos cesta\n5. Tramitar pedido\n6. Salir");
            menuCliente = sc.nextInt();
            sc.nextLine();
            try {
                productos = bd.listadoProductos();
            } catch (ErrorBBDD ex) {
                System.out.println("Error -> " + ex);
                break;
            }
            switch (menuCliente) {
                case 1:
                    for (Producto p : productos) {
                        p.toString();
                    }
                    break;
                case 2:
                    do {
                        valProd = false;
                        System.out.println("Introduce código producto");
                        codProducto = sc.nextLine();
                        for (Producto p : productos) {
                            if (p.getCodProducto().equalsIgnoreCase(codProducto)) {
                                valProd = true;
                                break;
                            }
                        }
                        try {
                            productosCesta = bd.getCodProductosCesta(usuLog.getIdUsuario());
                        } catch (ErrorBBDD ex) {
                            System.out.println("Error -> " + ex);
                            break;
                        }
                        for (Producto p : productos) {
                            if (p.getCodProducto().equalsIgnoreCase(codProducto)) {
                                valProd = false;
                                break;
                            }
                        }
                    } while (!valProd);
                    do {
                        System.out.println("Introduce cantidad");
                        cantidad = sc.nextInt();
                    } while (cantidad < 1 || cantidad > 10);
                    try {
                        bd.addProductoCesta(usuLog.getIdUsuario(), codProducto, cantidad);
                        System.out.println("Producto añadido");
                    } catch (ErrorBBDD ex) {
                        System.out.println("Error -> " + ex);
                    }
                    break;
                case 3:
                    do {
                        valProd = false;
                        System.out.println("Introduce código producto");
                        codProducto = sc.nextLine();
                        try {
                            productosCesta = bd.getCodProductosCesta(usuLog.getIdUsuario());
                        } catch (ErrorBBDD ex) {
                            System.out.println("Error -> " + ex);
                            break;
                        }
                        for (Producto p : productos) {
                            if (p.getCodProducto().equalsIgnoreCase(codProducto)) {
                                valProd = true;
                            }
                        }
                        for (int i = 0; i <= productosCesta.size() - 1; i++) {
                            if (productosCesta.get(i).equalsIgnoreCase(codProducto)) {
                                valProd = true;
                            }
                        }
                    } while (!valProd);
                    try {
                        bd.quitarProductoCesta(usuLog.getIdUsuario(), codProducto);
                        System.out.println("Producto borrado");
                    } catch (ErrorBBDD ex) {
                        System.out.println("Error -> " + ex);
                        break;
                    }
                    break;
                case 4:
                    Vector<String> cesta = new Vector<String>();
                    try {
                        cesta = bd.getProductosCesta(usuLog.getIdUsuario());
                    } catch (ErrorBBDD ex) {
                        System.out.println("Error -> " + ex);
                        break;
                    }
                    for (int i = 0; i <= cesta.size() - 1; i++) {
                        System.out.println(cesta.get(i));
                    }
                    break;
                case 5:
                    DateTimeFormatter formato = DateTimeFormatter.ofPattern("MM/yy");
                    tarjeta = null;
                    try {
                        tarjeta = bd.buscarTarjeta(usuLog.getIdUsuario());
                    } catch (ErrorBBDD ex) {
                        System.out.println("Error -> " + ex);
                        break;
                    }
                    if (tarjeta == null) {
                        String numTarjetaS, ccvS;
                        LocalDate fechaExpLD = null;
                        boolean valFecha;
                        do {
                            System.out.println("El usuario no posee tarjeta");
                            System.out.println("Introduce num tarjeta (16 números seguidos)");
                            numTarjetaS = sc.nextLine();
                        } while (!numTarjetaS.matches("[0-9]{16}$"));
                        numTarjeta = Integer.parseInt(numTarjetaS);
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
                            break;
                        }
                    }
                    usuLog.setTarjeta(tarjeta);
                    try {
                        codPedido = bd.getCodPedido();
                    } catch (ErrorBBDD ex) {
                        System.out.println("Error -> " + ex);
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
                        break;
                    }
                    try {
                        bd.addPedido(usuLog.getIdUsuario(), importeTotal, codPedido);
                    } catch (ErrorBBDD ex) {
                        System.out.println("Error -> " + ex);
                    }
                    break;
                case 6:
                    System.out.println("Menú cerrado");
                    break;
                default:
                    break;
            }
        } while (menuCliente != 6);
    }

}
