// En PacienteViewController.java (Esqueleto)
package co.edu.uniquindio.finalproyect.viewController;

import co.edu.uniquindio.finalproyect.application.App;
import co.edu.uniquindio.finalproyect.model.Paciente; // Específico para el paciente
import co.edu.uniquindio.finalproyect.model.SistemaHospitalario;
import co.edu.uniquindio.finalproyect.model.Usuario;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
// Importa lo que necesites

public class PacienteViewController {
    private App mainApp;
    private SistemaHospitalario sistemaHospitalario;
    private Paciente pacienteLogueado;

    @FXML
    private Label lblNombrePaciente; // Ejemplo

    public void setApp(App mainApp) {
        this.mainApp = mainApp;
        this.sistemaHospitalario = mainApp.getSistemaHospitalario();
    }

    public void setUsuarioLogueado(Usuario usuarioLogueado) {
        if (usuarioLogueado instanceof Paciente) {
            this.pacienteLogueado = (Paciente) usuarioLogueado;
            if (lblNombrePaciente != null) {
                lblNombrePaciente.setText("Paciente: " + this.pacienteLogueado.getNombre());
            }
        }
    }

    public void inicializarDatos(){
        System.out.println("Dashboard de Paciente inicializado para: " + (pacienteLogueado != null ? pacienteLogueado.getNombre() : "N/A"));
        // Cargar citas pendientes, historial resumido, etc.
    }

    @FXML
    void handleActualizarDatos(ActionEvent event) {
        // Lógica para actualizar datos
    }

    @FXML
    void handleSolicitarCita(ActionEvent event) {
        // Lógica para solicitar cita
    }

    @FXML
    void handleVerHistorial(ActionEvent event) {
        // Lógica para ver historial
        // HistorialMedico historial = sistemaHospitalario.consultarHistorialMedico(pacienteLogueado.getCedula());
    }

    @FXML
    void handleCerrarSesionPaciente(ActionEvent event) {
        if (mainApp != null) {
            mainApp.mostrarLoginView();
        }
    }

    @FXML
    void initialize() {
    }
}