/*
 * BDConector: Se encarga de abrir y cerrar la base de datos
 */
package bbdd;

import java.sql.*;

public class BDConector {

    private String base;
    private String usuario;
    private String pass;
    private String url;
    protected Connection c;

    /**
     * Constructor del conector a la base de datos
     *
     * @param bbdd nombre de la base de datos
     */
    public BDConector(String bbdd) {
        base = bbdd;
        usuario = "root";
        pass = "";
        url = "jdbc:mysql://localhost/" + base;
    }

    public void abrir() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }
        try {
            c = DriverManager.getConnection(url, usuario, pass);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void cerrar() {
        try {
            c.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
