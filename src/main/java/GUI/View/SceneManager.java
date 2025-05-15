package GUI.View;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
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

    public static void setStage(Stage stage) {
        primaryStage = stage;
    }

    public static Stage getStage() {
        return primaryStage;
    }

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

        primaryStage.setScene(new Scene(root));
        primaryStage.show();
        currentSceneName = name;
    }

    public static Parent loadSceneAsParent(String fxmlPath) throws IOException {
        URL fxmlUrl = SceneManager.class.getResource(fxmlPath);
        if (fxmlUrl == null) {
            System.err.println("Error: FXML file not found at " + fxmlPath);
            return null;
        }

        FXMLLoader fxmlLoader = new FXMLLoader(fxmlUrl);
        return fxmlLoader.load();
    }

    // New method to get current scene name
    public static String getCurrentSceneName() {
        return currentSceneName;
    }

    // New method to dynamically compose a scene with a list of scenes to add
    public static void composeScene(List<String> sceneNames, String composedSceneName) throws IOException {
        // Create a BorderPane layout to compose the scenes
        VBox rootLayout = new VBox();

        // Loop through the list of scene names to get their FXML and content
        for (int i = 0; i < sceneNames.size(); i++) {
            String sceneName = sceneNames.get(i);

            // Load the scene if not already loaded
            if (!scenes.containsKey(sceneName)) {
                System.err.println("Error: Scene '" + sceneName + "' is not loaded.");
                continue;
            }

            Parent sceneContent = scenes.get(sceneName);

            if (sceneContent == null) {
                System.err.println("Error: Scene '" + sceneName + "' could not be loaded.");
                continue;
            }

            rootLayout.getChildren().add(sceneContent);
            // You can add more conditions if you have more areas to place content
        }

        // Put the composed scene into the scenes map
        scenes.put(composedSceneName, rootLayout);
    }
}
