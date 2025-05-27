package co.edu.uniquindio.finalproyect.viewController;

import co.edu.uniquindio.finalproyect.application.App;
import co.edu.uniquindio.finalproyect.model.SistemaHospitalario;
import co.edu.uniquindio.finalproyect.model.Usuario;
import co.edu.uniquindio.finalproyect.viewController.adminSubViews.SubViewControllerBase;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import java.io.IOException;
import java.net.URL;

public class AdministradorViewController {

    private App mainApp;
    private SistemaHospitalario sistemaHospitalario;
    private Usuario usuarioLogueado;
    private final String ADMIN_SUBVIEWS_BASE_PATH = "/co/edu/uniquindio/finalproyect/views/";



    @FXML
    private BorderPane mainAdminPanel;

    @FXML
    private Label lblAdminNombre;

    @FXML private Button btnModuloGestionPacientes;
    @FXML private Button btnModuloGestionMedicos;
    @FXML private Button btnModuloGestionSalasHorarios;
    @FXML private Button btnModuloMonitoreoAsignacion;
    @FXML private Button btnModuloReportes;
    @FXML private Button btnCerrarSesion;


    public void setApp(App mainApp) {
        this.mainApp = mainApp;
        if (mainApp != null) {
            this.sistemaHospitalario = mainApp.getSistemaHospitalario();
        }
    }

    public void setUsuarioLogueado(Usuario usuarioLogueado) {
        this.usuarioLogueado = usuarioLogueado;
        if (lblAdminNombre != null && usuarioLogueado != null) {
            lblAdminNombre.setText("Admin: " + usuarioLogueado.getNombre());
        }
    }

    public void inicializarDatos() {
        System.out.println("Dashboard de Admin inicializado para: " + (usuarioLogueado != null ? usuarioLogueado.getNombre() : "N/A"));
        if (btnModuloGestionPacientes != null) {
            handleModuloGestionPacientes(new ActionEvent());
        }
    }

    @FXML
    void initialize() {
    }

    @FXML
    void handleModuloGestionPacientes(ActionEvent event) {
        System.out.println("Cargando módulo de Gestión de Pacientes...");
        cargarSubVistaEnCentro(ADMIN_SUBVIEWS_BASE_PATH + "AdminPacientesMainView.fxml");
    }

    @FXML
    void handleModuloGestionMedicos(ActionEvent event) {
        System.out.println("Cargando módulo de Gestión de Médicos...");
        cargarSubVistaEnCentro(ADMIN_SUBVIEWS_BASE_PATH + "AdminMedicosMainView.fxml");
    }

    @FXML
    void handleModuloGestionSalasHorarios(ActionEvent event) {
        System.out.println("Cargando módulo de Gestión de Salas...");
        cargarSubVistaEnCentro(ADMIN_SUBVIEWS_BASE_PATH + "AdminSalasMainView.fxml");
    }

    @FXML
    void handleModuloMonitoreoAsignacion(ActionEvent event) {
        System.out.println("Cargando módulo de Monitoreo y Asignación...");
        cargarSubVistaEnCentro(ADMIN_SUBVIEWS_BASE_PATH + "AdminMonitoreoMainView.fxml");
    }

    @FXML
    void handleModuloReportes(ActionEvent event) {
        System.out.println("Cargando módulo de Reportes...");
        cargarSubVistaEnCentro(ADMIN_SUBVIEWS_BASE_PATH + "AdminReportesMainView.fxml");
    }

    @FXML
    void handleCerrarSesion(ActionEvent event) {
        if (mainApp != null) {
            mainApp.mostrarLoginView();
        }
    }

    private void cargarSubVistaEnCentro(String fxmlPath) {
        try {
            URL resourceUrl = getClass().getResource(fxmlPath);
            if (resourceUrl == null) {
                System.err.println("Error Crítico en AdministradorViewController: No se encuentra el archivo FXML en la ruta: " + fxmlPath);
                mostrarAlerta("Error Fatal de Carga", "Archivo FXML de sub-vista no encontrado.", "No se pudo localizar el archivo: " + fxmlPath + ". Verifique la ruta.", Alert.AlertType.ERROR);
                return;
            }

            FXMLLoader loader = new FXMLLoader(resourceUrl);
            Node subVista = loader.load();

            Object controller = loader.getController();
            if (controller instanceof SubViewControllerBase) {
                SubViewControllerBase subController = (SubViewControllerBase) controller;
                subController.setMainApp(mainApp);
                subController.setSistemaHospitalario(sistemaHospitalario);
                subController.setAdministradorViewController(this);
                subController.inicializarSubView();
            } else {
                System.out.println("Advertencia: El controlador para " + fxmlPath + " no implementa SubViewControllerBase. La inicialización estándar no se aplicará.");
            }

            if (mainAdminPanel != null) {
                mainAdminPanel.setCenter(subVista);
            } else {
                System.err.println("Error Crítico en AdministradorViewController: mainAdminPanel es nulo. Asegúrate de que el fx:id='mainAdminPanel' esté correctamente asignado en AdminDashboard.fxml y que el controlador esté correctamente asociado.");
                mostrarAlerta("Error de Interfaz", "Panel principal no encontrado.", "No se pudo encontrar el área para cargar la vista.", Alert.AlertType.ERROR);
            }

        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlerta("Error de Carga de FXML", "No se pudo cargar la sub-vista: " + fxmlPath, "Detalle: " + e.getMessage(), Alert.AlertType.ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Error Inesperado", "Ocurrió un error al cargar la vista: " + fxmlPath, "Detalle: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    public void mostrarAlerta(String titulo, String cabecera, String contenido, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(cabecera);
        alert.setContentText(contenido);
        if (mainApp != null && mainApp.getPrimaryStage() != null) {
            alert.initOwner(mainApp.getPrimaryStage());
        }
        alert.showAndWait();
    }

    public App getMainApp() {
        return this.mainApp;
    }
}
