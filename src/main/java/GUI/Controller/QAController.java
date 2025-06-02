package GUI.Controller;

import BE.Order;
import BE.QCReport;
import Utils.PDFGenerator;
import DAL.OrderDB;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Base64;
import java.util.List;

public class QAController {
    @FXML private TextField orderNumberField;
    @FXML private TextField qaNameField;
    @FXML private TextField orderStatusField;
    @FXML private TextArea notesArea;
    @FXML private ImageView frontImage;
    @FXML private ImageView backImage;
    @FXML private ImageView rightImage;
    @FXML private ImageView leftImage;
    @FXML private ImageView topImage;
    
    @FXML private Button previewButton;
    @FXML private Button downloadButton;
    @FXML private Button approveButton;
    @FXML private Button rejectButton;
    @FXML private Button sendButton;

    private static Order selectedOrder;
    private static String qaName;
    private OrderDB orderDB;

    @FXML
    public void initialize() {
        orderDB = new OrderDB();
        
        if (selectedOrder != null) {
            updateView();
        }
        
        setupButtonHandlers();
        setupTextListeners();
    }

    private void setupButtonHandlers() {
        previewButton.setOnAction(event -> {
            qaName = qaNameField.getText().trim(); // Store the QA name before opening preview
            openQCReport();
        });
        
        if (downloadButton != null) {
            downloadButton.setOnAction(event -> handleDownload());
        }
        
        if (approveButton != null) {
            approveButton.setOnAction(event -> handleApprove());
        }
        
        if (rejectButton != null) {
            rejectButton.setOnAction(event -> handleReject());
        }
        
        if (sendButton != null) {
            sendButton.setOnAction(event -> handleSend());
        }
    }

    private void setupTextListeners() {
        if (notesArea != null) {
            notesArea.textProperty().addListener((observable, oldValue, newValue) -> {
                if (selectedOrder != null) {
                    selectedOrder.setNotes(newValue);
                    orderDB.updateOrder(selectedOrder);
                }
            });
        }
    }

