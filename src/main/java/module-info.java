module at.limpidness.juliaset {
    requires javafx.graphics;
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.swing;


    opens at.limpidness.juliaset to javafx.fxml;
    exports at.limpidness.juliaset;
}