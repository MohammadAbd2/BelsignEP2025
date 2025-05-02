package GUI.View;

import BE.Admin;
import BE.Operator;
import BE.User;
import DAL.DBConnector;
import DAL.UserDB;
import GUI.Controller.LoginController;
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

        LoginController controller = loader.getController();
        controller.setStage(primaryStage);

        Scene scene = new Scene(root);
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.setScene(scene);
        primaryStage.show();
        logger.displayOrders();
        DBConnector dbConnector = new DBConnector();
        dbConnector.getConnection();

// Test Users
        UserDB userDB = new UserDB();

// Create a new Operator
        User operator = new Operator(0, "Ahmed"); // ID is 0 because it will be auto-generated
        userDB.createUser(operator);

// Get all Operators
        List<User> operators = userDB.getAllUsersByRole("operator");

// Print them
        for (User u : operators) {
            System.out.println(u);
        }

    }

    public static void main(String[] args) {
        launch(args);
    }
}
