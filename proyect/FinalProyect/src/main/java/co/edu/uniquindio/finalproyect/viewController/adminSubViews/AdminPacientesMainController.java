package co.edu.uniquindio.finalproyect.viewController.adminSubViews;

import co.edu.uniquindio.finalproyect.application.App;
import co.edu.uniquindio.finalproyect.model.Paciente;
import co.edu.uniquindio.finalproyect.model.Sexo;
import co.edu.uniquindio.finalproyect.model.SistemaHospitalario;
import co.edu.uniquindio.finalproyect.model.TipoUsuario;
import co.edu.uniquindio.finalproyect.viewController.AdministradorViewController;
import co.edu.uniquindio.finalproyect.viewController.adminSubViews.SubViewControllerBase;
import co.edu.uniquindio.finalproyect.viewController.adminSubViews.forms.PacienteFormController;

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
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;

public class AdminPacientesMainController implements SubViewControllerBase {

    private App mainApp;
    private SistemaHospitalario sistemaHospitalario;
    private AdministradorViewController administradorViewController;

    private final String VIEWS_BASE_PATH = "/co/edu/uniquindio/finalproyect/views/";

    @FXML private TableView<Paciente> tablaPacientes;
    @FXML private TableColumn<Paciente, String> colCedulaPaciente;
    @FXML private TableColumn<Paciente, String> colNombrePaciente;
    @FXML private TableColumn<Paciente, Integer> colEdadPaciente;
    @FXML private TableColumn<Paciente, Sexo> colSexoPaciente;
    @FXML private TableColumn<Paciente, String> colUsuarioPaciente;
    @FXML private TableColumn<Paciente, String> colNumSeguroPaciente;

    @FXML private Button btnCrearPaciente;
    @FXML private Button btnActualizarPaciente;
    @FXML private Button btnEliminarPaciente;

    private ObservableList<Paciente> listaObservablePacientes;

    @Override
    public void setMainApp(App mainApp) {
        this.mainApp = mainApp;
    }

    @Override
    public void setSistemaHospitalario(SistemaHospitalario sistema) {
        this.sistemaHospitalario = sistema;
    }

    @Override
    public void setAdministradorViewController(AdministradorViewController adminController) {
        this.administradorViewController = adminController;
    }

    @Override
    public void inicializarSubView() {
        colCedulaPaciente.setCellValueFactory(new PropertyValueFactory<>("cedula"));
        colNombrePaciente.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colEdadPaciente.setCellValueFactory(new PropertyValueFactory<>("edad"));
        colSexoPaciente.setCellValueFactory(new PropertyValueFactory<>("sexo"));
        colUsuarioPaciente.setCellValueFactory(new PropertyValueFactory<>("nombreUsuario"));
        if (colNumSeguroPaciente != null) {
            colNumSeguroPaciente.setCellValueFactory(new PropertyValueFactory<>("numeroSeguroSocial"));
        }
        cargarPacientesEnTabla();
    }

    @FXML
    public void initialize() {
    }

    private void cargarPacientesEnTabla() {
        if (sistemaHospitalario != null) {
            listaObservablePacientes = FXCollections.observableArrayList(sistemaHospitalario.getListPacientes());
            tablaPacientes.setItems(listaObservablePacientes);
            tablaPacientes.refresh();
        } else {
            System.err.println("SistemaHospitalario es nulo en AdminPacientesMainController.");
            if (administradorViewController != null) {
                administradorViewController.mostrarAlerta("Error", "Error Interno", "No se pudo acceder a los datos del sistema (pacientes).", Alert.AlertType.ERROR);
            }
        }
    }

    @FXML
    void handleCrearPaciente(ActionEvent event) {
        System.out.println("Crear nuevo paciente...");
        mostrarDialogoFormularioPaciente(null);
    }

    @FXML
    void handleActualizarPaciente(ActionEvent event) {
        Paciente pacienteSeleccionado = tablaPacientes.getSelectionModel().getSelectedItem();
        if (pacienteSeleccionado != null) {
            System.out.println("Actualizar paciente: " + pacienteSeleccionado.getNombre());
            mostrarDialogoFormularioPaciente(pacienteSeleccionado);
        } else {
            if (administradorViewController != null) {
                administradorViewController.mostrarAlerta("Sin Selección", "No hay paciente seleccionado",
                        "Por favor, seleccione un paciente de la tabla para actualizar.", Alert.AlertType.WARNING);
            }
        }
    }

