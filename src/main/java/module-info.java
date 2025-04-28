module dk.easv.belsignep2025 {
    requires javafx.controls;
    requires javafx.fxml;

    exports dk.easv.belsignep2025;
    exports controllers;

    requires org.kordamp.bootstrapfx.core;

    opens dk.easv.belsignep2025 to javafx.fxml;
    opens controllers to javafx.fxml;
}