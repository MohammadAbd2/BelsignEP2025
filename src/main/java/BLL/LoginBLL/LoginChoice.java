package BLL.LoginBLL;

import GUI.View.SceneManager;
import Utils.LoggedInUser;
import Utils.UserSession;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginChoice {

    // Login for Admin
    public void adminLogin() throws IOException {
        LoggedInUser.setLoggedInRole("Admin");
        UserSession.setLoggedIn(true);

        VBox combinedLayout = new VBox();
        combinedLayout.getChildren().addAll(
            SceneManager.loadSceneAsParent("/View/TitleBar.fxml"),
            SceneManager.loadSceneAsParent("/View/NavBar.fxml"),
            SceneManager.loadSceneAsParent("/View/Admin.fxml")
        );

        setDynamicScene(combinedLayout);
        System.out.println("Logged in as Admin. Scenes dynamically displayed.");
    }

    // Login for Operator
    public void operatorLogin() throws IOException {
        LoggedInUser.setLoggedInRole("Operator");
        UserSession.setLoggedIn(true);

        VBox combinedLayout = new VBox();
        combinedLayout.getChildren().addAll(
            SceneManager.loadSceneAsParent("/View/TitleBar.fxml"),
            SceneManager.loadSceneAsParent("/View/NavBar.fxml"),
            SceneManager.loadSceneAsParent("/View/Orders.fxml")
        );

        setDynamicScene(combinedLayout);
        System.out.println("Logged in as Operator. Scenes dynamically displayed.");
    }

    // Login for QA/QC
    public void qaLogin() throws IOException {
        LoggedInUser.setLoggedInRole("QA");
        UserSession.setLoggedIn(true);

        VBox combinedLayout = new VBox();
        combinedLayout.getChildren().addAll(
            SceneManager.loadSceneAsParent("/View/TitleBar.fxml"),
            SceneManager.loadSceneAsParent("/View/NavBar.fxml"),
            SceneManager.loadSceneAsParent("/View/QA.fxml")
        );

        setDynamicScene(combinedLayout);
        System.out.println("Logged in as QA. Scenes dynamically displayed.");
    }

    // Helper to dynamically set a scene and adjust the stage size
    private void setDynamicScene(VBox layout) {
        Stage stage = SceneManager.getStage();

        Scene scene = new Scene(layout);
        stage.setScene(scene);
        stage.sizeToScene();

        stage.setMinWidth(layout.prefWidth(-1) > 0 ? layout.prefWidth(-1) : 600);
        stage.setMinHeight(layout.prefHeight(-1) > 0 ? layout.prefHeight(-1) : 400);

        stage.show();
    }
}