    @FXML
    void handleEliminarPaciente(ActionEvent event) {
        Paciente pacienteSeleccionado = tablaPacientes.getSelectionModel().getSelectedItem();
        if (pacienteSeleccionado != null) {
            Alert alertConfirmacion = new Alert(Alert.AlertType.CONFIRMATION);
            alertConfirmacion.setTitle("Confirmar Eliminación");
            alertConfirmacion.setHeaderText("Eliminar Paciente: " + pacienteSeleccionado.getNombre());
            alertConfirmacion.setContentText("¿Está seguro de que desea eliminar a este paciente? Esta acción no se puede deshacer.");

            if (mainApp != null && mainApp.getPrimaryStage() != null) {
                alertConfirmacion.initOwner(mainApp.getPrimaryStage());
            } else if (administradorViewController != null && administradorViewController.getMainApp() != null && administradorViewController.getMainApp().getPrimaryStage() != null){
                alertConfirmacion.initOwner(administradorViewController.getMainApp().getPrimaryStage());
            }

            Optional<ButtonType> resultado = alertConfirmacion.showAndWait();
            if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
                boolean eliminado = sistemaHospitalario.eliminarPaciente(pacienteSeleccionado.getCedula());
                if (eliminado) {
                    if (administradorViewController != null) {
                        administradorViewController.mostrarAlerta("Éxito", "Paciente Eliminado",
                                "El paciente " + pacienteSeleccionado.getNombre() + " ha sido eliminado.", Alert.AlertType.INFORMATION);
                    }
                    cargarPacientesEnTabla();
                } else {
                    if (administradorViewController != null) {
                        administradorViewController.mostrarAlerta("Error", "Error al Eliminar",
                                "No se pudo eliminar el paciente. Verifique la consola para más detalles.", Alert.AlertType.ERROR);
                    }
                }
            }
        } else {
            if (administradorViewController != null) {
                administradorViewController.mostrarAlerta("Sin Selección", "No hay paciente seleccionado",
                        "Por favor, seleccione un paciente de la tabla para eliminar.", Alert.AlertType.WARNING);
            }
        }
    }

    private void mostrarDialogoFormularioPaciente(Paciente paciente) {
        try {
            String fxmlFormPath = VIEWS_BASE_PATH + "PacienteFormView.fxml";
            URL resourceUrl = getClass().getResource(fxmlFormPath);

            if (resourceUrl == null) {
                System.err.println("Error Crítico en AdminPacientesMainController: No se encuentra el FXML del formulario en: " + fxmlFormPath);
                if (administradorViewController != null) {
                    administradorViewController.mostrarAlerta("Error Fatal de Carga", "Archivo FXML del formulario de paciente no encontrado.", "No se pudo localizar: " + fxmlFormPath, Alert.AlertType.ERROR);
                }
                return;
            }

            FXMLLoader loader = new FXMLLoader(resourceUrl);
            Parent page = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle(paciente == null ? "Registrar Nuevo Paciente" : "Actualizar Paciente");
            dialogStage.initModality(Modality.WINDOW_MODAL);

            if (mainApp != null && mainApp.getPrimaryStage() != null) {
                dialogStage.initOwner(mainApp.getPrimaryStage());
            } else if (administradorViewController != null && administradorViewController.getMainApp() != null && administradorViewController.getMainApp().getPrimaryStage() != null) {
                dialogStage.initOwner(administradorViewController.getMainApp().getPrimaryStage());
            }

            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            PacienteFormController controller = loader.getController();
            if (controller == null) {
                System.err.println("Error Crítico: El controlador para " + fxmlFormPath + " es nulo. Verifica el fx:controller en el FXML PacienteFormView.fxml.");
                if (administradorViewController != null) {
                    administradorViewController.mostrarAlerta("Error de Carga", "No se pudo obtener el controlador del formulario de paciente.","", Alert.AlertType.ERROR);
                }
                return;
            }
            controller.setDialogStage(dialogStage);
            controller.setSistemaHospitalario(this.sistemaHospitalario);
            controller.setPacienteParaEdicion(paciente);
            controller.setAdminPacientesMainController(this);

            dialogStage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
            if (administradorViewController != null) {
                administradorViewController.mostrarAlerta("Error de Carga (IOException)", "No se pudo cargar el formulario del paciente.", e.getMessage(), Alert.AlertType.ERROR);
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (administradorViewController != null) {
                administradorViewController.mostrarAlerta("Error Inesperado", "Ocurrió un error al abrir el formulario de paciente.", e.getMessage(), Alert.AlertType.ERROR);
            }
        }
    }

    public void refrescarTablaPacientes() {
        cargarPacientesEnTabla();
    }
}
