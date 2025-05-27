package co.edu.uniquindio.finalproyect.viewController.medicoSubViews; // O el paquete que uses

import co.edu.uniquindio.finalproyect.application.App;
import co.edu.uniquindio.finalproyect.model.Medico;
import co.edu.uniquindio.finalproyect.model.SistemaHospitalario;

public interface MedicoSubViewControllerBase {
    void setMainApp(App mainApp);
    void setSistemaHospitalario(SistemaHospitalario sistema);
    void setMedicoLogueado(Medico medico);
    void inicializarDatosSubVista();
}