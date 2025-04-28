package controllers;

import bll.LoginChoice;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class LoginController {
    private final LoginChoice loginChoice = new LoginChoice();

    @FXML
    private Button adminButton;

    @FXML
    private Button operatorButton;

    @FXML
    private Button qaButton;

    @FXML
    public void initialize() {
        // Set up button actions
        adminButton.setOnAction(e -> loginChoice.adminLogin());
        operatorButton.setOnAction(e -> loginChoice.operatorLogin());
        qaButton.setOnAction(e -> loginChoice.qaLogin());
    }
}
