package co.edu.uniquindio.finalproyect.viewController.adminSubViews; // O el paquete que uses

import co.edu.uniquindio.finalproyect.application.App;
import co.edu.uniquindio.finalproyect.model.EstadoCita;
import co.edu.uniquindio.finalproyect.model.SistemaHospitalario;
import co.edu.uniquindio.finalproyect.viewController.AdministradorViewController;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList; // IMPORTACIÓN AÑADIDA Y NECESARIA
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

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
        // Poblar ComboBox de EstadoCita
        ObservableList<EstadoCita> estados = FXCollections.observableArrayList(EstadoCita.values());
        cbEstadoCitaReporte.setItems(estados);
        cbEstadoCitaReporte.setPromptText("Todos (Opcional)");

        // Establecer valores por defecto
        datePickerReporteCitasInicio.setValue(LocalDate.now().minusMonths(1));
        datePickerReporteCitasFin.setValue(LocalDate.now());
        datePickerReporteOcupacion.setValue(LocalDate.now());
        txtDuracionCitaReporte.setText("30");
        txtAreaResultadoReporte.setText("Seleccione los parámetros y genere un reporte.");
    }

    @FXML
    public void initialize() {
        // Lógica de inicialización que no depende de mainApp, etc.
        // Por ejemplo, si cbEstadoCitaReporte no se poblara en inicializarSubView por alguna razón,
        // podría hacerse aquí, pero es mejor en inicializarSubView si depende de datos que se setean antes.
    }

    @FXML
    void handleGenerarReporteCitas(ActionEvent event) {
        LocalDate fechaInicio = datePickerReporteCitasInicio.getValue();
        LocalDate fechaFin = datePickerReporteCitasFin.getValue();
        String cedulaMedico = txtCedulaMedicoReporte.getText();
        EstadoCita estadoCita = cbEstadoCitaReporte.getValue();

        if (fechaInicio == null || fechaFin == null) {
            mostrarAlerta("Campos Requeridos", "Fechas Faltantes", "Las fechas de inicio y fin son obligatorias para el reporte de citas.", Alert.AlertType.WARNING);
            return;
        }
        if (fechaFin.isBefore(fechaInicio)) {
            mostrarAlerta("Fechas Inválidas", "Rango Incorrecto", "La fecha de fin no puede ser anterior a la fecha de inicio.", Alert.AlertType.WARNING);
            return;
        }

        String reporte = sistemaHospitalario.generarReporteCitasMedicas(
                fechaInicio,
                fechaFin,
                (cedulaMedico == null || cedulaMedico.trim().isEmpty()) ? null : cedulaMedico.trim(),
                estadoCita
        );

        txtAreaResultadoReporte.setText(reporte);
        if (reporte != null && reporte.contains("No se encontraron citas")) {
            mostrarAlerta("Información", "Reporte Generado", "No se encontraron citas que cumplan los criterios especificados.", Alert.AlertType.INFORMATION);
        } else if (reporte != null && !reporte.trim().isEmpty()){
            mostrarAlerta("Información", "Reporte Generado", "El reporte de citas ha sido generado.", Alert.AlertType.INFORMATION);
        } else {
            mostrarAlerta("Información", "Reporte Vacío", "El reporte generado está vacío o no se pudo generar.", Alert.AlertType.INFORMATION);
        }
    }

    @FXML
    void handleGenerarReporteOcupacion(ActionEvent event) {
        LocalDate fechaReporte = datePickerReporteOcupacion.getValue();
        String strDuracion = txtDuracionCitaReporte.getText();

        if (fechaReporte == null) {
            mostrarAlerta("Campo Requerido", "Fecha Faltante", "La fecha es obligatoria para el reporte de ocupación.", Alert.AlertType.WARNING);
            return;
        }

        int duracionCitaMinutos = 30;
        if (strDuracion != null && !strDuracion.trim().isEmpty()) {
            try {
                duracionCitaMinutos = Integer.parseInt(strDuracion.trim());
                if (duracionCitaMinutos <= 0) {
                    mostrarAlerta("Valor Inválido", "Duración Incorrecta", "La duración de la cita debe ser un número positivo.", Alert.AlertType.WARNING);
                    return;
                }
            } catch (NumberFormatException e) {
                mostrarAlerta("Formato Inválido", "Duración Numérica Requerida", "La duración de la cita debe ser un número entero.", Alert.AlertType.ERROR);
                return;
            }
        }

        String reporte = sistemaHospitalario.generarReporteOcupacionSalas(fechaReporte, duracionCitaMinutos);
        txtAreaResultadoReporte.setText(reporte);
        if (reporte != null && reporte.contains("No hay salas de tipo CONSULTORIO")) {
            mostrarAlerta("Información", "Reporte Generado", "No se puede generar el reporte: No hay consultorios registrados.", Alert.AlertType.INFORMATION);
        } else if (reporte != null && !reporte.trim().isEmpty()){
            mostrarAlerta("Información", "Reporte Generado", "El reporte de ocupación de salas ha sido generado.", Alert.AlertType.INFORMATION);
        } else {
            mostrarAlerta("Información", "Reporte Vacío", "El reporte de ocupación está vacío o no se pudo generar.", Alert.AlertType.INFORMATION);
        }
    }

    private void mostrarAlerta(String titulo, String cabecera, String contenido, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(cabecera);
        alert.setContentText(contenido);

        Stage ownerStage = null;
        if (mainApp != null && mainApp.getPrimaryStage() != null) {
            ownerStage = mainApp.getPrimaryStage();
        } else if (administradorViewController != null && administradorViewController.getMainApp() != null && administradorViewController.getMainApp().getPrimaryStage() != null) {
            ownerStage = administradorViewController.getMainApp().getPrimaryStage();
        }
        // Si este controlador maneja su propio Stage (por ejemplo, si fuera un diálogo), usarías eso:
        // else if (this.dialogStage != null) { // Asumiendo que tuvieras una variable dialogStage
        //     ownerStage = this.dialogStage;
        // }

        if (ownerStage != null) {
            alert.initOwner(ownerStage);
        }
        alert.showAndWait();
    }
}
