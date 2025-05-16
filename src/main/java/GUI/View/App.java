package GUI.View;

import GUI.Model.Logger;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.List;


public class App extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        // start using the Scenemanger
        SceneManager.setStage(primaryStage);
        SceneManager.loadSceneAsParent("/View/Login.fxml");
        SceneManager.loadScene("customTitleBar", "/View/CustomTitleBar.fxml");
        SceneManager.loadScene("loginPage", "/View/Login.fxml");
        SceneManager.loadScene("adminPage", "/View/Admin.fxml");
        SceneManager.loadScene("operatorPage", "/View/Operator.fxml");
        SceneManager.loadScene("QC", "/View/QA.fxml");
        SceneManager.loadScene("orderPage", "/View/Orders.fxml");

        //Logger.displayOrders();

        Logger.RegisterLog("New order added: Order #1234", 1); //  INFO
        Logger.RegisterLog("Order deletion attempt", 2); //  WARNING
        Logger.RegisterLog("Database connection failed", 3); //  ERROR


        List<String> infoLogs = Logger.displayLogsByType(1);
        List<String> warningLogs = Logger.displayLogsByType(2);
        List<String> errorLogs = Logger.displayLogsByType(3);

        System.out.println("There is " + infoLogs.size() + " Info messages");
        System.out.println("There is " +  warningLogs.size() + " Warning messages");
        System.out.println("There is " + errorLogs.size() + " Error messages");

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/Login.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.setScene(scene);
        primaryStage.show();

        List<String> loginScenes = List.of(
                "customTitleBar",
                "loginPage"
        );
        SceneManager.composeScene(loginScenes, "ComposedLogin");
        SceneManager.switchScene("ComposedLogin");
    }

    public static void main(String[] args) {
        launch(args);
    }
}
