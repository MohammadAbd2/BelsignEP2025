package GUI.Controller;


import GUI.Model.MLLoginController;
import GUI.View.SceneManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {
    private final MLLoginController loginChoice = new MLLoginController();

    @FXML private Button adminButton;

    @FXML private Button operatorButton;

    @FXML private Button qaButton;

    @FXML private Button minimizeButton;

    @FXML private Button closeButton;

    @FXML private HBox titleBar;

    private Stage stage;

    @FXML
    public void initialize() {
        // Set up button actions
        adminButton.setOnAction(e -> {
            try {
                loginChoice.adminLogin();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        operatorButton.setOnAction(e -> {
            try {
                loginChoice.operatorLogin();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        qaButton.setOnAction(e -> loginChoice.qaLogin());


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

    @FXML
    public void adminLogin() throws IOException {
        loginChoice.adminLogin();
        SceneManager.loadScene("navBar", "/navbar.fxml");
        SceneManager.switchScene("navBar");

    }

    @FXML
    public void operatorLogin() throws IOException {
        loginChoice.operatorLogin();
        SceneManager.loadScene("operator", "/View/OperatorPage.fxml");
        SceneManager.switchScene("operator");

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

