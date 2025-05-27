package co.edu.uniquindio.finalproyect.viewController;

import co.edu.uniquindio.finalproyect.application.App;
import co.edu.uniquindio.finalproyect.model.SistemaHospitalario;
import co.edu.uniquindio.finalproyect.model.Usuario;
// Importa tus controladores de sub-vistas aquí si necesitas una comprobación de tipo específica,
// pero si todos usan SubViewControllerBase, esa importación es la clave.
// Ejemplo: import co.edu.uniquindio.finalproyect.viewController.adminSubViews.AdminPacientesMainController;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import java.io.IOException;

public class AdministradorViewController {

    private App mainApp;
    private SistemaHospitalario sistemaHospitalario;
    private Usuario usuarioLogueado;

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
            // Cargar una vista por defecto, por ejemplo, la de pacientes.
            // Pasar null a ActionEvent si el método lo espera pero no es relevante aquí.
            handleModuloGestionPacientes(new ActionEvent());
        }
    }

    @FXML
    void initialize() {
        // El método initialize() es llamado por FXMLLoader después de inyectar los @FXML.
        // Lógica que no depende de mainApp o sistemaHospitalario puede ir aquí.
    }

    @FXML
    void handleModuloGestionPacientes(ActionEvent event) {
        System.out.println("Cargando módulo de Gestión de Pacientes...");
        // IMPORTANTE: Verifica que esta ruta sea correcta según tu estructura de carpetas.
        // Si AdminPacientesMainView.fxml está en /views/, la ruta sería "/views/AdminPacientesMainView.fxml"
        cargarSubVistaEnCentro("/views/AdminPacientesMainView.fxml");
    }

    @FXML
    void handleModuloGestionMedicos(ActionEvent event) {
        System.out.println("Cargando módulo de Gestión de Médicos...");
        // IMPORTANTE: Verifica la ruta
        cargarSubVistaEnCentro("/views/AdminMedicosMainView.fxml");
    }

    @FXML
    void handleModuloGestionSalasHorarios(ActionEvent event) {
        System.out.println("Cargando módulo de Gestión de Salas...");
        // IMPORTANTE: Verifica la ruta
        cargarSubVistaEnCentro("/views/AdminSalasMainView.fxml");
    }

    @FXML
    void handleModuloMonitoreoAsignacion(ActionEvent event) {
        System.out.println("Cargando módulo de Monitoreo y Asignación...");
        // IMPORTANTE: Verifica la ruta
        cargarSubVistaEnCentro("/views/AdminMonitoreoMainView.fxml");
    }

    @FXML
    void handleModuloReportes(ActionEvent event) {
        System.out.println("Cargando módulo de Reportes...");
        // IMPORTANTE: Verifica la ruta
        cargarSubVistaEnCentro("/views/AdminReportesMainView.fxml");
    }

    @FXML
    void handleCerrarSesion(ActionEvent event) {
        if (mainApp != null) {
            mainApp.mostrarLoginView();
        }
    }

    private void cargarSubVistaEnCentro(String fxmlPath) {
        try {
            // Asegurarse de que el ClassLoader pueda encontrar el recurso.
            // getClass().getResource(fxmlPath) busca desde la raíz del classpath si fxmlPath empieza con "/".
            if (getClass().getResource(fxmlPath) == null) {
                System.err.println("Error Crítico: No se encuentra el archivo FXML en la ruta: " + fxmlPath);
                mostrarAlerta("Error Fatal de Carga", "Archivo FXML no encontrado.", "No se pudo localizar el archivo: " + fxmlPath + ". Verifique la ruta.", Alert.AlertType.ERROR);
                return;
            }

            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Node subVista = loader.load();

            Object controller = loader.getController();
            if (controller instanceof SubViewControllerBase) {
                SubViewControllerBase subController = (SubViewControllerBase) controller;
                subController.setMainApp(mainApp);
                subController.setSistemaHospitalario(sistemaHospitalario);
                subController.setAdministradorViewController(this);
                subController.inicializarSubView(); // Llama al método de la interfaz para la inicialización post-inyección
            } else {
                System.out.println("Advertencia: El controlador para " + fxmlPath + " no implementa SubViewControllerBase. La inicialización estándar no se aplicará.");
                // Aquí podrías tener lógica específica si algunos controladores no usan la interfaz,
                // pero es mejor estandarizar con la interfaz.
            }

            if (mainAdminPanel != null) {
                mainAdminPanel.setCenter(subVista);
            } else {
                System.err.println("Error Crítico: mainAdminPanel es nulo. Asegúrate de que el fx:id='mainAdminPanel' esté correctamente asignado en AdminDashboard.fxml y que el controlador esté correctamente asociado.");
                mostrarAlerta("Error de Interfaz", "Panel principal no encontrado.", "No se pudo encontrar el área para cargar la vista.", Alert.AlertType.ERROR);

            }

        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlerta("Error de Carga de FXML", "No se pudo cargar la sub-vista: " + fxmlPath, "Detalle: " + e.getMessage(), Alert.AlertType.ERROR);
        } catch (NullPointerException e) { // Puede ocurrir si getResource retorna null y se intenta usar.
            e.printStackTrace();
            mostrarAlerta("Error de Recurso FXML", "Recurso FXML no encontrado: " + fxmlPath, "Verifica la ruta del archivo FXML. ¿Es correcta y el archivo existe? Ruta intentada: " + fxmlPath, Alert.AlertType.ERROR);
        } catch (Exception e) { // Captura general para otros errores inesperados durante la carga
            e.printStackTrace();
            mostrarAlerta("Error Inesperado", "Ocurrió un error al cargar la vista: " + fxmlPath, "Detalle: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    public void mostrarAlerta(String titulo, String cabecera, String contenido, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(cabecera);
        alert.setContentText(contenido);
        // Es bueno hacer la alerta modal a la ventana principal
        if (mainApp != null && mainApp.getPrimaryStage() != null) {
            alert.initOwner(mainApp.getPrimaryStage());
        }
        alert.showAndWait();
    }

    public App getMainApp() {
        return this.mainApp;
    }
}
