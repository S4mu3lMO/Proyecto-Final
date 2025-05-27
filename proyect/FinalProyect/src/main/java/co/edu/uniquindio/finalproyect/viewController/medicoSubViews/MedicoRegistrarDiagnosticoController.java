package co.edu.uniquindio.finalproyect.viewController.medicoSubViews;

import co.edu.uniquindio.finalproyect.application.App;
import co.edu.uniquindio.finalproyect.model.Medico;
import co.edu.uniquindio.finalproyect.model.Paciente;
import co.edu.uniquindio.finalproyect.model.SistemaHospitalario;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.time.LocalDate;

public class MedicoRegistrarDiagnosticoController implements MedicoSubViewControllerBase {

    private App mainApp;
    private SistemaHospitalario sistemaHospitalario;
    private Medico medicoLogueado;
    private Paciente pacienteSeleccionado;

    @FXML private TextField txtCedulaPacienteDiagnostico;
    @FXML private Button btnBuscarPacienteDiag;
    @FXML private Label lblNombrePacienteDiag;
    @FXML private DatePicker datePickerFechaDiagnostico;
    @FXML private TextArea txtAreaDescripcionDiagnostico;
    @FXML private Button btnGuardarDiagnostico;
    @FXML private Button btnLimpiarFormDiagnostico;

    @Override
    public void setMainApp(App mainApp) { this.mainApp = mainApp; }
    @Override
    public void setSistemaHospitalario(SistemaHospitalario sistema) { this.sistemaHospitalario = sistema; }
    @Override
    public void setMedicoLogueado(Medico medico) { this.medicoLogueado = medico; }

    @Override
    public void inicializarDatosSubVista() {
        limpiarFormularioDiagnostico();
        datePickerFechaDiagnostico.setValue(LocalDate.now());
    }

    @FXML
    void handleBuscarPacienteDiag() {
        String cedula = txtCedulaPacienteDiagnostico.getText();
        if (cedula == null || cedula.trim().isEmpty()) {
            mostrarAlerta("Entrada Inválida", "Cédula Vacía", "Ingrese la cédula del paciente.", Alert.AlertType.WARNING);
            return;
        }
        this.pacienteSeleccionado = sistemaHospitalario.buscarPacientePorCedula(cedula);
        if (pacienteSeleccionado != null) {
            lblNombrePacienteDiag.setText("Paciente: " + pacienteSeleccionado.getNombre());
        } else {
            lblNombrePacienteDiag.setText("Paciente: No encontrado");
            mostrarAlerta("No Encontrado", "Paciente no registrado", "No se encontró paciente con la cédula " + cedula, Alert.AlertType.INFORMATION);
        }
    }

    @FXML
    void handleGuardarDiagnostico() {
        if (pacienteSeleccionado == null) {
            mostrarAlerta("Error de Validación", "Paciente no seleccionado", "Busque y seleccione un paciente válido.", Alert.AlertType.WARNING);
            return;
        }
        LocalDate fecha = datePickerFechaDiagnostico.getValue();
        String descripcion = txtAreaDescripcionDiagnostico.getText();

        if (fecha == null || descripcion == null || descripcion.trim().isEmpty()) {
            mostrarAlerta("Campos Vacíos", "Datos Incompletos", "La fecha y la descripción son obligatorias.", Alert.AlertType.WARNING);
            return;
        }

        boolean registrado = sistemaHospitalario.registrarDiagnostico(
                medicoLogueado.getCedula(),
                pacienteSeleccionado.getCedula(),
                descripcion,
                fecha
        );

        if (registrado) {
            mostrarAlerta("Éxito", "Diagnóstico Registrado", "El diagnóstico ha sido guardado correctamente.", Alert.AlertType.INFORMATION);
            limpiarFormularioDiagnostico();
        } else {
            mostrarAlerta("Error", "Fallo al Registrar", "No se pudo guardar el diagnóstico. Verifique la consola.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    void handleLimpiarFormDiagnostico() {
        limpiarFormularioDiagnostico();
    }

    private void limpiarFormularioDiagnostico() {
        txtCedulaPacienteDiagnostico.clear();
        lblNombrePacienteDiag.setText("Paciente: -");
        if(datePickerFechaDiagnostico!=null) datePickerFechaDiagnostico.setValue(LocalDate.now());
        txtAreaDescripcionDiagnostico.clear();
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