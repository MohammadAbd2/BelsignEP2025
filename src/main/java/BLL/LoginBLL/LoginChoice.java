package BLL.LoginBLL;

import BE.Admin;
import BE.Operator;
import BE.QA;
import GUI.Model.Logger;
import GUI.View.SceneManager;
import Utils.LoggedInUser;
import Utils.UserSession;

import java.io.IOException;

public class LoginChoice {

//    private final Admin admin = new Admin(1 , "Admin User");      // Represents admin role
//    private final Operator operator = new Operator(2 , "Operator User");  // Represents operator role
//    private final QA qa = new QA(3 , "QA User");                // Represents qc role

    public void adminLogin() throws IOException {
        LoggedInUser.setLoggedInRole("Admin");
        UserSession.setLoggedIn(true);

        SceneManager.loadScene("Main", "/View/navbar.fxml");
        SceneManager.switchScene("Main");
        System.out.println("Logged in as Admin.");
         Logger.displayOrders();
        // Additional logic for admin-specific access can go here
    }

    public void operatorLogin() throws IOException {
        LoggedInUser.setLoggedInRole("Operator");
        UserSession.setLoggedIn(true);

        SceneManager.loadScene("operator", "/View/Orders.fxml");
        SceneManager.switchScene("operator");
        System.out.println("Logged in as Operator.");
        Logger.displayOrders();
        // Additional logic for operator-specific access can go here
    }

    public void qaLogin() {
        System.out.println("Logged in as QA.");
        // Additional logic for QA-specific access can go here
    }
}