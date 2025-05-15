package GUI.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class CustomTitleBarController {

    private Stage stage;
    @FXML
    private HBox titleBar;
    @FXML private Button minimizeButton;

    @FXML private Button closeButton;


    public void initialize(){
        // Variables to store initial mouse position
        final double[] xOffset = {0};
        final double[] yOffset = {0};

// Record the current mouse position on press
        titleBar.setOnMousePressed(event -> {
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            xOffset[0] = stage.getX() - event.getScreenX();
            yOffset[0] = stage.getY() - event.getScreenY();
        });

// Update the stage position on drag
        titleBar.setOnMouseDragged(event -> {
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setX(event.getScreenX() + xOffset[0]);
            stage.setY(event.getScreenY() + yOffset[0]);
        });


    }


    public void closeStage(ActionEvent event) {
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    public void minimizeStage(ActionEvent event) {
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setIconified(true);
    }
}
