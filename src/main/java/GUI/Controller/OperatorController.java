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
import java.util.ArrayList;
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

    private final CameraCapture cameraCapture = new CameraCapture();

    private static final int MAX_COLUMNS = 3;
    private static final int MAX_SLOTS = 5;
    private static final int IMAGE_PANE_HEIGHT = 100;

    @FXML
    public void initialize() {
        if (statusComboBox != null) {
            statusComboBox.getItems().addAll("Pending", "Completed", "In Progress");
        }

        imageGrid.setHgap(10);
        imageGrid.setVgap(10);
        imageGrid.setPadding(new Insets(10));

        imageGrid.getColumnConstraints().clear();
        for (int i = 0; i < MAX_COLUMNS; i++) {
            ColumnConstraints cc = new ColumnConstraints();
            cc.setPercentWidth(100.0 / MAX_COLUMNS);
            cc.setHgrow(Priority.ALWAYS);
            imageGrid.getColumnConstraints().add(cc);
        }

        imageGrid.getRowConstraints().clear();
        RowConstraints rc = new RowConstraints();
        imageGrid.getRowConstraints().add(rc);

        if (selectedOrder != null) {
            loadOrderData(selectedOrder);
        }

        saveButton.setOnAction(event -> saveOrder(selectedOrder));
    }

    private void loadOrderData(Order order) {
        orderNumberField.setText(order.getOrder_number());
        notesArea.setText(order.getNotes());
        imageGrid.getChildren().clear();

        List<String> imagePaths = new ArrayList<>();
        List<?> outerList = order.getImages();
        if (outerList != null) {
            for (Object item : outerList) {
                if (item instanceof List<?>) {
                    for (Object subItem : (List<?>) item) {
                        if (subItem != null) imagePaths.add(subItem.toString().trim());
                    }
                } else if (item != null) {
                    imagePaths.add(item.toString().trim());
                }
            }
        }

        int imageCount = 0;
        for (String path : imagePaths) {
            if (imageCount >= MAX_SLOTS) break;
            File file = new File(path);
            if (file.exists()) {
                Image image = new Image(file.toURI().toString());
                int col = imageCount % MAX_COLUMNS;
                int row = imageCount / MAX_COLUMNS;
                imageGrid.add(createImageSlot(image), col, row);
                imageCount++;
            }
        }

        for (int i = imageCount; i < MAX_SLOTS; i++) {
            int col = i % MAX_COLUMNS;
            int row = i / MAX_COLUMNS;
            imageGrid.add(createEmptySlot(), col, row);
        }
    }

    private StackPane createImageSlot(Image image) {
        StackPane pane = new StackPane();
        pane.setMaxWidth(Double.MAX_VALUE);

        ImageView imageView = new ImageView(image);
        imageView.setPreserveRatio(true);
        imageView.setSmooth(true);
        imageView.setCache(true);
        imageView.fitWidthProperty().bind(pane.widthProperty());
        imageView.fitHeightProperty().bind(pane.heightProperty());

        ImageView trashIcon = new ImageView(new Image(getClass().getResourceAsStream("/Img/trash.png")));
        trashIcon.setFitWidth(25);
        trashIcon.setFitHeight(20);
        trashIcon.setOpacity(0);
        trashIcon.setMouseTransparent(true);

        StackPane.setAlignment(trashIcon, Pos.TOP_RIGHT);
        StackPane.setMargin(trashIcon, new Insets(5));

        pane.getChildren().addAll(imageView, trashIcon);

        pane.setOnMouseEntered(e -> {
            trashIcon.setOpacity(0.9);
            trashIcon.setMouseTransparent(false);
        });
        pane.setOnMouseExited(e -> {
            trashIcon.setOpacity(0);
            trashIcon.setMouseTransparent(true);
        });
        trashIcon.setOnMouseClicked(e -> {
            e.consume();
            imageGrid.getChildren().remove(pane);
            refillImageSlots();
        });

        return pane;
    }

    private StackPane createEmptySlot() {
        StackPane pane = new StackPane();
        pane.setMaxWidth(Double.MAX_VALUE);

        Label plus = new Label("+");
        plus.setStyle("-fx-font-size: 48; -fx-text-fill: #999;");
        pane.getChildren().add(plus);
        pane.setOnMouseClicked(this::handleAddImage);

        return pane;
    }

    private void refillImageSlots() {
        List<Node> images = new ArrayList<>();
        for (Node node : imageGrid.getChildren()) {
            if (node instanceof StackPane && ((StackPane) node).getChildren().stream().anyMatch(n -> n instanceof ImageView)) {
                images.add(node);
            }
        }

        imageGrid.getChildren().clear();
        int index = 0;
        for (Node image : images) {
            int col = index % MAX_COLUMNS;
            int row = index / MAX_COLUMNS;
            imageGrid.add(image, col, row);
            index++;
        }
        for (int i = index; i < MAX_SLOTS; i++) {
            int col = i % MAX_COLUMNS;
            int row = i / MAX_COLUMNS;
            imageGrid.add(createEmptySlot(), col, row);
        }
    }

    @FXML
    private void handleAddImage(MouseEvent event) {
        Node source = (Node) event.getSource();
        StackPane targetPane = (source instanceof StackPane) ? (StackPane) source : null;
        if (targetPane == null) return;

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Add Image");
        alert.setHeaderText("Choose image source:");
        ButtonType cameraBtn = new ButtonType("Camera");
        ButtonType fileBtn = new ButtonType("Choose Picture");
        ButtonType cancelBtn = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(cameraBtn, fileBtn, cancelBtn);

        alert.showAndWait().ifPresent(type -> {
            if (type == cameraBtn) {
                captureImageFromCamera(targetPane);
            } else if (type == fileBtn) {
                chooseImageFromFile(targetPane);
            }
        });
    }

    private void chooseImageFromFile(StackPane targetPane) {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Select Image");
        chooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.jpeg", "*.png", "*.webp")
        );

        File file = chooser.showOpenDialog(null);
        if (file != null) {
            Image image = new Image(file.toURI().toString());
            replaceWithImageSlot(targetPane, image);
        }
    }

    private void captureImageFromCamera(StackPane targetPane) {
        new Thread(() -> {
            try {
                cameraCapture.startCameraAndCapture();
                Image capturedImage = cameraCapture.getCapturedImage();
                if (capturedImage != null) {
                    Platform.runLater(() -> replaceWithImageSlot(targetPane, capturedImage));
                } else {
                    Platform.runLater(() -> showAlert("Capture Failed", "No image captured."));
                }
            } catch (Exception e) {
                Platform.runLater(() -> showAlert("Camera Error", e.getMessage()));
            }
        }).start();
    }

    private void replaceWithImageSlot(StackPane targetPane, Image image) {
        int index = GridPane.getRowIndex(targetPane) * MAX_COLUMNS + GridPane.getColumnIndex(targetPane);
        imageGrid.getChildren().remove(targetPane);
        StackPane imageSlot = createImageSlot(image);
        int col = index % MAX_COLUMNS;
        int row = index / MAX_COLUMNS;
        imageGrid.add(imageSlot, col, row);
    }

    public void saveOrder(Order order) {
        OrderService orderService = new OrderService();

        // Collect current image paths from imageGrid
        List<String> currentImagePaths = new ArrayList<>();

        for (Node node : imageGrid.getChildren()) {
            if (node instanceof StackPane) {
                StackPane pane = (StackPane) node;
                for (Node child : pane.getChildren()) {
                    if (child instanceof ImageView) {
                        ImageView imageView = (ImageView) child;
                        Image img = imageView.getImage();

                        if (img != null && img.getUrl() != null
                                && !img.getUrl().startsWith("file:/+")
                                && !img.getUrl().contains("trash.png")) {

                            // Extract file path from URL
                            String url = img.getUrl();
                            // URL format is like file:/C:/path/to/image.jpg
                            // Remove "file:/" prefix to get system path
                            if (url.startsWith("file:/")) {
                                String path = url.substring(6); // Adjust if needed per OS
                                currentImagePaths.add(path);
                            }
                        }
                    }
                }
            }
        }

        // Update order images list
        if (currentImagePaths.isEmpty()) {
            List<String> emptyImagePaths = new ArrayList<>();
            order.setImages(emptyImagePaths); // or new ArrayList<>() depending on Order class design
        } else {
            order.setImages(currentImagePaths);
        }

        // Set order status to Pending
        order.setStatus("Pending");

        // Set order notes from the input text area
        order.setNotes(notesArea.getText());

        // Save any captured image from the camera
        cameraCapture.saveCapturedImage();

        // Update the order in the database or backend service
        orderService.updateOrder(order);
    }



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
}