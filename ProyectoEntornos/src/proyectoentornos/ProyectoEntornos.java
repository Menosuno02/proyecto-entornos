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
import java.util.Scanner;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author administrador
 */
public class ProyectoEntornos {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        BD_Restaurante bd = new BD_Restaurante("restaurante");
        Scanner sc = new Scanner(System.in);
        sc.useLocale(Locale.ENGLISH);

        String usuario, clave, dni, correo, nombreApellidos, direccion;
        boolean usuCon, usuExiste;
        Usuario usuLog = null;
        Vector<Usuario> usuarios;
        int menúInicio;
        System.out.println("1. Crear usuario\n2. Iniciar sesión\n3. Salir");
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
                            }
                        }
                    } while (usuExiste);
                    do {
                        System.out.println("Introduce clave");
                        clave = sc.nextLine();
                    } while (clave.length() < 4);
                    do {
                        System.out.println("Introduce correo");
                        correo = sc.nextLine();
                    } while (!correo.contains("@"));
                    System.out.println("Introduce nombre y apellidos");
                    nombreApellidos = sc.nextLine();
                    System.out.println("Introduce dirección");
                    direccion = sc.nextLine();
                    try {
                        bd.addCliente(new Usuario("a", dni, 'C', usuario, clave, correo, nombreApellidos, direccion, false));
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

}
