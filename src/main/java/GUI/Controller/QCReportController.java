package GUI.Controller;

import BE.Order;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class QCReportController {
    @FXML private Label orderNumberLabel;
    @FXML private Label dateSentLabel;
    @FXML private Label qaLabel;
    @FXML private Label notesLabel;
    @FXML private ImageView frontImage;
    @FXML private ImageView backImage;
    @FXML private ImageView leftImage;
    @FXML private ImageView rightImage;
    @FXML private ImageView topImage;

    @FXML
    public void initialize() {
        Order order = QAController.getSelectedOrder();
        String qaName = QAController.getQaName();  // Get the QA name
        
        if (order != null) {
            orderNumberLabel.setText("Order " + order.getOrder_number() + " Report");
            
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            dateSentLabel.setText(now.format(formatter));
            
            // Set QA name from the input field
            qaLabel.setText(qaName != null ? qaName : "");
            
            notesLabel.setText(order.getNotes());
            
            updateImages(order.getImage());
        }
    }
    
    private void updateImages(String basePath) {
        if (basePath == null) return;
        
        try {
            ClassLoader classLoader = getClass().getClassLoader();
            
            loadImage(frontImage, basePath + "/front.png", classLoader);
            loadImage(backImage, basePath + "/back.png", classLoader);
            loadImage(leftImage, basePath + "/left.png", classLoader);
            loadImage(rightImage, basePath + "/right.png", classLoader);
            loadImage(topImage, basePath + "/top.png", classLoader);
        } catch (Exception e) {
            System.err.println("Error loading images: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void loadImage(ImageView imageView, String path, ClassLoader classLoader) {
        if (imageView != null) {
            try {
                Image image = new Image(classLoader.getResourceAsStream(path));
                imageView.setImage(image);
            } catch (Exception e) {
                System.err.println("Failed to load image: " + path);
            }
        }
    }
}