package co.edu.uniquindio.finalproyect.viewController.pacienteSubViews;

import co.edu.uniquindio.finalproyect.application.App;
import co.edu.uniquindio.finalproyect.model.Diagnostico;
import co.edu.uniquindio.finalproyect.model.HistorialMedico;
import co.edu.uniquindio.finalproyect.model.Paciente;
import co.edu.uniquindio.finalproyect.model.SistemaHospitalario;
import co.edu.uniquindio.finalproyect.model.Tratamiento;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.stage.Stage; // Para initOwner en Alerta
import javafx.util.Callback; // Para ListCell

import java.time.format.DateTimeFormatter;

public class PacienteVerHistorialController implements PacienteSubViewControllerBase {

    private App mainApp;
    private SistemaHospitalario sistemaHospitalario;
    private Paciente pacienteLogueado;

    @FXML private Label lblNombrePacienteHistorial;
    @FXML private ListView<Diagnostico> listViewMisDiagnosticos;
    @FXML private ListView<Tratamiento> listViewMisTratamientos;
    @FXML private TextArea txtAreaDetalleHistorialSeleccionado;
    @FXML private Button btnRefrescarHistorial;

    private ObservableList<Diagnostico> misDiagnosticosObsList;
    private ObservableList<Tratamiento> misTratamientosObsList;

    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ISO_LOCAL_DATE;


    @Override
    public void setMainApp(App mainApp) { this.mainApp = mainApp; }

    @Override
    public void setSistemaHospitalario(SistemaHospitalario sistema) { this.sistemaHospitalario = sistema; }

    @Override
    public void setPacienteLogueado(Paciente paciente) { this.pacienteLogueado = paciente; }

    @Override
    public void inicializarDatosSubVistaPaciente() {
        if (pacienteLogueado == null) {
            mostrarAlerta("Error", "Paciente no identificado.", "No se pudieron cargar los datos del paciente.", Alert.AlertType.ERROR);
            return;
        }
        lblNombrePacienteHistorial.setText("Paciente: " + pacienteLogueado.getNombre());
        configurarListViews();
        cargarHistorialMedico();
    }

    @FXML
    public void initialize() {
        // La configuración de ListViews se hace en inicializarDatosSubVistaPaciente -> configurarListViews
    }

