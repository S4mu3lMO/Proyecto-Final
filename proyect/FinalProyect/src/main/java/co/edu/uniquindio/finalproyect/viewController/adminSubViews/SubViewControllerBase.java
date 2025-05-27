package co.edu.uniquindio.finalproyect.viewController.adminSubViews;

import co.edu.uniquindio.finalproyect.application.App;
import co.edu.uniquindio.finalproyect.model.SistemaHospitalario;
import co.edu.uniquindio.finalproyect.viewController.AdministradorViewController;

public interface SubViewControllerBase {
    void setMainApp(App mainApp);
    void setSistemaHospitalario(SistemaHospitalario sistema);
    void setAdministradorViewController(AdministradorViewController adminController);
    void inicializarSubView();
}