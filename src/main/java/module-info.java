module dk.easv.belsignep2025 {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;

    opens dk.easv.belsignep2025 to javafx.fxml;
    exports dk.easv.belsignep2025;
}