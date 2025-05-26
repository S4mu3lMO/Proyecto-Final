package co.edu.uniquindio.finalproyect.viewController;

import co.edu.uniquindio.finalproyect.application.App;
import co.edu.uniquindio.finalproyect.model.SistemaHospitalario;
import co.edu.uniquindio.finalproyect.model.Usuario;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane; // Asumiendo que AdminDashboard.fxml usa BorderPane
import java.io.IOException;

public class AdministradorViewController {

    private App mainApp;
    private SistemaHospitalario sistemaHospitalario;
    private Usuario usuarioLogueado;

    @FXML
    private BorderPane mainAdminPanel; // El fx:id del BorderPane en AdminDashboard.fxml

    @FXML
    private Label lblAdminNombre;

    // --- Botones del Menú Principal del Administrador ---
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
        // Opcionalmente, cargar una vista por defecto al iniciar
        if (btnModuloGestionPacientes != null) {
            handleModuloGestionPacientes(null); // Cargar gestión de pacientes por defecto
        }
    }

    @FXML
    void initialize() {
        // Puedes añadir listeners a los botones aquí si es necesario,
        // o configurar aspectos visuales iniciales.
    }

    // --- Manejadores para los botones del módulo principal ---

    @FXML
    void handleModuloGestionPacientes(ActionEvent event) {
        System.out.println("Cargando módulo de Gestión de Pacientes...");
        cargarSubVistaEnCentro("/views/admin/subviews/pacientes/AdminPacientesMainView.fxml");
    }

    @FXML
    void handleModuloGestionMedicos(ActionEvent event) {
        System.out.println("Cargando módulo de Gestión de Médicos...");
        cargarSubVistaEnCentro("/views/admin/subviews/medicos/AdminMedicosMainView.fxml");
    }

    @FXML
    void handleModuloGestionSalasHorarios(ActionEvent event) {
        System.out.println("Cargando módulo de Gestión de Salas y Horarios...");
        // Podrías tener una vista que luego se divida o tenga pestañas para Salas y Horarios de Atención
        cargarSubVistaEnCentro("/views/admin/subviews/salas/AdminSalasMainView.fxml");
        // Para horarios de atención (que son de médicos), podrías integrarlo en la gestión de médicos
        // o tener una vista específica aquí que primero pida seleccionar un médico.
    }

    @FXML
    void handleModuloMonitoreoAsignacion(ActionEvent event) {
        System.out.println("Cargando módulo de Monitoreo y Asignación...");
        cargarSubVistaEnCentro("/views/admin/subviews/monitoreo/AdminMonitoreoMainView.fxml");
    }

    @FXML
    void handleModuloReportes(ActionEvent event) {
        System.out.println("Cargando módulo de Reportes...");
        cargarSubVistaEnCentro("/views/admin/subviews/reportes/AdminReportesMainView.fxml");
    }

    @FXML
    void handleCerrarSesion(ActionEvent event) {
        if (mainApp != null) {
            mainApp.mostrarLoginView();
        }
    }

    // --- Método Auxiliar para Cargar Sub-Vistas en el Panel Central ---
    private void cargarSubVistaEnCentro(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Node subVista = loader.load();

            Object controller = loader.getController();
            if (controller instanceof SubViewControllerBase) {
                SubViewControllerBase subController = (SubViewControllerBase) controller;
                subController.setMainApp(mainApp);
                subController.setSistemaHospitalario(sistemaHospitalario);
                subController.setAdministradorViewController(this); // Pasar referencia del controlador padre
                subController.inicializarSubView();
            } else if (controller instanceof AdminPacientesMainController) { // Ejemplo específico
                AdminPacientesMainController pacController = (AdminPacientesMainController) controller;
                pacController.setMainApp(mainApp);
                pacController.setSistemaHospitalario(sistemaHospitalario);
                pacController.setAdministradorViewController(this);
                pacController.initialize(); // O un método específico de inicialización
            }
            // Añade más `else if` para otros tipos de controladores de sub-vistas si no usan la interfaz.


            if (mainAdminPanel != null) {
                mainAdminPanel.setCenter(subVista);
            } else {
                System.err.println("mainAdminPanel es nulo. Asegúrate de que el fx:id esté correctamente asignado en AdminDashboard.fxml.");
            }

        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlerta("Error de Carga", "No se pudo cargar la sub-vista: " + fxmlPath, e.getMessage(), Alert.AlertType.ERROR);
        } catch (NullPointerException e) {
            e.printStackTrace();
            mostrarAlerta("Error de Carga", "Recurso FXML no encontrado: " + fxmlPath, "Verifica la ruta del archivo FXML.", Alert.AlertType.ERROR);
        }
    }

    protected void mostrarAlerta(String titulo, String cabecera, String contenido, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(cabecera);
        alert.setContentText(contenido);
        alert.showAndWait();
    }
}