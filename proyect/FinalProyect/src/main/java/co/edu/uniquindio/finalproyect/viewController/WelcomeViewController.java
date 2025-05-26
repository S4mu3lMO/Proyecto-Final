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
    private Button btnContinuar;

    private App mainApp;
    private SistemaHospitalario sistemaHospitalario;

    public void setApp(App mainApp) {
        this.mainApp = mainApp;
        this.sistemaHospitalario = mainApp.getSistemaHospitalario(); // Obtener la instancia
        if (this.sistemaHospitalario != null) {
            lblNombreHospital.setText(this.sistemaHospitalario.getNombre());
        }
    }

    @FXML
    void handleContinuarButton(ActionEvent event) {
        if (mainApp != null) {
            mainApp.mostrarLoginView();
        }
    }

    @FXML
    void initialize() {
    }
}