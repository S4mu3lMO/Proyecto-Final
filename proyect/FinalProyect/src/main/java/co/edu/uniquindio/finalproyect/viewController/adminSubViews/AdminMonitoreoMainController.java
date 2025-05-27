package co.edu.uniquindio.finalproyect.viewController.adminSubViews; // O el paquete que uses

import co.edu.uniquindio.finalproyect.application.App;
import co.edu.uniquindio.finalproyect.model.CitaMedica;
import co.edu.uniquindio.finalproyect.model.Medico;
// import co.edu.uniquindio.finalproyect.model.Paciente; // No se usa directamente aquí, se busca por cédula
import co.edu.uniquindio.finalproyect.model.SistemaHospitalario; //
import co.edu.uniquindio.finalproyect.viewController.AdministradorViewController;
import co.edu.uniquindio.finalproyect.viewController.SubViewControllerBase; // Tu interfaz base

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label; // Aunque no haya un Label fx:id específico aquí, es buena práctica importarlo si usas Labels en el FXML
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.LinkedList; // Se usa en el método obtenerMedicosDisponibles de SistemaHospitalario

public class AdminMonitoreoMainController implements SubViewControllerBase {

    private App mainApp;
    private SistemaHospitalario sistemaHospitalario;
    private AdministradorViewController administradorViewController;

    @FXML private DatePicker datePickerFechaDisponibilidad;
    @FXML private TextField txtHoraDisponibilidad;
    @FXML private TextField txtEspecialidadFiltro;
    @FXML private Button btnBuscarMedicosDisponibles;
    @FXML private TableView<Medico> tablaMedicosDisponibles;
    @FXML private TableColumn<Medico, String> colDispCedulaMedico;
    @FXML private TableColumn<Medico, String> colDispNombreMedico;
    @FXML private TableColumn<Medico, String> colDispEspecialidadMedico;
    @FXML private TextField txtCedulaPacienteAsignar;
    @FXML private TextField txtMotivoCitaAsignar;
    @FXML private Button btnAsignarPacienteAMedico;

    private ObservableList<Medico> listaObservableMedicosDisponibles;
    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

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
        colDispCedulaMedico.setCellValueFactory(new PropertyValueFactory<>("cedula"));
        colDispNombreMedico.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colDispEspecialidadMedico.setCellValueFactory(new PropertyValueFactory<>("especialidad"));

        listaObservableMedicosDisponibles = FXCollections.observableArrayList();
        tablaMedicosDisponibles.setItems(listaObservableMedicosDisponibles);

        datePickerFechaDisponibilidad.setValue(LocalDate.now()); // Fecha actual por defecto
        txtHoraDisponibilidad.setText(LocalTime.now().format(DateTimeFormatter.ofPattern("HH:00"))); // Hora actual redondeada por defecto
    }

    @FXML
    public void initialize() {
        // Este método es llamado por FXMLLoader después de que los @FXML son inyectados.
        // La lógica principal de inicialización que depende de mainApp, etc., está en inicializarSubView().
    }

    @FXML
    void handleBuscarMedicosDisponibles(ActionEvent event) {
        LocalDate fecha = datePickerFechaDisponibilidad.getValue();
        String strHora = txtHoraDisponibilidad.getText();
        String especialidad = txtEspecialidadFiltro.getText();

        if (fecha == null || strHora == null || strHora.trim().isEmpty()) {
            mostrarAlertaLocal("Campos Vacíos", "Fecha y Hora Requeridas", "Por favor, ingrese la fecha y hora para buscar disponibilidad.", Alert.AlertType.WARNING);
            return;
        }

        LocalTime hora;
        try {
            hora = LocalTime.parse(strHora.trim(), timeFormatter);
        } catch (DateTimeParseException e) {
            mostrarAlertaLocal("Formato Inválido", "Formato de Hora Incorrecto", "Use HH:mm para la hora (ej. 09:00, 14:30).", Alert.AlertType.ERROR);
            return;
        }

        LinkedList<Medico> medicosEncontrados = sistemaHospitalario.obtenerMedicosDisponibles(fecha, hora, (especialidad == null || especialidad.trim().isEmpty()) ? null : especialidad.trim()); //
        listaObservableMedicosDisponibles.setAll(medicosEncontrados);
        tablaMedicosDisponibles.refresh();

        if (medicosEncontrados.isEmpty()) {
            mostrarAlertaLocal("Sin Resultados", "No hay médicos disponibles", "No se encontraron médicos con los criterios especificados para el " + fecha + " a las " + hora + ".", Alert.AlertType.INFORMATION);
        }
    }

    @FXML
    void handleAsignarPacienteAMedico(ActionEvent event) {
        Medico medicoSeleccionado = tablaMedicosDisponibles.getSelectionModel().getSelectedItem();
        String cedulaPaciente = txtCedulaPacienteAsignar.getText();
        String motivoCita = txtMotivoCitaAsignar.getText();

        // Tomar la fecha y hora de los campos de búsqueda de disponibilidad
        LocalDate fechaCita = datePickerFechaDisponibilidad.getValue();
        LocalTime horaCita = null;
        if (txtHoraDisponibilidad.getText() != null && !txtHoraDisponibilidad.getText().trim().isEmpty()) {
            try {
                horaCita = LocalTime.parse(txtHoraDisponibilidad.getText().trim(), timeFormatter);
            } catch (DateTimeParseException e) {
                mostrarAlertaLocal("Error Interno", "Hora de Cita no Válida", "La hora ingresada para la búsqueda de disponibilidad no es válida.", Alert.AlertType.ERROR);
                return;
            }
        }

        if (medicoSeleccionado == null) {
            mostrarAlertaLocal("Sin Selección", "Médico no seleccionado", "Por favor, seleccione un médico disponible de la tabla.", Alert.AlertType.WARNING);
            return;
        }
        if (cedulaPaciente == null || cedulaPaciente.trim().isEmpty()) {
            mostrarAlertaLocal("Campo Vacío", "Cédula de Paciente Requerida", "Por favor, ingrese la cédula del paciente a asignar.", Alert.AlertType.WARNING);
            return;
        }
        if (motivoCita == null || motivoCita.trim().isEmpty()) {
            mostrarAlertaLocal("Campo Vacío", "Motivo de Cita Requerido", "Por favor, ingrese el motivo de la cita.", Alert.AlertType.WARNING);
            return;
        }
        if (fechaCita == null || horaCita == null) {
            mostrarAlertaLocal("Datos Faltantes", "Fecha y Hora de Cita Requeridas", "Asegúrese de haber buscado disponibilidad por fecha y hora