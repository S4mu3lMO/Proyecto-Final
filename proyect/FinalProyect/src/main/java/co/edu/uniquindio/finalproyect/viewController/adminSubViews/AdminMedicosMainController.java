package co.edu.uniquindio.finalproyect.viewController.adminSubViews;

import co.edu.uniquindio.finalproyect.application.App;
import co.edu.uniquindio.finalproyect.model.Medico;
import co.edu.uniquindio.finalproyect.model.SistemaHospitalario;
import co.edu.uniquindio.finalproyect.viewController.AdministradorViewController;
import co.edu.uniquindio.finalproyect.viewController.adminSubViews.SubViewControllerBase;
import co.edu.uniquindio.finalproyect.viewController.adminSubViews.forms.MedicoFormController;
import co.edu.uniquindio.finalproyect.viewController.adminSubViews.forms.MedicoHorariosFormController;

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

public class AdminMedicosMainController implements SubViewControllerBase {

    private App mainApp;
    private SistemaHospitalario sistemaHospitalario;
    private AdministradorViewController administradorViewController;

    private final String VIEWS_BASE_PATH = "/co/edu/uniquindio/finalproyect/views/";

    @FXML private TableView<Medico> tablaMedicos;
    @FXML private TableColumn<Medico, String> colCedulaMedico;
    @FXML private TableColumn<Medico, String> colNombreMedico;
    @FXML private TableColumn<Medico, String> colEspecialidadMedico;
    @FXML private TableColumn<Medico, String> colUsuarioMedico;
    @FXML private TableColumn<Medico, String> colLicenciaMedico;

    @FXML private Button btnCrearMedico;
    @FXML private Button btnActualizarMedico;
    @FXML private Button btnEliminarMedico;
    @FXML private Button btnGestionarHorarios;

