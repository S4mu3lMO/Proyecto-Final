package co.edu.uniquindio.finalproyect.viewController.medicoSubViews;

import co.edu.uniquindio.finalproyect.application.App;
import co.edu.uniquindio.finalproyect.model.*; // Paciente, HistorialMedico, Diagnostico, Tratamiento
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.Callback;

import java.util.stream.Collectors;

public class MedicoVerHistorialController implements MedicoSubViewControllerBase {

    private App mainApp;
    private SistemaHospitalario sistemaHospitalario;
    private Medico medicoLogueado;
    private Paciente pacienteActual;

    @FXML private TextField txtCedulaPacienteHistorial;
    @FXML private Button btnBuscarPacienteHistorial;
    @FXML private Label lblNombrePacienteEncontrado;
    @FXML private ListView<Diagnostico> listViewDiagnosticos;
    @FXML private ListView<Tratamiento> listViewTratamientos;
    @FXML private TextArea txtAreaDetalleSeleccionado;

    private ObservableList<Diagnostico> diagnosticosObsList;
    private ObservableList<Tratamiento> tratamientosObsList;

    @Override
    public void setMainApp(App mainApp) { this.mainApp = mainApp; }
    @Override
    public void setSistemaHospitalario(SistemaHospitalario sistema) { this.sistemaHospitalario = sistema; }
    @Override
    public void setMedicoLogueado(Medico medico) { this.medicoLogueado = medico; }

    @Override
    public void inicializarDatosSubVista() {
        configurarListViews();
        limpiarCamposHistorial();
    }

    private void configurarListViews() {
        // Formatear cómo se muestra un Diagnostico en el ListView
        listViewDiagnosticos.setCellFactory(new Callback<ListView<Diagnostico>, ListCell<Diagnostico>>() {
            @Override
            public ListCell<Diagnostico> call(ListView<Diagnostico> listView) {
                return new ListCell<Diagnostico>() {
                    @Override
                    protected void updateItem(Diagnostico item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty || item == null) {
                            setText(null);
                        } else {
                            setText(item.getFecha().toString() + " - " + item.getDescripcion().substring(0, Math.min(item.getDescripcion().length(), 30))+"...");
                        }
                    }
                };
            }
        });