    private void configurarListViews() {
        // Formato para Diagnostico en ListView
        listViewMisDiagnosticos.setCellFactory(lv -> new ListCell<Diagnostico>() {
            @Override
            protected void updateItem(Diagnostico item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getFecha().format(dateFormatter) + " - " +
                            (item.getDescripcion().length() > 30 ? item.getDescripcion().substring(0, 30) + "..." : item.getDescripcion()));
                }
            }
        });

        // Formato para Tratamiento en ListView
        listViewMisTratamientos.setCellFactory(lv -> new ListCell<Tratamiento>() {
            @Override
            protected void updateItem(Tratamiento item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getFechaInicio().format(dateFormatter) + " a " +
                            (item.getFechaFin() != null ? item.getFechaFin().format(dateFormatter) : "Indefinido") + " - " +
                            (item.getDescripcion().length() > 25 ? item.getDescripcion().substring(0, 25) + "..." : item.getDescripcion()));
                }
            }
        });

        // Listeners para mostrar detalles
        listViewMisDiagnosticos.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                mostrarDetalleDiagnostico(newVal);
                listViewMisTratamientos.getSelectionModel().clearSelection();
            }
        });
        listViewMisTratamientos.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                mostrarDetalleTratamiento(newVal);
                listViewMisDiagnosticos.getSelectionModel().clearSelection();
            }
        });
    }

    @FXML
    void handleRefrescarHistorial(ActionEvent event) {
        cargarHistorialMedico();
    }

    private void cargarHistorialMedico() {
        if (pacienteLogueado == null || sistemaHospitalario == null) {
            mostrarAlerta("Error", "No se pudo cargar el historial.", "Información de sesión no disponible.", Alert.AlertType.WARNING);
            return;
        }

        HistorialMedico historial = sistemaHospitalario.consultarHistorialMedico(pacienteLogueado.getCedula());

        if (historial != null) {
            if (misDiagnosticosObsList == null) {
                misDiagnosticosObsList = FXCollections.observableArrayList();
                listViewMisDiagnosticos.setItems(misDiagnosticosObsList);
            }
            misDiagnosticosObsList.setAll(historial.getListDiagnosticos());

            if (misTratamientosObsList == null) {
                misTratamientosObsList = FXCollections.observableArrayList();
                listViewMisTratamientos.setItems(misTratamientosObsList);
            }
            misTratamientosObsList.setAll(historial.getListTratamientos());

            txtAreaDetalleHistorialSeleccionado.clear();
            if (historial.getListDiagnosticos().isEmpty() && historial.getListTratamientos().isEmpty()){
                txtAreaDetalleHistorialSeleccionado.setText("No hay diagnósticos ni tratamientos registrados en su historial.");
            }

        } else {
            mostrarAlerta("Información", "Historial no encontrado.", "No se encontró un historial médico asociado a su cuenta.", Alert.AlertType.INFORMATION);
            if (misDiagnosticosObsList != null) misDiagnosticosObsList.clear();
            if (misTratamientosObsList != null) misTratamientosObsList.clear();
            txtAreaDetalleHistorialSeleccionado.setText("No se pudo cargar su historial médico.");
        }
    }

    private void mostrarDetalleDiagnostico(Diagnostico diagnostico) {
        if (diagnostico == null) return;
        StringBuilder sb = new StringBuilder();
        sb.append("--- DETALLE DEL DIAGNÓSTICO ---\n\n");
        sb.append("Fecha: ").append(diagnostico.getFecha().format(dateFormatter)).append("\n");
        sb.append("Médico: Dr(a). ").append(diagnostico.getMedicoResponsable().getNombre()).append("\n");
        sb.append("Especialidad: ").append(diagnostico.getMedicoResponsable().getEspecialidad()).append("\n\n");
        sb.append("Descripción:\n").append(diagnostico.getDescripcion());
        txtAreaDetalleHistorialSeleccionado.setText(sb.toString());
    }

    private void mostrarDetalleTratamiento(Tratamiento tratamiento) {
        if (tratamiento == null) return;
        StringBuilder sb = new StringBuilder();
        sb.append("--- DETALLE DEL TRATAMIENTO ---\n\n");
        sb.append("Fecha Inicio: ").append(tratamiento.getFechaInicio().format(dateFormatter)).append("\n");
        sb.append("Fecha Fin: ").append(tratamiento.getFechaFin() != null ? tratamiento.getFechaFin().format(dateFormatter) : "No especificada").append("\n");
        sb.append("Médico Prescriptor: Dr(a). ").append(tratamiento.getMedicoPrescriptor().getNombre()).append("\n");
        sb.append("Especialidad: ").append(tratamiento.getMedicoPrescriptor().getEspecialidad()).append("\n\n");
        sb.append("Descripción del Tratamiento:\n").append(tratamiento.getDescripcion()).append("\n\n");
        sb.append("Dosis y Frecuencia:\n").append(tratamiento.getDosisFrecuencia()).append("\n\n");
        sb.append("Medicamentos Prescritos:\n");
        if (tratamiento.getListMedicamentos() != null && !tratamiento.getListMedicamentos().isEmpty()) {
            tratamiento.getListMedicamentos().forEach(med ->
                    sb.append("  - ").append(med.getNombreComercial())
                            .append(" (").append(med.getDosisMiligramos()).append("mg)")
                            .append(" - Presentación: ").append(med.getPresentacion())
                            .append("\n")
            );
        } else {
            sb.append("  (Ningún medicamento específico listado para este tratamiento)\n");
        }
        txtAreaDetalleHistorialSeleccionado.setText(sb.toString());
    }

    private void mostrarAlerta(String titulo, String cabecera, String contenido, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle("Información del Sistema");
        alert.setHeaderText(cabecera); // Usar el segundo parámetro como cabecera
        alert.setContentText(contenido);
        if (mainApp != null && mainApp.getPrimaryStage() != null) {
            alert.initOwner(mainApp.getPrimaryStage());
        }
        alert.showAndWait();
    }
}
