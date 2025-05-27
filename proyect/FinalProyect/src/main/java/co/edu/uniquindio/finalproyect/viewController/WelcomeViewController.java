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
            this.sistemaHospitalario = this.mainApp.getSistemaHospitalario();
            if (this.sistemaHospitalario != null && lblNombreHospital != null) {
                lblNombreHospital.setText(this.sistemaHospitalario.getNombre());
            } else if (lblNombreHospital != null) {
                lblNombreHospital.setText("Hospital General");
            }
        }
    }

    @FXML
    void handleContinuarLogin(ActionEvent event) {
        if (mainApp != null) {
            mainApp.mostrarLoginView();
        } else {
            System.err.println("Error: mainApp no fue inyectado en WelcomeViewController.");
        }
    }

    @FXML
    void initialize() {

    }
}