package GUI.Controller;

import GUI.Model.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;


import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class OperatorPageController {

    @FXML private ImageView logoImage;
    @FXML private ImageView profileImage;
    @FXML private Button editButton;

    @FXML private TextField searchField;
    @FXML private ComboBox<String> statusComboBox;
    @FXML private TextField orderNumberField;
    @FXML private TextArea notesArea;

    @FXML private TilePane imageGrid;
    @FXML private StackPane addImagePlaceholder;
    @FXML private TilePane cardGrid;

    @FXML private HBox titleBar;
    @FXML private Button minimizeButton;
    @FXML private Button closeButton;

    private Stage stage;


    // Called after FXML is loaded
    @FXML
    public void initialize() {
        addImagePlaceholder.setOnMouseClicked(e -> handleAddImage());
        if (statusComboBox != null) {
            statusComboBox.getItems().addAll("Pending", "Completed", "In Progress");
        } else {
            System.out.println("Error: statusComboBox is null!");
        }

        // Variables to store initial mouse position
        final double[] xOffset = {0};
        final double[] yOffset = {0};

// Record the current mouse position on press
        titleBar.setOnMousePressed(event -> {
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            xOffset[0] = stage.getX() - event.getScreenX();
            yOffset[0] = stage.getY() - event.getScreenY();
        });

// Update the stage position on drag
        titleBar.setOnMouseDragged(event -> {
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setX(event.getScreenX() + xOffset[0]);
            stage.setY(event.getScreenY() + yOffset[0]);
        });
    }


    @FXML
    public void handleAddImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose an Image");

        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.jpeg", "*.png", "*.webp")
        );

        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            try {
                String filePath = file.getAbsolutePath().toLowerCase();
                Image image;

                // Convert WebP to PNG if needed
                if (filePath.endsWith(".webp")) {
                    File convertedFile = convertWebPToPNG(file);
                    image = new Image(convertedFile.toURI().toString());
                } else {
                    image = new Image(file.toURI().toString());
                }

                // Create ImageView for the image
                ImageView imageView = new ImageView(image);
                imageView.setFitWidth(200);
                imageView.setFitHeight(150);
                imageView.setPreserveRatio(true);

                // Create trash icon (make sure the path is correct)
                ImageView trashIcon = new ImageView(new Image(getClass().getResourceAsStream("/Img/trash.png")));
                trashIcon.setFitWidth(25);
                trashIcon.setFitHeight(20);
                trashIcon.setStyle("-fx-background-color: #033678;");

                // Place the icon in the top-right corner
                StackPane.setAlignment(trashIcon, Pos.TOP_RIGHT);
                StackPane.setMargin(trashIcon, new Insets(5));

                // StackPane to hold the image and the trash icon
                StackPane imageContainer = new StackPane(imageView, trashIcon);
                trashIcon.setOpacity(0);

                // Show trash icon when mouse hovers over the image
                imageContainer.setOnMouseEntered(e -> {
                    trashIcon.setOpacity(0.9);
                    imageContainer.setStyle("-fx-cursor: hand");

                });
                imageContainer.setOnMouseClicked(e -> {
                        e.consume(); // Prevent event from propagating to the image
                        handleRemoveImage(imageContainer);
                });
                imageContainer.setOnMouseExited(e -> {
                    trashIcon.setOpacity(0);
                    imageView.setOpacity(1);
                    imageContainer.setStyle("-fx-cursor: pointer");
                });

                // Add the image container to the grid
                imageGrid.getChildren().remove(addImagePlaceholder);
                imageGrid.getChildren().add(imageContainer);
                imageGrid.getChildren().add(addImagePlaceholder);

            } catch (Exception e) {
                e.printStackTrace();
                showAlert("Error", "Failed to load the image, image type not supported!");
                Logger.RegisterLog("Failed to load the image, image type not supported!", 3);
                System.out.println(Logger.displayLogsByType(3).size() + " Error messages: " + Logger.displayLogsByType(3).getLast());
            }
        }
    }

@FXML
    public void handleRemoveImage(StackPane imageContainer) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Remove Image");
        alert.setHeaderText("Are you sure you would like to remove this image?");
        alert.setContentText(null);

        ButtonType yesButton = new ButtonType("Yes", ButtonBar.ButtonData.YES);
        ButtonType noButton = new ButtonType("No", ButtonBar.ButtonData.NO);
        alert.getButtonTypes().setAll(yesButton, noButton);

        alert.showAndWait().ifPresent(response -> {
            if (response == yesButton) {
                imageGrid.getChildren().remove(imageContainer);
            }
        });
    }



    // Helper method to convert WebP to PNG using an external command
    public File convertWebPToPNG(File webpFile) throws IOException {
        // Define the output file path for the converted image
        File outputFile = new File(webpFile.getParent(), "converted_image.png");

        // Run the cwebp command (you need to have cwebp installed)
        ProcessBuilder processBuilder = new ProcessBuilder(
                "cwebp", "-q", "80", webpFile.getAbsolutePath(), "-o", outputFile.getAbsolutePath()
        );
        Process process = processBuilder.start();
        try {
            int exitCode = process.waitFor();
            if (exitCode != 0) {
                throw new IOException("Failed to convert WebP to PNG.");
            }
        } catch (InterruptedException e) {
            throw new IOException("Error during WebP conversion.", e);
        }

        return outputFile;
    }

    // Helper method to show error alerts
    private void showAlert(String title, String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }



    public void closeStage(ActionEvent event) {
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    public void minimizeStage(ActionEvent event) {
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setIconified(true);
    }


}