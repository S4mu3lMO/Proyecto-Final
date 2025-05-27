package co.edu.uniquindio.finalproyect.viewController.pacienteSubViews;

import co.edu.uniquindio.finalproyect.application.App;
import co.edu.uniquindio.finalproyect.model.CitaMedica;
import co.edu.uniquindio.finalproyect.model.Medico;
import co.edu.uniquindio.finalproyect.model.Paciente;
import co.edu.uniquindio.finalproyect.model.SistemaHospitalario;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.LinkedList;
import java.util.stream.Collectors;

public class PacienteSolicitarCitaFormController {

    private Stage dialogStage;
    private App mainApp; // Para initOwner de alertas
    private SistemaHospitalario sistemaHospitalario;
    private Paciente pacienteLogueado;
    private PacienteGestionCitasController pacienteGestionCitasController; // Para refrescar la tabla de citas

    @FXML private ComboBox<String> cbEspecialidadCita;
    @FXML private DatePicker datePickerFechaCita;
    @FXML private TextField txtHoraCita;
    @FXML private Button btnBuscarDisponibilidad;
    @FXML private ListView<Medico> listViewMedicosDisponibles;
    @FXML private TextField txtMotivoNuevaCita;
    @FXML private Button btnConfirmarSolicitudCita;
    @FXML private Button btnCancelarSolicitud;

    private ObservableList<Medico> medicosDisponiblesObsList;
    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

    public void setDialogStage(Stage dialogStage) { this.dialogStage = dialogStage; }
    public void setMainApp(App mainApp) { this.mainApp = mainApp; }
    public void setSistemaHospitalario(SistemaHospitalario sistema) { this.sistemaHospitalario = sistema; }
    public void setPacienteLogueado(Paciente paciente) { this.pacienteLogueado = paciente; }
    public void setPacienteGestionCitasController(PacienteGestionCitasController controller) {
        this.pacienteGestionCitasController = controller;
    }

    @FXML
    public void initialize() {
        // Formato para mostrar médicos en el ListView
        listViewMedicosDisponibles.setCellFactory(param -> new ListCell<Medico>() {
            @Override
            protected void updateItem(Medico item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null || item.getNombre() == null) {
                    setText(null);
                } else {
                    setText("Dr(a). " + item.getNombre() + " (" + item.getEspecialidad() + ")");
                }
            }
        });
    }

    public void inicializarFormulario() {
        // Poblar ComboBox de especialidades
        if (sistemaHospitalario != null) {
            ObservableList<String> especialidades = FXCollections.observableArrayList(
                    sistemaHospitalario.getListMedicos().stream()
                            .map(Medico::getEspecialidad)
                            .distinct()
                            .sorted()
                            .collect(Collectors.toList())
            );
            cbEspecialidadCita.setItems(especialidades);
        }
        datePickerFechaCita.setValue(LocalDate.now().plusDays(1)); // Por defecto, mañana
        medicosDisponiblesObsList = FXCollections.observableArrayList();
        listViewMedicosDisponibles.setItems(medicosDisponiblesObsList);
    }

    @FXML
    void handleBuscarDisponibilidad(ActionEvent event) {
        String especialidad = cbEspecialidadCita.getValue();
        LocalDate fecha = datePickerFechaCita.getValue();
        String strHora = txtHoraCita.getText();

        if (especialidad == null || fecha == null || strHora == null || strHora.trim().isEmpty()) {
            mostrarAlerta("Campos Incompletos", "Por favor, seleccione especialidad, fecha y hora.", Alert.AlertType.WARNING);
            return;
        }

        LocalTime hora;
        try {
            hora = LocalTime.parse(strHora.trim(), timeFormatter);
        } catch (DateTimeParseException e) {
            mostrarAlerta("Formato Incorrecto", "Use HH:mm para la hora (ej. 09:00).", Alert.AlertType.ERROR);
            return;
        }

        LinkedList<Medico> medicosEncontrados = sistemaHospitalario.obtenerMedicosDisponibles(fecha, hora, especialidad);
        medicosDisponiblesObsList.setAll(medicosEncontrados);
        if (medicosEncontrados.isEmpty()) {
            mostrarAlerta("Sin Disponibilidad", "No se encontraron médicos disponibles para los criterios seleccionados.", Alert.AlertType.INFORMATION);
        }
    }

    @FXML
    void handleConfirmarSolicitudCita(ActionEvent event) {
        Medico medicoSeleccionado = listViewMedicosDisponibles.getSelectionModel().getSelectedItem();
        LocalDate fecha = datePickerFechaCita.getValue();
        String strHora = txtHoraCita.getText();
        String motivo = txtMotivoNuevaCita.getText();

        if (medicoSeleccionado == null) {
            mostrarAlerta("Selección Requerida", "Por favor, seleccione un médico de la lista de disponibles.", Alert.AlertType.WARNING);
            return;
        }
        if (fecha == null || strHora == null || strHora.trim().isEmpty() || motivo == null || motivo.trim().isEmpty()) {
            mostrarAlerta("Campos Incompletos", "Fecha, hora y motivo son requeridos.", Alert.AlertType.WARNING);
            return;
        }

        LocalTime hora;
        try {
            hora = LocalTime.parse(strHora.trim(), timeFormatter);
        } catch (DateTimeParseException e) {
            mostrarAlerta("Formato Incorrecto", "La hora ingresada no es válida.", Alert.AlertType.ERROR);
            return;
        }

        boolean solicitada = sistemaHospitalario.solicitarCitaMedica(
                pacienteLogueado.getCedula(),
                medicoSeleccionado.getCedula(),
                fecha,
                hora,
                motivo.trim()
        );

        if (solicitada) {
            mostrarAlerta("Cita Solicitada", "Su solicitud de cita ha sido registrada exitosamente.", Alert.AlertType.INFORMATION);
            if (pacienteGestionCitasController != null) {
                pacienteGestionCitasController.refrescarTablaCitas();
            }
            dialogStage.close();
        } else {
            // El método solicitarCitaMedica ya imprime errores en consola si falla (ej. sala no disponible, médico ya ocupado)
            mostrarAlerta("Error en Solicitud", "No se pudo registrar su cita. Es posible que el horario ya no esté disponible o no haya salas. Intente con otro horario o médico.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    void handleCancelarSolicitud(ActionEvent event) {
        if (dialogStage != null) {
            dialogStage.close();
        }
    }

    private void mostrarAlerta(String titulo, String contenido, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle("Solicitud de Cita");
        alert.setHeaderText(titulo);
        alert.setContentText(contenido);
        if (dialogStage != null) { // Hacerla modal al diálogo actual
            alert.initOwner(dialogStage);
        } else if (mainApp != null && mainApp.getPrimaryStage() != null) {
            alert.initOwner(mainApp.getPrimaryStage());
        }
        alert.showAndWait();
    }
}
