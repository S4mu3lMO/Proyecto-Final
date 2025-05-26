package co.edu.uniquindio.finalproyect.application;

import co.edu.uniquindio.finalproyect.singleton.SistemaHospitalarioSingleton;
import co.edu.uniquindio.finalproyect.viewController.LoginViewController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane; // O el tipo de layout que uses en tu FXML de login
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {

    private Stage primaryStage; // La ventana principal de la aplicación

    @Override
    public void start(Stage stage) throws IOException {
        this.primaryStage = stage;
        this.primaryStage.setTitle("Sistema de Gestión Hospitalaria");

        // 1. Inicializar el Singleton de tu sistema
        // Es crucial que esto se haga una sola vez al inicio de la aplicación
        SistemaHospitalarioSingleton.getInstance().inicializarSistema();

        // 2. Cargar la vista de login
        mostrarLoginView();
    }

    /**
     * Muestra la vista de inicio de sesión.
     */
    public void mostrarLoginView() {
        try {
            FXMLLoader loader = new FXMLLoader();
            // La ruta al FXML es crucial: /views/LoginView.fxml
            loader.setLocation(getClass().getResource("/views/LoginView.fxml"));

            // Carga el layout raíz de tu LoginView.fxml (puede ser AnchorPane, VBox, etc.)
            AnchorPane rootLayout = (AnchorPane) loader.load();
            Scene scene = new Scene(rootLayout);

            // Obtiene el controlador de la vista de login
            LoginViewController loginViewController = loader.getController();
            // Le pasa una referencia a esta clase 'App' para que el controlador pueda cambiar la vista
            loginViewController.setApp(this);

            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (IOException e) {
            e.printStackTrace();
            // Considera usar un Alert para informar al usuario si no se puede cargar la vista
            System.err.println("Error al cargar LoginView.fxml: " + e.getMessage());
        }
    }

    /**
     * Muestra la vista principal de la aplicación después de un login exitoso.
     */
    public void mostrarMainView() {
        try {
            FXMLLoader loader = new FXMLLoader();
            // Asume que tienes una vista principal llamada MainView.fxml
            loader.setLocation(getClass().getResource("/views/MainView.fxml"));

            // Carga el layout raíz de tu MainView.fxml (puede ser BorderPane, VBox, etc.)
            AnchorPane mainLayout = (AnchorPane) loader.load(); // Ajusta el tipo de layout si es diferente
            Scene scene = new Scene(mainLayout);

            // Opcional: Si tu MainViewController necesita la instancia de App o del sistema
            // MainViewController mainViewController = loader.getController();
            // mainViewController.setApp(this);
            // mainViewController.setSistemaHospitalario(SistemaHospitalarioSingleton.getInstance().getSistemaHospitalario());

            primaryStage.setScene(scene);
            primaryStage.setMaximized(true); // Opcional: maximizar la ventana principal
            primaryStage.show();

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error al cargar MainView.fxml: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
