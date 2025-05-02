package GUI.Controller;


import GUI.Model.MLLoginController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

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
        adminButton.setOnAction(e -> loginChoice.adminLogin());
        operatorButton.setOnAction(e -> loginChoice.operatorLogin());
        qaButton.setOnAction(e -> loginChoice.qaLogin());
    }

    public void setStage(Stage stage) {
        this.stage = stage;

        minimizeButton.setOnAction(e -> stage.setIconified(true));
        closeButton.setOnAction(e -> stage.close());

        final Delta dragDelta = new Delta();
        titleBar.setOnMousePressed(e -> {
            dragDelta.x = e.getSceneX();
            dragDelta.y = e.getSceneY();
        });
        titleBar.setOnMouseDragged(e -> {
            stage.setX(e.getScreenX() - dragDelta.x);
            stage.setY(e.getScreenY() - dragDelta.y);
        });
    }

    private static class Delta {
        double x, y;
    }
}

