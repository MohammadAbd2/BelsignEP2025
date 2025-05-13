package GUI.Controller;

import com.gluonhq.attach.camera.CameraService;
import com.gluonhq.attach.camera.CameraService.PhotoHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.InputStream;
import java.util.Optional;

import java.io.File;

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
    private void handleAddImage() {
        Optional<CameraService> cameraService = CameraService.create();
        if (cameraService.isPresent()) {
            cameraService.get().startPhotoCapture(new PhotoHandler() {
                @Override
                public void onPhotoCaptured(InputStream photoInputStream) {
                    Image image = new Image(photoInputStream);
                    ImageView imageView = new ImageView(image);
                    imageView.setFitWidth(200);
                    imageView.setFitHeight(150);
                    imageView.setPreserveRatio(true);

                    // UI updates must happen on the FX thread
                    javafx.application.Platform.runLater(() -> {
                        imageGrid.getChildren().remove(addImagePlaceholder);
                        imageGrid.getChildren().add(imageView);
                        imageGrid.getChildren().add(addImagePlaceholder);
                    });
                }

                @Override
                public void onError(String message, Exception exception) {
                    System.err.println("Camera error: " + message);
                    exception.printStackTrace();
                }
            });
        } else {
            System.out.println("Camera service is not available on this platform.");
        }
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