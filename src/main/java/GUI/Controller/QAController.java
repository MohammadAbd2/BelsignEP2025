package GUI.Controller;

import BE.Order;
import BE.QCReport;
import BLL.OrderService;
import jakarta.mail.*;
import jakarta.mail.internet.*;
import java.io.File;
import java.util.Properties;
import javafx.concurrent.Task;
import Utils.PDFGenerator;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import jakarta.mail.PasswordAuthentication;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
    PDFGenerator pdfGenerator = new PDFGenerator();

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
        try{
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
            pdfGenerator.setReport(report);


            Node reportView = pdfGenerator.getReportView();

            Stage reportStage = new Stage();
            reportStage.setTitle("QC Report Preview");

            double width = 595;
            double height = 842;

            StackPane root = new StackPane(reportView);
            root.setAlignment(Pos.CENTER);
            root.setPadding(new Insets(10));

            Scene scene = new Scene(root, width, height);
            reportStage.setScene(scene);
            reportStage.initModality(Modality.APPLICATION_MODAL);
            reportStage.show();
        }catch (Exception e){
            System.out.println("Error in Preview QC Report : " + e.getMessage());
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
            if (path != null && !path.isEmpty()) {
                Image image = new Image("file:" + path);
                imageView.setImage(image);
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
    public void sendEmail(String recipientEmail, String subject, String content, File PDFfile) {
        final String fromEmail = "Mohammad.abd.dk@gmail.com"; // Replace with your sender email
        final String password = "jfeq buiw bbjw rljw";          // Replace with your email app password

        Task<Void> emailTask = new Task<>() {
            @Override
            protected Void call() {
                try {
                    Properties props = new Properties();
                    props.put("mail.smtp.host", "smtp.gmail.com");
                    props.put("mail.smtp.port", "465");
                    props.put("mail.smtp.auth", "true");
                    props.put("mail.smtp.ssl.enable", "true");
                    props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");

                    Session session = Session.getInstance(props, new Authenticator() {
                        @Override
                        protected jakarta.mail.PasswordAuthentication getPasswordAuthentication() {
                            return new PasswordAuthentication(fromEmail, password);
                        }
                    });

                    Message message = new MimeMessage(session);
                    message.setFrom(new InternetAddress(fromEmail));
                    message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
                    message.setSubject(subject);

                    // Create the text part of the email
                    MimeBodyPart textPart = new MimeBodyPart();
                    textPart.setText(content, "utf-8");

                    // Create the attachment part for the PDF file
                    MimeBodyPart attachmentPart = new MimeBodyPart();
                    attachmentPart.attachFile(PDFfile);

                    // Combine parts into a multipart message
                    Multipart multipart = new MimeMultipart();
                    multipart.addBodyPart(textPart);
                    multipart.addBodyPart(attachmentPart);

                    // Set the multipart content to the message
                    message.setContent(multipart);

                    // Send the email
                    Transport.send(message);
                    System.out.println("Email sent successfully to " + recipientEmail);

                } catch (Exception e) {
                    System.err.println("Failed to send email: " + e.getMessage());
                    e.printStackTrace();
                }
                return null;
            }
        };

        // Start the email sending task on a new thread
        new Thread(emailTask).start();
    }

    private void handleSend() {
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

        try{
            sendEmail(qaEmailField.getText(), "QC Report from Bellsign for order" + orderNumberField.getText(), notesArea.getText(), pdfGenerator.getReportFile(report)  );
            showInfo("Order Sent", "Order has been sent successfully");

        }catch (Exception e) {
            System.err.println("Failed to send email: " + e.getMessage());
            showError("Error", "Failed to send email to: " + qaEmailField.getText());

        }

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