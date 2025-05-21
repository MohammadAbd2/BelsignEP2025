package GUI.Controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class QAController {
    @FXML
    private Button previewButton;

    @FXML
    private void initialize() {
        previewButton.setOnAction(event -> openQCReport());
    }

    private void openQCReport() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/View/QCReport.fxml"));
            Stage stage = new Stage();
            stage.setTitle("QC Report Preview");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
