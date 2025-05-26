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
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.stage.FileChooser;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
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

    @FXML private GridPane imageGrid;
    @FXML private TilePane cardGrid;

    @FXML private HBox titleBar;
    @FXML private Button minimizeButton;
    @FXML private Button closeButton;
    @FXML private javafx.scene.layout.AnchorPane operatorAnchorPane;

    private Stage stage;


    // Called after FXML is loaded
    @FXML
    public void initialize() {
        if (statusComboBox != null) {
            statusComboBox.getItems().addAll("Pending", "Completed", "In Progress");
        } else {
            System.out.println("Error: statusComboBox is null!");
        }
        double screenHeight = Screen.getPrimary().getBounds().getHeight();
        double screenWidth = Screen.getPrimary().getBounds().getWidth();

        double verticalPadding = screenHeight * 0.05; // 5% height
        double horizontalPadding = screenWidth * 0.05; // 5% width

        operatorAnchorPane.setPadding(new Insets(verticalPadding, horizontalPadding, verticalPadding, horizontalPadding));

    }


    @FXML
    public void handleAddImage(javafx.scene.input.MouseEvent event) {
        StackPane targetPane = (StackPane) event.getSource();

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose an Image");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.jpeg", "*.png", "*.webp"));

        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            try {
                Image image = new Image(file.toURI().toString());

                ImageView imageView = new ImageView(image);
                imageView.setFitWidth(200);
                imageView.setFitHeight(150);
                imageView.setPreserveRatio(true);

                ImageView trashIcon = new ImageView(new Image(getClass().getResourceAsStream("/Img/trash.png")));
                trashIcon.setFitWidth(25);
                trashIcon.setFitHeight(20);
                StackPane.setAlignment(trashIcon, Pos.TOP_RIGHT);
                StackPane.setMargin(trashIcon, new Insets(5));
                trashIcon.setOpacity(0);

                StackPane imageContainer = new StackPane(imageView, trashIcon);

                imageContainer.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
                StackPane.setAlignment(imageView, Pos.CENTER);

                imageContainer.setOnMouseEntered(e -> trashIcon.setOpacity(0.9));
                imageContainer.setOnMouseExited(e -> trashIcon.setOpacity(0));

                trashIcon.setOnMouseClicked(e -> {
                    e.consume();
                    targetPane.getChildren().clear();
                    targetPane.getChildren().add(new Label("+"));
                });

                targetPane.getChildren().clear();
                targetPane.getChildren().add(imageContainer);

            } catch (Exception e) {
                e.printStackTrace();
                showAlert("Error", "Failed to load image!");
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