package GUI.Controller;

import BE.Order;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;

import javax.swing.text.html.ImageView;
import java.awt.event.MouseEvent;

public class OrderCardController {
    @FXML
    private ImageView productImage;
    @FXML
    private ImageView statusIcon;
    @FXML private Label productName;
    @FXML private Label productId;
    @FXML
    private void handleClick(MouseEvent event) {
        // Navigate to OperatorPage and pass selected order
    }

    public void setOrderData(Order order) {
        productName.setText(order.getOrderNumber());
        productId.setText("ID: " + order.getId());
        //productImage.setImage(new Image("/Img/BELMAN_Logo.png")); // or based on product
        //statusIcon.setImage(new Image("/Img/" + order.getStatusIcon())); // dynamic icon
    }

    public void handleClick(javafx.scene.input.MouseEvent mouseEvent) {
    }
}