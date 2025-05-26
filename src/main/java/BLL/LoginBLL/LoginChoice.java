package BLL.LoginBLL;

import GUI.Model.Logger;
import GUI.View.SceneManager;
import Utils.LoggedInUser;
import Utils.UserSession;

import java.io.IOException;
import java.util.List;

public class LoginChoice {

//    private final Admin admin = new Admin(1 , "Admin User");      // Represents admin role
//    private final Operator operator = new Operator(2 , "Operator User");  // Represents operator role
//    private final QA qa = new QA(3 , "QA User");                // Represents qc role

    public void adminLogin() throws IOException {
        LoggedInUser.setLoggedInRole("Admin");
        UserSession.setLoggedIn(true);
        SceneManager.loadScene("navBar", "/View/NavBar.fxml");
        List<String> scenes = List.of(
                "navBar",
                "adminPage"
        );
        SceneManager.composeScene(scenes, "composedAdminPage");
        SceneManager.switchScene("composedAdminPage");
        System.out.println("Logged in as Admin.");

         Logger.displayOrders();
        // Additional logic for admin-specific access can go here
    }

    public void operatorLogin() throws IOException {
        LoggedInUser.setLoggedInRole("Operator");
        UserSession.setLoggedIn(true);
        SceneManager.loadScene("navBar", "/View/NavBar.fxml");
        List<String> scenes = List.of(
                "navBar",
                "operatorPage"
        );

        SceneManager.composeScene(scenes, "composedOperatorPage");
        SceneManager.switchScene("composedOperatorPage");
        System.out.println("Logged in as Operator.");
        Logger.displayOrders();
        // Additional logic for operator-specific access can go here
    }

    public void qaLogin() throws IOException {
        LoggedInUser.setLoggedInRole("QA");
        UserSession.setLoggedIn(true);
        SceneManager.loadScene("navBar", "/View/NavBar.fxml");
        List<String> scenes = List.of(
                "navBar",
                "QC"
        );

        SceneManager.composeScene(scenes, "composedQCPage");
        SceneManager.switchScene("composedQCPage");
        System.out.println("Logged in as QC.");
        Logger.displayOrders();
    }
}