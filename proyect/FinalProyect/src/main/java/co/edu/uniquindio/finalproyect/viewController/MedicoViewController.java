package co.edu.uniquindio.finalproyect.viewController;

import co.edu.uniquindio.finalproyect.application.App;
import co.edu.uniquindio.finalproyect.model.Medico;
import co.edu.uniquindio.finalproyect.model.SistemaHospitalario;
import co.edu.uniquindio.finalproyect.model.Usuario;
import co.edu.uniquindio.finalproyect.viewController.medicoSubViews.MedicoSubViewControllerBase;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class MedicoViewController {

    private App mainApp;
    private SistemaHospitalario sistemaHospitalario;
    private Medico medicoLogueado;

    // RUTA BASE CORRECTA para las sub-vistas del médico
    private final String VIEWS_BASE_PATH = "/co/edu/uniquindio/finalproyect/views/";

    @FXML private BorderPane mainMedicoPanel;
    @FXML private Label lblNombreMedicoMenu;

    @FXML private Button btnVerHistorialesMedicos;
    @FXML private Button btnRegistrarDiagnostico;
    @FXML private Button btnRegistrarTratamiento;
    @FXML private Button btnGestionarMisHorarios;
    @FXML private Button btnVerNotificacionesCitas;
    @FXML private Button btnCerrarSesionMedico;


    public void setApp(App mainApp) {
        this.mainApp = mainApp;
        if (this.mainApp != null) {
            this.sistemaHospitalario = this.mainApp.getSistemaHospitalario();
        }
    }

    public void setUsuarioLogueado(Usuario usuarioLogueado) {
        if (usuarioLogueado instanceof Medico) {
            this.medicoLogueado = (Medico) usuarioLogueado;
            if (lblNombreMedicoMenu != null) {
                lblNombreMedicoMenu.setText("Dr(a). " + this.medicoLogueado.getNombre());
            }
        } else {
            if (lblNombreMedicoMenu != null) {
                lblNombreMedicoMenu.setText("Médico No Identificado");
            }
        }
    }

    public void inicializarDatos() {
        if (medicoLogueado == null) {
            mostrarAlerta("Error de Sesión", "No se pudo identificar al médico.", "Por favor, inicie sesión de nuevo.", Alert.AlertType.ERROR);
            if (mainApp != null) mainApp.mostrarLoginView();
            return;
        }
        System.out.println("Dashboard de Médico inicializado para: Dr(a). " + medicoLogueado.getNombre());
        if (btnGestionarMisHorarios != null) {
            handleGestionarMisHorarios(new ActionEvent());
        }
    }

    @FXML
    void initialize() {
        // Método llamado por FXMLLoader
    }

    @FXML
    void handleVerHistoriales(ActionEvent event) {
        cargarSubVistaMedico(VIEWS_BASE_PATH + "MedicoVerHistorialView.fxml");
    }

    @FXML
    void handleRegistrarDiagnostico(ActionEvent event) {
        cargarSubVistaMedico(VIEWS_BASE_PATH + "MedicoRegistrarDiagnosticoView.fxml");
    }

    @FXML
    void handleRegistrarTratamiento(ActionEvent event) {
        cargarSubVistaMedico(VIEWS_BASE_PATH + "MedicoRegistrarTratamientoView.fxml");
    }

    @FXML
    void handleGestionarMisHorarios(ActionEvent event) {
        cargarSubVistaMedico(VIEWS_BASE_PATH + "MedicoGestionarMisHorariosView.fxml");
    }

    @FXML
    void handleVerNotificaciones(ActionEvent event) {
        cargarSubVistaMedico(VIEWS_BASE_PATH + "MedicoNotificacionesView.fxml");
    }

    @FXML
    void handleCerrarSesionMedico(ActionEvent event) {
        if (mainApp != null) {
            mainApp.mostrarLoginView();
        }
    }

    private void cargarSubVistaMedico(String fxmlPath) {
        if (medicoLogueado == null) {
            mostrarAlerta("Error de Sesión", "Médico no identificado.", "No se puede cargar la sección. Por favor, reinicie sesión.", Alert.AlertType.WARNING);
            return;
        }
        try {
            URL resourceUrl = getClass().getResource(fxmlPath);
            if (resourceUrl == null) {
                System.err.println("Error Crítico en MedicoViewController: No se encuentra el archivo FXML en la ruta: " + fxmlPath);
                mostrarAlerta("Error Fatal de Carga", "Archivo FXML de sub-vista no encontrado.", "No se pudo localizar el archivo: " + fxmlPath, Alert.AlertType.ERROR);
                return;
            }
            FXMLLoader loader = new FXMLLoader(resourceUrl);
            Node subVista = loader.load();

            Object controller = loader.getController();
            if (controller instanceof MedicoSubViewControllerBase) {
                MedicoSubViewControllerBase subController = (MedicoSubViewControllerBase) controller;
                subController.setMainApp(mainApp);
                subController.setSistemaHospitalario(sistemaHospitalario);
                subController.setMedicoLogueado(medicoLogueado);
                subController.inicializarDatosSubVista();
            } else if (controller != null) {
                System.out.println("Advertencia: El controlador para " + fxmlPath + " ("+controller.getClass().getName()+") no implementa MedicoSubViewControllerBase.");
            } else {
                System.err.println("Error Crítico: El controlador para " + fxmlPath + " es nulo. Verifica el fx:controller en el FXML.");
            }

            if (mainMedicoPanel != null) {
                mainMedicoPanel.setCenter(subVista);
            } else {
                System.err.println("Error Crítico en MedicoViewController: mainMedicoPanel es nulo.");
                mostrarAlerta("Error de Interfaz", "Panel principal del médico no encontrado.", "No se pudo encontrar el área para cargar la vista.", Alert.AlertType.ERROR);
            }

        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlerta("Error de Carga de FXML", "No se pudo cargar la sub-vista del médico: " + fxmlPath, "Detalle: " + e.getMessage(), Alert.AlertType.ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Error Inesperado", "Ocurrió un error al cargar la vista del médico: " + fxmlPath, "Detalle: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

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
