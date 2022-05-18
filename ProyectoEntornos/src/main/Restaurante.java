package main;

import bbdd.*;
import clases.*;
import java.util.Locale;
import java.util.Scanner;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author administrador
 */
public class Restaurante {

    public static BDRestaurante bd = new BDRestaurante("restaurante");
    public static Usuario usuLog = null;
    public static Scanner sc = new Scanner(System.in).useLocale(Locale.ENGLISH);

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        int menúInicio;
        System.out.println("\n1. Crear usuario\n2. Iniciar sesión\n3. Salir");
        menúInicio = sc.nextInt();
        sc.nextLine();
        do {
            switch (menúInicio) {
                case 1:
                    if (Usuario.addUsuario()) {
                        System.out.println("Usuario añadido");
                    }
                    break;
                case 2:
                    if (iniciarSesion()) {
                        System.out.println("Sesión iniciada");
                    } else {
                        System.out.println("Fallo iniciando sesión");
                    }
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
                    menuEmple();
                    break;
                case 'G':
                    menuGestor();
                    break;
                case 'A':
                    menuAdmin();
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * Método para iniciar sesión, verificando que el usuario exista y la clave introducida sea correcta
     *
     * @return true si se ha iniciado sesión con éxito
     */
    public static boolean iniciarSesion() {
        String username, clave;
        boolean usuCon;
        Vector<Usuario> usuarios;
        do {
            usuCon = false;
            System.out.println("Introduce nombre usuario (username)");
            username = sc.nextLine();
            System.out.println("Introduce contraseña");
            clave = sc.nextLine();
            try {
                usuarios = bd.listadoUsuarios();
            } catch (ErrorBBDD ex) {
                System.out.println("Error -> " + ex);
                return false;
            }
            for (Usuario u : usuarios) {
                if (u.getNombreUsuario().equalsIgnoreCase(username)) {
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
                return false;
            }
        } while (!usuCon);
        return true;
    }

    /**
     * Menú de los clientes
     */
    public static void menuCliente() {
        int menuCliente;
        Vector<Producto> productos = new Vector<Producto>();
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
                    if (ProductoCesta.addProductoCesta()) {
                        System.out.println("Producto añadido a cesta");
                    }
                    break;
                case 3:
                    if (ProductoCesta.deleteProductoCesta()) {
                        System.out.println("Producto quitado de cesta");
                    }
                    break;
                case 4:
                    listarProductosCesta();
                    break;
                case 5:
                    if (Pedido.tramitarPedido()) {
                        System.out.println("Pedido creado");
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

    /**
     * Método que lista los productos de la cesta del usuario
     */
    public static void listarProductosCesta() {
        Vector<ProductoCesta> cesta = new Vector<ProductoCesta>();
        try {
            cesta = bd.getProductosCesta(usuLog.getIdUsuario());
        } catch (ErrorBBDD ex) {
            System.out.println("Error -> " + ex);
            return;
        }
        for (int i = 0; i <= cesta.size() - 1; i++) {
            System.out.println(cesta.get(i).toString());
        }
    }

    /**
     * Menú de los empleados
     */
    public static void menuEmple() {
        int menuEmple;
        Vector<Producto> productos = new Vector<Producto>();
        Vector<ProcesoPedido> procesos = new Vector<ProcesoPedido>();
        Vector<Pedido> pedidos = new Vector<Pedido>();
        do {
            System.out.println("\n1. Lista productos\n2. Lista procesos pedidos\n3. Lista pedidos\n4. Salir");
            menuEmple = sc.nextInt();
            sc.nextLine();
            switch (menuEmple) {
                case 1:
                    try {
                        productos = bd.listadoProductos();
                    } catch (ErrorBBDD ex) {
                        System.out.println("Error -> " + ex);
                        break;
                    }
                    for (Producto p : productos) {
                        p.toString();
                    }
                    break;
                case 2:
                    try {
                        procesos = bd.listadoProcesosPedidos();
                    } catch (ErrorBBDD ex) {
                        System.out.println("Error -> " + ex);
                        break;
                    }
                    for (ProcesoPedido pp : procesos) {
                        pp.toString();
                    }
                    break;
                case 3:
                    try {
                        pedidos = bd.listadoPedidos();
                    } catch (ErrorBBDD ex) {
                        System.out.println("Error -> " + ex);
                        break;
                    }
                    for (Pedido p : pedidos) {
                        p.toString();
                    }
                    break;
                case 4:
                    System.out.println("Menú cerrado");
                    break;
                default:
                    break;
            }
        } while (menuEmple != 4);
    }

    /**
     * Menú de los gestores
     */
    public static void menuGestor() {
        int menu;
        do {
            System.out.println("\n1. Añadir producto\n2. Modificar producto\n3. Borrar producto\n4. Añadir empleado\n5. Modificar empleado\n6. Borrar empleado\n7. Listado modificaciones productos"
                    + "\n8. Listado usuarios\n9. Salir");
            menu = sc.nextInt();
            sc.nextLine();
            opcionesGestorAdmin(menu);
        } while (menu != 9);
    }

    /**
     * Menú de los administradores
     */
    public static void menuAdmin() {
        int menu;
        do {
            System.out.println("\n1. Añadir producto\n2. Modificar producto\n3. Borrar producto\n4. Añadir usuario\n5. Modificar usuario\n6. Borrar usuario\n7. Listado modificaciones productos"
                    + "\n8. Listado usuarios\n9. Salir");
            menu = sc.nextInt();
            sc.nextLine();
            opcionesGestorAdmin(menu);
        } while (menu != 9);
    }

    /**
     * Opciones del menú del gestor/administrador
     *
     * @param opcion opción elegida por el gestor/administrador
     */
    public static void opcionesGestorAdmin(int opcion) {
        switch (opcion) {
            case 1:
                if (Producto.addProducto()) {
                    System.out.println("Producto añadido");
                }
                break;
            case 2:
                if (Producto.modProducto()) {
                    System.out.println("Producto modificado");
                }
                break;
            case 3:
                if (Producto.deleteProducto()) {
                    System.out.println("Producto borrado");
                }
                break;
            case 4:
                if (Usuario.addUsuario()) {
                    System.out.println("Usuario añadido");
                }
                break;
            case 5:
                if (Usuario.modUsuario()) {
                    System.out.println("Usuario modificado");
                }
                break;
            case 6:
                if (Usuario.deleteUsuario()) {
                    System.out.println("Usuario borrado");
                }
                break;
            case 7:
                Vector<ModProducto> modificaciones = new Vector<ModProducto>();
                try {
                    modificaciones = bd.listadoModificaciones();
                } catch (ErrorBBDD ex) {
                    System.out.println("Error -> " + ex);
                    break;
                }
                for (ModProducto m : modificaciones) {
                    System.out.println(m.toString());
                }
                break;
            case 8:
                Vector<Usuario> usuarios = new Vector<Usuario>();
                try {
                    usuarios = bd.listadoUsuarios();
                } catch (ErrorBBDD ex) {
                    System.out.println("Error -> " + ex);
                }
                for (Usuario u : usuarios) {
                    System.out.println(u.toString());
                }
                break;
            case 9:
                System.out.println("Menú cerrado");
                break;
            default:
                break;
        }
    }

}
