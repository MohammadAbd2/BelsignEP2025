package GUI.View;

import GUI.View.Header.CustomTitleBar;
import GUI.View.Header.Navbar;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class  OrderPage {
    public void loadPage(ActionEvent event) {

        try {
            // declaration of the variables
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            VBox Application = new VBox();
            VBox WindowBox = new VBox();
            VBox rootContainer = new VBox();
            VBox Body = new VBox();

            // Sort the VBox to fit with the style and Application
            CustomTitleBar customTitleBar = new CustomTitleBar(currentStage);
            WindowBox.getChildren().add(customTitleBar);
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
}
