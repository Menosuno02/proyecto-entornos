package bbdd;

import main.ErrorBBDD;
import clases.*;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;

/**
 *
 * @author administrador
 */
public class BDRestaurante extends BDConector {

    private static Statement s;
    private static ResultSet reg;

    /**
     * Constructor de la clase para acceder a la base de datos del restaurante
     *
     * @param file nombre de la base de datos
     */
    public BDRestaurante(String file) {
        super(file);
    }

    /**
     * Método que busca los usuarios de la base de datos y los devuelve mediante un vector
     *
     * @return vector con los usuarios de la base de datos
     * @throws ErrorBBDD error al acceder a la base de datos
     */
    public Vector<Usuario> listadoUsuarios() throws ErrorBBDD {
        Vector<Usuario> usuarios = new Vector<Usuario>();
        String sql = "SELECT * FROM usuarios";
        try {
            this.abrir();
            s = c.createStatement();
            reg = s.executeQuery(sql);
            while (reg.next()) {
                if (reg.getString("tipo").charAt(0) != 'E') {
                    usuarios.add(new Usuario(reg.getString("idUsuario"), reg.getString("dni"), reg.getString("tipo").charAt(0), reg.getString("nombreUsuario"), reg.getString("clave"), reg.getString("correo"), reg.getString("nombreApellidos"), reg.getString("direccion")));
                } else {
                    usuarios.add(new Empleado(reg.getString("idUsuario"), reg.getString("dni"), reg.getString("tipo").charAt(0), reg.getString("nombreUsuario"), reg.getString("clave"), reg.getString("correo"), reg.getString("nombreApellidos"), reg.getString("direccion"), reg.getBoolean("repartidor")));
                }
            }
            s.close();
            this.cerrar();
            return usuarios;
        } catch (SQLException ex) {
            throw new ErrorBBDD("Error listando usuarios");
        }
    }

