package GUI.Controller;


import GUI.Model.MLLoginController;
import GUI.View.SceneManager;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

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
        qaButton.setOnAction(e -> {
            try {
                loginChoice.qaLogin();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });


    }

    @FXML
    public void adminLogin() throws IOException {
        loginChoice.adminLogin();
        SceneManager.loadScene("navBar", "/NavBar.fxml");
        SceneManager.switchScene("navBar");

    }

    @FXML
    public void operatorLogin() throws IOException {
        loginChoice.operatorLogin();
        SceneManager.loadScene("operator", "/View/Operator.fxml");
        SceneManager.switchScene("operator");

    }





}

