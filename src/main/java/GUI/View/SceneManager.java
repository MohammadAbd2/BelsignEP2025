package GUI.View;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SceneManager {

    private static Stage primaryStage;
    private static final Map<String, Parent> scenes = new HashMap<>();
    private static String currentSceneName;
    private static Scene scene;

    public static void setStage(Stage stage) {
        primaryStage = stage;
    }

    public static Stage getStage() {
        return primaryStage;
    }

    // Load reusable FXML components into memory
    public static void loadScene(String name, String fxmlPath) throws IOException {
        URL fxmlUrl = SceneManager.class.getResource(fxmlPath);
        if (fxmlUrl == null) {
            System.err.println("Error: FXML file not found at " + fxmlPath);
            return;
        }

        FXMLLoader fxmlLoader = new FXMLLoader(fxmlUrl);
        Parent root = fxmlLoader.load();
        scenes.put(name, root);
    }
    // Switch the main root of the Scene
    public static void switchScene(String name) {
        if (primaryStage == null) {
            System.err.println("Error: Primary stage is not set.");
            return;
        }

        Parent root = scenes.get(name);
        if (root == null) {
            System.err.println("Error: Scene '" + name + "' is not loaded.");
            return;
        }

        if (scene == null) {
            scene = new Scene(root);
            primaryStage.setScene(scene);
        } else {
            scene.setRoot(root);
        }
        // Wait until layout is calculated before applying size
        Platform.runLater(() -> {
            double minWidth = root.minWidth(-1);
            double minHeight = root.minHeight(-1);

            primaryStage.setMinWidth(minWidth);
            primaryStage.setMinHeight(minHeight);
        });
        currentSceneName = name;
    }

// Compose scene using vertical VBox for simpler layout (used by NavbarController)
    public static Parent composeScene(List<String> sceneNames, String composedSceneName) {
        VBox root = new VBox();
        root.setPrefSize(primaryStage.getWidth(), primaryStage.getHeight());
        root.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        VBox.setVgrow(root, Priority.ALWAYS);

        for (String name : sceneNames) {
            Parent content = scenes.get(name);
            if (content == null) {
                System.err.println("Error: Scene '" + name + "' is not loaded.");
                continue;
            }

            VBox.setVgrow(content, Priority.ALWAYS);  // Let it grow inside VBox
            root.getChildren().add(content);
        }

        scenes.put(composedSceneName, root);
        return root;
    }

    public static String getCurrentSceneName() {
        return currentSceneName;
    }
}
