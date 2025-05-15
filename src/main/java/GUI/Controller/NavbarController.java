package GUI.Controller;

import GUI.View.*;
import GUI.View.Header.Navbar;
import Utils.LoggedInUser;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

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
        Image image = new Image(getClass().getResource("/img/profile_picture.png").toExternalForm());
        profile_pic.setImage(image);

        // Apply circular clipping
        Circle clip = new Circle(profile_pic.getFitWidth() / 2, profile_pic.getFitHeight() / 2.5,
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
