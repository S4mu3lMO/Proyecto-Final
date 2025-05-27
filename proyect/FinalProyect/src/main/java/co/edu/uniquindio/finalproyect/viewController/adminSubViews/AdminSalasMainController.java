package co.edu.uniquindio.finalproyect.viewController.adminSubViews; // O el paquete que uses

import co.edu.uniquindio.finalproyect.application.App;
import co.edu.uniquindio.finalproyect.model.Sala;
import co.edu.uniquindio.finalproyect.model.SistemaHospitalario;
import co.edu.uniquindio.finalproyect.model.TipoSala; // Para la columna de la tabla
import co.edu.uniquindio.finalproyect.viewController.AdministradorViewController;
import co.edu.uniquindio.finalproyect.viewController.SubViewControllerBase; // Tu interfaz base
import co.edu.uniquindio.finalproyect.viewController.adminSubViews.forms.SalaFormController;

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
import java.util.Optional;

public class AdminSalasMainController implements SubViewControllerBase {

    private App mainApp;
    private SistemaHospitalario sistemaHospitalario;
    private AdministradorViewController administradorViewController;

    @FXML private TableView<Sala> tablaSalas;
    @FXML private TableColumn<Sala, String> colIdSala;
    @FXML private TableColumn<Sala, String> colNumeroSala;
    @FXML private TableColumn<Sala, TipoSala> colTipoSala;
    @FXML private TableColumn<Sala, Integer> colCapacidadSala;
    @FXML private TableColumn<Sala, Boolean> colDisponibleSala;

    @FXML private Button btnCrearSala;
    @FXML private Button btnActualizarSala;
    @FXML private Button btnEliminarSala;

    private ObservableList<Sala> listaObservableSalas;

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
        colIdSala.setCellValueFactory(new PropertyValueFactory<>("idSala")); //
        colNumeroSala.setCellValueFactory(new PropertyValueFactory<>("numeroSala")); //
        colTipoSala.setCellValueFactory(new PropertyValueFactory<>("tipoSala")); //
        colCapacidadSala.setCellValueFactory(new PropertyValueFactory<>("capacidad")); //
        colDisponibleSala.setCellValueFactory(new PropertyValueFactory<>("estaDisponible")); //

        cargarSalasEnTabla();
    }

    @FXML
    public void initialize() {
        // Este método es llamado por FXMLLoader.
        // La inicialización principal que depende de referencias externas está en inicializarSubView().
    }

    private void cargarSalasEnTabla() {
        if (sistemaHospitalario != null) {
            listaObservableSalas = FXCollections.observableArrayList(sistemaHospitalario.getListSalas()); //
            tablaSalas.setItems(listaObservableSalas);
            tablaSalas.refresh();
        } else {
            if (administradorViewController != null)
                administradorViewController.mostrarAlerta("Error", "Error Interno", "No se pudo acceder a los datos del sistema para cargar las salas.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    void handleCrearSala(ActionEvent event) {
        mostrarDialogoFormularioSala(null); // null indica que es para una nueva sala
    }

    @FXML
    void handleActualizarSala(ActionEvent event) {
        Sala salaSeleccionada = tablaSalas.getSelectionModel().getSelectedItem();
        if (salaSeleccionada != null) {
            mostrarDialogoFormularioSala(salaSeleccionada);
        } else {
            if (administradorViewController != null)
                administradorViewController.mostrarAlerta("Sin Selección", "No hay sala seleccionada", "Por favor, seleccione una sala de la tabla para actualizar.", Alert.AlertType.WARNING);
        }
    }

    @FXML
    void handleEliminarSala(ActionEvent event) {
        Sala salaSeleccionada = tablaSalas.getSelectionModel().getSelectedItem();
        if (salaSeleccionada != null) {
            Alert alertConfirmacion = new Alert(Alert.AlertType.CONFIRMATION);
            alertConfirmacion.setTitle("Confirmar Eliminación");
            alertConfirmacion.setHeaderText("Eliminar Sala: " + salaSeleccionada.getNumeroSala());
            alertConfirmacion.setContentText("¿Está seguro de que desea eliminar esta sala? Verifique que no tenga citas futuras asociadas.");
            Optional<ButtonType> resultado = alertConfirmacion.showAndWait();

            if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
                boolean eliminada = sistemaHospitalario.eliminarSala(salaSeleccionada.getIdSala()); //
                if (eliminada) {
                    if (administradorViewController != null)
                        administradorViewController.mostrarAlerta("Éxito", "Sala Eliminada", "La sala " + salaSeleccionada.getNumeroSala() + " ha sido eliminada.", Alert.AlertType.INFORMATION);
                    cargarSalasEnTabla(); // Refrescar la tabla
                } else {
                    // El método eliminarSala en SistemaHospitalario ya imprime un mensaje si no se puede eliminar debido a citas.
                    if (administradorViewController != null)
                        administradorViewController.mostrarAlerta("Error", "Error al Eliminar", "No se pudo eliminar la sala. Es posible que tenga citas futuras activas o no fue encontrada.", Alert.AlertType.ERROR);
                }
            }
        } else {
            if (administradorViewController != null)
                administradorViewController.mostrarAlerta("Sin Selección", "No hay sala seleccionada", "Por favor, seleccione una sala de la tabla para eliminar.", Alert.AlertType.WARNING);
        }
    }

    private void mostrarDialogoFormularioSala(Sala sala) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/SalaFormView.fxml")); // Asegúrate que la ruta sea correcta
            Parent page = loader.load();
            Stage dialogStage = new Stage();
            dialogStage.setTitle(sala == null ? "Registrar Nueva Sala" : "Actualizar Sala");
            dialogStage.initModality(Modality.WINDOW_MODAL);

            // Establecer el owner para el diálogo modal
            if (mainApp != null && mainApp.getPrimaryStage() != null) {
                dialogStage.initOwner(mainApp.getPrimaryStage());
            } else if (administradorViewController != null && administradorViewController.getMainApp() != null && administradorViewController.getMainApp().getPrimaryStage() != null) {
                // Si mainApp no está directamente disponible, intentar obtenerlo a través del admin controller
                dialogStage.initOwner(administradorViewController.getMainApp().getPrimaryStage());
            }


            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            SalaFormController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setSistemaHospitalario(this.sistemaHospitalario);
            controller.setSalaParaEdicion(sala); // Pasar la sala para edición (o null si es nueva)
            controller.setAdminSalasMainController(this); // Para poder refrescar la tabla

            dialogStage.showAndWait();
            // La tabla se refrescará desde SalaFormController si la operación es exitosa
        } catch (IOException e) {
            e.printStackTrace();
            if (administradorViewController != null)
                administradorViewController.mostrarAlerta("Error de Carga", "No se pudo cargar el formulario de la sala.", e.getMessage(), Alert.AlertType.ERROR);
        } catch (NullPointerException e) {
            e.printStackTrace();
            if (administradorViewController != null)
                administradorViewController.mostrarAlerta("Error de Carga", "Recurso FXML del formulario de sala no encontrado.", "Verifique la ruta del archivo: /views/SalaFormView.fxml", Alert.AlertType.ERROR);
        }
    }

    // Este método puede ser llamado por SalaFormController después de una operación exitosa
    public void refrescarTablaSalas() {
        cargarSalasEnTabla();
    }
}