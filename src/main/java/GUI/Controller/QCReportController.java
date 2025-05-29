package GUI.Controller;

import BE.Order;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

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
            // Set Order Number
            orderNumberLabel.setText("Order " + order.getOrder_number() + " Report");

            // Set Date
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            dateSentLabel.setText(now.format(formatter));

            // Set QA Name
            qaLabel.setText(qaName != null ? qaName : "");

            // Set Notes
            notesLabel.setText(order.getNotes() != null ? order.getNotes() : "");

            // Load Images
            updateImages(order.getImages());
        }
    }

    private void updateImages(List<String> basePath) {
        if (basePath == null || basePath.isEmpty()) return;

        try {
            loadImage(frontImage, String.valueOf(basePath));
            loadImage(backImage, basePath + File.separator + "back.png");
            loadImage(leftImage, basePath + File.separator + "left.png");
            loadImage(rightImage, basePath + File.separator + "right.png");
            loadImage(topImage, basePath + File.separator + "top.png");
        } catch (Exception e) {
            System.err.println("Error loading images: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void loadImage(ImageView imageView, String imagePath) {
        if (imageView == null || imagePath == null) return;

        File imageFile = new File(imagePath);
        if (imageFile.exists()) {
            try {
                Image image = new Image(imageFile.toURI().toString());
                imageView.setImage(image);
            } catch (Exception e) {
                System.err.println("Failed to load image: " + imagePath);
            }
        } else {
            System.err.println("Image file not found: " + imagePath);
        }
    }
}
