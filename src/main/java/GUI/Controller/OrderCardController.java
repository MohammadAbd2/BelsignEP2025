package GUI.Controller;

import BE.Order;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

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
        if (order == null) {
            // Clear all fields for empty space
            productName.setText("");
            productId.setText("");
            productImage.setImage(null);
            statusIcon.setImage(null);
            return;
        }

        // Set the product name and ID
        productName.setText(order.getOrder_number());
        productId.setText("ID: " + order.getId());

        // Set the main product image
        productImage.setFitHeight(100.0);
        productImage.setFitWidth(100.0);
        productImage.setPreserveRatio(true);
        productImage.setImage(new Image(getClass().getResource("/Img/BELMAN_Logo.png").toExternalForm()));

        // Set the status icon
        statusIcon.setFitHeight(20.0);
        statusIcon.setFitWidth(20.0);
        statusIcon.setPreserveRatio(true);
        StackPane.setAlignment(statusIcon, javafx.geometry.Pos.TOP_RIGHT);

        // Use the existing getStatusIcon method
        String statusIconPath = "/Img/" + order.getStatusIcon();
        //statusIcon.setImage(new Image(getClass().getResource(statusIconPath).toExternalForm()));
    }


    @FXML
    private void handleClick(javafx.scene.input.MouseEvent event) {
        // TODO: Implement navigation to OperatorPage with the selected order
    }
}
