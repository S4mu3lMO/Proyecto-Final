module co.edu.uniquindio.finalproyect {

    requires javafx.controls;
    requires javafx.fxml;

    opens co.edu.uniquindio.finalproyect.application to javafx.fxml, javafx.graphics;

    opens co.edu.uniquindio.finalproyect.viewController to javafx.fxml;
    opens co.edu.uniquindio.finalproyect.viewController.adminSubViews to javafx.fxml;
    opens co.edu.uniquindio.finalproyect.viewController.adminSubViews.forms to javafx.fxml;
    opens co.edu.uniquindio.finalproyect.viewController.medicoSubViews to javafx.fxml;
    opens co.edu.uniquindio.finalproyect.viewController.pacienteSubViews to javafx.fxml;


    exports co.edu.uniquindio.finalproyect.model;
    exports co.edu.uniquindio.finalproyect.singleton;
    exports co.edu.uniquindio.finalproyect.application;
}
