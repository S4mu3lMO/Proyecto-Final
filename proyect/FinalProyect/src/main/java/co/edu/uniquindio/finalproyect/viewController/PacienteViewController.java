package co.edu.uniquindio.finalproyect.viewController;

import co.edu.uniquindio.finalproyect.application.App;
import co.edu.uniquindio.finalproyect.model.Paciente;
import co.edu.uniquindio.finalproyect.model.SistemaHospitalario;
import co.edu.uniquindio.finalproyect.model.Usuario;
import co.edu.uniquindio.finalproyect.viewController.pacienteSubViews.PacienteSubViewControllerBase; // Importar la nueva interfaz

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage; // Para initOwner en Alerta

import java.io.IOException;

public class PacienteViewController {
    private App mainApp;
    private SistemaHospitalario sistemaHospitalario;
    private Paciente pacienteLogueado;

    @FXML private BorderPane mainPacientePanel;
    @FXML private Label lblNombrePacienteMenu;

    // Botones del menú
    @FXML private Button btnMisDatosPersonales;
    @FXML private Button btnGestionarMisCitas;
    @FXML private Button btnVerMiHistorial;
    @FXML private Button btnVerMisNotificaciones;
    @FXML private Button btnCerrarSesionPaciente;


    public void setApp(App mainApp) {
        this.mainApp = mainApp;
        if (this.mainApp != null) {
            this.sistemaHospitalario = this.mainApp.getSistemaHospitalario();
        }
    }

    public void setUsuarioLogueado(Usuario usuarioLogueado) {
        if (usuarioLogueado instanceof Paciente) {
            this.pacienteLogueado = (Paciente) usuarioLogueado;
            if (lblNombrePacienteMenu != null) {
                lblNombrePacienteMenu.setText("Paciente: " + this.pacienteLogueado.getNombre());
            }
        } else {
            // Manejar caso improbable de que no sea un paciente
            if (lblNombrePacienteMenu != null) {
                lblNombrePacienteMenu.setText("Paciente: No Identificado");
            }
            // Podrías deshabilitar botones o redirigir
        }
    }

    public Paciente getPacienteLogueado() {
        return pacienteLogueado;
    }

    public void inicializarDatos(){
        if (pacienteLogueado == null) {
            mostrarAlerta("Error de Sesión", "No se pudo identificar al paciente.", "Por favor, inicie sesión de nuevo.", Alert.AlertType.ERROR);
            if (mainApp != null) mainApp.mostrarLoginView();
            return;
        }
        System.out.println("Dashboard de Paciente inicializado para: " + pacienteLogueado.getNombre());
        // Cargar una vista por defecto, por ejemplo, la de gestionar citas
        if (btnGestionarMisCitas != null) {
            handleGestionarMisCitas(new ActionEvent());
        }
    }

    @FXML
    void initialize() {
        // Método llamado por FXMLLoader
    }

    @FXML
    void handleMisDatosPersonales(ActionEvent event) {
        System.out.println("Cargando Mis Datos Personales...");
        // Ruta al FXML para actualizar datos del paciente
        cargarSubVistaPaciente("/views/PacienteDatosPersonalesView.fxml");
    }

    @FXML
    void handleGestionarMisCitas(ActionEvent event) {
        System.out.println("Cargando Gestión de Mis Citas...");
        // Ruta al FXML para solicitar/cancelar citas
        cargarSubVistaPaciente("/views/PacienteGestionCitasView.fxml");
    }

    @FXML
    void handleVerMiHistorial(ActionEvent event) {
        System.out.println("Cargando Mi Historial Médico...");
        // Ruta al FXML para ver historial médico
        cargarSubVistaPaciente("/views/PacienteVerHistorialView.fxml");
    }

    @FXML
    void handleVerMisNotificaciones(ActionEvent event) {
        System.out.println("Cargando Mis Notificaciones...");
        // Ruta al FXML para ver notificaciones
        cargarSubVistaPaciente("/views/PacienteNotificacionesView.fxml");
    }

    @FXML
    void handleCerrarSesionPaciente(ActionEvent event) {
        if (mainApp != null) {
            mainApp.mostrarLoginView();
        }
    }

    private void cargarSubVistaPaciente(String fxmlPath) {
        if (pacienteLogueado == null) {
            mostrarAlerta("Error de Sesión", "Paciente no identificado.", "No se puede cargar la sección. Por favor, reinicie sesión.", Alert.AlertType.WARNING);
            return;
        }
        try {
            if (getClass().getResource(fxmlPath) == null) {
                System.err.println("Error Crítico: No se encuentra el archivo FXML en la ruta: " + fxmlPath);
                mostrarAlerta("Error Fatal de Carga", "Archivo FXML no encontrado.", "No se pudo localizar el archivo: " + fxmlPath + ". Verifique la ruta.", Alert.AlertType.ERROR);
                return;
            }

            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Node subVista = loader.load();

            Object controller = loader.getController();
            if (controller instanceof PacienteSubViewControllerBase) {
                PacienteSubViewControllerBase subController = (PacienteSubViewControllerBase) controller;
                subController.setMainApp(mainApp);
                subController.setSistemaHospitalario(sistemaHospitalario);
                subController.setPacienteLogueado(pacienteLogueado);
                subController.inicializarDatosSubVistaPaciente();
            } else {
                System.out.println("Advertencia: El controlador para " + fxmlPath + " no implementa PacienteSubViewControllerBase.");
            }

            if (mainPacientePanel != null) {
                mainPacientePanel.setCenter(subVista);
            } else {
                System.err.println("Error Crítico: mainPacientePanel es nulo.");
                mostrarAlerta("Error de Interfaz", "Panel principal no encontrado.", "No se pudo encontrar el área para cargar la vista.", Alert.AlertType.ERROR);
            }

        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlerta("Error de Carga de FXML", "No se pudo cargar la vista: " + fxmlPath, "Detalle: " + e.getMessage(), Alert.AlertType.ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Error Inesperado", "Ocurrió un error al cargar la vista: " + fxmlPath, "Detalle: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    // Método de alerta autónomo
    private void mostrarAlerta(String titulo, String cabecera, String contenido, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(cabecera);
        alert.setContentText(contenido);
        if (mainApp != null && mainApp.getPrimaryStage() != null) {
            alert.initOwner(mainApp.getPrimaryStage());
        }
        alert.showAndWait();
    }
}
