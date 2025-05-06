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
        operatorButton.setOnAction(e -> loginChoice.operatorLogin());
        qaButton.setOnAction(e -> loginChoice.qaLogin());

    }

    @FXML
    public void adminLogin() throws IOException {
        loginChoice.adminLogin();
        SceneManager.loadScene("navBar", "/navbar.fxml");
        SceneManager.switchScene("navBar");

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

