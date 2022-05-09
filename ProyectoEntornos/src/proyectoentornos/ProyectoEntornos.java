package proyectoentornos;

import bbdd.*;
import clases.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import static java.nio.file.StandardOpenOption.WRITE;
import java.time.LocalDate;
import java.util.Locale;
import java.util.regex.*;
import java.util.Scanner;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author administrador
 */
public class ProyectoEntornos {

    static BD_Restaurante bd = new BD_Restaurante("restaurante");
    static Usuario usuLog = null;

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
                    idUsu = "E" + dni.substring(dni.length() - 6, dni.length() - 1);
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
                                    usuLog = u;
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
                    System.out.println("Error (tipo inválido)");
                    break;
            }
        }
    }

    /**
     * Método que comprueba si una contraseña: - Tiene entre 6 y 30 caracteres - Posee al menos un número, una minúscula, una mayúscula y un caracter especial - No posee espacios
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
        int menuCliente, cantidad, numTarjeta, ccv;
        boolean valProd;
        String codProducto, fechaExp;
        Tarjeta tarjeta;
        Vector<Producto> productos = null;
        Vector<String> productosCesta = null;
        do {
            System.out.println("\n1. Lista productos\n2. Añadir producto a cesta\n3. Quitar producto de cesta\n4. Tramitar pedido\n5. Salir");
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
                            productosCesta = bd.getProductosCesta(usuLog.getIdUsuario());
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
                            productosCesta = bd.getProductosCesta(usuLog.getIdUsuario());
                        } catch (ErrorBBDD ex) {
                            System.out.println("Error -> " + ex);
                            break;
                        }
                        for (Producto p : productos) {
                            if (p.getCodProducto().equalsIgnoreCase(codProducto)) {
                                valProd = false;
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
                    try {
                        tarjeta = bd.buscarTarjeta(usuLog.getIdUsuario());
                    } catch (ErrorBBDD ex) {
                        System.out.println("Error -> " + ex);
                        break;
                    }
                    if (tarjeta == null) {
                        System.out.println("El usuario no posee tarjeta");
                        System.out.println("Introduce num tarjeta (16 números seguidos)");
                        numTarjeta = sc.nextInt();
                        System.out.println("Introduce CCV");
                        ccv = sc.nextInt();
                        System.out.println("Introduce fecha expiración (MM/YY)");
                        fechaExp = sc.nextLine();
                    }
                    break;
                case 5:
                    System.out.println("Menú cerrado");
                    break;
                default:
                    break;
            }
        } while (menuCliente != 5);
    }

}
