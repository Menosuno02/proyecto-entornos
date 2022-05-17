package clases;

import java.util.Vector;
import proyectoentornos.ErrorBBDD;
import static proyectoentornos.ProyectoEntornos.bd;
import static proyectoentornos.ProyectoEntornos.sc;
import static proyectoentornos.ProyectoEntornos.usuLog;

/**
 *
 * @author administrador
 */
public class Producto {

    private String codProducto;
    private String nomProducto;
    private String ingredientes;
    private String alergenos;
    private double precio;
    private int minPrep;

    /**
     * Constructor de un producto
     *
     * @param codProducto código del producto
     * @param nomProducto nombre del producto
     * @param ingredientes ingredientes del producto (seguidos y por comas)
     * @param alergenos alérgenos del producto (seguidos y por comas)
     * @param precio precio del producto
     * @param minPrep minutos de preparación mínimos del producto
     */
    public Producto(String codProducto, String nomProducto, String ingredientes, String alergenos, double precio, int minPrep) {
        this.codProducto = codProducto;
        this.nomProducto = nomProducto;
        this.ingredientes = ingredientes;
        this.alergenos = alergenos;
        this.precio = precio;
        this.minPrep = minPrep;
    }

    /**
     * Getter del código del producto
     *
     * @return código del producto
     */
    public String getCodProducto() {
        return codProducto;
    }

    /**
     * Getter del nombre del producto
     *
     * @return nombre del producto
     */
    public String getNomProducto() {
        return nomProducto;
    }

    /**
     * Getter de los ingredientes del producto
     *
     * @return ingredientes del producto
     */
    public String getIngredientes() {
        return ingredientes;
    }

    /**
     * Getter de los alérgenos del producto
     *
     * @return alérgenos del producto
     */
    public String getAlergenos() {
        return alergenos;
    }

    /**
     * Getter del precio del producto
     *
     * @return precio del producto
     */
    public double getPrecio() {
        return precio;
    }

    /**
     * Getter de los minutos de preparación del producto
     *
     * @return minutos de preparación del producto
     */
    public int getMinPrep() {
        return minPrep;
    }

    @Override
    public String toString() {
        return "Producto{" + "codProducto=" + codProducto + ", nomProducto=" + nomProducto + ", ingredientes=" + ingredientes + ", alergenos=" + alergenos + ", precio=" + precio + ", minPrep=" + minPrep + '}';
    }

    /**
     * Método para añadir un producto a la base de datos verificando los datos del producto y registrando la modificación realizada (alta)
     *
     * @return true si se ha dado de alta el producto con éxito
     */
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

    /**
     * Método que modifica los atributos de un producto de la base de datos preguntando el atributo, validando los datos y registrando la modificación realizada (modificación)
     *
     * @return true si se ha modificado el producto con éxito
     */
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

    /**
     * Método que da de baja un producto validando que exista y registrando la modificación realizada (baja)
     *
     * @return true si se ha dado de baja el producto con éxito
     */
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

}