    private void openQCReport() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/QCReport.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("QC Report Preview - Order " + selectedOrder.getOrder_number());
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showError("Error", "Could not open QC Report: " + e.getMessage());
        }
    }

    private void updateView() {
        if (selectedOrder == null) return;

        System.out.println("Updating view with order: " + selectedOrder.getOrder_number());
        
        if (orderNumberField != null) {
            orderNumberField.setText(selectedOrder.getOrder_number());
        }
        
        if (orderStatusField != null) {
            orderStatusField.setText(selectedOrder.getStatus());
        }
        
        if (notesArea != null) {
            notesArea.setText(selectedOrder.getNotes());
        }
        
        updateImages(selectedOrder.getImages());

        // Update button states based on order status
        boolean isFinalized = "Approved".equalsIgnoreCase(selectedOrder.getStatus()) 
                         || "Rejected".equalsIgnoreCase(selectedOrder.getStatus());
        if (approveButton != null) approveButton.setDisable(isFinalized);
        if (rejectButton != null) rejectButton.setDisable(isFinalized);
    }

    private void updateImages(List<String> images) {
        if (images == null || images.isEmpty()) {
            loadDefaultImages();
            return;
        }

        try {
            // Set each image if available in the list
            if (images.size() > 0 && !images.get(0).isEmpty()) setImageFromBase64(frontImage, images.get(0));
            if (images.size() > 1 && !images.get(1).isEmpty()) setImageFromBase64(backImage, images.get(1));
            if (images.size() > 2 && !images.get(2).isEmpty()) setImageFromBase64(rightImage, images.get(2));
            if (images.size() > 3 && !images.get(3).isEmpty()) setImageFromBase64(leftImage, images.get(3));
            if (images.size() > 4 && !images.get(4).isEmpty()) setImageFromBase64(topImage, images.get(4));
        } catch (Exception e) {
            System.err.println("Error loading images: " + e.getMessage());
            loadDefaultImages();
        }
    }

    private void setImageFromBase64(ImageView imageView, String base64String) {
        try {
            if (base64String != null && !base64String.isEmpty()) {
                byte[] imageData = Base64.getDecoder().decode(base64String);
                Image image = new Image(new ByteArrayInputStream(imageData));
                imageView.setImage(image);
            } else {
                loadDefaultImage(imageView);
            }
        } catch (IllegalArgumentException e) {
            System.err.println("Error decoding Base64 image: " + e.getMessage());
            loadDefaultImage(imageView);
        }
    }

    private void loadDefaultImages() {
        loadDefaultImage(frontImage);
        loadDefaultImage(backImage);
        loadDefaultImage(rightImage);
        loadDefaultImage(leftImage);
        loadDefaultImage(topImage);
    }

    private void loadDefaultImage(ImageView imageView) {
        try {
            String defaultImagePath = "/View/Img/example/example" +
                    imageView.getId().substring(0, 1).toUpperCase() +
                    imageView.getId().substring(1) + ".png";
            URL imageUrl = getClass().getResource(defaultImagePath);
            if (imageUrl != null) {
                imageView.setImage(new Image(imageUrl.toString()));
            }
        } catch (Exception e) {
            System.err.println("Error loading default image: " + e.getMessage());
        }
    }


    @FXML
    private void handleDownload() {
        if (selectedOrder == null || qaNameField.getText().trim().isEmpty()) {
            showError("Error", "Please ensure order is selected and QA name is filled in");
            return;
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save QC Report");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("PDF files (*.pdf)", "*.pdf")
        );

        File file = fileChooser.showSaveDialog(downloadButton.getScene().getWindow());
        if (file != null) {
            try {
                generateAndSaveQCReport(file);
                showInfo("Success", "Report successfully downloaded");
            } catch (Exception e) {
                showError("Error", "Failed to generate and save the report: " + e.getMessage());
            }
        }
    }


    private void generateAndSaveQCReport(File outputFile) throws IOException {
        if (selectedOrder == null) {
            throw new IllegalArgumentException("No order is selected");
        }

        List<String> images = selectedOrder.getImages();

        // Create QCReport with all required parameters
        QCReport report = new QCReport(
                selectedOrder.getId(),
                selectedOrder.getOrder_number(),
                qaNameField.getText().trim(),
                selectedOrder.getStatus(),
                "", // email field - if you have it in the UI, get it from there
                images != null && images.size() > 0 ? images.get(0) : "", // front
                images != null && images.size() > 1 ? images.get(1) : "", // back
                images != null && images.size() > 2 ? images.get(2) : "", // left
                images != null && images.size() > 3 ? images.get(3) : "", // right
                images != null && images.size() > 4 ? images.get(4) : "", // top
                notesArea.getText()
        );

        try {
            PDFGenerator pdfGenerator = new PDFGenerator();
            pdfGenerator.generateQCReport(report, outputFile.getAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
            throw new IOException("Failed to generate PDF: " + e.getMessage());
        }
    }



    private void handleApprove() {
        if (selectedOrder != null) {
            selectedOrder.setStatus("Approved");
            orderDB.updateOrder(selectedOrder);
            orderStatusField.setText("Approved");
            updateView();
            showInfo("Status Updated", "Order has been approved");
        }
    }

    private void handleReject() {
        if (selectedOrder != null) {
            selectedOrder.setStatus("Rejected");
            orderDB.updateOrder(selectedOrder);
            orderStatusField.setText("Rejected");
            updateView();
            showInfo("Status Updated", "Order has been rejected");
        }
    }

    private void handleSend() {
        // TODO: Implement sending functionality
        showInfo("Order Sent", "Order has been sent successfully");
    }

    private void showError(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void showInfo(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    // Move the getters to the bottom
    public static Order getSelectedOrder() {
        return selectedOrder;
    }

    public static String getQaName() {
        return qaName;
    }

    public static void setSelectedOrder(Order order) {
        selectedOrder = order;
    }
}