        // Formatear cómo se muestra un Tratamiento en el ListView
        listViewTratamientos.setCellFactory(new Callback<ListView<Tratamiento>, ListCell<Tratamiento>>() {
            @Override
            public ListCell<Tratamiento> call(ListView<Tratamiento> listView) {
                return new ListCell<Tratamiento>() {
                    @Override
                    protected void updateItem(Tratamiento item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty || item == null) {
                            setText(null);
                        } else {
                            setText(item.getFechaInicio().toString() + " a " + item.getFechaFin().toString() + " - " + item.getDescripcion().substring(0, Math.min(item.getDescripcion().length(), 30))+"...");
                        }
                    }
                };
            }
        });

        // Listeners para mostrar detalles cuando se selecciona un item
        listViewDiagnosticos.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                mostrarDetalleDiagnostico(newVal);
                listViewTratamientos.getSelectionModel().clearSelection();
            }
        });
        listViewTratamientos.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                mostrarDetalleTratamiento(newVal);
                listViewDiagnosticos.getSelectionModel().clearSelection();
            }
        });
    }


    @FXML
    void handleBuscarPacienteHistorial() {
        String cedulaPaciente = txtCedulaPacienteHistorial.getText();
        if (cedulaPaciente == null || cedulaPaciente.trim().isEmpty()) {
            mostrarAlerta("Entrada Inválida", "Cédula Vacía", "Por favor, ingrese la cédula del paciente.", Alert.AlertType.WARNING);
            return;
        }

        // El médico solo puede ver historiales de pacientes que ha atendido o si tiene un rol que lo permita.
        // El método obtenerHistorialPacienteParaMedico ya incluye esta lógica.
        HistorialMedico historial = sistemaHospitalario.obtenerHistorialPacienteParaMedico(medicoLogueado.getCedula(), cedulaPaciente);

        if (historial != null && historial.getPacienteAsociado() != null) {
            pacienteActual = historial.getPacienteAsociado();
            lblNombrePacienteEncontrado.setText("Paciente: " + pacienteActual.getNombre());
            diagnosticosObsList = FXCollections.observableArrayList(historial.getListDiagnosticos());
            tratamientosObsList = FXCollections.observableArrayList(historial.getListTratamientos());
            listViewDiagnosticos.setItems(diagnosticosObsList);
            listViewTratamientos.setItems(tratamientosObsList);
            txtAreaDetalleSeleccionado.clear();
        } else {
            // El mensaje de error ya se imprime desde obtenerHistorialPacienteParaMedico
            mostrarAlerta("No Encontrado", "Paciente o Historial no Accesible", "No se encontró el paciente con cédula " + cedulaPaciente + " o no tiene permiso para ver su historial.", Alert.AlertType.INFORMATION);
            limpiarCamposHistorial();
        }
    }

    private void mostrarDetalleDiagnostico(Diagnostico diagnostico) {
        StringBuilder sb = new StringBuilder();
        sb.append("--- DETALLE DEL DIAGNÓSTICO ---\n");
        sb.append("ID: ").append(diagnostico.getIdDiagnostico()).append("\n");
        sb.append("Fecha: ").append(diagnostico.getFecha().toString()).append("\n");
        sb.append("Médico Responsable: Dr(a). ").append(diagnostico.getMedicoResponsable().getNombre()).append("\n");
        sb.append("Descripción:\n").append(diagnostico.getDescripcion());
        txtAreaDetalleSeleccionado.setText(sb.toString());
    }

    private void mostrarDetalleTratamiento(Tratamiento tratamiento) {
        StringBuilder sb = new StringBuilder();
        sb.append("--- DETALLE DEL TRATAMIENTO ---\n");
        sb.append("ID: ").append(tratamiento.getIdTratamiento()).append("\n");
        sb.append("Fecha Inicio: ").append(tratamiento.getFechaInicio().toString()).append("\n");
        sb.append("Fecha Fin: ").append(tratamiento.getFechaFin().toString()).append("\n");
        sb.append("Médico Prescriptor: Dr(a). ").append(tratamiento.getMedicoPrescriptor().getNombre()).append("\n");
        sb.append("Descripción:\n").append(tratamiento.getDescripcion()).append("\n\n");
        sb.append("Dosis y Frecuencia:\n").append(tratamiento.getDosisFrecuencia()).append("\n\n");
        sb.append("Medicamentos:\n");
        if (tratamiento.getListMedicamentos() != null && !tratamiento.getListMedicamentos().isEmpty()) {
            tratamiento.getListMedicamentos().forEach(med -> sb.append("  - ").append(med.getNombreComercial()).append(" (").append(med.getDosisMiligramos()).append("mg)\n"));
        } else {
            sb.append("  (Ninguno especificado)\n");
        }
        txtAreaDetalleSeleccionado.setText(sb.toString());
    }

    private void limpiarCamposHistorial() {
        txtCedulaPacienteHistorial.clear();
        lblNombrePacienteEncontrado.setText("Paciente: -");
        if (diagnosticosObsList != null) diagnosticosObsList.clear();
        if (tratamientosObsList != null) tratamientosObsList.clear();
        if (listViewDiagnosticos!=null) listViewDiagnosticos.getItems().clear();
        if (listViewTratamientos!=null) listViewTratamientos.getItems().clear();
        txtAreaDetalleSeleccionado.clear();
        pacienteActual = null;
    }

    private void mostrarAlerta(String titulo, String cabecera, String contenido, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(cabecera);
        alert.setContentText(contenido);
        alert.showAndWait();
    }
}