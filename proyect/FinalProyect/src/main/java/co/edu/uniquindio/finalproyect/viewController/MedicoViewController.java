package co.edu.uniquindio.finalproyect.viewController;

import co.edu.uniquindio.finalproyect.application.App;
import co.edu.uniquindio.finalproyect.model.SistemaHospitalario;
import co.edu.uniquindio.finalproyect.singleton.SistemaHospitalarioSingleton;

public class MedicoViewController {
    private SistemaHospitalario sistemaHospitalario;

    public void setApp(App mainApp) { // O en el método initialize() si no se inyecta
        this.mainApp = mainApp;
        this.sistemaHospitalario = SistemaHospitalarioSingleton.getInstance().getSistemaHospitalario();
    }
}
