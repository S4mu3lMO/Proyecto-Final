package co.edu.uniquindio.finalproyect.viewController.adminSubViews; // Nueva subcarpeta para claridad

import co.edu.uniquindio.finalproyect.application.App;
import co.edu.uniquindio.finalproyect.model.Paciente;
import co.edu.uniquindio.finalproyect.model.Sexo;
import co.edu.uniquindio.finalproyect.model.SistemaHospitalario;
import co.edu.uniquindio.finalproyect.model.TipoUsuario; // Necesario para crear Paciente
import co.edu.uniquindio.finalproyect.viewController.AdministradorViewController;
import co.edu.uniquindio.finalproyect.viewController.SubViewControllerBase; // Si la usas
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;


public class AdminPacientesMainController implements SubViewControllerBase { // Implementa la interfaz

    private App mainApp;
    private SistemaHospitalario sistemaHospitalario;
    private AdministradorViewController administradorViewController; // Referencia al controlador padre

    @FXML private TableView<Paciente> tablaPacientes;
    @FXML private TableColumn<Paciente, String> colCedulaPaciente;
    @FXML private TableColumn<Paciente, String> colNombrePaciente;
    @FXML private TableColumn<Paciente, Integer> colEdadPaciente;
    @FXML private TableColumn<Paciente, Sexo> colSexoPaciente;
    @FXML private TableColumn<Paciente, String> colUsuarioPaciente;
    // Añade más columnas según necesites

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
        // Configurar las columnas de la tabla
        colCedulaPaciente.setCellValueFactory(new PropertyValueFactory<>("cedula"));
        colNombrePaciente.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colEdadPaciente.setCellValueFactory(new PropertyValueFactory<>("edad"));
        colSexoPaciente.setCellValueFactory(new PropertyValueFactory<>("sexo"));
        colUsuarioPaciente.setCellValueFactory(new PropertyValueFactory<>("nombreUsuario"));
        // ... configura las demás columnas

        cargarPacientesEnTabla();
    }

    @FXML
    public void initialize() { // Este método es llamado por FXMLLoader después de inyectar los @FXML
        // Es mejor llamar a inicializarSubView() desde el AdministradorViewController
        // después de que todas las dependencias (mainApp, sistemaHospitalario) estén seteadas.
        // Pero si no usas SubViewControllerBase, puedes poner la lógica de inicialización aquí
        // siempre y cuando las dependencias se inyecten antes.
    }


    private void cargarPacientesEnTabla() {
        if (sistemaHospitalario != null) {
            listaObservablePacientes = FXCollections.observableArrayList(sistemaHospitalario.getListPacientes());
            tablaPacientes.setItems(listaObservablePacientes);
            tablaPacientes.refresh();
        } else {
            System.err.println("SistemaHospitalario es nulo en AdminPacientesMainController.");
        }
    }

    @FXML
    void handleCrearPaciente(ActionEvent event) {
        System.out.println("Crear nuevo paciente...");
        // Aquí llamarías a una nueva ventana/dialogo para el formulario de creación
        // Usando el método que podrías definir en AdministradorViewController
        // o creando uno nuevo aquí si la lógica es específica.
        mostrarDialogoFormularioPaciente(null); // null para nuevo paciente
    }

    @FXML
    void handleActualizarPaciente(ActionEvent event) {
        Paciente pacienteSeleccionado = tablaPacientes.getSelectionModel().getSelectedItem();
        if (pacienteSeleccionado != null) {
            System.out.println("Actualizar paciente: " + pacienteSeleccionado.getNombre());
            mostrarDialogoFormularioPaciente(pacienteSeleccionado);
        } else {
            administradorViewController.mostrarAlerta("Sin Selección", "No hay paciente seleccionado",
                    "Por favor, seleccione un paciente de la tabla para actualizar.", Alert.AlertType.WARNING);
        }
    }

    @FXML
    void handleEliminarPaciente(ActionEvent event) {
        Paciente pacienteSeleccionado = tablaPacientes.getSelectionModel().getSelectedItem();
        if (pacienteSeleccionado != null) {
            // Confirmación antes de eliminar
            Alert alertConfirmacion = new Alert(Alert.AlertType.CONFIRMATION);
            alertConfirmacion.setTitle("Confirmar Eliminación");
            alertConfirmacion.setHeaderText("Eliminar Paciente: " + pacienteSeleccionado.getNombre());
            alertConfirmacion.setContentText("¿Está seguro de que desea eliminar a este paciente? Esta acción no se puede deshacer.");

            Optional<javafx.scene.control.ButtonType> resultado = alertConfirmacion.showAndWait();
            if (resultado.isPresent() && resultado.get() == javafx.scene.control.ButtonType.OK) {
                boolean eliminado = sistemaHospitalario.eliminarPaciente(pacienteSeleccionado.getCedula());
                if (eliminado) {
                    administradorViewController.mostrarAlerta("Éxito", "Paciente Eliminado",
                            "El paciente " + pacienteSeleccionado.getNombre() + " ha sido eliminado.", Alert.AlertType.INFORMATION);
                    cargarPacientesEnTabla(); // Recargar la lista
                } else {
                    administradorViewController.mostrarAlerta("Error", "Error al Eliminar",
                            "No se pudo eliminar el paciente. Verifique la consola para más detalles.", Alert.AlertType.ERROR);
                }
            }
        } else {
            administradorViewController.mostrarAlerta("Sin Selección", "No hay paciente seleccionado",
                    "Por favor, seleccione un paciente de la tabla para eliminar.", Alert.AlertType.WARNING);
        }
    }

    // Método para mostrar el diálogo del formulario de paciente (Crear/Actualizar)
    private void mostrarDialogoFormularioPaciente(Paciente paciente) {
        try {
            FXMLLoader loader = new FXMLLoader();
            // Asegúrate de que la ruta sea correcta y que este FXML exista
            loader.setLocation(getClass().getResource("/views/admin/forms/PacienteFormView.fxml"));
            Parent page = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle(paciente == null ? "Registrar Nuevo Paciente" : "Actualizar Paciente");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            // Establecer el owner puede ser desde mainApp.getPrimaryStage() o desde el stage actual si lo tienes
            if (mainApp != null) dialogStage.initOwner(mainApp.getPrimaryStage());


            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            // Obtener el controlador del formulario
            PacienteFormController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setSistemaHospitalario(this.sistemaHospitalario); // Pasar la instancia
            controller.setPacienteParaEdicion(paciente); // Si es null, es para crear uno nuevo
            controller.setAdminPacientesMainController(this); // Para refrescar la tabla después

            dialogStage.showAndWait();

            // No necesitas verificar isOkClicked si el formulario mismo guarda y cierra.
            // La actualización de la tabla se hará desde el PacienteFormController si la operación es exitosa.

        } catch (IOException e) {
            e.printStackTrace();
            administradorViewController.mostrarAlerta("Error de Carga", "No se pudo cargar el formulario del paciente.", e.getMessage(), Alert.AlertType.ERROR);
        } catch (NullPointerException e) {
            e.printStackTrace();
            administradorViewController.mostrarAlerta("Error de Carga", "Recurso FXML del formulario no encontrado.", "Verifique la ruta del archivo FXML del formulario.", Alert.AlertType.ERROR);

        }
    }

    // Este método puede ser llamado por PacienteFormController después de una operación exitosa
    public void refrescarTablaPacientes() {
        cargarPacientesEnTabla();
    }
}
