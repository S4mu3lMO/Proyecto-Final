package co.edu.uniquindio.finalproyect.viewController.medicoSubViews;

import co.edu.uniquindio.finalproyect.application.App;
import co.edu.uniquindio.finalproyect.model.Medicamento;
import co.edu.uniquindio.finalproyect.model.Medico;
import co.edu.uniquindio.finalproyect.model.Paciente;
import co.edu.uniquindio.finalproyect.model.SistemaHospitalario;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.UUID;

public class MedicoRegistrarTratamientoController implements MedicoSubViewControllerBase {

    private App mainApp;
    private SistemaHospitalario sistemaHospitalario;
    private Medico medicoLogueado;
    private Paciente pacienteSeleccionado;

    @FXML private TextField txtCedulaPacienteTratamiento;
    @FXML private Button btnBuscarPacienteTrat;
    @FXML private Label lblNombrePacienteTrat;
    @FXML private DatePicker datePickerFechaInicioTrat;
    @FXML private DatePicker datePickerFechaFinTrat;
    @FXML private TextArea txtAreaDescripcionTratamiento;
    @FXML private TextField txtNombreMedicamento;
    @FXML private Button btnAddMedicamentoLista;
    @FXML private ListView<Medicamento> listViewMedicamentosTrat;
    @FXML private Button btnRemoveMedicamentoLista;
    @FXML private TextField txtDosisFrecuencia;
    @FXML private Button btnGuardarTratamiento;
    @FXML private Button btnLimpiarFormTratamiento;

    private ObservableList<Medicamento> medicamentosParaTratamientoList;

    @Override
    public void setMainApp(App mainApp) { this.mainApp = mainApp; }
    @Override
    public void setSistemaHospitalario(SistemaHospitalario sistema) { this.sistemaHospitalario = sistema; }
    @Override
    public void setMedicoLogueado(Medico medico) { this.medicoLogueado = medico; }

    @Override
    public void inicializarDatosSubVista() {
        medicamentosParaTratamientoList = FXCollections.observableArrayList();
        listViewMedicamentosTrat.setItems(medicamentosParaTratamientoList);
        listViewMedicamentosTrat.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Medicamento item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null || item.getNombreComercial() == null) {
                    setText(null);
                } else {
                    setText(item.getNombreComercial());
                }
            }
        });
        limpiarFormularioTratamiento();
        datePickerFechaInicioTrat.setValue(LocalDate.now());
    }

    @FXML
    void handleBuscarPacienteTrat() {
        String cedula = txtCedulaPacienteTratamiento.getText();
        if (cedula == null || cedula.trim().isEmpty()) {
            mostrarAlerta("Entrada Inválida", "Cédula Vacía", "Ingrese la cédula del paciente.", Alert.AlertType.WARNING);
            return;
        }
        this.pacienteSeleccionado = sistemaHospitalario.buscarPacientePorCedula(cedula); //
        if (pacienteSeleccionado != null) {
            lblNombrePacienteTrat.setText("Paciente: " + pacienteSeleccionado.getNombre());
        } else {
            lblNombrePacienteTrat.setText("Paciente: No encontrado");
            mostrarAlerta("No Encontrado", "Paciente no registrado", "No se encontró paciente con la cédula " + cedula, Alert.AlertType.INFORMATION);
        }
    }

    @FXML
    void handleAddMedicamentoLista() {
        String nombreMed = txtNombreMedicamento.getText();
        if (nombreMed != null && !nombreMed.trim().isEmpty()) {
            Medicamento med = new Medicamento(
                    UUID.randomUUID().toString().substring(0,8),
                    nombreMed,
                    "Presentación Desconocida",
                    0,
                    "Fabricante Desconocido",
                    false
            );
            medicamentosParaTratamientoList.add(med);
            txtNombreMedicamento.clear();
        }
    }

    @FXML
    void handleRemoveMedicamentoLista() {
        Medicamento seleccionado = listViewMedicamentosTrat.getSelectionModel().getSelectedItem();
        if (seleccionado != null) {
            medicamentosParaTratamientoList.remove(seleccionado);
        }
    }

    @FXML
    void handleGuardarTratamiento() {
        if (pacienteSeleccionado == null) {
            mostrarAlerta("Error de Validación", "Paciente no seleccionado", "Busque y seleccione un paciente válido.", Alert.AlertType.WARNING);
            return;
        }
        LocalDate fechaInicio = datePickerFechaInicioTrat.getValue();
        LocalDate fechaFin = datePickerFechaFinTrat.getValue();
        String descripcion = txtAreaDescripcionTratamiento.getText();
        String dosisFrec = txtDosisFrecuencia.getText();

        if (fechaInicio == null || fechaFin == null || descripcion == null || descripcion.trim().isEmpty() || dosisFrec == null || dosisFrec.trim().isEmpty()) {
            mostrarAlerta("Campos Vacíos", "Datos Incompletos", "Todos los campos (excepto medicamentos) son obligatorios.", Alert.AlertType.WARNING);
            return;
        }
        if(fechaFin.isBefore(fechaInicio)){
            mostrarAlerta("Fechas Inválidas", "Error en Fechas", "La fecha de fin no puede ser anterior a la fecha de inicio.", Alert.AlertType.WARNING);
            return;
        }

        LinkedList<Medicamento> listaMedicamentosFinal = new LinkedList<>(medicamentosParaTratamientoList);

        boolean registrado = sistemaHospitalario.registrarTratamiento(
                medicoLogueado.getCedula(),
                pacienteSeleccionado.getCedula(),
                fechaInicio,
                fechaFin,
                descripcion,
                listaMedicamentosFinal,
                dosisFrec
        );

        if (registrado) {
            mostrarAlerta("Éxito", "Tratamiento Registrado", "El tratamiento ha sido guardado correctamente.", Alert.AlertType.INFORMATION);
            limpiarFormularioTratamiento();
        } else {
            mostrarAlerta("Error", "Fallo al Registrar", "No se pudo guardar el tratamiento. Verifique la consola.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    void handleLimpiarFormTratamiento() {
        limpiarFormularioTratamiento();
    }

    private void limpiarFormularioTratamiento() {
        txtCedulaPacienteTratamiento.clear();
        lblNombrePacienteTrat.setText("Paciente: -");
        if(datePickerFechaInicioTrat!=null) datePickerFechaInicioTrat.setValue(LocalDate.now());
        if(datePickerFechaFinTrat!=null) datePickerFechaFinTrat.setValue(null);
        txtAreaDescripcionTratamiento.clear();
        txtNombreMedicamento.clear();
        if(medicamentosParaTratamientoList!=null) medicamentosParaTratamientoList.clear();
        txtDosisFrecuencia.clear();
        pacienteSeleccionado = null;
    }

    private void mostrarAlerta(String titulo, String cabecera, String contenido, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(cabecera);
        alert.setContentText(contenido);
        alert.showAndWait();
    }
}


