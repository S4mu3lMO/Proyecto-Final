package co.edu.uniquindio.finalproyect.application;

import co.edu.uniquindio.finalproyect.model.SistemaHospitalario;
import co.edu.uniquindio.finalproyect.model.Usuario;
import co.edu.uniquindio.finalproyect.model.TipoUsuario;
import co.edu.uniquindio.finalproyect.singleton.SistemaHospitalarioSingleton;
import co.edu.uniquindio.finalproyect.viewController.*; // Importa todos los view controllers
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane; // O el tipo de layout que uses
import javafx.scene.layout.BorderPane; // Para un layout más flexible en dashboards
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {

    private Stage primaryStage;
    private SistemaHospitalario sistemaHospitalario;

    @Override
    public void start(Stage stage) throws IOException {
        this.primaryStage = stage;
        this.primaryStage.setTitle("Sistema de Gestión Hospitalaria");

        // Inicializar el Singleton y obtener la instancia del sistema
        SistemaHospitalarioSingleton.getInstance().inicializarSistema();
        this.sistemaHospitalario = SistemaHospitalarioSingleton.getInstance().getSistemaHospitalario();

        mostrarWelcomeView();
    }

    public void mostrarWelcomeView() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/views/WelcomeView.fxml"));
            AnchorPane welcomeLayout = loader.load();
            Scene scene = new Scene(welcomeLayout);

            WelcomeViewController controller = loader.getController();
            controller.setApp(this);

            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error al cargar WelcomeView.fxml: " + e.getMessage());
        }
    }

    public void mostrarLoginView() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/views/LoginView.fxml"));
            AnchorPane rootLayout = loader.load(); // Ajusta si tu FXML usa otro layout raíz
            Scene scene = new Scene(rootLayout);

            LoginViewController loginViewController = loader.getController();
            loginViewController.setApp(this);

            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error al cargar LoginView.fxml: " + e.getMessage());
        }
    }

    public void mostrarDashboard(Usuario usuarioLogueado) {
        if (usuarioLogueado == null) {
            System.err.println("No se puede mostrar el dashboard, usuario nulo.");
            mostrarLoginView(); // Volver al login si hay un problema
            return;
        }

        String fxmlFile = "";
        Object controllerInstance = null;

        try {
            FXMLLoader loader = new FXMLLoader();
            TipoUsuario tipoUsuario = usuarioLogueado.getTipoUsuario();

            switch (tipoUsuario) {
                case ADMINISTRADOR:
                    fxmlFile = "/views/AdminDashboard.fxml";
                    loader.setLocation(getClass().getResource(fxmlFile));
                    BorderPane adminLayout = loader.load(); // Usar BorderPane o el layout que elijas
                    Scene adminScene = new Scene(adminLayout);
                    primaryStage.setScene(adminScene);
                    AdministradorViewController adminController = loader.getController();
                    adminController.setApp(this);
                    adminController.setUsuarioLogueado(usuarioLogueado);
                    adminController.inicializarDatos(); // Nuevo método para cargar datos específicos
                    controllerInstance = adminController;
                    break;
                case MEDICO:
                    fxmlFile = "/views/MedicoDashboard.fxml";
                    loader.setLocation(getClass().getResource(fxmlFile));
                    BorderPane medicoLayout = loader.load();
                    Scene medicoScene = new Scene(medicoLayout);
                    primaryStage.setScene(medicoScene);
                    MedicoViewController medicoController = loader.getController();
                    medicoController.setApp(this);
                    medicoController.setUsuarioLogueado(usuarioLogueado);
                    medicoController.inicializarDatos();
                    controllerInstance = medicoController;
                    break;
                case PACIENTE:
                    fxmlFile = "/views/PacienteDashboard.fxml";
                    loader.setLocation(getClass().getResource(fxmlFile));
                    BorderPane pacienteLayout = loader.load();
                    Scene pacienteScene = new Scene(pacienteLayout);
                    primaryStage.setScene(pacienteScene);
                    PacienteViewController pacienteController = loader.getController();
                    pacienteController.setApp(this);
                    pacienteController.setUsuarioLogueado(usuarioLogueado);
                    pacienteController.inicializarDatos();
                    controllerInstance = pacienteController;
                    break;
                default:
                    System.err.println("Tipo de usuario desconocido: " + tipoUsuario);
                    mostrarLoginView();
                    return;
            }

            primaryStage.setTitle("Dashboard - " + tipoUsuario.name());
            primaryStage.setMaximized(true); // Opcional: maximizar la ventana
            primaryStage.show();

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error al cargar " + fxmlFile + ": " + e.getMessage());
        }
    }

    public SistemaHospitalario getSistemaHospitalario() {
        return sistemaHospitalario;
    }

    public static void main(String[] args) {
        launch(args);
    }
    public Stage getPrimaryStage() {
        return this.primaryStage;
    }

}