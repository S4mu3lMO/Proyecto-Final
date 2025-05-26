package co.edu.uniquindio.finalproyect.viewController;

import co.edu.uniquindio.finalproyect.application.App;
import co.edu.uniquindio.finalproyect.model.SistemaHospitalario;
import co.edu.uniquindio.finalproyect.model.Usuario;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
// Importa otros componentes que necesites

public class AdministradorViewController {

    private App mainApp;
    private SistemaHospitalario sistemaHospitalario;
    private Usuario usuarioLogueado; // Para saber quién es el administrador

    @FXML
    private Button btnRegistrarMedico;
    // Define más @FXML para tus botones y otros controles

    public void setApp(App mainApp) {
        this.mainApp = mainApp;
        this.sistemaHospitalario = mainApp.getSistemaHospitalario();
    }

    public void setUsuarioLogueado(Usuario usuarioLogueado) {
        this.usuarioLogueado = usuarioLogueado;
        // Aquí podrías, por ejemplo, personalizar algo basado en el admin específico
    }

    public void inicializarDatos(){
        // Cargar datos iniciales si es necesario, como listas en tablas, etc.
        System.out.println("Dashboard de Admin inicializado para: " + (usuarioLogueado != null ? usuarioLogueado.getNombre() : "N/A"));
    }

    @FXML
    void handleRegistrarMedico(ActionEvent event) {
        System.out.println("Botón Registrar Médico presionado.");
        // Lógica para mostrar una nueva ventana/vista para registrar médicos
        // Ejemplo: mainApp.mostrarVistaRegistroMedico();
    }

    @FXML
    void handleGestionarPacientes(ActionEvent event) {
        System.out.println("Botón Gestionar Pacientes presionado.");
        // Lógica para la gestión de pacientes
    }

    @FXML
    void handleGestionarSalas(ActionEvent event) {
        System.out.println("Botón Gestionar Salas presionado.");
        // Lógica para la gestión de salas
    }

    @FXML
    void handleGenerarReportes(ActionEvent event) {
        System.out.println("Botón Generar Reportes presionado.");
        // Lógica para la generación de reportes
    }

    @FXML
    void handleCerrarSesion(ActionEvent event) {
        if (mainApp != null) {
            // Opcional: Limpiar cualquier estado de sesión en el Singleton
            // SistemaHospitalarioSingleton.getInstance().cerrarSesion();
            mainApp.mostrarLoginView();
        }
    }


    @FXML
    void initialize() {
        // Código de inicialización si es necesario
    }
}
