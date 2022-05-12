package proyectoentornos;

import bbdd.*;
import clases.*;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.InputMismatchException;
import java.util.Locale;
import java.util.Scanner;
import java.util.Vector;
import java.util.regex.*;

/**
 *
 * @author administrador
 */
public class ProyectoEntornos {

    private static BD_Restaurante bd = new BD_Restaurante("restaurante");
    private static Usuario usuLog = null;
    private static Scanner sc = new Scanner(System.in).useLocale(Locale.ENGLISH);

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
                    if (addUsuario()) {
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
     * Método que comprueba si una contraseña:
     * - Tiene entre 6 y 30 caracteres
     * - Posee al menos un número, una minúscula, una mayúscula y un caracter especial
     * - No posee espacios
     *
     * @param clave la contraseña
     * @return true si la contraseña cumple la condición
     */
    public static boolean valClave(String clave) {
        String regex = "^.*(?=.{6,30})(?=..*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=]).*$";
        Pattern p = Pattern.compile(regex);
        if (clave == null) {
            return false;
        }
        Matcher m = p.matcher(clave);
        return m.matches();
    }

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
                    if (addProductoCesta()) {
                        System.out.println("Producto añadido a cesta");
                    }
                    break;
                case 3:
                    if (deleteProductoCesta()) {
                        System.out.println("Producto quitado de cesta");
                    }
                    break;
                case 4:
                    listarProductosCesta();
                    break;
                case 5:
                    if (tramitarPedido()) {
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

    public static boolean addProductoCesta() {
        int cantidad;
        boolean valProd;
        String codProducto;
        Vector<Producto> productos = new Vector<Producto>();
        Vector<String> codigosCesta = new Vector<String>();
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
                codigosCesta = bd.getCodProductosCesta(usuLog.getIdUsuario());
            } catch (ErrorBBDD ex) {
                System.out.println("Error -> " + ex);
                break;
            }
            for (int i = 0; i <= codigosCesta.size() - 1; i++) {
                if (codigosCesta.get(i).equalsIgnoreCase(codProducto)) {
                    valProd = false;
                    break;
                }
            }
        } while (!valProd);
        do {
            System.out.println("Introduce cantidad");
            cantidad = sc.nextInt();
        } while (cantidad < 1 || cantidad > 10);
        sc.nextLine();
        try {
            bd.addProductoCesta(usuLog.getIdUsuario(), codProducto, cantidad);
        } catch (ErrorBBDD ex) {
            System.out.println("Error -> " + ex);
            return false;
        }
        return true;
    }

