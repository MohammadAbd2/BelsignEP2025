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
import java.util.Arrays;
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

    private static final int MAX_COLUMNS = 3;
    private static final int MAX_SLOTS = 5;
    private static final int IMAGE_PANE_HEIGHT = 200;

    @FXML
    public void initialize() {
        // Setup status combo box options
        if (statusComboBox != null) {
            statusComboBox.getItems().addAll("Pending", "Completed", "In Progress");
        }

        // Configure GridPane for images to be responsive and flexible
        imageGrid.setHgap(10);
        imageGrid.setVgap(10);
        imageGrid.setPadding(new Insets(10));

        // Clear existing constraints and set flexible percentage widths for columns
        imageGrid.getColumnConstraints().clear();
        for (int i = 0; i < MAX_COLUMNS; i++) {
            ColumnConstraints cc = new ColumnConstraints();
            cc.setPercentWidth(100.0 / MAX_COLUMNS);
            cc.setHgrow(Priority.ALWAYS);
            imageGrid.getColumnConstraints().add(cc);
        }

        // Set row constraints for fixed height rows
        imageGrid.getRowConstraints().clear();
        RowConstraints rc = new RowConstraints();
        rc.setPrefHeight(IMAGE_PANE_HEIGHT);
        rc.setMinHeight(IMAGE_PANE_HEIGHT);
        rc.setMaxHeight(IMAGE_PANE_HEIGHT);
        imageGrid.getRowConstraints().add(rc);

        // Load selected order data if available
        if (selectedOrder != null) {
            loadOrderData(selectedOrder);
        }
        saveButton.setOnAction(event -> {
            saveOrder(selectedOrder);
        });
    }

    private void loadOrderData(Order order) {
        orderNumberField.setText(order.getOrder_number());
        notesArea.setText(order.getNotes());

        imageGrid.getChildren().clear();

        String imagesStr = String.valueOf(order.getImages());
        List<String> images = (imagesStr == null || imagesStr.trim().isEmpty())
                ? List.of()
                : Arrays.asList(imagesStr.split(";"));

        for (int i = 0; i < MAX_SLOTS; i++) {
            StackPane pane;
            if (i < images.size()) {
                String path = images.get(i).trim();
                File file = new File(path);
                if (file.exists()) {
                    Image image = new Image(file.toURI().toString());
                    pane = createResponsiveImagePane(image);
                } else {
                    pane = createPlusPane();
                }
            } else {
                pane = createPlusPane();
            }
            int col = i % MAX_COLUMNS;
            int row = i / MAX_COLUMNS;
            imageGrid.add(pane, col, row);
        }
    }

    private StackPane createPlusPane() {
        StackPane plusPane = new StackPane();
        plusPane.setPrefHeight(IMAGE_PANE_HEIGHT);
        plusPane.setMaxWidth(Double.MAX_VALUE);
        plusPane.getChildren().add(createPlusLabel());
        plusPane.setOnMouseClicked(this::handleAddImage);
        return plusPane;
    }

    private StackPane createResponsiveImagePane(Image image) {
        StackPane pane = new StackPane();
        pane.setPrefHeight(IMAGE_PANE_HEIGHT);
        pane.setMaxWidth(Double.MAX_VALUE);

        ImageView imageView = new ImageView(image);
        imageView.setPreserveRatio(true);
        imageView.setSmooth(true);
        imageView.setCache(true);

        // Bind imageView size to StackPane size to allow responsive resizing
        imageView.fitWidthProperty().bind(pane.widthProperty());
        imageView.fitHeightProperty().bind(pane.heightProperty());

        pane.getChildren().add(imageView);

        // Allow replacing image on click
        pane.setOnMouseClicked(this::handleAddImage);

        return pane;
    }

    @FXML
    private void handleAddImage(MouseEvent event) {
        Node source = (Node) event.getSource();
        StackPane targetPane;

        if (source instanceof StackPane) {
            targetPane = (StackPane) source;
        } else if (source.getParent() instanceof StackPane) {
            targetPane = (StackPane) source.getParent();
        } else {
            targetPane = null;
        }

        if (targetPane == null) {
            System.err.println("Target pane for image not found");
            return;
        }

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
            addImageToPane(targetPane, image);
        }
    }

    private void captureImageFromCamera(StackPane targetPane) {
        new Thread(() -> {
            try {
                cameraCapture.startCameraAndCapture();
                Image capturedImage = cameraCapture.getCapturedImage();
                if (capturedImage != null) {
                    Platform.runLater(() -> addImageToPane(targetPane, capturedImage));
                } else {
                    Platform.runLater(() -> showAlert("Capture Failed", "No image captured."));
                }
            } catch (Exception e) {
                Platform.runLater(() -> showAlert("Camera Error", e.getMessage()));
            }
        }).start();
    }

    private void addImageToPane(StackPane pane, Image image) {
        ImageView imageView = new ImageView(image);
        imageView.setPreserveRatio(true);
        imageView.setSmooth(true);
        imageView.setCache(true);

        // Bind size of imageView to pane width and height for responsiveness
        imageView.fitWidthProperty().bind(pane.widthProperty());
        imageView.fitHeightProperty().bind(pane.heightProperty());

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
            pane.getChildren().clear();
            pane.getChildren().add(createPlusLabel());
        });

        pane.getChildren().clear();
        pane.getChildren().add(imageContainer);
    }

    private Label createPlusLabel() {
        Label plus = new Label("+");
        plus.setStyle("-fx-font-size: 48; -fx-text-fill: #999;");
        plus.setAlignment(Pos.CENTER);
        plus.setMaxWidth(Double.MAX_VALUE);
        plus.setMaxHeight(Double.MAX_VALUE);
        return plus;
    }

    public void saveOrder(Order order) {
        OrderService orderService = new OrderService();
        order.setStatus("New");
        order.setNotes(notesArea.getText());
        cameraCapture.saveCapturedImage();
        orderService.updateOrder(order);
        cameraCapture.saveCapturedImage();
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