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
        String qaName = QAController.getQaName();

        if (order != null) {
            // Set order number label
            orderNumberLabel.setText("Order " + order.getOrder_number() + " Report");

            // Set current date in format dd/MM/yyyy
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            dateSentLabel.setText(now.format(formatter));

            // Set QA name label (if available)
            qaLabel.setText(qaName != null ? qaName : "");

            // Set notes label (if available)
            notesLabel.setText(order.getNotes() != null ? order.getNotes() : "");

            // Load images from order image paths list
            updateImages(order.getImages());
        }
    }

    /**
     * Update image views by loading images from the list of image paths.
     * Assumes imagePaths list order:
     * 0 - front, 1 - back, 2 - left, 3 - right, 4 - top
     */
    private void updateImages(List<String> imagePaths) {
        if (imagePaths == null || imagePaths.isEmpty()) return;

        setImageFromPath(frontImage, getPath(imagePaths, 0));
        setImageFromPath(backImage, getPath(imagePaths, 1));
        setImageFromPath(leftImage, getPath(imagePaths, 2));
        setImageFromPath(rightImage, getPath(imagePaths, 3));
        setImageFromPath(topImage, getPath(imagePaths, 4));
    }

    /**
     * Helper method to get image path safely by index.
     */
    private String getPath(List<String> paths, int index) {
        return (paths.size() > index) ? paths.get(index) : null;
    }

    /**
     * Load image from path and set it to the given ImageView.
     * Clears ImageView if image file does not exist.
     */
    private void setImageFromPath(ImageView imageView, String path) {
        if (imageView == null) return;

        if (path != null && !path.isEmpty()) {
            File imageFile = new File(path);
            if (imageFile.exists()) {
                try {
                    Image image = new Image(imageFile.toURI().toString());
                    imageView.setImage(image);
                } catch (Exception e) {
                    System.err.println("Failed to load image: " + path);
                    imageView.setImage(null);
                }
            } else {
                System.err.println("Image file not found: " + path);
                imageView.setImage(null);
            }
        } else {
            // Clear ImageView if path is null or empty
            imageView.setImage(null);
        }
    }
}
