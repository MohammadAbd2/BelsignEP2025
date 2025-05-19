package GUI.View.Header;


import GUI.View.SceneManager;
import Utils.LoggedInUser;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import java.io.IOException;
import java.net.URL;

public class Navbar {


    private static final String NAVBAR_FXML = "/View/NavBar.fxml";
    private static final String CSS_PATH = "/css/styles.css";
    private static final String LOGO_PATH = "/Img/BELMAN_Logo.png";
    private static final String PROFILE_PIC_PATH = "/Img/profile_picture.png";
    private static final String GUEST_PIC_PATH = "/Img/guest.png";

    public static Parent loadNavbar() throws IOException {
        URL fxmlUrl = Navbar.class.getResource(NAVBAR_FXML);
        if (fxmlUrl == null) {
            System.err.println("Error: FXML file not found at " + NAVBAR_FXML);
            return null;
        }

        FXMLLoader fxmlLoader = new FXMLLoader(fxmlUrl);
        Parent navbarRoot = fxmlLoader.load();

        // Apply CSS
        URL cssUrl = Navbar.class.getResource(CSS_PATH);
        if (cssUrl != null) {
            navbarRoot.getStylesheets().add(cssUrl.toExternalForm());
        }

        // Set logo
        setImage(navbarRoot, "#logo", LOGO_PATH);

        boolean isAuthenticated = LoggedInUser.getInstance().isAuthenticated();

        if (isAuthenticated) {
            setImage(navbarRoot, "#profile_pic", PROFILE_PIC_PATH);
        } else {
            // Remove profile image or show empty image
            ImageView profile = (ImageView) navbarRoot.lookup("#profile_pic");
            profile.setFitWidth(70);
            profile.setFitHeight(60);
            setImage(navbarRoot, "#profile_pic", GUEST_PIC_PATH);

            // Hide all tabs except "home"
            Node homeTab = navbarRoot.lookup("#homeTab");
            Node tabContainer = navbarRoot.lookup("#tabContainer"); // Assuming it's an HBox or container with all tabs
            if (tabContainer instanceof HBox tabs) {
                tabs.getChildren().removeIf(node -> node != homeTab);
            }
            SceneManager.loadScene("Login", "/View/Login.fxml");
            SceneManager.switchScene("Login");
        }

        return navbarRoot;
    }

    private static void setImage(Parent root, String fxId, String imagePath) {
        URL imgUrl = Navbar.class.getResource(imagePath);
        if (imgUrl != null) {
            ImageView imageView = (ImageView) root.lookup(fxId);
            if (imageView != null) {
                imageView.setImage(new Image(imgUrl.toExternalForm()));
            } else {
                System.err.println("Warning: ImageView with fx:id='" + fxId + "' not found.");
            }
        } else {
            System.err.println("Warning: Image file not found at " + imagePath);
        }
    }
}
