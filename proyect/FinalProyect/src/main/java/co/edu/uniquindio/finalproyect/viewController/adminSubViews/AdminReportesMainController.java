package co.edu.uniquindio.finalproyect.viewController.adminSubViews; // O el paquete que uses

import co.edu.uniquindio.finalproyect.application.App;
import co.edu.uniquindio.finalproyect.model.EstadoCita; //
import co.edu.uniquindio.finalproyect.model.SistemaHospitalario; //
import co.edu.uniquindio.finalproyect.viewController.AdministradorViewController;
import co.edu.uniquindio.finalproyect.viewController.SubViewControllerBase; // Tu interfaz base

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.time.LocalDate;

public class AdminReportesMainController implements SubViewControllerBase {

    private App mainApp;
    private SistemaHospitalario sistemaHospitalario;
    private AdministradorViewController administradorViewController;

    @FXML private DatePicker datePickerReporteCitasInicio;
    @FXML private DatePicker datePickerReporteCitasFin;
    @FXML private TextField txtCedulaMedicoReporte;
    @FXML private ComboBox<EstadoCita> cbEstadoCitaReporte;
    @FXML private Button btnGenerarReporteCitas;
    @FXML private DatePicker datePickerReporteOcupacion;
    @FXML private TextField txtDuracionCitaReporte;
    @FXML private Button btnGenerarReporteOcupacion;
    @FXML private TextArea txtAreaResultadoReporte;

    @Override
    public void setMainApp(App mainApp) { this.mainApp = mainApp; }

    @Override
    public void setSistemaHospitalario(SistemaHospitalario sistema) { this.sistemaHospitalario = sistema; }

    @Override
    public void setAdministradorViewController(AdministradorViewController adminController) {
        this.administradorViewController = adminController;
    }

    @Override
    public void inicializarSubView() {
        // Poblar ComboBox de EstadoCita y añadir una opción para "Todos" (null)
        ObservableList<EstadoCita> estados = FXCollections.observableArrayList(EstadoCita.values());
        cbEstadoCitaReporte.setItems(estados);
        // Para permitir "Todos", el usuario simplemente no selecciona nada o se podría añadir un item null
        // o manejar la lógica de que si es null, no se filtra por estado.
        // El promptText ya sugiere "Todos (Opcional)".

        // Establecer valores por defecto para los DatePickers y TextField de duración
        datePickerReporteCitasInicio.setValue(LocalDate.now().minusMonths(1)); // Un mes atrás
        datePickerReporteCitasFin.setValue(LocalDate.now()); // Hoy
        datePickerReporteOcupacion.setValue(LocalDate.now()); // Hoy
        txtDuracionCitaReporte.setText("30"); // Duración por defecto de 30 minutos
        txtAreaResultadoReporte.setText("Seleccione los parámetros y genere un reporte.");
    }

    @FXML
    public void initialize() {
        // Lógica de inicialización que no depende de mainApp, etc.
    }

    @FXML
    void handleGenerarReporteCitas(ActionEvent event) {
        LocalDate fechaInicio = datePickerReporteCitasInicio.getValue();
        LocalDate fechaFin = datePickerReporteCitasFin.getValue();
        String cedulaMedico = txtCedulaMedicoReporte.getText();
        EstadoCita estadoCita = cbEstadoCitaReporte.getValue(); // Esto será null si no se selecciona nada (para "Todos")

        if (fechaInicio == null || fechaFin == null) {
            mostrarAlertaLocal("Campos Requeridos", "Fechas Faltantes", "Las fechas de inicio y fin son obligatorias para el reporte de citas.", Alert.AlertType.WARNING);
            return;
        }
        if (fechaFin.isBefore(fechaInicio)) {
            mostrarAlertaLocal("Fechas Inválidas", "Rango Incorrecto", "La fecha de fin no puede ser anterior a la fecha de inicio.", Alert.AlertType.WARNING);
            return;
        }

        String reporte = sistemaHospitalario.generarReporteCitasMedicas(
                fechaInicio,
                fechaFin,
                (cedulaMedico == null || cedulaMedico.trim().isEmpty()) ? null : cedulaMedico.trim(), // Pasar null si está vacío
                estadoCita // Pasar el valor del ComboBox (puede ser null)
        ); //

        txtAreaResultadoReporte.setText(reporte);
        if (reporte.contains("No se encontraron citas")) {
            mostrarAlertaLocal("Información", "Reporte Generado", "No se encontraron citas que cumplan los criterios especificados.", Alert.AlertType.INFORMATION);
        } else {
            mostrarAlertaLocal("Información", "Reporte Generado", "El reporte de citas ha sido generado.", Alert.AlertType.INFORMATION);
        }
    }

    @FXML
    void handleGenerarReporteOcupacion(ActionEvent event) {
        LocalDate fechaReporte = datePickerReporteOcupacion.getValue();
        String strDuracion = txtDuracionCitaReporte.getText();

        if (fechaReporte == null) {
            mostrarAlertaLocal("Campo Requerido", "Fecha Faltante", "La fecha es obligatoria para el reporte de ocupación.", Alert.AlertType.WARNING);
            return;
        }

        int duracionCitaMinutos = 30; // Valor por defecto si el campo está vacío o es inválido
        if (strDuracion != null && !strDuracion.trim().isEmpty()) {
            try {
                duracionCitaMinutos = Integer.parseInt(strDuracion.trim());
                if (duracionCitaMinutos <= 0) {
                    mostrarAlertaLocal("Valor Inválido", "Duración Incorrecta", "La duración de la cita debe ser un número positivo.", Alert.AlertType.WARNING);
                    return; // No generar reporte si la duración es inválida
                }
            } catch (NumberFormatException e) {
                mostrarAlertaLocal("Formato Inválido", "Duración Numérica Requerida", "La duración de la cita debe ser un número entero.", Alert.AlertType.ERROR);
                return; // No generar reporte si el formato es inválido
            }
        }

        String reporte = sistemaHospitalario.generarReporteOcupacionSalas(fechaReporte, duracionCitaMinutos); //
        txtAreaResultadoReporte.setText(reporte);
        if (reporte.contains("No hay salas de tipo CONSULTORIO")) {
            mostrarAlertaLocal("Información", "Reporte Generado", "No se puede generar el reporte: No hay consultorios registrados.", Alert.AlertType.INFORMATION);
        } else {
            mostrarAlertaLocal("Información", "Reporte Generado", "El reporte de ocupación de salas ha sido generado.", Alert.AlertType.INFORMATION);
        }
    }

    // Método para mostrar alertas, preferiblemente delegado desde AdministradorViewController
    private void mostrarAlertaLocal(String titulo, String cabecera, String contenido, Alert.AlertType tipo) {
        if (administradorViewController != null) {
            administradorViewController.mostrarAlerta(titulo, cabecera, contenido, tipo);
        } else {
            Alert alert = new Alert(tipo);
            alert.setTitle(titulo);
            alert.setHeaderText(cabecera);
            alert.setContentText(contenido);
            alert.showAndWait();
        }
    }
}