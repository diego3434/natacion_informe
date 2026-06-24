import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Clase principal – punto de entrada de la aplicación JavaFX.
 * Carga la ventana de Login al iniciar.
 */
public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/vista/login.fxml"));

        primaryStage.setTitle("Login – Sistema de Natación");
        primaryStage.setScene(new Scene(root, 420, 380));
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