    public static boolean deleteProductoCesta() {
        boolean valProd;
        String codProducto;
        Vector<Producto> productos = new Vector<Producto>();
        Vector<String> productosCesta = new Vector<String>();
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
        } catch (ErrorBBDD ex) {
            System.out.println("Error -> " + ex);
            return false;
        }
        return true;
    }

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
        int menuGestor;
        do {
            System.out.println("\n1. Añadir producto\n2. Modificar producto\n3. Borrar producto\n4. Añadir empleado\n5. Modificar empleado\n6. Borrar empleado\n7. Salir");
            menuGestor = sc.nextInt();
            sc.nextLine();
            switch (menuGestor) {
                case 1:
                    if (addProducto()) {
                        System.out.println("Producto añadido");
                    }
                    break;
                case 2:
                    if (modProducto()) {
                        System.out.println("Producto modificado");
                    }
                    break;
                case 3:
                    if (deleteProducto()) {
                        System.out.println("Producto borrado");
                    }
                    break;
                case 4:
                    if (addUsuario()) {
                        System.out.println("Usuario añadido");
                    }
                    break;
                case 5:
                    if (modUsuario()) {
                        System.out.println("Modificaciones aplicadas");
                    }
                    break;
                case 6:
                    if (deleteUsuario()) {
                        System.out.println("Usuario borrado");
                    }
                    break;
                case 7:
                    System.out.println("Menú cerrado");
                    break;
                default:
                    break;
            }
        } while (menuGestor != 7);
    }

    /**
     * Menú de los administradores
     */
    public static void menuAdmin() {
        int menuAdmin;
        do {
            System.out.println("\n1. Añadir producto\n2. Modificar producto\n3. Borrar producto\n4. Añadir usuario\n5. Modificar usuario\n6. Borrar usuario\n7. Salir");
            menuAdmin = sc.nextInt();
            sc.nextLine();
            switch (menuAdmin) {
                case 1:
                    if (addProducto()) {
                        System.out.println("Producto añadido");
                    }
                    break;
                case 2:
                    if (modProducto()) {
                        System.out.println("Modificaciones aplicadas");
                    }
                    break;
                case 3:
                    if (deleteProducto()) {
                        System.out.println("Producto borrado");
                    }
                    break;
                case 4:
                    if (addUsuario()) {
                        System.out.println("Usuario añadido");
                    }
                    break;
                case 5:
                    if (modUsuario()) {
                        System.out.println("Modificaciones aplicadas");
                    }
                    break;
                case 6:
                    if (deleteUsuario()) {
                        System.out.println("Usuario borrado");
                    }
                    break;
                case 7:
                    System.out.println("Menú cerrado");
                    break;
                default:
                    break;
            }
        } while (menuAdmin != 7);
    }

    public static boolean addProducto() {
        int minPrep;
        double precio;
        String codProducto = null, nomProducto, ingrediente, ingredientes = "", alergeno, alergenos;
        do {
            System.out.println("Introduce nombre producto");
            nomProducto = sc.nextLine();
        } while (nomProducto.length() < 1 || nomProducto.length() > 50);
        do {
            System.out.println("Introduce ingrediente (STOP para parar)");
            ingrediente = sc.nextLine();
            if (ingrediente.equalsIgnoreCase("STOP")) {
                break;
            } else if (ingrediente.length() + ingredientes.length() > 200) {
                System.out.println("No se pudo añadir ingrediente (supera límite de caracteres)");
                ingrediente = "STOP";
                break;
            } else if (ingredientes.length() == 0) {
                ingredientes = ingrediente;
            } else {
                ingredientes = ingredientes + ingrediente;
            }
        } while (!ingrediente.equalsIgnoreCase("STOP"));
        alergenos = "";
        do {
            System.out.println("Introduce alérgeno (STOP para parar)");
            alergeno = sc.nextLine();
            if (alergeno.equalsIgnoreCase("STOP")) {
                break;
            } else if (alergeno.length() + alergenos.length() > 100) {
                System.out.println("No se pudo añadir alergeno (supera límite de caracteres)");
                alergeno = "STOP";
                break;
            } else if (alergenos.length() == 0) {
                alergenos = alergeno;
            } else {
                alergenos = alergenos + alergeno;
            }
        } while (!alergeno.equalsIgnoreCase("STOP"));
        do {
            System.out.println("Introduce precio");
            precio = sc.nextDouble();
        } while (precio < 0.01 || precio > 99.99);
        do {
            System.out.println("Introduce minutos de preparación");
            minPrep = sc.nextInt();
        } while (minPrep < 1 || minPrep > 120);
        sc.nextLine();
        try {
            codProducto = bd.getCodProducto();
            bd.addProducto(new Producto(codProducto, nomProducto, ingredientes, alergenos, precio, minPrep));
            bd.addModProducto(usuLog.getIdUsuario(), codProducto, "Añadido producto " + codProducto, null);
            System.out.println("Producto añadido");
        } catch (ErrorBBDD ex) {
            System.out.println("Error -> " + ex);
            return false;
        }
        return true;
    }

    public static boolean modProducto() {
        boolean val;
        int minPrep;
        double precio;
        String codProducto = "", nomProducto, ingrediente, ingredientes, alergeno, alergenos, menuModProd;
        Vector<Producto> productos = new Vector<Producto>();
        try {
            productos = bd.listadoProductos();
        } catch (ErrorBBDD ex) {
            System.out.println("Error -> " + ex);
            return false;
        }
        for (Producto p : productos) {
            System.out.println(p.toString());
        }
        do {
            val = false;
            System.out.println("Introduce código del producto a modificar");
            codProducto = sc.nextLine();
            for (Producto p : productos) {
                if (p.getCodProducto().equalsIgnoreCase(codProducto)) {
                    val = true;
                    break;
                }
            }
        } while (!val);
        do {
            System.out.println("Introduce atributo a modificar (nomProducto, ingredientes, alergenos, precio, minPrep, stop para cerrar)");
            menuModProd = sc.nextLine();
            switch (menuModProd) {
                case "nomProducto":
                    nomProducto = "";
                    do {
                        System.out.println("Introduce nuevo nom producto");
                        nomProducto = sc.nextLine();
                    } while (nomProducto.length() < 1 || nomProducto.length() > 50);
                    try {
                        bd.modProducto(codProducto, menuModProd, nomProducto);
                        bd.addModProducto(usuLog.getIdUsuario(), codProducto, "Modificación del nombre del producto " + codProducto, "Nuevo nomProducto: " + nomProducto);
                        System.out.println("Producto modificado");
                    } catch (ErrorBBDD ex) {
                        System.out.println("Error -> " + ex);
                    }
                    break;
                case "ingredientes":
                    ingredientes = "";
                    do {
                        System.out.println("Introduce ingrediente (STOP para parar)");
                        ingrediente = sc.nextLine();
                        if (ingrediente.equalsIgnoreCase("STOP")) {
                            break;
                        } else if (ingrediente.length() + ingredientes.length() > 200) {
                            System.out.println("No se pudo añadir ingrediente (supera límite de caracteres)");
                        } else if (ingredientes.length() == 0) {
                            ingredientes = ingrediente;
                        } else {
                            ingredientes = ingredientes + ingrediente;
                        }
                    } while (!ingrediente.equalsIgnoreCase("STOP"));
                    try {
                        bd.modProducto(codProducto, menuModProd, ingredientes);
                        bd.addModProducto(usuLog.getIdUsuario(), codProducto, "Modificación de los ingredientes del producto " + codProducto, null);
                        System.out.println("Producto modificado");
                    } catch (ErrorBBDD ex) {
                        System.out.println("Error -> " + ex);
                    }
                    break;
                case "alergenos":
                    alergenos = "";
                    do {
                        alergenos = "";
                        System.out.println("Introduce alérgeno (STOP para parar)");
                        alergeno = sc.nextLine();
                        if (alergeno.equalsIgnoreCase("STOP")) {
                            break;
                        } else if (alergeno.length() + alergenos.length() > 200) {
                            System.out.println("No se pudo añadir alergeno (supera límite de caracteres)");
                        } else if (alergenos.length() == 0) {
                            alergenos = alergeno;
                        } else {
                            alergenos = alergenos + alergeno;
                        }
                    } while (!alergeno.equalsIgnoreCase("STOP"));
                    try {
                        bd.modProducto(codProducto, menuModProd, alergenos);
                        bd.addModProducto(usuLog.getIdUsuario(), codProducto, "Modificación de los alergenos del producto " + codProducto, null);
                        System.out.println("Producto modificado");
                    } catch (ErrorBBDD ex) {
                        System.out.println("Error -> " + ex);
                    }
                    break;
                case "precio":
                    precio = 0;
                    do {
                        System.out.println("Introduce precio");
                        precio = sc.nextDouble();
                    } while (precio < 0.01 || precio > 99.99);
                    try {
                        bd.modProducto(codProducto, menuModProd, Double.toString(precio));
                        bd.addModProducto(usuLog.getIdUsuario(), codProducto, "Modificación del precio del producto " + codProducto, "Nuevo precio: " + precio);
                        System.out.println("Producto modificado");
                    } catch (ErrorBBDD ex) {
                        System.out.println("Error -> " + ex);
                    }
                    break;
                case "minPrep":
                    minPrep = 0;
                    do {
                        System.out.println("Introduce minutos de preparación");
                        minPrep = sc.nextInt();
                    } while (minPrep < 1 || minPrep > 120);
                    sc.nextLine();
                    try {
                        bd.modProducto(codProducto, menuModProd, Integer.toString(minPrep));
                        bd.addModProducto(usuLog.getIdUsuario(), codProducto, "Modificación de los min. de prep. del producto " + codProducto, "Nuevo minPrep: " + minPrep);
                        System.out.println("Producto modificado");
                    } catch (ErrorBBDD ex) {
                        System.out.println("Error -> " + ex);
                    }
                    break;
                case "stop":
                    System.out.println("Menú cerrado");
                    break;
                default:
                    break;
            }
        } while (!menuModProd.equalsIgnoreCase("stop"));
        return true;
    }

    public static boolean deleteProducto() {
        boolean val;
        String codProducto = null;
        Vector<Producto> productos = new Vector<Producto>();
        try {
            productos = bd.listadoProductos();
        } catch (ErrorBBDD ex) {
            System.out.println("Error -> " + ex);
            return false;
        }
        for (Producto p : productos) {
            System.out.println(p.toString());
        }
        do {
            val = false;
            System.out.println("Introduce código del producto a borrar");
            codProducto = sc.nextLine();
            for (Producto p : productos) {
                if (p.getCodProducto().equalsIgnoreCase(codProducto)) {
                    val = true;
                    break;
                }
            }
        } while (!val);
        try {
            bd.deleteProducto(codProducto);
            bd.addModProducto(usuLog.getIdUsuario(), codProducto, "Borrado producto " + codProducto, null);
            System.out.println("Producto borrado");
        } catch (ErrorBBDD ex) {
            System.out.println("Error -> " + ex);
            return false;
        }
        return true;
    }

    public static boolean addUsuario() {
        String username, clave, dni, correo, nombreApellidos, direccion, idUsu, tipo = null;
        boolean usuExiste, repartidor = false, val;
        Vector<Usuario> usuarios;
        do {
            System.out.println("Introduce DNI");
            dni = sc.nextLine();
        } while (!dni.matches("[0-9]{8}[A-Z]{1}$"));
        do {
            usuExiste = false;
            System.out.println("Introduce username (nombre usuario)");
            username = sc.nextLine();
            try {
                usuarios = bd.listadoUsuarios();
            } catch (ErrorBBDD ex) {
                System.out.println("Error -> " + ex);
                continue;
            }
            for (Usuario u : usuarios) {
                if (u.getNombreUsuario().equalsIgnoreCase(username)) {
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
        } while (!correo.matches(".*@.*[.].*$") || (correo.length() < 1 || correo.length() > 50));
        do {
            System.out.println("Introduce nombre y apellidos");
            nombreApellidos = sc.nextLine();
        } while (nombreApellidos.length() < 1 || nombreApellidos.length() > 50);
        do {
            System.out.println("Introduce dirección");
            direccion = sc.nextLine();
        } while (direccion.length() < 1 || direccion.length() > 50);
        if (usuLog == null) {
            tipo = "C";
        } else if (usuLog.getTipo() == 'G') {
            tipo = "E";
            do {
                val = false;
                try {
                    System.out.println("¿Empleado repartidor? (true/false");
                    repartidor = sc.nextBoolean();
                    val = true;
                } catch (InputMismatchException ex) {
                    val = false;
                }
            } while (!val);
        } else if (usuLog.getTipo() == 'A') {
            do {
                System.out.println("Introduce tipo");
                tipo = sc.nextLine().toUpperCase();
            } while (!tipo.equals("G") && !tipo.equals("E"));
            if (tipo.equals("E")) {
                do {
                    val = false;
                    try {
                        System.out.println("¿Empleado repartidor? (true/false");
                        repartidor = sc.nextBoolean();
                        val = true;
                    } catch (InputMismatchException ex) {
                        val = false;
                    }
                } while (!val);
            }
        }
        try {
            idUsu = bd.getCodUsu(tipo);
        } catch (ErrorBBDD ex) {
            System.out.println("Error -> " + ex);
            return false;
        }
        try {
            bd.addUsuario(new Usuario(idUsu, dni, tipo.charAt(0), username, clave, correo, nombreApellidos, direccion), repartidor);
        } catch (ErrorBBDD ex) {
            System.out.println("Error -> " + ex);
            return false;
        }
        return true;
    }

    public static boolean modUsuario() {
        boolean val, repartidor;
        String idUsuario = "", menuModUsu, correo, direccion;
        Vector<Usuario> usuarios = new Vector<Usuario>();
        try {
            usuarios = bd.listadoUsuarios();
        } catch (ErrorBBDD ex) {
            System.out.println("Error -> " + ex);
            return false;
        }
        for (Usuario u : usuarios) {
            System.out.println(u.toString());
        }
        do {
            val = false;
            System.out.println("Introduce código del producto a modificar");
            idUsuario = sc.nextLine();
            for (Usuario u : usuarios) {
                if (u.getIdUsuario().equalsIgnoreCase(idUsuario)) {
                    if ((usuLog.getTipo() == 'G' && u.getTipo() == 'E')
                            || (usuLog.getTipo() == 'A' && (u.getTipo() == 'E' || u.getTipo() == 'G'))) {
                        val = true;
                    }
                    break;
                }
            }
        } while (!val);
        do {
            System.out.println("Introduce atributo a modificar (correo, direccion, repartidor, stop para cerrar)");
            menuModUsu = sc.nextLine();
            switch (menuModUsu) {
                case "correo":
                    correo = "";
                    do {
                        System.out.println("Introduce correo");
                        correo = sc.nextLine();
                    } while (!correo.matches(".*@.*[.].*$") || (correo.length() < 1 || correo.length() > 50));
                    try {
                        bd.modProducto(idUsuario, menuModUsu, correo);
                        System.out.println("Usuario modificado");
                    } catch (ErrorBBDD ex) {
                        System.out.println("Error -> " + ex);
                    }
                    break;
                case "direccion":
                    direccion = "";
                    do {
                        System.out.println("Introduce dirección");
                        direccion = sc.nextLine();
                    } while (direccion.length() < 1 || direccion.length() > 50);
                    try {
                        bd.modProducto(idUsuario, menuModUsu, direccion);
                        System.out.println("Usuario modificado");
                    } catch (ErrorBBDD ex) {
                        System.out.println("Error -> " + ex);
                    }
                    break;
                case "repartidor":
                    boolean valRepartidor;
                    repartidor = false;
                    for (Usuario u : usuarios) {
                        if (u.getIdUsuario().equalsIgnoreCase(idUsuario) && u.getTipo() == 'E') {
                            do {
                                valRepartidor = false;
                                try {
                                    System.out.println("¿Empleado repartidor? (true/false");
                                    repartidor = sc.nextBoolean();
                                    valRepartidor = true;
                                } catch (InputMismatchException ex) {
                                    valRepartidor = false;
                                }
                            } while (!valRepartidor);
                            try {
                                bd.modProducto(idUsuario, menuModUsu, Boolean.toString(repartidor));
                                System.out.println("Usuario modificado");
                            } catch (ErrorBBDD ex) {
                                System.out.println("Error -> " + ex);
                            }
                        }
                    }
                    break;
                case "stop":
                    System.out.println("Menú cerrado");
                    break;
                default:
                    break;
            }
        } while (!menuModUsu.equalsIgnoreCase("stop"));
        return true;
    }

    public static boolean deleteUsuario() {
        boolean val;
        String codProducto = null, idUsu;
        Vector<Usuario> usuarios = new Vector<Usuario>();
        try {
            usuarios = bd.listadoUsuarios();
        } catch (ErrorBBDD ex) {
            System.out.println("Error -> " + ex);
            return false;
        }
        for (Usuario u : usuarios) {
            System.out.println(u.toString());
        }
        do {
            val = false;
            System.out.println("Introduce código del usuario a borrar");
            idUsu = sc.nextLine();
            for (Usuario u : usuarios) {
                if (u.getIdUsuario().equalsIgnoreCase(idUsu)) {
                    if ((usuLog.getTipo() == 'G' && u.getTipo() == 'E')
                            || (usuLog.getTipo() == 'A' && (u.getTipo() == 'E' || u.getTipo() == 'G'))) {
                        val = true;
                    }
                    break;
                }
            }
        } while (!val);
        try {
            bd.deleteProducto(codProducto);
            System.out.println("Empleado borrado");
        } catch (ErrorBBDD ex) {
            System.out.println("Error -> " + ex);
            return false;
        }
        return true;
    }
}
