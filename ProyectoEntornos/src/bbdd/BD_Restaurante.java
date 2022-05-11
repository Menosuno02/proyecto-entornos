package bbdd;

import java.sql.*;
import java.util.*;
import proyectoentornos.*;
import clases.*;
import java.time.LocalDateTime;

/**
 *
 * @author administrador
 */
public class BD_Restaurante extends BD_Conector {

    private static Statement s;
    private static ResultSet reg;

    /**
     * 
     * @param file 
     */
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

    public String getCodUsu(String tipoUsu) throws ErrorBBDD {
        int cuenta = 0;
        String codUsu = "";
        String sql = "SELECT COUNT(*) AS cuenta FROM usuarios GROUP BY tipo HAVING tipo = '" + tipoUsu + "'";
        try {
            this.abrir();
            s = c.createStatement();
            reg = s.executeQuery(sql);
            while (reg.next()) {
                cuenta = reg.getInt("cuenta");
            }
            codUsu = tipoUsu + Integer.toString(cuenta + 1);
            s.close();
            this.cerrar();
            return codUsu;
        } catch (SQLException ex) {
            throw new ErrorBBDD("Error creando cod pedido");
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
            throw new ErrorBBDD("No se pudo dar de alta del cliente");
        }
    }

    public Vector<Producto> listadoProductos() throws ErrorBBDD {
        Vector<Producto> productos = new Vector<Producto>();
        String sql = "SELECT * FROM productos";
        try {
            this.abrir();
            s = c.createStatement();
            reg = s.executeQuery(sql);
            while (reg.next()) {
                productos.add(new Producto(reg.getString("codProducto"), reg.getString("nomProducto"), reg.getString("ingredientes"), reg.getString("alergenos"), reg.getDouble("precio"), reg.getInt("minPrep")));
            }
            s.close();
            this.cerrar();
            return productos;
        } catch (SQLException ex) {
            throw new ErrorBBDD("Error listando productos");
        }
    }

    public boolean addProductoCesta(String idCliente, String codProducto, int cantidad) throws ErrorBBDD {
        PreparedStatement ps;
        String sql = "INSERT INTO productosCesta VALUES (?,?,?)";
        try {
            this.abrir();
            ps = c.prepareStatement(sql);
            ps.setString(1, idCliente);
            ps.setString(2, codProducto);
            ps.setInt(3, cantidad);
            ps.executeUpdate(sql);
            ps.close();
            this.cerrar();
            return true;
        } catch (SQLException e) {
            throw new ErrorBBDD("No se pudo añadir el producto a la cesta ");
        }
    }

    public Vector<String> getCodProductosCesta(String idCliente) throws ErrorBBDD {
        Vector<String> productos = new Vector<String>();
        String sql = "SELECT * FROM productosCesta WHERE idCliente = '" + idCliente + "'";
        try {
            this.abrir();
            s = c.createStatement();
            reg = s.executeQuery(sql);
            while (reg.next()) {
                productos.add(reg.getString("codProducto"));
            }
            s.close();
            this.cerrar();
            return productos;
        } catch (SQLException ex) {
            throw new ErrorBBDD("Error listando productos de la cesta");
        }
    }

    public boolean quitarProductoCesta(String idCliente, String codProducto) throws ErrorBBDD {
        PreparedStatement ps;
        String sql = "DELETE productosCesta WHERE idCliente = ? AND codProducto = ?";
        try {
            this.abrir();
            ps = c.prepareStatement(sql);
            ps.setString(1, idCliente);
            ps.setString(2, codProducto);
            ps.executeUpdate(sql);
            ps.close();
            this.cerrar();
            return true;
        } catch (SQLException e) {
            throw new ErrorBBDD("No se pudo borrar el producto de la cesta");
        }
    }

    public Vector<String> getProductosCesta(String idCliente) throws ErrorBBDD {
        Vector<String> productos = new Vector<String>();
        String sql = "SELECT * FROM productosCesta WHERE idCliente = '" + idCliente + "'";
        try {
            this.abrir();
            s = c.createStatement();
            reg = s.executeQuery(sql);
            while (reg.next()) {
                productos.add("Producto " + reg.getString("codProducto") + ", cantidad: " + reg.getInt("cantidad"));
            }
            s.close();
            this.cerrar();
            return productos;
        } catch (SQLException ex) {
            throw new ErrorBBDD("Error listando productos de la cesta");
        }
    }

    public Tarjeta buscarTarjeta(String idCliente) throws ErrorBBDD {
        Tarjeta tarjeta = null;
        String sql = "SELECT * FROM tarjetas WHERE idCliente = '" + idCliente + "'";
        try {
            this.abrir();
            s = c.createStatement();
            reg = s.executeQuery(sql);
            while (reg.next()) {
                tarjeta = new Tarjeta(reg.getInt("numTarjeta"), reg.getInt("ccv"), reg.getString("fechaExpira"));
            }
            s.close();
            this.cerrar();
            return tarjeta;
        } catch (SQLException ex) {
            throw new ErrorBBDD("Error buscando tarjeta");
        }
    }

