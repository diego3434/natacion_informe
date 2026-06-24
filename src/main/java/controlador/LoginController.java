package controlador;

import dao.UsuarioDAO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

/**
 * Controlador de la ventana Login.
 * Valida credenciales contra la BD y abre la ventana CRUD si son correctas.
 */
public class LoginController {

    @FXML private TextField     txtUsuario;
    @FXML private PasswordField txtClave;

    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    @FXML
    public void onIngresar() {
        String usuario = txtUsuario.getText().trim();
        String clave   = txtClave.getText().trim();

        // Validar campos vacíos
        try {
            if (usuario.isBlank() || clave.isBlank()) {
                throw new IllegalArgumentException("Usuario y contraseña son obligatorios.");
            }

            // Autenticar contra la BD
            if (usuarioDAO.autenticar(usuario, clave)) {
                abrirVentanaCRUD();
            } else {
                alerta(Alert.AlertType.ERROR, "Acceso denegado",
                        "Usuario o contraseña incorrectos.");
            }

        } catch (IllegalArgumentException e) {
            alerta(Alert.AlertType.WARNING, "Campos vacíos", e.getMessage());
        } catch (Exception e) {
            alerta(Alert.AlertType.ERROR, "Error", "Ocurrió un error: " + e.getMessage());
        }
    }

    @FXML
    public void onSalir() {
        Stage stage = (Stage) txtUsuario.getScene().getWindow();
        stage.close();
    }

    /** Abre la ventana principal de CRUD y cierra el Login. */
    private void abrirVentanaCRUD() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/vista/participante.fxml"));
            Parent root = loader.load();

            Stage stageCRUD = new Stage();
            stageCRUD.setTitle("Sistema de Registro – Participantes de Natación");
            stageCRUD.setScene(new Scene(root, 1000, 650));
            stageCRUD.show();

            // Cerrar la ventana de Login
            ((Stage) txtUsuario.getScene().getWindow()).close();

        } catch (Exception e) {
            alerta(Alert.AlertType.ERROR, "Error al abrir ventana",
                    "No se pudo cargar la ventana CRUD: " + e.getMessage());
        }
    }

    private void alerta(Alert.AlertType tipo, String titulo, String mensaje) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
