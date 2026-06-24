package dao;

import modelo.Participante;
import util.Conexion;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementación concreta del DAO usando JDBC + MySQL.
 *
 * Cada método que interactúa con la BD está dentro de un bloque try-catch
 * para manejar correctamente las excepciones SQLException.
 *
 * Se usa PreparedStatement en todos los casos para evitar SQL Injection.
 */
public class ParticipanteDAOImpl implements ParticipanteDAO {

    // Obtenemos la conexión mediante el Singleton
    private final Connection con = Conexion.getInstancia().getConnection();

    // ================================================================
    //  C – CREAR (INSERT)
    // ================================================================
    @Override
    public boolean crear(Participante p) {
        String sql = """
                INSERT INTO participantes
                    (cedula, nombre, apellido, edad, correo,
                     estado_civil, jornada, categoria, observaciones)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, p.getCedula());
            ps.setString(2, p.getNombre());
            ps.setString(3, p.getApellido());
            ps.setInt   (4, p.getEdad());
            ps.setString(5, p.getCorreo());
            ps.setString(6, p.getEstadoCivil());
            ps.setString(7, p.getJornada());
            ps.setString(8, p.getCategoria());
            ps.setString(9, p.getObservaciones());

            int filas = ps.executeUpdate();
            return filas > 0;

        } catch (SQLException e) {
            System.err.println("Error al crear participante: " + e.getMessage());
            return false;
        }
    }

    // ================================================================
    //  R – LEER (SELECT)
    // ================================================================
    @Override
    public List<Participante> listar() {
        List<Participante> lista = new ArrayList<>();
        String sql = "SELECT * FROM participantes ORDER BY id";

        try (Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                Participante p = new Participante(
                        rs.getInt   ("id"),
                        rs.getString("cedula"),
                        rs.getString("nombre"),
                        rs.getString("apellido"),
                        rs.getInt   ("edad"),
                        rs.getString("correo"),
                        rs.getString("estado_civil"),
                        rs.getString("jornada"),
                        rs.getString("categoria"),
                        rs.getString("observaciones")
                );
                lista.add(p);
            }

        } catch (SQLException e) {
            System.err.println("Error al listar participantes: " + e.getMessage());
        }
        return lista;
    }

    // ================================================================
    //  U – ACTUALIZAR (UPDATE)
    // ================================================================
    @Override
    public boolean actualizar(Participante p) {
        String sql = """
                UPDATE participantes SET
                    cedula       = ?,
                    nombre       = ?,
                    apellido     = ?,
                    edad         = ?,
                    correo       = ?,
                    estado_civil = ?,
                    jornada      = ?,
                    categoria    = ?,
                    observaciones= ?
                WHERE id = ?
                """;
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1,  p.getCedula());
            ps.setString(2,  p.getNombre());
            ps.setString(3,  p.getApellido());
            ps.setInt   (4,  p.getEdad());
            ps.setString(5,  p.getCorreo());
            ps.setString(6,  p.getEstadoCivil());
            ps.setString(7,  p.getJornada());
            ps.setString(8,  p.getCategoria());
            ps.setString(9,  p.getObservaciones());
            ps.setInt   (10, p.getId());

            int filas = ps.executeUpdate();
            return filas > 0;

        } catch (SQLException e) {
            System.err.println("Error al actualizar participante: " + e.getMessage());
            return false;
        }
    }

    // ================================================================
    //  D – ELIMINAR (DELETE)
    // ================================================================
    @Override
    public boolean eliminar(int id) {
        String sql = "DELETE FROM participantes WHERE id = ?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            int filas = ps.executeUpdate();
            return filas > 0;

        } catch (SQLException e) {
            System.err.println("Error al eliminar participante: " + e.getMessage());
            return false;
        }
    }

    // ================================================================
    //  VALIDACIONES DE UNICIDAD
    // ================================================================

    /**
     * Verifica si un correo ya existe en la BD.
     * @param correo     el correo a verificar
     * @param idExcluir  id del participante actual (en actualizar, para no
     *                   compararse consigo mismo). Usar 0 en modo crear.
     */
    @Override
    public boolean correoExiste(String correo, int idExcluir) {
        String sql = "SELECT COUNT(*) FROM participantes WHERE correo = ? AND id != ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, correo);
            ps.setInt   (2, idExcluir);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1) > 0;
        } catch (SQLException e) {
            System.err.println("Error al verificar correo: " + e.getMessage());
        }
        return false;
    }

    /**
     * Verifica si una cédula ya existe en la BD.
     * @param cedula     la cédula a verificar
     * @param idExcluir  id del participante actual. Usar 0 en modo crear.
     */
    @Override
    public boolean cedulaExiste(String cedula, int idExcluir) {
        String sql = "SELECT COUNT(*) FROM participantes WHERE cedula = ? AND id != ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, cedula);
            ps.setInt   (2, idExcluir);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1) > 0;
        } catch (SQLException e) {
            System.err.println("Error al verificar cédula: " + e.getMessage());
        }
        return false;
    }
}
