package GUI.View;

import BLL.OrderService;
import GUI.Controller.LoginController;
import Utils.Logger;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


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

        OrderService orderService = new OrderService();
        System.out.println(orderService.loadOrders());

    }

    public static void main(String[] args) {
        launch(args);
    }
}