    /**
     * Método que busca el número de usuarios de un tipo de usuario para retornar el menor código de usuario posible para uno nuevo de ese tipo
     *
     * @param tipoUsu tipo de usuario del que queremos buscar el menor código de usuario
     * @return el código de usuario del nuevo usuario
     * @throws ErrorBBDD error al acceder a la base de datos
     */
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
            throw new ErrorBBDD("Error creando cod usuario");
        }
    }

    /**
     * Método que añade un nuevo usuario a la base de datos
     *
     * @param u el nuevo usuario a añadir
     * @param repartidor si el empleado a añadir puede trabajar como repartidor o no
     * @return true si el usuario se ha añadido con éxito
     * @throws ErrorBBDD error al acceder a la base de datos
     */
    public boolean addUsuario(Usuario u, boolean repartidor) throws ErrorBBDD {
        PreparedStatement ps;
        String sql = "INSERT INTO usuarios VALUES(?,?,?,?,?,?,?,?,?)";
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
            if (u instanceof Empleado) {
                ps.setInt(9, (repartidor ? 1 : 0));
            } else {
                ps.setInt(9, 0);
            }
            ps.executeUpdate(sql);
            ps.close();
            this.cerrar();
            return true;
        } catch (SQLException e) {
            throw new ErrorBBDD("No se pudo dar de alta del usuario");
        }
    }

    /**
     * Método que busca los productos/platos de la base de datos y los devuelve mediante un vector
     *
     * @return vector con los productos de la base de datos
     * @throws ErrorBBDD error al acceder a la base de datos
     */
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

    /**
     * Método que añade un nuevo producto a la cesta de un usuario en la base de datos
     *
     * @param idCliente el id del cliente que añade un producto a su cesta
     * @param codProducto código del producto a añadir
     * @param cantidad cantidad del producto (número de platos)
     * @return true si el producto se añadió a la cesta con éxito
     * @throws ErrorBBDD error al acceder a la base de datos
     */
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

    /**
     * Método que busca los códigos de los productos en la cesta de cierto cliente y los devuelve mediante un vector
     *
     * @param idCliente el id del cliente dueño de la cesta
     * @return vector con los códigos de los productos de la cesta del cliente
     * @throws ErrorBBDD error al acceder a la base de datos
     */
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

    /**
     * Método que quita un producto de la cesta de cierto cliente (sin importar la cantidad)
     *
     * @param idCliente el id del cliente dueño de la cesta
     * @param codProducto el código del producto que queremos retirar de la cesta
     * @return true si el producto se ha retirado de la cesta con éxito
     * @throws ErrorBBDD error al acceder a la base de datos
     */
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

    /**
     * Método que busca los productos de una cesta de cierto cliente y los devuelve mediante un vector
     *
     * @param idCliente el id del cliente dueño de la cesta
     * @return vector con los productos de la cesta
     * @throws ErrorBBDD error al acceder a la base de datos
     */
    public Vector<ProductoCesta> getProductosCesta(String idCliente) throws ErrorBBDD {
        Vector<ProductoCesta> productos = new Vector<ProductoCesta>();
        String sql = "SELECT * FROM productosCesta WHERE idCliente = '" + idCliente + "'";
        try {
            this.abrir();
            s = c.createStatement();
            reg = s.executeQuery(sql);
            while (reg.next()) {
                productos.add(new ProductoCesta(reg.getString("idCliente"), reg.getString("codProducto"), reg.getInt("cantidad")));
            }
            s.close();
            this.cerrar();
            return productos;
        } catch (SQLException ex) {
            throw new ErrorBBDD("Error listando productos de la cesta");
        }
    }

    /**
     * Método que busca y devuelve la tarjeta de cierto cliente, usado para saber si el cliente tiene tarjeta o no
     *
     * @param idCliente el id del cliente del que queremos buscar una tarjeta
     * @return la tarjeta
     * @throws ErrorBBDD error al acceder a la base de datos
     */
    public Tarjeta buscarTarjeta(String idCliente) throws ErrorBBDD {
        Tarjeta tarjeta = null;
        String sql = "SELECT * FROM tarjetas WHERE idCliente = '" + idCliente + "'";
        try {
            this.abrir();
            s = c.createStatement();
            reg = s.executeQuery(sql);
            while (reg.next()) {
                tarjeta = new Tarjeta(reg.getString("numTarjeta"), reg.getInt("ccv"), reg.getString("fechaExpira"));
            }
            s.close();
            this.cerrar();
            return tarjeta;
        } catch (SQLException ex) {
            throw new ErrorBBDD("Error buscando tarjeta");
        }
    }

    /**
     * Método que añade una nueva tarjeta de cierto cliente a la base de datos
     *
     * @param idCliente el id del cliente dueño de la tarjeta
     * @param t la tarjeta que se quiere añadir a la base de datos
     * @return true si se ha añadido la tarjeta a la base de datos con éxito
     * @throws ErrorBBDD error al acceder a la base de datos
     */
    public boolean addTarjeta(String idCliente, Tarjeta t) throws ErrorBBDD {
        PreparedStatement ps;
        String sql = "INSERT INTO tarjetas VALUES (?,?,?,?)";
        try {
            this.abrir();
            ps = c.prepareStatement(sql);
            ps.setString(1, t.getNumTarjeta());
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

    /**
     * Método que busca el número de pedidos en la base de datos para retornar el menor número de pedido posible para uno nuevo
     *
     * @return el código de pedido del nuevo pedido
     * @throws ErrorBBDD error al acceder a la base de datos
     */
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
            codPedido = "P" + Integer.toString(cuenta + 1);
            s.close();
            this.cerrar();
            return codPedido;
        } catch (SQLException ex) {
            throw new ErrorBBDD("Error creando cod pedido");
        }
    }

    /**
     * Método que devuelve la cantidad de platos de cierto producto que cierto dueño tiene en su cesta, usado para calcular el importe 
     * de un nuevo proceso de pedido antes de añadirlo a la base de datos
     *
     * @param idCliente el id del cliente dueño de la cesta
     * @param codProducto el código del producto especificado
     * @return la cantidad de cierto producto en la cesta de cierto cliente
     * @throws ErrorBBDD error al acceder a la base de datos
     */
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

    /**
     * Método que añade un nuevo proceso de cierto pedido a la base de datos, asignando como empleado encargado de realizar el proceso 
     * a uno aleatorio de todos los empleados de la base de datos, como fecha de realización la actual más los minutos de preparación del producto más 
     * una cantidad aleatoria de minutos y como precio el precio del producto multiplicado por la cantidad de platos del producto
     *
     * @param codPedido el código del pedido del que pertenece el proceso
     * @param codProducto el código del producto del proceso
     * @param cantidad la cantidad de platos del producto
     * @return true si se ha añadido un nuevo proceso de pedido a la base de datos con éxito
     * @throws ErrorBBDD error al acceder a la base de datos
     */
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
            throw new ErrorBBDD("Error añadiendo proceso de pedido");
        }
    }

    /**
     * Método que añade un nuevo pedido a la base de datos, asignando como repartidor encargado de repartir el proceso a uno aleatorio de 
     * todos los empleados repartidores de la base de datos, como fecha de entrega la actual más los máximos minutos de preparación de todos los procesos del pedido
     * más una cantidad aleatoria de minutos
     *
     * @param idCliente el id del cliente que ha realizado el pedido
     * @param importeTotal el importe total de todos platos del pedido
     * @param codPedido el código del pedido
     * @return true si se ha añadido un nuevo pedido a la base de datos con éxito
     * @throws ErrorBBDD error al acceder a la base de datos
     */
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
            reg = s.executeQuery(sql);
            while (reg.next()) {
                repartidores.add(reg.getString("idUsuario"));
            }
            idRepartidor = repartidores.get(r.nextInt(repartidores.size()));
            reg = s.executeQuery(sql2);
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
            ps.setTimestamp(5, java.sql.Timestamp.valueOf(LocalDateTime.now().plusMinutes(minPrep + r.nextInt(21))));
            ps.setDouble(6, importeTotal);
            ps.executeUpdate(sql3);
            ps.close();
            this.cerrar();
            return true;
        } catch (SQLException e) {
            throw new ErrorBBDD("No se pudo añadir el pedido");
        }
    }

    /**
     * Método que busca los procesos de pedidos de la base de datos y los devuelve mediante un vector
     *
     * @return vector con los procesos de pedidos
     * @throws ErrorBBDD error al acceder a la base de datos
     */
    public Vector<ProcesoPedido> listadoProcesosPedidos() throws ErrorBBDD {
        Vector<ProcesoPedido> procesos = new Vector<ProcesoPedido>();
        String sql = "SELECT * FROM procesosPedidos";
        try {
            this.abrir();
            s = c.createStatement();
            reg = s.executeQuery(sql);
            while (reg.next()) {
                procesos.add(new ProcesoPedido(reg.getString("codPedido"), reg.getString("idEmple"), reg.getString("codProducto"), reg.getTimestamp("fechaPrep").toLocalDateTime(), reg.getInt("cantidad")));
            }
            s.close();
            this.cerrar();
            return procesos;
        } catch (SQLException ex) {
            throw new ErrorBBDD("Error listando procesos");
        }
    }

    /**
     * Método que busca los pedidos de la base de datos y los devuelve mediante un vector
     *
     * @return vector con los pedidos
     * @throws ErrorBBDD error al acceder a la base de datos
     */
    public Vector<Pedido> listadoPedidos() throws ErrorBBDD {
        Vector<Pedido> pedidos = new Vector<Pedido>();
        String sql = "SELECT * FROM pedidos";
        try {
            this.abrir();
            s = c.createStatement();
            reg = s.executeQuery(sql);
            while (reg.next()) {
                pedidos.add(new Pedido(reg.getString("codPedido"), reg.getString("idCliente"), reg.getString("idRepartidor"), reg.getTimestamp("fechaAlta").toLocalDateTime(), reg.getTimestamp("fechaEntrega").toLocalDateTime(), reg.getDouble("precio")));
            }
            s.close();
            this.cerrar();
            return pedidos;
        } catch (SQLException ex) {
            throw new ErrorBBDD("Error listando pedidos");
        }
    }

    /**
     * Método que busca el número de productos en la base de datos para retornar el menor número de producto posible para uno nuevo
     *
     * @return el código de producto del nuevo producto
     * @throws ErrorBBDD error al acceder a la base de datos
     */
    public String getCodProducto() throws ErrorBBDD {
        int cuenta = 0;
        String codProducto = "";
        String sql = "SELECT COUNT(*) AS cuenta FROM productos";
        try {
            this.abrir();
            s = c.createStatement();
            reg = s.executeQuery(sql);
            while (reg.next()) {
                cuenta = reg.getInt("cuenta");
            }
            codProducto = "PRO" + Integer.toString(cuenta + 1);
            s.close();
            this.cerrar();
            return codProducto;
        } catch (SQLException ex) {
            throw new ErrorBBDD("Error creando cod producto");
        }
    }

    /**
     * Método que añade un nuevo producto/plato a la base de datos
     *
     * @param p el nuevo producto a añadir
     * @return true si se ha añadido el nuevo producto a la base de datos con éxito
     * @throws ErrorBBDD error al acceder a la base de datos
     */
    public boolean addProducto(Producto p) throws ErrorBBDD {
        PreparedStatement ps;
        String sql = "INSERT INTO productos VALUES(?,?,?,?,?,?)";
        try {
            this.abrir();
            ps = c.prepareStatement(sql);
            ps.setString(1, p.getCodProducto());
            ps.setString(2, p.getNomProducto());
            ps.setString(3, p.getIngredientes());
            ps.setString(4, p.getAlergenos());
            ps.setDouble(5, p.getPrecio());
            ps.setInt(6, p.getMinPrep());
            ps.executeUpdate(sql);
            ps.close();
            this.cerrar();
            return true;
        } catch (SQLException e) {
            throw new ErrorBBDD("No se pudo dar de alta del producto");
        }
    }

    /**
     * Método que añade una nueva modificación de un producto realizada por un usuario a la base de datos
     *
     * @param idUsuario el id del usuario que realiza la modificación
     * @param codProducto el código del producto que sufre la modificación
     * @param accion modificación realizada sobre el producto (alta, modificación, baja)
     * @param descripcion descripción de la modificación
     * @return true si se ha añadido la modificación del producto a la base de datos con éxito
     * @throws ErrorBBDD error al acceder a la base de datos
     */
    public boolean addModProducto(String idUsuario, String codProducto, String accion, String descripcion) throws ErrorBBDD {
        PreparedStatement ps;
        String sql = "INSERT INTO modProductos VALUES(?,?,?,?,?)";
        try {
            this.abrir();
            ps = c.prepareStatement(sql);
            ps.setString(1, idUsuario);
            ps.setString(2, codProducto);
            ps.setTimestamp(3, java.sql.Timestamp.valueOf(LocalDateTime.now()));
            ps.setString(4, accion);
            ps.setString(5, descripcion);
            ps.executeUpdate(sql);
            ps.close();
            this.cerrar();
            return true;
        } catch (SQLException e) {
            throw new ErrorBBDD("No se pudo dar de alta la modificación del producto");
        }
    }

    /**
     * Método que cambia el valor de un atributo de un producto de la base de datos
     *
     * @param codProducto el código del producto a modificar
     * @param atributo atributo a modificar del producto
     * @param valor valor del atributo a modificar
     * @return true si se ha modificado el producto con éxito
     * @throws ErrorBBDD error al acceder a la base de datos
     */
    public boolean modProducto(String codProducto, String atributo, String valor) throws ErrorBBDD {
        int nuevoMinPrep = 0;
        double nuevoPrecio = 0;
        PreparedStatement ps;
        if (atributo.equals("precio")) {
            nuevoPrecio = Double.parseDouble(valor);
        } else if (atributo.equals("minPrep")) {
            nuevoMinPrep = Integer.parseInt(valor);
        }
        String sql = "UPDATE productos SET " + atributo + " = ? WHERE codProducto = ?";
        try {
            this.abrir();
            ps = c.prepareStatement(sql);
            if (atributo.equals("precio")) {
                ps.setDouble(1, nuevoPrecio);
            } else if (atributo.equals("minPrep")) {
                ps.setInt(1, nuevoMinPrep);
            } else {
                ps.setString(1, valor);
            }
            ps.setString(2, codProducto);
            ps.executeUpdate(sql);
            ps.close();
            this.cerrar();
            return true;
        } catch (SQLException e) {
            throw new ErrorBBDD("No se pudo modificar el producto");
        }
    }

    /**
     * Método que da de baja un producto/plato de la base de datos
     *
     * @param codProducto el código del producto a borrar
     * @return true si se ha borrado el producto de la base de datos con éxito
     * @throws ErrorBBDD error al acceder a la base de datos
     */
    public boolean deleteProducto(String codProducto) throws ErrorBBDD {
        PreparedStatement ps;
        String sql = "DELETE productos WHERE codProducto = ?";
        try {
            this.abrir();
            ps = c.prepareStatement(sql);
            ps.setString(1, codProducto);
            ps.executeUpdate(sql);
            ps.close();
            this.cerrar();
            return true;
        } catch (SQLException e) {
            throw new ErrorBBDD("No se pudo borrar el producto");
        }
    }

    /**
     * Método que cambia el valor de un atributo de un usuario de la base de datos
     *
     * @param idUsuario el id del usuario a modificar
     * @param atributo atributo a modificar del usuario
     * @param valor valor del atributo a modificar
     * @return true si se ha modificado el usuario de la base de datos con éxito
     * @throws ErrorBBDD error al acceder a la base de datos
     */
    public boolean modUsuario(String idUsuario, String atributo, String valor) throws ErrorBBDD {
        boolean repartidor = false;
        PreparedStatement ps;
        if (atributo.equals("repartidor")) {
            if (valor.equalsIgnoreCase("true")) {
                repartidor = true;
            } else {
                repartidor = false;
            }
        }
        String sql = "UPDATE usuarios SET " + atributo + " = ? WHERE idUsuario = ?";
        try {
            this.abrir();
            ps = c.prepareStatement(sql);
            if (atributo.equals("repartidor")) {
                ps.setInt(1, (repartidor ? 1 : 0));
            } else {
                ps.setString(1, valor);
            }
            ps.setString(2, idUsuario);
            ps.executeUpdate(sql);
            ps.close();
            this.cerrar();
            return true;
        } catch (SQLException e) {
            throw new ErrorBBDD("No se pudo modificar el usuario");
        }
    }

    /**
     * Método que da de baja un usuario de la base de datos
     *
     * @param idUsu el id del usuario a borrar
     * @return true si se ha borrado el usuario de la base de datos con éxito
     * @throws ErrorBBDD error al acceder a la base de datos
     */
    public boolean deleteUsuario(String idUsu) throws ErrorBBDD {
        PreparedStatement ps;
        String sql = "DELETE usuarios where idUsuario = ?";
        try {
            this.abrir();
            ps = c.prepareStatement(sql);
            ps.setString(1, idUsu);
            ps.executeUpdate(sql);
            ps.close();
            this.cerrar();
            return true;
        } catch (SQLException e) {
            throw new ErrorBBDD("No se pudo borrar el usuario");
        }
    }

    /**
     * Método que busca las modificaciones a productos en la base de datos y las devuelve mediante un vector
     *
     * @return vector con las modificaciones hechas a los productos
     * @throws ErrorBBDD error al acceder a la base de datos
     */
    public Vector<ModProducto> listadoModificaciones() throws ErrorBBDD {
        Vector<ModProducto> modificaciones = new Vector<ModProducto>();
        String sql = "SELECT * FROM modProductos";
        try {
            this.abrir();
            s = c.createStatement();
            reg = s.executeQuery(sql);
            while (reg.next()) {
                modificaciones.add(new ModProducto(reg.getString("idUsuario"), reg.getString("codProducto"), reg.getTimestamp("fecha").toLocalDateTime(), reg.getString("accion"), reg.getString("descripcion")));
            }
            s.close();
            this.cerrar();
            return modificaciones;
        } catch (SQLException ex) {
            throw new ErrorBBDD("Error listando modificaciones");
        }
    }
}
