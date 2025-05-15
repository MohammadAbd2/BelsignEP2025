package GUI.Controller;

import GUI.View.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;
import java.io.IOException;
import java.util.List;

public class NavbarController {
    @FXML
    private Button OrderTabId;
    @FXML
    private Button OperatorTabId;
    @FXML
    private Button QCTabId;
    @FXML
    private Button AdminTabId;

    @FXML
    private ImageView profile_pic;

    public void initialize() throws IOException {

        // Load profile image
        Image image = new Image(getClass().getResource("/img/logout.png").toExternalForm());
        profile_pic.setImage(image);

        // Apply circular clipping
        Circle clip = new Circle(profile_pic.getFitWidth() / 1.2, profile_pic.getFitHeight() / 1.7,
                Math.min(profile_pic.getFitWidth(), profile_pic.getFitHeight()) / 2.4);
        profile_pic.setClip(clip);



    }



    public void OrderTab(ActionEvent event) {

    }

    public void OperatorTab(ActionEvent event) {

    }



    public void QCTab(ActionEvent event) {

    }

    public void AdminTab(ActionEvent event) {

    }


    public void ProfileTab(javafx.scene.input.MouseEvent event) throws IOException {

        List<String> loginScenes = List.of(
                "customTitleBar",
                "loginPage"
        );

        SceneManager.composeScene(loginScenes, "ComposedLogin");
        SceneManager.switchScene("ComposedLogin");

    }
}
