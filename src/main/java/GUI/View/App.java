package GUI.View;

import BLL.LoggerBLL.LogAnalyzer;
import BLL.OrderService;
import GUI.Controller.LoginController;
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
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/Login.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.setScene(scene);
        primaryStage.show();

        // loading the controller
        LoginController controller = loader.getController();
        controller.setStage(primaryStage);

        // start using the Scenemanger
        SceneManager.setStage(primaryStage);
        SceneManager.loadSceneAsParent("/View/Login.fxml");
        SceneManager.loadScene("loginPage", "/View/Login.fxml");
        SceneManager.loadScene("navBar", "/View/navbar.fxml");
        Logger.displayOrders();

        LogAnalyzer.RegisterLog("New order added: Order #1234", 1); //  INFO
        LogAnalyzer.RegisterLog("Order deletion attempt", 2); //  WARNING
        LogAnalyzer.RegisterLog("Database connection failed", 3); //  ERROR


        List<String> infoLogs = LogAnalyzer.getInfoLogs();
        List<String> warningLogs = LogAnalyzer.getWarningLogs();
        List<String> errorLogs = LogAnalyzer.getErrorLogs();

        System.out.println(infoLogs);
        System.out.println(warningLogs);
        System.out.println(errorLogs);

        OrderService orderService = new OrderService();
        Logger.displayLogsByType(2);


    }

    public static void main(String[] args) {
        launch(args);
    }
}