    public boolean addTarjeta(String idCliente, Tarjeta t) throws ErrorBBDD {
        PreparedStatement ps;
        String sql = "INSERT INTO tarjetas VALUES (?,?,?,?)";
        try {
            this.abrir();
            ps = c.prepareStatement(sql);
            ps.setInt(1, t.getNumTarjeta());
            ps.setInt(2, t.getCcv());
            ps.setString(3, t.getFechaCaducidad());
            ps.setString(4, idCliente);
            ps.executeUpdate(sql);
            ps.close();
            this.cerrar();
            return true;
        } catch (SQLException e) {
            throw new ErrorBBDD("No se pudo añadir la tarjeta");
        }
    }

    public String getCodPedido() throws ErrorBBDD {
        int cuenta = 0;
        String codPedido = "";
        String sql = "SELECT COUNT(*) AS cuenta FROM pedidos";
        try {
            this.abrir();
            s = c.createStatement();
            reg = s.executeQuery(sql);
            while (reg.next()) {
                cuenta = reg.getInt("cuenta");
            }
            codPedido = "PE" + (cuenta + 1);
            s.close();
            this.cerrar();
            return codPedido;
        } catch (SQLException ex) {
            throw new ErrorBBDD("Error creando cod pedido");
        }
    }

    public int getCantidadCesta(String idCliente, String codProducto) throws ErrorBBDD {
        int cantidad = 0;
        String sql = "SELECT cantidad FROM productosCesta WHERE idCliente = '" + idCliente + "'"
                + " AND codProducto = '" + codProducto + "'";
        try {
            this.abrir();
            s = c.createStatement();
            reg = s.executeQuery(sql);
            while (reg.next()) {
                cantidad = reg.getInt("cantidad");
            }
            s.close();
            this.cerrar();
            return cantidad;
        } catch (SQLException ex) {
            throw new ErrorBBDD("Error buscando cantidad");
        }
    }

    public double addProcesoPedido(String codPedido, String codProducto, int cantidad) throws ErrorBBDD {
        PreparedStatement ps;
        Random r = new Random();
        int minPrep = 0;
        double precio = 0;
        String idEmple;
        Vector<String> empleados = new Vector<String>();
        String sql = "SELECT * FROM usuarios WHERE tipo = 'E'";
        String sql2 = "SELECT * FROM productos WHERE codProducto = '" + codProducto + "'";
        try {
            s = c.createStatement();
            reg = s.executeQuery(sql);
            while (reg.next()) {
                empleados.add(reg.getString("idUsuario"));
            }
            idEmple = empleados.get(r.nextInt(empleados.size()));
            s = c.createStatement();
            reg = s.executeQuery(sql2);
            while (reg.next()) {
                minPrep = reg.getInt("minPrep");
                precio = reg.getDouble("precio");
            }
            precio = precio * cantidad;
            s.close();
            String sql3 = "INSERT INTO procesosPedido VALUES(?,?,?,?,?)";
            ps = c.prepareStatement(sql3);
            ps.setString(1, codPedido);
            ps.setString(2, idEmple);
            ps.setString(3, codProducto);
            ps.setTimestamp(4, java.sql.Timestamp.valueOf(LocalDateTime.now().plusMinutes(minPrep + r.nextInt(11))));
            ps.setInt(5, cantidad);
            ps.executeUpdate(sql3);
            ps.close();
            this.cerrar();
            return precio;
        } catch (SQLException ex) {
            throw new ErrorBBDD("Error buscando cantidad");
        }
    }

    public boolean addPedido(String idCliente, double importeTotal, String codPedido) throws ErrorBBDD {
        int minPrep = 0;
        Random r = new Random();
        Vector<String> repartidores = new Vector<String>();
        String idRepartidor;
        String sql = "SELECT * FROM usuarios WHERE tipo = 'E' AND repartidor = 1";
        String sql2 = "SELECT MAX(minPrep) AS max FROM productos WHERE codProducto IN"
                + " (SELECT codProducto FROM procesosPedido WHERE codPedido = '" + codPedido + "')";
        PreparedStatement ps;
        try {
            this.abrir();
            s = c.createStatement();
            reg = s.executeQuery(sql2);
            while (reg.next()) {
                repartidores.add(reg.getString("idUsuario"));
            }
            idRepartidor = repartidores.get(r.nextInt(repartidores.size()));
            s = c.createStatement();
            reg = s.executeQuery(sql2);
            while (reg.next()) {
                repartidores.add(reg.getString("idUsuario"));
            }
            idRepartidor = repartidores.get(r.nextInt(repartidores.size()));
            reg = s.executeQuery(sql);
            while (reg.next()) {
                minPrep = reg.getInt("max");
            }
            s.close();
            String sql3 = "INSERT INTO pedidos VALUES (?,?,?,?,?,?)";
            ps = c.prepareStatement(sql3);
            ps.setString(1, codPedido);
            ps.setString(2, idCliente);
            ps.setString(3, idRepartidor);
            ps.setTimestamp(4, java.sql.Timestamp.valueOf(LocalDateTime.now()));
            ps.setTimestamp(5, java.sql.Timestamp.valueOf(LocalDateTime.now().plusMinutes(minPrep + r.nextInt(11))));
            ps.setDouble(6, importeTotal);
            ps.executeUpdate(sql3);
            ps.close();
            this.cerrar();
            return true;
        } catch (SQLException e) {
            throw new ErrorBBDD("No se pudo añadir el pedido");
        }
    }
}