    private ObservableList<Medico> listaObservableMedicos;

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
        colCedulaMedico.setCellValueFactory(new PropertyValueFactory<>("cedula"));
        colNombreMedico.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colEspecialidadMedico.setCellValueFactory(new PropertyValueFactory<>("especialidad"));
        colUsuarioMedico.setCellValueFactory(new PropertyValueFactory<>("nombreUsuario"));
        colLicenciaMedico.setCellValueFactory(new PropertyValueFactory<>("numeroLicenciaMedica"));
        cargarMedicosEnTabla();
    }

    @FXML
    public void initialize() {
    }

    private void cargarMedicosEnTabla() {
        if (sistemaHospitalario != null) {
            listaObservableMedicos = FXCollections.observableArrayList(sistemaHospitalario.getListMedicos());
            tablaMedicos.setItems(listaObservableMedicos);
            tablaMedicos.refresh();
        } else {
            System.err.println("SistemaHospitalario es nulo en AdminMedicosMainController.");
            if (administradorViewController != null) {
                administradorViewController.mostrarAlerta("Error", "Error Interno", "No se pudo acceder a los datos del sistema (médicos).", Alert.AlertType.ERROR);
            }
        }
    }

    @FXML
    void handleCrearMedico(ActionEvent event) {
        System.out.println("Botón Crear Médico presionado.");
        mostrarDialogoFormularioMedico(null);
    }

    @FXML
    void handleActualizarMedico(ActionEvent event) {
        Medico medicoSeleccionado = tablaMedicos.getSelectionModel().getSelectedItem();
        if (medicoSeleccionado != null) {
            System.out.println("Actualizar médico: " + medicoSeleccionado.getNombre());
            mostrarDialogoFormularioMedico(medicoSeleccionado);
        } else {
            if (administradorViewController != null)
                administradorViewController.mostrarAlerta("Sin Selección", "No hay médico seleccionado", "Por favor, seleccione un médico de la tabla para actualizar.", Alert.AlertType.WARNING);
        }
    }

    @FXML
    void handleEliminarMedico(ActionEvent event) {
        Medico medicoSeleccionado = tablaMedicos.getSelectionModel().getSelectedItem();
        if (medicoSeleccionado != null) {
            Alert alertConfirmacion = new Alert(Alert.AlertType.CONFIRMATION);
            alertConfirmacion.setTitle("Confirmar Eliminación");
            alertConfirmacion.setHeaderText("Eliminar Médico: " + medicoSeleccionado.getNombre());
            alertConfirmacion.setContentText("¿Está seguro de que desea eliminar a este médico? Esta acción también eliminará sus citas asociadas.");

            if (mainApp != null && mainApp.getPrimaryStage() != null) {
                alertConfirmacion.initOwner(mainApp.getPrimaryStage());
            } else if (administradorViewController != null && administradorViewController.getMainApp() != null && administradorViewController.getMainApp().getPrimaryStage() != null){
                alertConfirmacion.initOwner(administradorViewController.getMainApp().getPrimaryStage());
            }

            Optional<ButtonType> resultado = alertConfirmacion.showAndWait();
            if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
                boolean eliminado = sistemaHospitalario.eliminarMedico(medicoSeleccionado.getCedula());
                if (eliminado) {
                    if (administradorViewController != null)
                        administradorViewController.mostrarAlerta("Éxito", "Médico Eliminado", "El médico " + medicoSeleccionado.getNombre() + " ha sido eliminado.", Alert.AlertType.INFORMATION);
                    cargarMedicosEnTabla();
                } else {
                    if (administradorViewController != null)
                        administradorViewController.mostrarAlerta("Error", "Error al Eliminar", "No se pudo eliminar el médico. Verifique la consola.", Alert.AlertType.ERROR);
                }
            }
        } else {
            if (administradorViewController != null)
                administradorViewController.mostrarAlerta("Sin Selección", "No hay médico seleccionado", "Por favor, seleccione un médico de la tabla para eliminar.", Alert.AlertType.WARNING);
        }
    }

    @FXML
    void handleGestionarHorarios(ActionEvent event) {
        Medico medicoSeleccionado = tablaMedicos.getSelectionModel().getSelectedItem();
        if (medicoSeleccionado != null) {
            System.out.println("Gestionar horarios para: " + medicoSeleccionado.getNombre());
            mostrarDialogoGestionHorarios(medicoSeleccionado);
        } else {
            if (administradorViewController != null)
                administradorViewController.mostrarAlerta("Sin Selección", "No hay médico seleccionado", "Por favor, seleccione un médico para gestionar sus horarios.", Alert.AlertType.WARNING);
        }
    }

    private void mostrarDialogoFormularioMedico(Medico medico) {
        try {
            String fxmlFormPath = VIEWS_BASE_PATH + "MedicoFormView.fxml";
            URL resourceUrl = getClass().getResource(fxmlFormPath);

            if (resourceUrl == null) {
                System.err.println("Error Crítico en AdminMedicosMainController: No se encuentra el FXML del formulario de médico en: " + fxmlFormPath);
                if (administradorViewController != null) {
                    administradorViewController.mostrarAlerta("Error Fatal de Carga", "Archivo FXML del formulario de médico no encontrado.", "No se pudo localizar: " + fxmlFormPath, Alert.AlertType.ERROR);
                }
                return;
            }

            FXMLLoader loader = new FXMLLoader(resourceUrl);
            Parent page = loader.load();
            Stage dialogStage = new Stage();
            dialogStage.setTitle(medico == null ? "Registrar Nuevo Médico" : "Actualizar Médico");
            dialogStage.initModality(Modality.WINDOW_MODAL);

            if (mainApp != null && mainApp.getPrimaryStage() !=null ) {
                dialogStage.initOwner(mainApp.getPrimaryStage());
            } else if (administradorViewController != null && administradorViewController.getMainApp() != null && administradorViewController.getMainApp().getPrimaryStage() != null) {
                dialogStage.initOwner(administradorViewController.getMainApp().getPrimaryStage());
            }

            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            MedicoFormController controller = loader.getController();
            if (controller == null) {
                System.err.println("Error Crítico: El controlador para " + fxmlFormPath + " es nulo. Verifica el fx:controller en MedicoFormView.fxml.");
                if (administradorViewController != null) {
                    administradorViewController.mostrarAlerta("Error de Carga", "No se pudo obtener el controlador del formulario de médico.", "",Alert.AlertType.ERROR);
                }
                return;
            }
            controller.setDialogStage(dialogStage);
            controller.setSistemaHospitalario(this.sistemaHospitalario);
            controller.setMedicoParaEdicion(medico);
            controller.setAdminMedicosMainController(this);

            dialogStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
            if (administradorViewController != null)
                administradorViewController.mostrarAlerta("Error de Carga (IOException)", "No se pudo cargar el formulario del médico.", e.getMessage(), Alert.AlertType.ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            if (administradorViewController != null)
                administradorViewController.mostrarAlerta("Error Inesperado", "Ocurrió un error al abrir el formulario de médico.", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void mostrarDialogoGestionHorarios(Medico medico) {
        try {
            String fxmlHorariosPath = VIEWS_BASE_PATH + "MedicoHorariosFormView.fxml"; // RUTA CORREGIDA
            URL resourceUrl = getClass().getResource(fxmlHorariosPath);

            if (resourceUrl == null) {
                System.err.println("Error Crítico en AdminMedicosMainController: No se encuentra el FXML del formulario de horarios en: " + fxmlHorariosPath);
                if (administradorViewController != null) {
                    administradorViewController.mostrarAlerta("Error Fatal de Carga", "Archivo FXML del formulario de horarios no encontrado.", "No se pudo localizar: " + fxmlHorariosPath, Alert.AlertType.ERROR);
                }
                return;
            }

            FXMLLoader loader = new FXMLLoader(resourceUrl);
            Parent page = loader.load();
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Gestionar Horarios de Dr(a). " + medico.getNombre());
            dialogStage.initModality(Modality.WINDOW_MODAL);

            if (mainApp != null && mainApp.getPrimaryStage() !=null ) {
                dialogStage.initOwner(mainApp.getPrimaryStage());
            } else if (administradorViewController != null && administradorViewController.getMainApp() != null && administradorViewController.getMainApp().getPrimaryStage() != null) {
                dialogStage.initOwner(administradorViewController.getMainApp().getPrimaryStage());
            }

            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            MedicoHorariosFormController controller = loader.getController();
            if (controller == null) {
                System.err.println("Error Crítico: El controlador para " + fxmlHorariosPath + " es nulo. Verifica el fx:controller en MedicoHorariosFormView.fxml.");
                if (administradorViewController != null) {
                    administradorViewController.mostrarAlerta("Error de Carga", "No se pudo obtener el controlador del formulario de horarios.", "",Alert.AlertType.ERROR);
                }
                return;
            }
            controller.setDialogStage(dialogStage);
            controller.setSistemaHospitalario(this.sistemaHospitalario);
            controller.setMedico(medico);
            controller.inicializarVistaHorarios();

            dialogStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
            if (administradorViewController != null)
                administradorViewController.mostrarAlerta("Error de Carga (IOException)", "No se pudo cargar el formulario de gestión de horarios.", e.getMessage(), Alert.AlertType.ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            if (administradorViewController != null)
                administradorViewController.mostrarAlerta("Error Inesperado", "Ocurrió un error al abrir el formulario de horarios.", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    public void refrescarTablaMedicos() {
        cargarMedicosEnTabla();
    }
}
