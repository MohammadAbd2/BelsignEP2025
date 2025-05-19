package GUI.Controller;

import GUI.View.SceneManager;
import Utils.LoggedInUser;
import Utils.UserSession;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.io.IOException;
import java.util.List;

public class NavbarController {

    @FXML
    private Button OrderTabId;
    @FXML
    private static Button OperatorTabId;
    @FXML
    private static Button QCTabId;
    @FXML
    private static Button AdminTabId;

    @FXML
    private ImageView profile_pic;

    @FXML
    public void initialize() {
        try {
            // Load profile image
            Image image = new Image(getClass().getResource("/img/logout.png").toExternalForm());
            profile_pic.setFitHeight(20);
            profile_pic.setFitWidth(20);
            profile_pic.setImage(image);


            setNavbarStatus();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setNavbarStatus() {
        if (!LoggedInUser.isAuthenticated()) {
            return;
        }

        System.out.println("Authenticated: " + LoggedInUser.isAuthenticated());
        System.out.println("Role: " + LoggedInUser.getLoggedInRole());

        // Hide all tabs initially with null safety
        if (OperatorTabId != null) OperatorTabId.setVisible(false);
        if (QCTabId != null) QCTabId.setVisible(false);
        if (AdminTabId != null) AdminTabId.setVisible(false);

        // Show tab based on role
        String role = LoggedInUser.getLoggedInRole();
        switch (role) {
            case "Admin":
                if (AdminTabId != null) AdminTabId.setVisible(true);
                break;
            case "Operator":
                if (OperatorTabId != null) OperatorTabId.setVisible(true);
                break;
            case "QC":
                if (QCTabId != null) QCTabId.setVisible(true);
                break;
            default:
                System.out.println("Unknown role: " + role);
        }
    }

    public void OrderTab(ActionEvent event) throws IOException {
        UserSession.setLoggedIn(true);
        SceneManager.composeScene(List.of("customTitleBar", "navBar", "orderPage"), "composedOrderPage");
        SceneManager.switchScene("composedOrderPage");
        setNavbarStatus();
    }

    public void OperatorTab(ActionEvent event) throws IOException {
        UserSession.setLoggedIn(true);
        SceneManager.composeScene(List.of("customTitleBar", "navBar", "operatorPage"), "composedOperatorPage");
        SceneManager.switchScene("composedOperatorPage");
        setNavbarStatus();
    }

    public void QCTab(ActionEvent event) throws IOException {
        UserSession.setLoggedIn(true);
        SceneManager.composeScene(List.of("customTitleBar", "navBar", "QC"), "composedQCPage");
        SceneManager.switchScene("composedQCPage");
        setNavbarStatus();
    }

    public void AdminTab(ActionEvent event) throws IOException {
        UserSession.setLoggedIn(true);
        SceneManager.composeScene(List.of("customTitleBar", "navBar", "adminPage"), "composedAdminPage");
        SceneManager.switchScene("composedAdminPage");
        setNavbarStatus();
    }

    public void ProfileTab(javafx.scene.input.MouseEvent event) throws IOException {
        SceneManager.composeScene(List.of("customTitleBar", "loginPage"), "ComposedLogin");
        SceneManager.switchScene("ComposedLogin");
        setNavbarStatus();
    }
}
