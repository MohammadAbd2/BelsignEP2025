package GUI.View;

import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class App extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        // Set the stage globally
        SceneManager.setStage(primaryStage);



        // Load your scenes normally (only the ones you actually use)
        SceneManager.loadScene("loginPage", "/View/Login.fxml");
        SceneManager.loadScene("adminPage", "/View/Admin.fxml");
        SceneManager.loadScene("operatorPage", "/View/Operator.fxml");
        SceneManager.loadScene("QA", "/View/QA.fxml");
        SceneManager.loadScene("orderPage", "/View/Orders.fxml");

        // Switch to login scene as the first screen
        SceneManager.switchScene("loginPage");

        // Responsive logic (no multiple stages)
        double screenWidth = Screen.getPrimary().getBounds().getWidth();
        double screenHeight = Screen.getPrimary().getBounds().getHeight();

        if (screenWidth <= 1280 && screenHeight <= 800) {
            primaryStage.setFullScreen(true);
        } else {
            primaryStage.setMaximized(true);
        }

        primaryStage.setTitle("QC_Belsign Application");
        IconUtil.addAppIconToStage(primaryStage);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
    public class IconUtil {

        private static final Image APP_ICON = new Image("/Img/BELMAN_Logo.png");

        public static void addAppIconToStage(Stage stage) {
            stage.getIcons().add(APP_ICON);
        }
    }
}
