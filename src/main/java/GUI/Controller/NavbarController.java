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
    private Button OperatorTabId;
    @FXML
    private Button QCTabId;
    @FXML
    private Button AdminTabId;

    @FXML
    private ImageView profile_pic;

    @FXML
    public void initialize() {
        System.out.println("Initialize Navbar");
        System.out.println("Authenticated: " + LoggedInUser.isAuthenticated());

        try {
            // Load profile image
            Image image = new Image(getClass().getResource("/img/logout.png").toExternalForm());
            profile_pic.setFitHeight(20);
            profile_pic.setFitWidth(20);
            profile_pic.setImage(image);
            profile_pic.setFitWidth(30);  // Set your desired width
            profile_pic.setFitHeight(30);



            setNavbarStatus();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setNavbarStatus() {
        if (!LoggedInUser.isAuthenticated()) return;

        String role = LoggedInUser.getLoggedInRole();
        System.out.println("Role: " + role);

        // Hide all first
        if (OperatorTabId != null) OperatorTabId.setVisible(false);
        if (QCTabId != null) QCTabId.setVisible(false);
        if (AdminTabId != null) AdminTabId.setVisible(false);


        switch (role) {
            case "Admin" -> {
                OrderTabId.setVisible(true);
                OrderTabId.setManaged(true);

                OperatorTabId.setVisible(false);
                OperatorTabId.setManaged(false);

                QCTabId.setVisible(false);
                QCTabId.setManaged(false);

                AdminTabId.setVisible(true);
                AdminTabId.setManaged(true);
            }

            case "Operator" -> {
                OrderTabId.setVisible(true);
                OrderTabId.setManaged(true);

                OperatorTabId.setVisible(true);
                OperatorTabId.setManaged(true);

                QCTabId.setVisible(false);
                QCTabId.setManaged(false);

                AdminTabId.setVisible(false);
                AdminTabId.setManaged(false);
            }

            case "QA" -> {
                OrderTabId.setVisible(true);
                OrderTabId.setManaged(true);

                OperatorTabId.setVisible(false);
                OperatorTabId.setManaged(false);

                QCTabId.setVisible(true);
                QCTabId.setManaged(true);

                AdminTabId.setVisible(false);
                AdminTabId.setManaged(false);
            }
        }
    }

    // Navigation Handlers (no change)
    public void OrderTab(ActionEvent event) throws IOException {
        // Load all required components
        SceneManager.loadScene("customTitleBar", "/View/TitleBar.fxml");
        SceneManager.loadScene("navbar", "/View/Navbar.fxml");
        SceneManager.loadScene("orderPage", "/View/Orders.fxml");

        // Compose them in order
        List<String> scenes = List.of(
                "customTitleBar",
                "navbar",
                "orderPage"
        );
        SceneManager.composeScene(scenes, "ComposedOrders");
        SceneManager.switchScene("ComposedOrders");
    }

    public void OperatorTab(ActionEvent event) throws IOException {
        // Load all required components
        SceneManager.loadScene("customTitleBar", "/View/TitleBar.fxml");
        SceneManager.loadScene("navbar", "/View/Navbar.fxml");
        SceneManager.loadScene("operatorPage", "/View/Operator.fxml");

        List<String> scenes = List.of(
                "customTitleBar",
                "navbar",
                "operatorPage"
        );
        SceneManager.composeScene(scenes, "ComposedOperator");
        SceneManager.switchScene("ComposedOperator");
    }

    public void QCTab(ActionEvent event) throws IOException {
        // Load all required components
        SceneManager.loadScene("customTitleBar", "/View/TitleBar.fxml");
        SceneManager.loadScene("navbar", "/View/Navbar.fxml");
        SceneManager.loadScene("QCPage", "/View/QA.fxml");

        List<String> scenes = List.of(
                "customTitleBar",
                "navbar",
                "QCPage"
        );
        SceneManager.composeScene(scenes, "ComposedQC");
        SceneManager.switchScene("ComposedQC");
    }

    public void AdminTab(ActionEvent event) throws IOException {
        // Load all required components
        SceneManager.loadScene("customTitleBar", "/View/TitleBar.fxml");
        SceneManager.loadScene("navbar", "/View/Navbar.fxml");
        SceneManager.loadScene("adminPage", "/View/Admin.fxml");

        List<String> scenes = List.of(
                "customTitleBar",
                "navbar",
                "adminPage"
        );
        SceneManager.composeScene(scenes, "ComposedAdmin");
        SceneManager.switchScene("ComposedAdmin");
    }


    public void ProfileTab(javafx.scene.input.MouseEvent event) throws IOException {
        SceneManager.composeScene(List.of("customTitleBar", "loginPage"), "ComposedLogin");
        SceneManager.switchScene("ComposedLogin");
    }
}
