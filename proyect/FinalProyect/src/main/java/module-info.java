module co.edu.uniquindio.finalproyect {
    requires javafx.controls;
    requires javafx.fxml;

    opens co.edu.uniquindio.finalproyect.application to javafx.fxml;
    opens co.edu.uniquindio.finalproyect.viewController to javafx.fxml;

    exports co.edu.uniquindio.finalproyect.application;
    exports co.edu.uniquindio.finalproyect.controller;
    exports co.edu.uniquindio.finalproyect.model;
    exports co.edu.uniquindio.finalproyect.singleton;
}