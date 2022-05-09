package bbdd;

import java.sql.*;
import java.util.*;
import java.time.*;
import proyectoentornos.*;
import clases.*;

/**
 *
 * @author administrador
 */
public class BD_Restaurante extends BD_Conector {

    private static Statement s;
    private static ResultSet reg;

    public BD_Restaurante(String file) {
        super(file);
    }

    public Vector<Usuario> listadoUsuarios() throws ErrorBBDD {
        Vector<Usuario> usuarios = new Vector<Usuario>();
        String sql = "SELECT * FROM usuarios";
        try {
            this.abrir();
            s = c.createStatement();
            reg = s.executeQuery(sql);
            while (reg.next()) {
                usuarios.add(new Usuario(reg.getString("idUsuario"), reg.getString("dni"), reg.getString("tipo").charAt(0), reg.getString("nombreUsuario"), reg.getString("clave"), reg.getString("correo"), reg.getString("nombreApellidos"), reg.getString("direccion"), reg.getBoolean("repartidor")));
            }
            s.close();
            this.cerrar();
            return usuarios;
        } catch (SQLException ex) {
            throw new ErrorBBDD("Error listando usuarios");
        }
    }

    public boolean addCliente(Usuario u) throws ErrorBBDD {
        PreparedStatement ps;
        String sql = "INSERT INTO tarjetas VALUES(?,?,'C',?,?,?,?,?,0)";
        try {
            this.abrir();
            ps = c.prepareStatement(sql);
            ps.setString(1, u.getIdUsuario());
            ps.setString(2, u.getDni());
            ps.setString(3, Character.toString(u.getTipo()));
            ps.setString(4, u.getNombreUsuario());
            ps.setString(5, u.getClave());
            ps.setString(6, u.getCorreo());
            ps.setString(7, u.getNombreApellidos());
            ps.setString(8, u.getDireccion());
            ps.setInt(9, u.isRepartidor() ? 1 : 0);
            ps.executeUpdate(sql);
            ps.close();
            this.cerrar();
            return true;
        } catch (SQLException e) {
            throw new ErrorBBDD("No se puede realizar el alta");
        }
    }
}
