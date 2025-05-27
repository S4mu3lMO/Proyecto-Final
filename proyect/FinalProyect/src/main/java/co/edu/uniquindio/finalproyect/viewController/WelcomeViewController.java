package co.edu.uniquindio.finalproyect.viewController;

import co.edu.uniquindio.finalproyect.application.App;
import co.edu.uniquindio.finalproyect.model.SistemaHospitalario;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class WelcomeViewController {

    @FXML
    private Label lblNombreHospital;

    @FXML
    private Button btnContinuarLogin;

    private App mainApp;
    private SistemaHospitalario sistemaHospitalario;

    public void setApp(App mainApp) {
        this.mainApp = mainApp;
        if (this.mainApp != null) {
            // Obtener la instancia del sistema hospitalario desde la App principal
            this.sistemaHospitalario = this.mainApp.getSistemaHospitalario();
            if (this.sistemaHospitalario != null && lblNombreHospital != null) {
                lblNombreHospital.setText(this.sistemaHospitalario.getNombre());
            } else if (lblNombreHospital != null) {
                lblNombreHospital.setText("Hospital General"); // Valor por defecto si algo falla
            }
        }
    }

    @FXML
    void handleContinuarLogin(ActionEvent event) {
        if (mainApp != null) {
            mainApp.mostrarLoginView(); // Llama al método en App para cambiar a la vista de login
        } else {
            System.err.println("Error: mainApp no fue inyectado en WelcomeViewController.");
            // Podrías mostrar una alerta al usuario aquí
        }
    }

    @FXML
    void initialize() {
        // Este método es llamado por FXMLLoader después de que los @FXML son inyectados.
        // Si lblNombreHospital no depende de sistemaHospitalario (que se setea en setApp),
        // podrías inicializarlo aquí. Pero como sí depende, es mejor en setApp.
    }
}