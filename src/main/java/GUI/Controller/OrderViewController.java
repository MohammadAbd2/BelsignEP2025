package GUI.Controller;

import BE.Order;
import GUI.View.SceneManager;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import javafx.scene.image.Image;

public class OrderViewController {
    private static Order currentOrder;  // Make this static

    @FXML private TextField orderNumberField;
    @FXML private TextField partStatusField;
    @FXML private TextField orderStatusField;
    @FXML private TextArea notesArea;
    @FXML private ImageView frontImage;
    @FXML private ImageView backImage;
    @FXML private ImageView rightImage;
    @FXML private ImageView leftImage;
    @FXML private ImageView topImage;
    @FXML private Button previewButton;

    @FXML
    public void initialize() {
        // Initialize with current order data if available
        if (currentOrder != null) {
            updateView();
        }
        
        if (previewButton != null) {
            previewButton.setOnAction(event -> openQCReport());
        }
    }

    private void openQCReport() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/View/QCReport.fxml"));
            Stage stage = new Stage();
            stage.setTitle("QC Report Preview");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void setOrder(Order order) {
        currentOrder = order;  // Store in static field
    }

    private void updateView() {
        if (currentOrder == null) return;

        System.out.println("Updating view with order: " + currentOrder.getOrder_number());
        
        if (orderNumberField != null) {
            orderNumberField.setText(currentOrder.getOrder_number());
            System.out.println("Set order number: " + currentOrder.getOrder_number());
        }
        
        if (orderStatusField != null) {
            orderStatusField.setText(currentOrder.getStatus());
            System.out.println("Set order status: " + currentOrder.getStatus());
        }
        
        if (notesArea != null) {
            notesArea.setText(currentOrder.getNotes());
            System.out.println("Set notes: " + currentOrder.getNotes());
        }
        
        // Update images if they exist
        updateImages(currentOrder.getImage());
    }

    private void updateImages(String basePath) {
        if (basePath == null) return;
        try {
            if (frontImage != null) frontImage.setImage(new Image(basePath + "_front.png"));
            if (backImage != null) backImage.setImage(new Image(basePath + "_back.png"));
            if (rightImage != null) rightImage.setImage(new Image(basePath + "_right.png"));
            if (leftImage != null) leftImage.setImage(new Image(basePath + "_left.png"));
            if (topImage != null) topImage.setImage(new Image(basePath + "_top.png"));
        } catch (Exception e) {
            System.err.println("Error loading images: " + e.getMessage());
        }
    }
}