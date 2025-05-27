package co.edu.uniquindio.finalproyect.viewController.pacienteSubViews;

import co.edu.uniquindio.finalproyect.application.App;
import co.edu.uniquindio.finalproyect.model.CitaMedica;
import co.edu.uniquindio.finalproyect.model.EstadoCita;
import co.edu.uniquindio.finalproyect.model.Medico;
import co.edu.uniquindio.finalproyect.model.Paciente;
import co.edu.uniquindio.finalproyect.model.SistemaHospitalario;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.TableCell; // <-- IMPORTACIÓN AÑADIDA Y NECESARIA
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.stream.Collectors;

public class PacienteGestionCitasController implements PacienteSubViewControllerBase {

    private App mainApp;
    private SistemaHospitalario sistemaHospitalario;
    private Paciente pacienteLogueado;

    @FXML private TableView<CitaMedica> tablaMisCitas;
    @FXML private TableColumn<CitaMedica, LocalDate> colFechaCita;
    @FXML private TableColumn<CitaMedica, LocalTime> colHoraCita;
    @FXML private TableColumn<CitaMedica, String> colMedicoCita;
    @FXML private TableColumn<CitaMedica, String> colEspecialidadCita;
    @FXML private TableColumn<CitaMedica, String> colSalaCita;
    @FXML private TableColumn<CitaMedica, String> colMotivoCita;
    @FXML private TableColumn<CitaMedica, EstadoCita> colEstadoCita;

    @FXML private Button btnSolicitarNuevaCita;
    @FXML private Button btnCancelarCitaSeleccionada;
    @FXML private Button btnRefrescarCitas;


    private ObservableList<CitaMedica> misCitasObsList;
    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
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
            mostrarAlerta("Error", "No se pudo identificar al paciente.", Alert.AlertType.ERROR);
            return;
        }
        configurarTablaCitas();
        cargarMisCitas();
    }

    @FXML
    public void initialize() {
        // La configuración de las columnas se hace en configurarTablaCitas
        // que es llamado desde inicializarDatosSubVistaPaciente
    }

    private void configurarTablaCitas() {
        colFechaCita.setCellValueFactory(new PropertyValueFactory<>("fecha"));
        colHoraCita.setCellValueFactory(new PropertyValueFactory<>("hora"));
        colMedicoCita.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getMedico().getNombre()));
        colEspecialidadCita.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getMedico().getEspecialidad()));
        colSalaCita.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getSala().getNumeroSala()));
        colMotivoCita.setCellValueFactory(new PropertyValueFactory<>("motivo"));
        colEstadoCita.setCellValueFactory(new PropertyValueFactory<>("estadoCita"));

        colFechaCita.setCellFactory(column -> new TableCell<CitaMedica, LocalDate>() {
            @Override
            protected void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(dateFormatter.format(item));
                }
            }
        });

        colHoraCita.setCellFactory(column -> new TableCell<CitaMedica, LocalTime>() {
            @Override
            protected void updateItem(LocalTime item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(timeFormatter.format(item));
                }
            }
        });
    }


    private void cargarMisCitas() {
        if (pacienteLogueado == null || sistemaHospitalario == null) return;

        misCitasObsList = FXCollections.observableArrayList(
                sistemaHospitalario.getListCitasMedicas().stream()
                        .filter(cita -> cita.getPaciente().getCedula().equals(pacienteLogueado.getCedula()))
                        .sorted((c1, c2) -> {
                            int dateCompare = c1.getFecha().compareTo(c2.getFecha());
                            if (dateCompare == 0) {
                                return c1.getHora().compareTo(c2.getHora());
                            }
                            return dateCompare;
                        })
                        .collect(Collectors.toList())
        );
        tablaMisCitas.setItems(misCitasObsList);
        tablaMisCitas.refresh();
    }

    @FXML
    void handleRefrescarCitas(ActionEvent event) {
        cargarMisCitas();
    }


    @FXML
    void handleSolicitarNuevaCita(ActionEvent event) {
        if (pacienteLogueado == null) {
            mostrarAlerta("Error de Sesión", "Debe iniciar sesión para solicitar una cita.", Alert.AlertType.ERROR);
            return;
        }
        System.out.println("Botón Solicitar Nueva Cita presionado por: " + pacienteLogueado.getNombre());
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/PacienteSolicitarCitaForm.fxml"));
            Parent page = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Solicitar Nueva Cita Médica");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            if (mainApp != null && mainApp.getPrimaryStage() != null) {
                dialogStage.initOwner(mainApp.getPrimaryStage());
            }

            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            PacienteSolicitarCitaFormController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setMainApp(mainApp);
            controller.setSistemaHospitalario(this.sistemaHospitalario);
            controller.setPacienteLogueado(this.pacienteLogueado);
            controller.setPacienteGestionCitasController(this);
            controller.inicializarFormulario();


            dialogStage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlerta("Error de Carga", "No se pudo abrir el formulario de solicitud de cita.", Alert.AlertType.ERROR);
        } catch (NullPointerException e) {
            e.printStackTrace();
            mostrarAlerta("Error de Carga", "Recurso FXML del formulario de solicitud de cita no encontrado. Verifique la ruta: /views/PacienteSolicitarCitaForm.fxml", Alert.AlertType.ERROR);
        }
    }

    @FXML
    void handleCancelarCitaSeleccionada(ActionEvent event) {
        CitaMedica citaSeleccionada = tablaMisCitas.getSelectionModel().getSelectedItem();

        if (citaSeleccionada == null) {
            mostrarAlerta("Sin Selección", "Por favor, seleccione una cita de la tabla para cancelar.", Alert.AlertType.WARNING);
            return;
        }

        if (citaSeleccionada.getEstadoCita() == EstadoCita.CANCELADA) {
            mostrarAlerta("Información", "La cita ya está cancelada.", Alert.AlertType.INFORMATION);
            return;
        }
        if (citaSeleccionada.getEstadoCita() == EstadoCita.FINALIZADA) {
            mostrarAlerta("Operación no Permitida", "No puede cancelar una cita que ya ha finalizado.", Alert.AlertType.WARNING);
            return;
        }

        Alert alertConfirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        alertConfirmacion.setTitle("Confirmar Cancelación");
        alertConfirmacion.setHeaderText("Cancelar Cita con Dr(a). " + citaSeleccionada.getMedico().getNombre());
        alertConfirmacion.setContentText("¿Está seguro de que desea cancelar esta cita programada para el " +
                citaSeleccionada.getFecha().format(dateFormatter) + " a las " +
                citaSeleccionada.getHora().format(timeFormatter) + "?");

        Optional<ButtonType> resultado = alertConfirmacion.showAndWait();
        if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
            boolean cancelada = sistemaHospitalario.cancelarCitaMedica(citaSeleccionada.getIdCita());
            if (cancelada) {
                mostrarAlerta("Cita Cancelada", "Su cita ha sido cancelada exitosamente.", Alert.AlertType.INFORMATION);
                cargarMisCitas();
            } else {
                mostrarAlerta("Error", "No se pudo cancelar la cita. Intente más tarde.", Alert.AlertType.ERROR);
            }
        }
    }

    public void refrescarTablaCitas() {
        cargarMisCitas();
    }

    private void mostrarAlerta(String titulo, String contenido, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle("Información del Sistema");
        alert.setHeaderText(titulo);
        alert.setContentText(contenido);
        if (mainApp != null && mainApp.getPrimaryStage() != null) {
            alert.initOwner(mainApp.getPrimaryStage());
        }
        alert.showAndWait();
    }
}
