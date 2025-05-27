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

import java.io.IOException;

public class MedicoViewController {

    private App mainApp;
    private SistemaHospitalario sistemaHospitalario;
    private Medico medicoLogueado;

    @FXML private BorderPane mainMedicoPanel;
    // El Label del top ahora es solo para el título general, o se puede quitar del controlador si no se actualiza
    // @FXML private Label lblNombreMedicoDashboard; // Si decides mantener un label general en el TOP y actualizarlo

    @FXML private Label lblNombreMedicoMenu; // NUEVO fx:id para el Label en el menú lateral

    // Botones del menú
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
            // Actualizar el nuevo Label en el menú lateral
            if (lblNombreMedicoMenu != null) {
                lblNombreMedicoMenu.setText("Dr(a). " + this.medicoLogueado.getNombre());
            }
            // Si tenías un Label general en el TOP y quieres actualizarlo también:
            // if (lblNombreMedicoDashboard != null) {
            //    lblNombreMedicoDashboard.setText("Panel del Médico: Dr(a). " + this.medicoLogueado.getNombre());
            // }
        } else {
            if (lblNombreMedicoMenu != null) {
                lblNombreMedicoMenu.setText("Médico No Identificado");
            }
            // if (lblNombreMedicoDashboard != null) {
            //    lblNombreMedicoDashboard.setText("Panel del Médico: Usuario no reconocido");
            // }
        }
    }

    public void inicializarDatos() {
        if (medicoLogueado == null) {
            mostrarAlerta("Error", "Error de Sesión", "No se pudo identificar al médico. Por favor, inicie sesión de nuevo.", Alert.AlertType.ERROR);
            if (mainApp != null) mainApp.mostrarLoginView();
            return;
        }
        System.out.println("Dashboard de Médico inicializado para: Dr(a). " + medicoLogueado.getNombre());
        handleGestionarMisHorarios(null);
    }

    // ... (resto de los métodos: initialize, handlers, cargarSubVistaMedico, mostrarAlerta) ...
    // Asegúrate de que el método mostrarAlerta y cargarSubVistaMedico estén presentes como los definimos antes.

    @FXML
    void initialize() {
    }

    @FXML
    void handleVerHistoriales(ActionEvent event) {
        cargarSubVistaMedico("/views/MedicoVerHistorialView.fxml");
    }

    @FXML
    void handleRegistrarDiagnostico(ActionEvent event) {
        cargarSubVistaMedico("/views/MedicoRegistrarDiagnosticoView.fxml");
    }

    @FXML
    void handleRegistrarTratamiento(ActionEvent event) {
        cargarSubVistaMedico("/views/MedicoRegistrarTratamientoView.fxml");
    }

    @FXML
    void handleGestionarMisHorarios(ActionEvent event) {
        cargarSubVistaMedico("/views/MedicoGestionarMisHorariosView.fxml");
    }

    @FXML
    void handleVerNotificaciones(ActionEvent event) {
        cargarSubVistaMedico("/views/MedicoNotificacionesView.fxml");
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Node subVista = loader.load();

            Object controller = loader.getController();
            if (controller instanceof MedicoSubViewControllerBase) { // Asumiendo que tienes esta interfaz
                MedicoSubViewControllerBase subController = (MedicoSubViewControllerBase) controller;
                subController.setMainApp(mainApp);
                subController.setSistemaHospitalario(sistemaHospitalario);
                subController.setMedicoLogueado(medicoLogueado);
                subController.inicializarDatosSubVista();
            }

            if (mainMedicoPanel != null) {
                mainMedicoPanel.setCenter(subVista);
            } else {
                System.err.println("Error: mainMedicoPanel es nulo en MedicoViewController.");
            }

        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlerta("Error de Carga", "No se pudo cargar la vista: " + fxmlPath, e.getMessage(), Alert.AlertType.ERROR);
        } catch (NullPointerException e) {
            e.printStackTrace();
            mostrarAlerta("Error de Carga", "Recurso FXML no encontrado: " + fxmlPath, "Verifica la ruta del archivo FXML.", Alert.AlertType.ERROR);
        }
    }

    protected void mostrarAlerta(String titulo, String cabecera, String contenido, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(cabecera);
        alert.setContentText(contenido);
        alert.showAndWait();
    }
}