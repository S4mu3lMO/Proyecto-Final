package co.edu.uniquindio.finalproyect.application;

import co.edu.uniquindio.finalproyect.model.SistemaHospitalario;
import co.edu.uniquindio.finalproyect.model.TipoUsuario;
import co.edu.uniquindio.finalproyect.model.Usuario;
import co.edu.uniquindio.finalproyect.singleton.SistemaHospitalarioSingleton;
import co.edu.uniquindio.finalproyect.viewController.*;
// No necesitas importar los controladores de sub-vistas aquí directamente
// si los controladores principales los manejan a través de interfaces.

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL; // Importar URL

public class App extends Application {

    private Stage primaryStage;
    private SistemaHospitalario sistemaHospitalario;

    // Ruta base correcta a tus vistas FXML
    private final String VIEWS_BASE_PATH = "/co/edu/uniquindio/finalproyect/views/";

    @Override
    public void start(Stage stage) {
        this.primaryStage = stage;
        this.primaryStage.setTitle("Sistema de Gestión Hospitalaria UniSalud");

        try {
            SistemaHospitalarioSingleton.getInstance().inicializarSistema();
            this.sistemaHospitalario = SistemaHospitalarioSingleton.getInstance().getSistemaHospitalario();

            if (this.sistemaHospitalario == null) {
                System.err.println("Error Crítico: No se pudo inicializar SistemaHospitalario desde el Singleton.");
                mostrarAlertaErrorCritico("Error de Inicialización", "No se pudo iniciar el sistema. Contacte a soporte.");
                return;
            }
            mostrarWelcomeView();
        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlertaErrorCritico("Error Inesperado al Iniciar", "Ocurrió un error fatal al iniciar la aplicación: " + e.getMessage());
        }
    }

    public void mostrarWelcomeView() {
        cargarVista(VIEWS_BASE_PATH + "WelcomeView.fxml", WelcomeViewController.class, null, false, "Bienvenido a UniSalud");
    }

    public void mostrarLoginView() {
        cargarVista(VIEWS_BASE_PATH + "LoginView.fxml", LoginViewController.class, null, false, "Inicio de Sesión - UniSalud");
    }

    public void mostrarDashboard(Usuario usuarioLogueado) {
        if (usuarioLogueado == null) {
            System.err.println("Intento de mostrar dashboard con usuario nulo.");
            mostrarLoginView();
            return;
        }

        String fxmlFile = "";
        String windowTitle = "Dashboard";
        Class<?> controllerClass = null; // Usamos Class<?> para generalizar

        switch (usuarioLogueado.getTipoUsuario()) {
            case ADMINISTRADOR:
                fxmlFile = VIEWS_BASE_PATH + "AdminDashboard.fxml";
                controllerClass = AdministradorViewController.class;
                windowTitle = "Dashboard - Administrador";
                break;
            case MEDICO:
                fxmlFile = VIEWS_BASE_PATH + "MedicoDashboard.fxml";
                controllerClass = MedicoViewController.class;
                windowTitle = "Dashboard - Médico";
                break;
            case PACIENTE:
                fxmlFile = VIEWS_BASE_PATH + "PacienteDashboard.fxml";
                controllerClass = PacienteViewController.class;
                windowTitle = "Dashboard - Paciente";
                break;
            default:
                System.err.println("Tipo de usuario desconocido: " + usuarioLogueado.getTipoUsuario());
                mostrarAlertaErrorCritico("Error de Usuario", "Tipo de usuario no reconocido: " + usuarioLogueado.getTipoUsuario() + ". Contacte a soporte.");
                mostrarLoginView();
                return;
        }
        cargarVista(fxmlFile, controllerClass, usuarioLogueado, true, windowTitle);
    }

    /**
     * Método genérico para cargar vistas FXML.
     */
    private <T> void cargarVista(String fxmlPath, Class<T> controllerExpectedType, Usuario usuarioLogueado, boolean esDashboard, String windowTitleOverride) {
        try {
            URL resourceUrl = App.class.getResource(fxmlPath);
            if (resourceUrl == null) {
                System.err.println("Error Crítico en App.cargarVista: No se encuentra el archivo FXML en la ruta: " + fxmlPath);
                mostrarAlertaErrorCritico("Archivo FXML no encontrado", "No se pudo localizar el archivo: " + fxmlPath + ". Verifique la ruta y la estructura de carpetas en src/main/resources.");
                return;
            }

            FXMLLoader loader = new FXMLLoader(resourceUrl);
            Parent rootLayout = loader.load();
            Scene scene = new Scene(rootLayout);

            Object rawController = loader.getController();

            // Inyectar dependencias y datos
            if (controllerExpectedType.isInstance(rawController)) {
                T controller = controllerExpectedType.cast(rawController);
                if (controller instanceof WelcomeViewController) {
                    ((WelcomeViewController) controller).setApp(this);
                } else if (controller instanceof LoginViewController) {
                    ((LoginViewController) controller).setApp(this);
                } else if (controller instanceof AdministradorViewController) {
                    AdministradorViewController adminCtrl = (AdministradorViewController) controller;
                    adminCtrl.setApp(this);
                    if (usuarioLogueado != null) adminCtrl.setUsuarioLogueado(usuarioLogueado);
                    if (esDashboard) adminCtrl.inicializarDatos();
                } else if (controller instanceof MedicoViewController) {
                    MedicoViewController medicoCtrl = (MedicoViewController) controller;
                    medicoCtrl.setApp(this);
                    if (usuarioLogueado != null) medicoCtrl.setUsuarioLogueado(usuarioLogueado);
                    if (esDashboard) medicoCtrl.inicializarDatos();
                } else if (controller instanceof PacienteViewController) {
                    PacienteViewController pacienteCtrl = (PacienteViewController) controller;
                    pacienteCtrl.setApp(this);
                    if (usuarioLogueado != null) pacienteCtrl.setUsuarioLogueado(usuarioLogueado);
                    if (esDashboard) pacienteCtrl.inicializarDatos();
                }
            } else {
                System.err.println("Error de Casteo: El controlador obtenido de " + fxmlPath + " no es del tipo esperado " + controllerExpectedType.getName());
            }


            primaryStage.setScene(scene);
            if (windowTitleOverride != null) {
                primaryStage.setTitle(windowTitleOverride + " | UniSalud");
            } else if (!esDashboard) {
                primaryStage.setTitle("Sistema de Gestión Hospitalaria UniSalud");
            }

            if (esDashboard) {
                primaryStage.setMaximized(true);
            } else {
                primaryStage.sizeToScene();
            }
            primaryStage.centerOnScreen();
            primaryStage.show();

        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlertaErrorCritico("Error al Cargar Vista (IOException)", "No se pudo cargar la pantalla (" + fxmlPath + "): " + e.getMessage());
        } catch (NullPointerException e) {
            e.printStackTrace();
            mostrarAlertaErrorCritico("Error de Recurso (NullPointer)", "No se encontró el archivo FXML o el controlador es nulo. Ruta esperada: " + fxmlPath);
        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlertaErrorCritico("Error Inesperado al Cargar Vista", "Ocurrió un error al cargar la vista ("+ fxmlPath +"): " + e.getMessage());
        }
    }

    public Stage getPrimaryStage() {
        return this.primaryStage;
    }

    public SistemaHospitalario getSistemaHospitalario() {
        return this.sistemaHospitalario;
    }

    private void mostrarAlertaErrorCritico(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error Crítico - UniSalud");
        alert.setHeaderText(titulo);
        alert.setContentText(mensaje);
        if (primaryStage != null && primaryStage.isShowing()) {
            alert.initOwner(primaryStage);
        }
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
