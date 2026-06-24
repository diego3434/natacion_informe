package dao;

import modelo.Participante;
import java.util.List;

/**
 * Interfaz ParticipanteDAO – define el contrato de las operaciones CRUD.
 *
 * Usar una interfaz permite aplicar el principio de inversión de dependencias:
 * el controlador depende de la interfaz, no de la implementación concreta.
 * Esto también es la base del patrón Factory (se puede cambiar la implementación
 * sin tocar el resto del código).
 */
public interface ParticipanteDAO {

    /** Insertar un nuevo participante. Retorna true si tuvo éxito. */
    boolean crear(Participante p);

    /** Obtener todos los participantes. */
    List<Participante> listar();

    /** Actualizar los datos de un participante existente (por su id). */
    boolean actualizar(Participante p);

    /** Eliminar un participante por su id. */
    boolean eliminar(int id);

    /** Verificar si ya existe un correo en la BD (para validación). */
    boolean correoExiste(String correo, int idExcluir);

    /** Verificar si ya existe una cédula en la BD (para validación). */
    boolean cedulaExiste(String cedula, int idExcluir);
}
