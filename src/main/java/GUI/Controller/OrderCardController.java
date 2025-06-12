package GUI.Controller;

import BE.Order;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.io.ByteArrayInputStream;
import java.net.URL;
import java.util.Base64;
import java.util.List;

public class OrderCardController {
    @FXML
    private ImageView productImage;
    @FXML
    private ImageView statusIcon;
    @FXML
    private Label productName;
    @FXML
    private Label productId;
    @FXML
    private VBox containerVBox;
    @FXML
    private StackPane imageStack;

    public void initialize() {
        // Set default alignment and spacing for the container
        containerVBox.setAlignment(javafx.geometry.Pos.TOP_CENTER);
        containerVBox.setSpacing(10);
    }

    public void setOrderData(Order order) {
        if (order == null) return;
        {
            // Clear all fields for empty space
            productName.setText("");
            productId.setText("");
            productImage.setImage(null);
            statusIcon.setVisible(false);

            // Get the first image if available
            if (order.getImages() != null && !order.getImages().isEmpty()) {
                setOrderImage(order.getImages().get(0));
            } else {
                // Load default logo if there is no images
                loadDefaultLogo();
            }
        }

        // Set the product name, order number and ID
        String displayText = order.getOrder_name() != null ?
                order.getOrder_name() + " (" + order.getOrder_number() + ")" :
                order.getOrder_number();
        productName.setText(displayText);
        productId.setText("ID: " + order.getId());

        // Set the main product image
        productImage.setFitHeight(100.0);
        productImage.setFitWidth(100.0);
        productImage.setPreserveRatio(true);
        productImage.setImage(new Image(getClass().getResource("/Img/BELMAN_Logo.png").toExternalForm()));

        String status = order.getStatus();
        if (status == null || status.isEmpty() || status.equalsIgnoreCase("new")) {
            // Creating the "NEW" label
            Label newLabel = new Label("NEW");
            newLabel.setStyle(
                    "-fx-background-color: linear-gradient(from 70% 70% to 100% 100%, #0095FF 0%, #0077CC 100%); " + // Green background
                    "-fx-text-fill: white; " +           // White text
                    "-fx-padding: 2 5 2 5; " +          // Small padding
                    "-fx-font-size: 10; " +             // Small font
                    "-fx-font-weight: bold; " +         // Bold text
                    "-fx-background-radius: 3;");        // Rounded corners


            // Remove any existing status icon
            statusIcon.setVisible(false);

            // Remove any existing labels
            imageStack.getChildren().removeIf(node -> node instanceof Label);

            // Add the new label to the StackPane
            imageStack.getChildren().add(newLabel);
            StackPane.setAlignment(newLabel, javafx.geometry.Pos.TOP_RIGHT);
            StackPane.setMargin(newLabel, new Insets(5, 5, 0, 0));
        } else {
            // Handle other status icons
            try {
                String statusIconPath = "/Img/" + order.getStatusIcon();
                Image statusImage = new Image(getClass().getResource(statusIconPath).toExternalForm());
                statusIcon.setImage(statusImage);
                statusIcon.setFitHeight(20.0);
                statusIcon.setFitWidth(20.0);
                statusIcon.setPreserveRatio(true);

                // Make sure the status icon is visible and properly positioned
                statusIcon.setVisible(true);
                StackPane.setAlignment(statusIcon, javafx.geometry.Pos.TOP_RIGHT);
                StackPane.setMargin(statusIcon, new Insets(5, 5, 0, 0));

                // Remove any existing "NEW" label if it exists
                imageStack.getChildren().removeIf(node -> node instanceof Label);

            } catch (Exception e) {
                System.err.println("Error loading status icon for order " + order.getId() + ": " + e.getMessage());
                statusIcon.setVisible(false);
            }
        }
    }

    public void setOrderImage(String base64Image) {
        try {
            if (base64Image != null && !base64Image.isEmpty()) {
                byte[] imageData = Base64.getDecoder().decode(base64Image);
                Image image = new Image(new ByteArrayInputStream(imageData));
                productImage.setImage(image);
            } else {
                loadDefaultLogo();
            }
        } catch (IllegalArgumentException e) {
            loadDefaultLogo();
        }
    }

    private void loadDefaultLogo() {
        try {
            URL logoUrl = getClass().getResource("/View/Img/BELMAN_Logo.png");
            if (logoUrl != null) {
                productImage.setImage(new Image(logoUrl.toString()));
            }
        } catch (Exception e) {
            System.err.println("Error loading default logo: " + e.getMessage());
        }
    }

}