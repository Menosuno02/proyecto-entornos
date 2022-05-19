package clases;

import java.util.InputMismatchException;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import main.ErrorBBDD;
import static main.Restaurante.bd;
import static main.Restaurante.sc;
import static main.Restaurante.usuLog;

/**
 *
 * @author administrador
 */
public class Usuario {

    private String idUsuario;
    private String dni;
    private char tipo;
    private String nombreUsuario;
    private String clave;
    private String correo;
    private String nombreApellidos;
    private String direccion;

    /**
     * Constructor de un usuario
     *
     * @param idUsuario id del usuario
     * @param dni dni del usuario
     * @param tipo tipo de usuario (C - cliente, E - empleado, G - gestor, A - administrador)
     * @param nombreUsuario nombre/username del usuario
     * @param clave clave/contraseña del usuario
     * @param correo correo electrónico del usuario
     * @param nombreApellidos nombre y apellidos del usuario
     * @param direccion dirección del usuario
     */
    public Usuario(String idUsuario, String dni, char tipo, String nombreUsuario, String clave, String correo, String nombreApellidos, String direccion) {
        this.idUsuario = idUsuario;
        this.dni = dni;
        this.tipo = tipo;
        this.nombreUsuario = nombreUsuario;
        this.clave = clave;
        this.correo = correo;
        this.nombreApellidos = nombreApellidos;
        this.direccion = direccion;
    }

    /**
     * Getter del ID del usuario
     *
     * @return id del usuario
     */
    public String getIdUsuario() {
        return idUsuario;
    }

    /**
     * Getter del DNI del usuario
     *
     * @return dni del usuario
     */
    public String getDni() {
        return dni;
    }

    /**
     * Getter del tipo del usuario
     *
     * @return tipo del usuario
     */
    public char getTipo() {
        return tipo;
    }

    /**
     * Getter del nombre del usuario
     *
     * @return nombre del usuario
     */
    public String getNombreUsuario() {
        return nombreUsuario;
    }

    /**
     * Getter de la clave del usuario
     *
     * @return clave del usuario
     */
    public String getClave() {
        return clave;
    }

    /**
     * Getter del correo electrónico del usuario
     *
     * @return correo del usuario
     */
    public String getCorreo() {
        return correo;
    }

    /**
     * Getter del nombre y apellidos del usuario
     *
     * @return nombre y apellidos del usuario
     */
    public String getNombreApellidos() {
        return nombreApellidos;
    }

    /**
     * Getter de la dirección del usuario
     *
     * @return dirección del usuario
     */
    public String getDireccion() {
        return direccion;
    }

    @Override
    public String toString() {
        return "Usuario{" + "idUsuario=" + idUsuario + ", dni=" + dni + ", tipo=" + tipo + ", nombreUsuario=" + nombreUsuario + ", correo=" + correo + ", nombreApellidos=" + nombreApellidos + ", direccion=" + direccion + '}';
    }

    /**
     * Método que da de alta un usuario, se usa en el inicio de la aplicación para crear clientes (crear usuario) o cuando un gestor o administrador 
     * quiere dar de alta un empleado o gestor (dependiendo del tipo de usuario logeado)
     *
     * @return true si se ha dado de alta el usuario con éxito
     */
    public static boolean addUsuario() {
        String username, clave, dni, correo, nombreApellidos, direccion, idUsu, tipo = null;
        boolean usuExiste, repartidor = false, val;
        Vector<Usuario> usuarios = new Vector<Usuario>();
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
        } else if (usuLog.getTipo() == 'A') {
            do {
                System.out.println("Introduce tipo");
                tipo = sc.nextLine().toUpperCase();
            } while (!tipo.equals("G") && !tipo.equals("E"));
        }
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
        try {
            idUsu = bd.getCodUsu(tipo);
        } catch (ErrorBBDD ex) {
            System.out.println("Error -> " + ex);
            return false;
        }
        try {
            bd.addUsuario(new Usuario(idUsu, dni, tipo.charAt(0), username, clave, correo, nombreApellidos, direccion), repartidor);
            System.out.println("Usuario creado");
        } catch (ErrorBBDD ex) {
            System.out.println("Error -> " + ex);
            return false;
        }
        return true;
    }

    /**
     * Método que comprueba si una contraseña: 
     * - Tiene entre 6 y 30 caracteres 
     * - Posee al menos un número, una minúscula, una mayúscula y un caracter especial 
     * - No posee espacios
     *
     * @param clave la contraseña a validar
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

    /**
     * Método que modifica los atributos de un usuario de la base de datos comprobando si el usuario logeado puede modificar el usuario, 
     * preguntando el atributo que se quieren modificar y validando los datos
     *
     * @return true si se ha modificado el usuario con éxito
     */
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
            System.out.println("Introduce código del usuario a modificar");
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

    /**
     * Método que da de baja un usuario, validando que exista y que el usuario logeado tenga permisos para borrarlo
     *
     * @return true si se ha dado de baja el usuario con éxito
     */
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
            System.out.println("Usuario borrado");
        } catch (ErrorBBDD ex) {
            System.out.println("Error -> " + ex);
            return false;
        }
        return true;
    }

}
