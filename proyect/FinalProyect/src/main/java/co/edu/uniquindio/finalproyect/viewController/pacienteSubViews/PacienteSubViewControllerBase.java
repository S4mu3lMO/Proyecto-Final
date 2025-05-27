package co.edu.uniquindio.finalproyect.viewController.pacienteSubViews; // O el paquete que elijas

import co.edu.uniquindio.finalproyect.application.App;
import co.edu.uniquindio.finalproyect.model.Paciente;
import co.edu.uniquindio.finalproyect.model.SistemaHospitalario;

public interface PacienteSubViewControllerBase {
    void setMainApp(App mainApp);
    void setSistemaHospitalario(SistemaHospitalario sistema);
    void setPacienteLogueado(Paciente paciente); // Específico para sub-vistas de paciente
    void inicializarDatosSubVistaPaciente();
}
