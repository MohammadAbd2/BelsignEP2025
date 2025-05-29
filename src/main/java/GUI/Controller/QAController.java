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

import java.io.File;
import java.io.IOException;

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
        
        updateImages(selectedOrder.getImage());

        // Update button states based on order status
        boolean isFinalized = "Approved".equalsIgnoreCase(selectedOrder.getStatus()) 
                         || "Rejected".equalsIgnoreCase(selectedOrder.getStatus());
        if (approveButton != null) approveButton.setDisable(isFinalized);
        if (rejectButton != null) rejectButton.setDisable(isFinalized);
    }

    private void updateImages(String basePath) {
        if (basePath == null) return;
        
        try {
            ClassLoader classLoader = getClass().getClassLoader();
            
            if (frontImage != null) {
                String frontPath = basePath + "/front.png";
                Image frontImg = new Image(classLoader.getResourceAsStream(frontPath));
                frontImage.setImage(frontImg);
            }
            
            if (backImage != null) {
                String backPath = basePath + "/back.png";
                Image backImg = new Image(classLoader.getResourceAsStream(backPath));
                backImage.setImage(backImg);
            }
            
            if (rightImage != null) {
                String rightPath = basePath + "/right.png";
                Image rightImg = new Image(classLoader.getResourceAsStream(rightPath));
                rightImage.setImage(rightImg);
            }
            
            if (leftImage != null) {
                String leftPath = basePath + "/left.png";
                Image leftImg = new Image(classLoader.getResourceAsStream(leftPath));
                leftImage.setImage(leftImg);
            }
            
            if (topImage != null) {
                String topPath = basePath + "/top.png";
                Image topImg = new Image(classLoader.getResourceAsStream(topPath));
                topImage.setImage(topImg);
            }
        } catch (Exception e) {
            System.err.println("Error loading images: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void handleDownload() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save QC Report");
        fileChooser.getExtensionFilters().add(
            new FileChooser.ExtensionFilter("PDF files (*.pdf)", "*.pdf")
        );
        
        File file = fileChooser.showSaveDialog(downloadButton.getScene().getWindow());
        if (file != null) {
            try {
                generateAndSaveQCReport(file);
                showInfo("Download", "Report successfully downloaded");
            } catch (Exception e) {
                showError("Error", "Failed to generate and save the report: " + e.getMessage());
            }

        }
    }

    private void generateAndSaveQCReport(File outputFile) throws IOException {
        if (selectedOrder == null) {
            throw new IllegalArgumentException("No order is selected");
        }

        QCReport report = new QCReport();
        report.setOrderNumber(selectedOrder.getOrder_number());
        report.setQaName(qaNameField.getText());
        report.setStatus(selectedOrder.getStatus());
        report.setNotes(selectedOrder.getNotes());

        String basePath = selectedOrder.getImage();
        if (basePath != null) {
            report.setFrontImage(basePath + "/front.png");
            report.setBackImage(basePath + "/back.png");
            report.setRightImage(basePath + "/right.png");
            report.setLeftImage(basePath + "/left.png");
            report.setTopImage(basePath + "/top.png");
        }
        try {
            PDFGenerator pdfGenerator = new PDFGenerator();
            pdfGenerator.generateQCReport(report, outputFile.getAbsolutePath());
        } catch (Exception e) {
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