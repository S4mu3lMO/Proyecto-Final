package co.edu.uniquindio.finalproyect.viewController;

import co.edu.uniquindio.finalproyect.application.App;
import co.edu.uniquindio.finalproyect.model.SistemaHospitalario;

public interface SubViewControllerBase {
    void setMainApp(App mainApp);
    void setSistemaHospitalario(SistemaHospitalario sistema);
    void setAdministradorViewController(AdministradorViewController adminController); // Para comunicación
    void inicializarSubView(); // Para cargar datos o configurar la sub-vista
}