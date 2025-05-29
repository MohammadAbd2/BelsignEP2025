package GUI.Controller;

import BE.Order;
import BLL.OrderService;
import GUI.Model.CameraCapture;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.List;

public class OperatorController {

    @FXML private ImageView logoImage;
    @FXML private Button saveButton;
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
    @FXML private BorderPane operatorAnchorPane;

    private Stage stage;
    private static Order selectedOrder;
    private static StackPane targetPFI;

    private final CameraCapture cameraCapture = new CameraCapture();

    @FXML
    public void initialize() {
        // Initialize status options
        if (statusComboBox != null) {
            statusComboBox.getItems().addAll("Pending", "Completed", "In Progress");
        }

        // Load data into fields if selectedOrder is not null
        if (selectedOrder != null) {
            loadOrderData(selectedOrder);
        }
    }

    // Loads the selected order's data into the UI fields
    private void loadOrderData(Order order) {
        orderNumberField.setText(order.getOrder_number());
        notesArea.setText(order.getNotes());

        // Display images in grid
        List<String> imagePaths = order.getImages();
        if (imagePaths != null) {
            imageGrid.getChildren().clear();
            int column = 0;
            int row = 0;

            for (String path : imagePaths) {
                File file = new File(path);
                if (file.exists()) {
                    Image image = new Image(file.toURI().toString());
                    StackPane imagePane = createImagePane(image);
                    imageGrid.add(imagePane, column, row);
                    column = (column + 1) % 2;
                    if (column == 0) row++;
                }
            }
        }

        saveButton.setOnAction(e -> saveOrder(order));
    }

    // Triggered when the user clicks on the image pane
    @FXML
    private void handleAddImage(MouseEvent event) {
        Object source = event.getSource();
        StackPane targetPane;

        // Case 1: User clicked directly on the StackPane
        if (source instanceof StackPane) {
            targetPane = (StackPane) source;
        }

        // Case 2: User clicked on a Label inside the StackPane
        else if (source instanceof Label) {
            Label clickedLabel = (Label) source;
            if (clickedLabel.getParent() instanceof StackPane) {
                targetPane = (StackPane) clickedLabel.getParent();
            } else {
                targetPane = null;
            }
        } else {
            targetPane = null;
        }

        // If targetPane is identified correctly, proceed
        if (targetPane != null) {
            Alert choiceAlert = new Alert(Alert.AlertType.CONFIRMATION);
            choiceAlert.setTitle("Add Image");
            choiceAlert.setHeaderText("Choose image source:");

            ButtonType cameraButton = new ButtonType("Camera");
            ButtonType fileButton = new ButtonType("Choose Picture");
            ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

            choiceAlert.getButtonTypes().setAll(cameraButton, fileButton, cancelButton);

            choiceAlert.showAndWait().ifPresent(type -> {
                if (type == fileButton) {
                    chooseImageFromFile(targetPane);
                } else if (type == cameraButton) {
                    captureImageFromCamera(targetPane);
                }
            });

        } else {
            System.err.println("Could not determine target StackPane for image insertion.");
        }
    }

    // Allows the user to select an image from the file system
    private void chooseImageFromFile(StackPane targetPane) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose an Image");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.jpeg", "*.png", "*.webp")
        );

        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            Image selectedImage = new Image(file.toURI().toString());
            addImageToPane(targetPane, selectedImage);
        }
    }

    // Captures an image from the connected camera
    private void captureImageFromCamera(StackPane targetPane) {
        new Thread(() -> {
            try {
                cameraCapture.startCameraAndCapture();
                Image capturedImage = cameraCapture.getCapturedImage();
                if (capturedImage != null) {
                    Platform.runLater(() -> addImageToPane(targetPane, capturedImage));
                } else {
                    Platform.runLater(() -> showAlert("Capture Failed", "Image capture returned null."));
                }
            } catch (Exception e) {
                Platform.runLater(() -> showAlert("Camera Error", "Failed to capture image: " + e.getMessage()));
            }
        }).start();
    }

    // Adds an image to the specified StackPane with delete icon functionality
    private void addImageToPane(StackPane targetPane, Image image) {
        ImageView imageView = new ImageView(image);
        imageView.fitWidthProperty().bind(targetPane.widthProperty());
        imageView.fitHeightProperty().bind(targetPane.heightProperty());
        imageView.setPreserveRatio(true);

        ImageView trashIcon = new ImageView(new Image(getClass().getResourceAsStream("/Img/trash.png")));
        trashIcon.setFitWidth(25);
        trashIcon.setFitHeight(20);
        trashIcon.setOpacity(0);
        trashIcon.setMouseTransparent(true);

        StackPane.setAlignment(trashIcon, Pos.TOP_RIGHT);
        StackPane.setMargin(trashIcon, new Insets(5));

        StackPane imageContainer = new StackPane(imageView, trashIcon);
        imageContainer.setOnMouseEntered(e -> {
            trashIcon.setOpacity(0.9);
            trashIcon.setMouseTransparent(false);
        });
        imageContainer.setOnMouseExited(e -> {
            trashIcon.setOpacity(0);
            trashIcon.setMouseTransparent(true);
        });

        trashIcon.setOnMouseClicked(e -> {
            e.consume();
            targetPane.getChildren().clear();
            targetPane.getChildren().add(createPlusLabel());
        });

        targetPane.getChildren().clear();
        targetPane.getChildren().add(imageContainer);
    }

    // Helper method to create a StackPane with the plus sign
    private StackPane createImagePane(Image image) {
        StackPane pane = new StackPane();
        pane.setPrefSize(200, 200);
        pane.getChildren().add(createPlusLabel());
        pane.setOnMouseClicked(this::handleAddImage);
        addImageToPane(pane, image);
        return pane;
    }

    // Creates a plus label for empty image placeholders
    private Label createPlusLabel() {
        Label plus = new Label("+");
        plus.setStyle("-fx-font-size: 48; -fx-text-fill: #999;");
        plus.setOnMouseClicked(this::handleAddImage);
        return plus;
    }

    // Saves the order data
    public void saveOrder(Order order) {
        OrderService orderService = new OrderService();
        order.setStatus("New");
        order.setNotes(notesArea.getText());
        cameraCapture.saveCapturedImage();
        orderService.updateOrder(order);
    }

    // Shows an alert with a title and message
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
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

    public static void setSelectedOrder(Order order) {
        selectedOrder = order;
    }

    public static void setTargetPFI(StackPane targetPane) {
        targetPFI = targetPane;
    }
}