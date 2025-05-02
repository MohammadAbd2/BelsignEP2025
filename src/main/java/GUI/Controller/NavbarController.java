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

    public void initialize() {
        // Load profile image
        Image image = new Image(getClass().getResource("/img/profile_picture.png").toExternalForm());
        profile_pic.setImage(image);

        // Apply circular clipping
        Circle clip = new Circle(profile_pic.getFitWidth() / 2, profile_pic.getFitHeight() / 2.5,
                Math.min(profile_pic.getFitWidth(), profile_pic.getFitHeight()) / 2.4);
        profile_pic.setClip(clip);



    }



    public void OrderTab(ActionEvent event) {
        try {
            // declaration of the variables
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            VBox Application = new VBox();
            VBox WindowBox = new VBox();
            VBox rootContainer = new VBox();
            VBox Body  = new VBox();

            // Sort the VBox to fit with the style and Application

            Application.getChildren().add(WindowBox);
            rootContainer.getChildren().add(Navbar.loadNavbar());

            //add the new component here bellow



            // create and add the ScrollPane
            ScrollPane scrollPane = new ScrollPane();
            scrollPane.setContent(Body);
            scrollPane.setFitToWidth(true);
            scrollPane.setFitToHeight(true);
            scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
            scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
            scrollPane.getStyleClass().add("scroll-pane");
            rootContainer.getChildren().add(scrollPane);

            //render & set the new Scene to the Stage
            Application.getChildren().add(rootContainer);
            Scene currentScene = currentStage.getScene();
            currentScene.setRoot(Application);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void OperatorTab(ActionEvent event) {
        try {
            // Get current stage
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // === Root layout ===
            VBox rootContainer = new VBox();


            Node navbar = Navbar.loadNavbar(); // Assuming this returns a Node

            rootContainer.getChildren().add(navbar);

            // === Ticket Page ===
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/dk/easv/mohammadabd/ems/ticketPage.fxml"));
            Node ticketPage = loader.load();

            VBox body = new VBox(ticketPage);
            body.setSpacing(10);

            // === Scrollable Content ===
            ScrollPane scrollPane = new ScrollPane(body);
            scrollPane.setFitToWidth(true);
            scrollPane.setFitToHeight(true);
            scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
            scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
            scrollPane.getStyleClass().add("scroll-pane");

            rootContainer.getChildren().add(scrollPane);

            // === Set to current scene ===
            Scene currentScene = currentStage.getScene();
            currentScene.setRoot(rootContainer);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public void QCTab(ActionEvent event) {
        try {
            // declaration of the variables
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            VBox Application = new VBox();
            VBox WindowBox = new VBox();
            VBox rootContainer = new VBox();
            VBox Body  = new VBox();

            // Sort the VBox to fit with the style and Application
            Application.getChildren().add(WindowBox);
            rootContainer.getChildren().add(Navbar.loadNavbar());

            //add the new component here bellow


            // create and add the ScrollPane
            ScrollPane scrollPane = new ScrollPane();
            scrollPane.setContent(Body);
            scrollPane.setFitToWidth(true);
            scrollPane.setFitToHeight(true);
            scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
            scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
            scrollPane.getStyleClass().add("scroll-pane");
            rootContainer.getChildren().add(scrollPane);

            //render & set the new Scene to the Stage
            Application.getChildren().add(rootContainer);
            Scene currentScene = currentStage.getScene();
            currentScene.setRoot(Application);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void AdminTab(ActionEvent event) {
        try {
            // declaration of the variables
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            VBox Application = new VBox();
            VBox WindowBox = new VBox();
            VBox rootContainer = new VBox();
            VBox Body  = new VBox();

            // Sort the VBox to fit with the style and Application
            Application.getChildren().add(WindowBox);
            rootContainer.getChildren().add(Navbar.loadNavbar());

            //add the new component here bellow


            // create and add the ScrollPane
            ScrollPane scrollPane = new ScrollPane();
            scrollPane.setContent(Body);
            scrollPane.setFitToWidth(true);
            scrollPane.setFitToHeight(true);
            scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
            scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
            scrollPane.getStyleClass().add("scroll-pane");
            rootContainer.getChildren().add(scrollPane);

            //render & set the new Scene to the Stage
            Application.getChildren().add(rootContainer);
            Scene currentScene = currentStage.getScene();
            currentScene.setRoot(Application);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void ProfileTab(javafx.scene.input.MouseEvent event) {
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        if(currentStage == SceneManager.getStage() && Objects.equals(SceneManager.getCurrentSceneName(), "navBar")){
            SceneManager.switchScene("loginPage");
        }else{
            SceneManager.switchScene("navBar");
        }

//        SceneManager.switchScene("loginPage");
//        try {
//            // declaration of the variables
//            VBox Application = new VBox();
//            VBox WindowBox = new VBox();
//            VBox rootContainer = new VBox();
//            VBox Body  = new VBox();
//
//            // Sort the VBox to fit with the style and Application
//            rootContainer.getChildren().add(Navbar.loadNavbar());
//
//            //add the new component here bellow
//
//
//
//
//            // create and add the ScrollPane
//            ScrollPane scrollPane = new ScrollPane();
//            scrollPane.setContent(Body);
//            scrollPane.setFitToWidth(true);
//            scrollPane.setFitToHeight(true);
//            scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
//            scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
//            scrollPane.getStyleClass().add("scroll-pane");
//            rootContainer.getChildren().add(scrollPane);
//
//            //render & set the new Scene to the Stage
//            Application.getChildren().add(rootContainer);
//            Scene currentScene = currentStage.getScene();
//            currentScene.setRoot(Application);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }
}
