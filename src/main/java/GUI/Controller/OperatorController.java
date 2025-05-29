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
    CameraCapture cameraCapture = new CameraCapture();

    private static StackPane targetPFI;

    @FXML
    public void initialize() {
        if (statusComboBox != null) {
            statusComboBox.getItems().addAll("Pending", "Completed", "In Progress");
        }
        if(selectedOrder != null){
            List<String> exampleImages = Arrays.asList(
                    "resources/Img/IMG_20250529_105549.jpg",
                    "resources/Img/IMG_20250529_105549.jpg",
                    "resources/Img/IMG_20250529_105549.jpg",
                    "resources/Img/IMG_20250529_105549.jpg",
                    "resources/Img/IMG_20250529_105549.jpg"
            );

            selectedOrder.setImages(exampleImages);
            if (selectedOrder.getImages() != null) {
                imageGrid.getChildren().clear();
                int column = 0;
                int row = 0;

                for (String imagePath : selectedOrder.getImages()) {
                    try {
                        File imageFile = new File(imagePath);
                        if (imageFile.exists()) {
                            Image image = new Image(imageFile.toURI().toString());

                            StackPane imagePane = new StackPane();
                            imagePane.setPrefWidth(200);
                            imagePane.setPrefHeight(200);
                            imagePane.getChildren().add(new Label("+"));

                            addImageToPane(imagePane, image);

                            imageGrid.add(imagePane, column, row);
                            column++;
                            if (column == 2) {
                                column = 0;
                                row++;
                            }
                        } else {
                            System.out.println("Image not found: " + imagePath);
                        }
                    } catch (Exception e) {
                        System.out.println("Failed to load image: " + imagePath + " Error: " + e.getMessage());
                    }
                }
            }

            saveButton.setOnAction(event -> {
                saveOrder(selectedOrder);
            });

            orderNumberField.setText(selectedOrder.getOrder_number());
            notesArea.setText(selectedOrder.getNotes());

            if (selectedOrder.getImages() != null) {
                for (String imagePath : selectedOrder.getImages()) {
                    try {
                        File imageFile = new File(imagePath);
                        if (imageFile.exists()) {
                            Image image = new Image(imageFile.toURI().toString());
                            addImageToPane(targetPFI, image);
                        } else {
                            System.out.println("Image not found: " + imagePath);
                        }
                    } catch (Exception e) {
                        System.out.println("Failed to load image: " + imagePath + " Error: " + e.getMessage());
                    }
                }
            }

        }

    }

    @FXML
    public void handleAddImage(MouseEvent event) {
        StackPane targetPane = (StackPane) event.getSource();

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
    }

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

    private void captureImageFromCamera(StackPane targetPane) {
        new Thread(() -> {
            try {

                cameraCapture.startCameraAndCapture(); // Blocking call

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

    private void addImageToPane(StackPane targetPane, Image image) {
        ImageView imageView = new ImageView(image);


        imageView.fitWidthProperty().bind(targetPane.widthProperty());
        imageView.fitHeightProperty().bind(targetPane.heightProperty());
        imageView.setPreserveRatio(true);
        imageView.setSmooth(true);
        imageView.setCache(true);

        targetPane.setMinWidth(400);
        targetPane.setMinHeight(300);
        targetPane.setPrefHeight(300);

        ImageView trashIcon = new ImageView(new Image(getClass().getResourceAsStream("/Img/trash.png")));
        trashIcon.setFitWidth(25);
        trashIcon.setFitHeight(20);
        StackPane.setAlignment(trashIcon, Pos.TOP_RIGHT);
        StackPane.setMargin(trashIcon, new Insets(5));
        trashIcon.setOpacity(0);
        trashIcon.setMouseTransparent(true);

        StackPane imageContainer = new StackPane(imageView, trashIcon);
        StackPane.setAlignment(imageView, Pos.CENTER);

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
            targetPane.getChildren().add(new Label("+"));

            targetPane.setPrefWidth(Region.USE_COMPUTED_SIZE);
            targetPane.setPrefHeight(Region.USE_COMPUTED_SIZE);
            targetPane.setMinWidth(Region.USE_COMPUTED_SIZE);
            targetPane.setMinHeight(Region.USE_COMPUTED_SIZE);
        });

        targetPane.getChildren().clear();
        targetPane.getChildren().add(imageContainer);
    }


    public void saveOrder(Order order) {
        OrderService orderService = new OrderService();
        order.setStatus("New");
        order.setNotes(notesArea.getText());
        cameraCapture.saveCapturedImage();
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

    public static void setTargetPFI(StackPane targetPane) {
        targetPFI = targetPane;
    }


}