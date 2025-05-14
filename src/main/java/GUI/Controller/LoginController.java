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





}

