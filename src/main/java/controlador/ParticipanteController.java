package controlador;

import dao.ParticipanteDAO;
import dao.ParticipanteDAOImpl;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import modelo.Participante;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Controlador de la ventana CRUD de Participantes.
 *
 * Maneja:
 *  - Llenar el TableView
 *  - Crear, Actualizar y Eliminar participantes
 *  - Todas las validaciones requeridas
 *  - Sincronización formulario ↔ tabla
 */
public class ParticipanteController implements Initializable {

    // ----------------------------------------------------------------
    // Inyección de controles desde el FXML
    // ----------------------------------------------------------------
    @FXML private TextField    txtCedula;
    @FXML private TextField    txtNombre;
    @FXML private TextField    txtApellido;
    @FXML private TextField    txtEdad;
    @FXML private TextField    txtCorreo;
    @FXML private ComboBox<String> cmbEstadoCivil;
    @FXML private ComboBox<String> cmbCategoria;
    @FXML private RadioButton  rbMatutina;
    @FXML private RadioButton  rbVespertina;
    @FXML private RadioButton  rbNocturna;
    @FXML private ToggleGroup  tgJornada;
    @FXML private TextArea     txtObservaciones;

    @FXML private TableView<Participante>        tabla;
    @FXML private TableColumn<Participante, Integer> colId;
    @FXML private TableColumn<Participante, String>  colCedula;
    @FXML private TableColumn<Participante, String>  colNombre;
    @FXML private TableColumn<Participante, String>  colApellido;
    @FXML private TableColumn<Participante, Integer> colEdad;
    @FXML private TableColumn<Participante, String>  colCorreo;
    @FXML private TableColumn<Participante, String>  colEstadoCivil;
    @FXML private TableColumn<Participante, String>  colJornada;
    @FXML private TableColumn<Participante, String>  colCategoria;

    // ----------------------------------------------------------------
    // DAO y estado interno
    // ----------------------------------------------------------------
    private final ParticipanteDAO dao = new ParticipanteDAOImpl();
    private final ObservableList<Participante> listaObservable = FXCollections.observableArrayList();

    // Guarda el id del participante seleccionado (-1 = ninguno / modo crear)
    private int idSeleccionado = -1;

