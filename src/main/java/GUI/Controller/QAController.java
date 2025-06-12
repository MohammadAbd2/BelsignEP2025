package GUI.Controller;

import BE.Order;
import BE.QCReport;
import BLL.OrderService;
import Utils.PDFGenerator;
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
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

public class QAController {
    @FXML private TextField orderNumberField;
    @FXML private TextField qaNameField;
    @FXML private TextField qaEmailField;
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
    private OrderService orderService;

    @FXML
    public void initialize() {
        orderService = new OrderService();

        if (selectedOrder != null) {
            updateView();
        }

        setupButtonHandlers();
        setupTextListeners();
    }

    private void setupButtonHandlers() {
        previewButton.setOnAction(event -> {
            qaName = qaNameField.getText().trim();
            openQCReport();
        });

        downloadButton.setOnAction(event -> handleDownload());
        approveButton.setOnAction(event -> handleApprove());
        rejectButton.setOnAction(event -> handleReject());
        sendButton.setOnAction(event -> handleSend());
    }

    private void setupTextListeners() {
        notesArea.textProperty().addListener((observable, oldValue, newValue) -> {
            if (selectedOrder != null) {
                selectedOrder.setNotes(newValue);
                orderService.updateOrder(selectedOrder);
            }
        });
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

        orderNumberField.setText(selectedOrder.getOrder_number());
        orderStatusField.setText(selectedOrder.getStatus());
        notesArea.setText(selectedOrder.getNotes());

        List<String> imagePaths = selectedOrder.getImages();
        updateImages(imagePaths);

        boolean isFinalized = "Approved".equalsIgnoreCase(selectedOrder.getStatus())
                || "Rejected".equalsIgnoreCase(selectedOrder.getStatus());
        approveButton.setDisable(isFinalized);
        rejectButton.setDisable(isFinalized);
    }

    private void updateImages(List<String> paths) {
        try {
            setImageFromPath(frontImage, getPath(paths, 0));
            setImageFromPath(backImage, getPath(paths, 1));
            setImageFromPath(rightImage, getPath(paths, 2));
            setImageFromPath(leftImage, getPath(paths, 3));
            setImageFromPath(topImage, getPath(paths, 4));
        } catch (Exception e) {
            System.err.println("Error loading images: " + e.getMessage());
        }
    }

    private String getPath(List<String> paths, int index) {
        return (paths.size() > index) ? paths.get(index) : null;
    }

    private void setImageFromPath(ImageView imageView, String path) {
        try {
            if (path != null && !path.isEmpty()) {
                Image image = new Image("file:" + path);
                imageView.setImage(image);
            } else {
                loadDefaultImage(imageView);
            }
        } catch (Exception e) {
            System.err.println("Error loading image from path: " + e.getMessage());
            loadDefaultImage(imageView);
        }
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

    private void handleDownload() {
        if (selectedOrder == null || qaNameField.getText().trim().isEmpty()) {
            showError("Error", "Please ensure order is selected and QA name is filled in");
            return;
        }

        // Create dynamic file name
        String formattedDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("ddMMyyyy_HHmm"));
        String fileName = "QC_Report_Order_" + selectedOrder.getOrder_number() + "_" + formattedDate + ".pdf";

        // Configure FileChooser with default name
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save QC Report");
        fileChooser.setInitialFileName(fileName);  // Set the suggested file name
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("PDF files (*.pdf)", "*.pdf")
        );

        // Show save dialog
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

        QCReport report = new QCReport(
                selectedOrder.getId(),
                selectedOrder.getOrder_number(),
                qaNameField.getText().trim(),
                selectedOrder.getStatus(),
                qaEmailField.getText().trim(),
                getPath(images, 0),
                getPath(images, 1),
                getPath(images, 2),
                getPath(images, 3),
                getPath(images, 4),
                notesArea.getText()
        );

        PDFGenerator pdfGenerator = new PDFGenerator();
        pdfGenerator.generateQCReport(report, outputFile.getAbsolutePath());
    }

    private void handleApprove() {
        if (selectedOrder != null) {
            selectedOrder.setStatus("Approved");
            orderService.updateOrder(selectedOrder);
            orderStatusField.setText("Approved");
            updateView();
            showInfo("Status Updated", "Order has been approved");
        }
    }

    private void handleReject() {
        if (selectedOrder != null) {
            selectedOrder.setStatus("Rejected");
            orderService.updateOrder(selectedOrder);
            orderStatusField.setText("Rejected");
            updateView();
            showInfo("Status Updated", "Order has been rejected");
        }
    }

    private void handleSend() {
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