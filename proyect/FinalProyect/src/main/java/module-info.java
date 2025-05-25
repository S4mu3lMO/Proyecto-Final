module co.edu.uniquindio.finalproyect {
    requires javafx.controls;
    requires javafx.fxml;


    opens co.edu.uniquindio.finalproyect to javafx.fxml;
    exports co.edu.uniquindio.finalproyect;
}