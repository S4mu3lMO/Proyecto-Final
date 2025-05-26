// En MedicoViewController.java (Esqueleto)
package co.edu.uniquindio.finalproyect.viewController;

import co.edu.uniquindio.finalproyect.application.App;
import co.edu.uniquindio.finalproyect.model.Medico; // Específico para el médico
import co.edu.uniquindio.finalproyect.model.SistemaHospitalario;
import co.edu.uniquindio.finalproyect.model.Usuario;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
// Importa TableView, etc., según necesites

public class MedicoViewController {
    private App mainApp;
    private SistemaHospitalario sistemaHospitalario;
    private Medico medicoLogueado; // Para acceder a datos específicos del médico

    @FXML
    private Label lblNombreMedico; // Ejemplo

    public void setApp(App mainApp) {
        this.mainApp = mainApp;
        this.sistemaHospitalario = mainApp.getSistemaHospitalario();
    }

    public void setUsuarioLogueado(Usuario usuarioLogueado) {
        if (usuarioLogueado instanceof Medico) {
            this.medicoLogueado = (Medico) usuarioLogueado;
            if (lblNombreMedico != null) {
                lblNombreMedico.setText("Dr(a). " + this.medicoLogueado.getNombre());
            }
        }
    }

    public void inicializarDatos(){
        // Cargar citas del médico, etc.
        System.out.println("Dashboard de Médico inicializado para: " + (medicoLogueado != null ? medicoLogueado.getNombre() : "N/A"));
        // Aquí podrías llamar a sistemaHospitalario.consultarCitasFuturasDeMedico(medicoLogueado.getCedula())
        // y poblar una TableView.
    }

    @FXML
    void handleVerHistoriales(ActionEvent event) {
        // Lógica para ver historiales
    }

    @FXML
    void handleRegistrarDiagnostico(ActionEvent event) {
        // Lógica para registrar diagnóstico
    }

    @FXML
    void handleGestionarHorarios(ActionEvent event) {
        // Lógica para gestionar horarios del médico logueado
    }

    @FXML
    void handleCerrarSesionMedico(ActionEvent event) {
        if (mainApp != null) {
            mainApp.mostrarLoginView();
        }
    }

    @FXML
    void initialize() {
    }
}
