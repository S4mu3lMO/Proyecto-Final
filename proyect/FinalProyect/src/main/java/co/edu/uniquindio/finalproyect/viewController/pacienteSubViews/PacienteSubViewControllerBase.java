package co.edu.uniquindio.finalproyect.viewController.pacienteSubViews;

import co.edu.uniquindio.finalproyect.application.App;
import co.edu.uniquindio.finalproyect.model.Paciente;
import co.edu.uniquindio.finalproyect.model.SistemaHospitalario;

public interface PacienteSubViewControllerBase {
    void setMainApp(App mainApp);
    void setSistemaHospitalario(SistemaHospitalario sistema);
    void setPacienteLogueado(Paciente paciente);
    void inicializarDatosSubVistaPaciente();
}
