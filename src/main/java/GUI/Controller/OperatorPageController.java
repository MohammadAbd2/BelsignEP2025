package GUI.Controller;

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
    }

    @FXML
    private void handleAddImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose an Image");
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            Image image = new Image(file.toURI().toString());
            ImageView imageView = new ImageView(image);
            imageView.setFitWidth(200);
            imageView.setFitHeight(150);
            imageView.setPreserveRatio(true);

            imageGrid.getChildren().remove(addImagePlaceholder);
            imageGrid.getChildren().add(imageView);
            imageGrid.getChildren().add(addImagePlaceholder);
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