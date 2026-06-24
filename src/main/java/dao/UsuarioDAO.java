package dao;

import util.Conexion;
import java.sql.*;

/**
 * DAO para autenticación de usuarios en la ventana Login.
 */
public class UsuarioDAO {

    private final Connection con = Conexion.getInstancia().getConnection();

    /**
     * Verifica si el usuario y clave ingresados existen en la tabla usuarios.
     * @return true si las credenciales son válidas.
     */
    public boolean autenticar(String usuario, String clave) {
        String sql = "SELECT COUNT(*) FROM usuarios WHERE usuario = ? AND clave = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, usuario);
            ps.setString(2, clave);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1) > 0;
        } catch (SQLException e) {
            System.err.println("Error al autenticar: " + e.getMessage());
        }
        return false;
    }
}