    // ----------------------------------------------------------------
    // Inicialización (se ejecuta al abrir la ventana)
    // ----------------------------------------------------------------
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarColumnas();
        configurarCombos();
        cargarTabla();
        configurarSeleccionTabla();
    }

    // ----------------------------------------------------------------
    // Configuración inicial
    // ----------------------------------------------------------------

    private void configurarColumnas() {
        colId          .setCellValueFactory(new PropertyValueFactory<>("id"));
        colCedula      .setCellValueFactory(new PropertyValueFactory<>("cedula"));
        colNombre      .setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colApellido    .setCellValueFactory(new PropertyValueFactory<>("apellido"));
        colEdad        .setCellValueFactory(new PropertyValueFactory<>("edad"));
        colCorreo      .setCellValueFactory(new PropertyValueFactory<>("correo"));
        colEstadoCivil .setCellValueFactory(new PropertyValueFactory<>("estadoCivil"));
        colJornada     .setCellValueFactory(new PropertyValueFactory<>("jornada"));
        colCategoria   .setCellValueFactory(new PropertyValueFactory<>("categoria"));
    }

    private void configurarCombos() {
        cmbEstadoCivil.setItems(FXCollections.observableArrayList(
                "Soltero", "Casado", "Divorciado", "Viudo"));

        cmbCategoria.setItems(FXCollections.observableArrayList(
                "Infantil", "Juvenil A", "Juvenil B", "Adulto", "Master"));
    }

    /** Cuando el usuario hace clic en una fila, carga los datos en el formulario. */
    private void configurarSeleccionTabla() {
        tabla.getSelectionModel().selectedItemProperty().addListener(
                (obs, anterior, seleccionado) -> {
                    if (seleccionado != null) {
                        cargarEnFormulario(seleccionado);
                    }
                });
    }

    // ----------------------------------------------------------------
    // CRUD – Crear
    // ----------------------------------------------------------------
    @FXML
    public void onGuardar() {
        if (!validarCampos()) return;

        Participante p = obtenerDatosFormulario();

        // Verificar duplicados (correo y cédula)
        if (dao.correoExiste(p.getCorreo(), 0)) {
            alerta(Alert.AlertType.WARNING, "Correo duplicado",
                    "Ya existe un participante con ese correo electrónico.");
            return;
        }
        if (dao.cedulaExiste(p.getCedula(), 0)) {
            alerta(Alert.AlertType.WARNING, "Cédula duplicada",
                    "Ya existe un participante con esa cédula.");
            return;
        }

        if (dao.crear(p)) {
            alerta(Alert.AlertType.INFORMATION, "Éxito", "Participante registrado correctamente.");
            cargarTabla();
            limpiarFormulario();
        } else {
            alerta(Alert.AlertType.ERROR, "Error", "No se pudo registrar el participante.");
        }
    }

    // ----------------------------------------------------------------
    // CRUD – Actualizar
    // ----------------------------------------------------------------
    @FXML
    public void onActualizar() {
        if (idSeleccionado == -1) {
            alerta(Alert.AlertType.WARNING, "Sin selección",
                    "Selecciona un participante en la tabla para actualizar.");
            return;
        }
        if (!validarCampos()) return;

        Participante p = obtenerDatosFormulario();
        p.setId(idSeleccionado);

        // Verificar duplicados excluyendo el registro actual
        if (dao.correoExiste(p.getCorreo(), idSeleccionado)) {
            alerta(Alert.AlertType.WARNING, "Correo duplicado",
                    "Ese correo ya pertenece a otro participante.");
            return;
        }
        if (dao.cedulaExiste(p.getCedula(), idSeleccionado)) {
            alerta(Alert.AlertType.WARNING, "Cédula duplicada",
                    "Esa cédula ya pertenece a otro participante.");
            return;
        }

        if (dao.actualizar(p)) {
            alerta(Alert.AlertType.INFORMATION, "Éxito", "Participante actualizado correctamente.");
            cargarTabla();
            limpiarFormulario();
        } else {
            alerta(Alert.AlertType.ERROR, "Error", "No se pudo actualizar el participante.");
        }
    }

    // ----------------------------------------------------------------
    // CRUD – Eliminar
    // ----------------------------------------------------------------
    @FXML
    public void onEliminar() {
        if (idSeleccionado == -1) {
            alerta(Alert.AlertType.WARNING, "Sin selección",
                    "Selecciona un participante en la tabla para eliminar.");
            return;
        }

        // Confirmación antes de eliminar
        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Confirmar eliminación");
        confirmacion.setHeaderText("¿Estás seguro?");
        confirmacion.setContentText("Se eliminará permanentemente el participante seleccionado.");

        Optional<ButtonType> resultado = confirmacion.showAndWait();

        if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
            if (dao.eliminar(idSeleccionado)) {
                alerta(Alert.AlertType.INFORMATION, "Éxito", "Participante eliminado.");
                cargarTabla();
                limpiarFormulario();
            } else {
                alerta(Alert.AlertType.ERROR, "Error", "No se pudo eliminar el participante.");
            }
        }
    }

    // ----------------------------------------------------------------
    // Limpiar formulario
    // ----------------------------------------------------------------
    @FXML
    public void onLimpiar() {
        limpiarFormulario();
    }

    // ----------------------------------------------------------------
    // Métodos auxiliares
    // ----------------------------------------------------------------

    /** Carga todos los registros de la BD en la ObservableList → TableView. */
    private void cargarTabla() {
        listaObservable.setAll(dao.listar());
        tabla.setItems(listaObservable);
    }

    /** Rellena el formulario con los datos del participante seleccionado. */
    private void cargarEnFormulario(Participante p) {
        idSeleccionado = p.getId();
        txtCedula      .setText(p.getCedula());
        txtNombre      .setText(p.getNombre());
        txtApellido    .setText(p.getApellido());
        txtEdad        .setText(String.valueOf(p.getEdad()));
        txtCorreo      .setText(p.getCorreo());
        cmbEstadoCivil .setValue(p.getEstadoCivil());
        cmbCategoria   .setValue(p.getCategoria());
        txtObservaciones.setText(p.getObservaciones());

        // Seleccionar el RadioButton de jornada correspondiente
        switch (p.getJornada()) {
            case "Matutina"   -> rbMatutina  .setSelected(true);
            case "Vespertina" -> rbVespertina.setSelected(true);
            case "Nocturna"   -> rbNocturna  .setSelected(true);
        }
    }

    /** Lee los datos del formulario y construye un objeto Participante. */
    private Participante obtenerDatosFormulario() {
        String jornada = "";
        RadioButton rb = (RadioButton) tgJornada.getSelectedToggle();
        if (rb != null) jornada = rb.getText();

        return new Participante(
                txtCedula.getText().trim(),
                txtNombre.getText().trim(),
                txtApellido.getText().trim(),
                Integer.parseInt(txtEdad.getText().trim()),
                txtCorreo.getText().trim(),
                cmbEstadoCivil.getValue(),
                jornada,
                cmbCategoria.getValue(),
                txtObservaciones.getText().trim()
        );
    }

    /** Limpia todos los campos del formulario y reinicia el id seleccionado. */
    private void limpiarFormulario() {
        idSeleccionado = -1;
        txtCedula.clear();
        txtNombre.clear();
        txtApellido.clear();
        txtEdad.clear();
        txtCorreo.clear();
        txtObservaciones.clear();
        cmbEstadoCivil.setValue(null);
        cmbCategoria.setValue(null);
        tgJornada.selectToggle(null);
        tabla.getSelectionModel().clearSelection();
    }

    // ----------------------------------------------------------------
    // Validaciones
    // ----------------------------------------------------------------

    /**
     * Valida todos los campos requeridos antes de crear o actualizar.
     * Muestra un Alert con el primer error encontrado.
     * @return true si todos los campos son válidos.
     */
    private boolean validarCampos() {

        // 1. Campos vacíos
        if (txtCedula.getText().isBlank() || txtNombre.getText().isBlank()
                || txtApellido.getText().isBlank() || txtEdad.getText().isBlank()
                || txtCorreo.getText().isBlank()) {
            alerta(Alert.AlertType.WARNING, "Campos incompletos",
                    "Todos los campos obligatorios deben estar llenos.");
            return false;
        }

        // 2. ComboBox sin selección
        if (cmbEstadoCivil.getValue() == null) {
            alerta(Alert.AlertType.WARNING, "Estado civil",
                    "Selecciona el estado civil.");
            return false;
        }
        if (cmbCategoria.getValue() == null) {
            alerta(Alert.AlertType.WARNING, "Categoría",
                    "Selecciona una categoría.");
            return false;
        }

        // 3. Jornada sin seleccionar
        if (tgJornada.getSelectedToggle() == null) {
            alerta(Alert.AlertType.WARNING, "Jornada",
                    "Selecciona una jornada (Matutina, Vespertina o Nocturna).");
            return false;
        }

        // 4. Cédula: solo números
        if (!txtCedula.getText().matches("\\d+")) {
            alerta(Alert.AlertType.WARNING, "Cédula inválida",
                    "La cédula debe contener solo dígitos numéricos.");
            return false;
        }

        // 5. Edad: solo números y mayor a 5
        String edadStr = txtEdad.getText().trim();
        if (!edadStr.matches("\\d+")) {
            alerta(Alert.AlertType.WARNING, "Edad inválida",
                    "La edad debe contener solo números.");
            return false;
        }
        int edad = Integer.parseInt(edadStr);
        if (edad <= 5) {
            alerta(Alert.AlertType.WARNING, "Edad inválida",
                    "La edad debe ser mayor a 5 años.");
            return false;
        }

        // 6. Correo: debe contener @
        if (!txtCorreo.getText().contains("@")) {
            alerta(Alert.AlertType.WARNING, "Correo inválido",
                    "El correo electrónico debe contener el símbolo @.");
            return false;
        }

        return true; // Todas las validaciones pasaron ✔
    }

    /** Método utilitario para mostrar Alerts de forma concisa. */
    private void alerta(Alert.AlertType tipo, String titulo, String mensaje) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
