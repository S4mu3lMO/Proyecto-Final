package co.edu.uniquindio.finalproyect.viewController.pacienteSubViews;

import co.edu.uniquindio.finalproyect.application.App;
import co.edu.uniquindio.finalproyect.model.CitaMedica;
import co.edu.uniquindio.finalproyect.model.EstadoCita;
import co.edu.uniquindio.finalproyect.model.Medico;
import co.edu.uniquindio.finalproyect.model.Paciente;
import co.edu.uniquindio.finalproyect.model.SistemaHospitalario;
import co.edu.uniquindio.finalproyect.model.Sala;

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
import javafx.scene.control.TableCell;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator; // Importar Comparator
import java.util.Optional;
import java.util.stream.Collectors;

public class PacienteGestionCitasController implements PacienteSubViewControllerBase {

    private App mainApp;
    private SistemaHospitalario sistemaHospitalario;
    private Paciente pacienteLogueado;

    // RUTA BASE CORRECTA Y COMPLETA para los FXML dentro de este paquete de vistas
    private final String VIEWS_BASE_PATH = "/co/edu/uniquindio/finalproyect/views/";


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
            mostrarAlerta("Error de Sesión", "No se pudo identificar al paciente. Por favor, inicie sesión nuevamente.", Alert.AlertType.ERROR);
            return;
        }
        configurarTablaCitas();
        cargarMisCitas();
    }

    @FXML
    public void initialize() {
        // La configuración de las columnas se realiza en configurarTablaCitas
    }

    private void configurarTablaCitas() {
        colFechaCita.setCellValueFactory(new PropertyValueFactory<>("fecha"));
        colHoraCita.setCellValueFactory(new PropertyValueFactory<>("hora"));

        colMedicoCita.setCellValueFactory(cellData -> {
            Medico medico = cellData.getValue().getMedico();
            return new SimpleStringProperty(medico != null ? medico.getNombre() : "N/A");
        });
        colEspecialidadCita.setCellValueFactory(cellData -> {
            Medico medico = cellData.getValue().getMedico();
            return new SimpleStringProperty(medico != null ? medico.getEspecialidad() : "N/A");
        });
        colSalaCita.setCellValueFactory(cellData -> {
            Sala sala = cellData.getValue().getSala();
            return new SimpleStringProperty(sala != null ? sala.getNumeroSala() : "N/A");
        });
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
        if (pacienteLogueado == null || sistemaHospitalario == null) {
            if (misCitasObsList == null) {
                misCitasObsList = FXCollections.observableArrayList();
                if(tablaMisCitas != null) tablaMisCitas.setItems(misCitasObsList);
            }
            if(misCitasObsList != null) misCitasObsList.clear();
            // Considerar mostrar un mensaje en la tabla si está vacía
            // tablaMisCitas.setPlaceholder(new Label("Error al cargar citas o no hay citas."));
            return;
        }

        misCitasObsList = FXCollections.observableArrayList(
                sistemaHospitalario.getListCitasMedicas().stream()
                        .filter(cita -> cita.getPaciente() != null && cita.getPaciente().getCedula().equals(pacienteLogueado.getCedula()))
                        .sorted(Comparator.comparing(CitaMedica::getFecha).thenComparing(CitaMedica::getHora))
                        .collect(Collectors.toList())
        );
        if(tablaMisCitas != null) {
            tablaMisCitas.setItems(misCitasObsList);
            tablaMisCitas.refresh();
        }
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
            String fxmlFormPath = VIEWS_BASE_PATH + "PacienteSolicitarCitaForm.fxml"; // RUTA CORREGIDA
            URL resourceUrl = getClass().getResource(fxmlFormPath);

            if (resourceUrl == null) {
                System.err.println("Error Crítico en PacienteGestionCitasController: No se encuentra el archivo FXML del formulario en la ruta: " + fxmlFormPath);
                mostrarAlerta("Error Fatal de Carga", "Archivo FXML del formulario no encontrado. Verifique la ruta.", Alert.AlertType.ERROR);
                return;
            }

            FXMLLoader loader = new FXMLLoader(resourceUrl);
            Parent page = loader.load(); // Esta es la línea 154 que causaba el error

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Solicitar Nueva Cita Médica");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            if (mainApp != null && mainApp.getPrimaryStage() != null) {
                dialogStage.initOwner(mainApp.getPrimaryStage());
            }

            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            PacienteSolicitarCitaFormController controller = loader.getController();
            if (controller == null) {
                System.err.println("Error Crítico: El controlador para " + fxmlFormPath + " es nulo. Verifica el fx:controller en el FXML.");
                mostrarAlerta("Error de Carga", "No se pudo obtener el controlador del formulario.", Alert.AlertType.ERROR);
                return;
            }
            controller.setDialogStage(dialogStage);
            controller.setMainApp(mainApp);
            controller.setSistemaHospitalario(this.sistemaHospitalario);
            controller.setPacienteLogueado(this.pacienteLogueado);
            controller.setPacienteGestionCitasController(this);
            controller.inicializarFormulario();

            dialogStage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlerta("Error de Carga (IOException)", "No se pudo abrir el formulario de solicitud de cita: " + e.getMessage(), Alert.AlertType.ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Error Inesperado", "Ocurrió un error al intentar abrir el formulario: " + e.getMessage(), Alert.AlertType.ERROR);
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
        alertConfirmacion.setHeaderText("Cancelar Cita con Dr(a). " + (citaSeleccionada.getMedico() != null ? citaSeleccionada.getMedico().getNombre() : "N/A"));
        alertConfirmacion.setContentText("¿Está seguro de que desea cancelar esta cita programada para el " +
                citaSeleccionada.getFecha().format(dateFormatter) + " a las " +
                citaSeleccionada.getHora().format(timeFormatter) + "?");

        if (mainApp != null && mainApp.getPrimaryStage() != null) {
            alertConfirmacion.initOwner(mainApp.getPrimaryStage());
        }

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

    private void mostrarAlerta(String cabecera, String contenido, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle("Información del Sistema");
        alert.setHeaderText(cabecera);
        alert.setContentText(contenido);
        if (mainApp != null && mainApp.getPrimaryStage() != null) {
            alert.initOwner(mainApp.getPrimaryStage());
        }
        alert.showAndWait();
    }
}
