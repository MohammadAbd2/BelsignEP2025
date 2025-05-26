package GUI.View.Header;

import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

public class CustomTitleBar extends HBox {


    private double xOffset = 0;
    private double yOffset = 0;

    public CustomTitleBar(Stage stage) {
        // Create Buttons
        Button minimizeBtn = new Button("🟡");  // O for Minimize
        Button closeBtn = new Button("❌");  // X for Close

        // Apply CSS classes
        minimizeBtn.getStyleClass().add("title-bar-button");
        closeBtn.getStyleClass().add("title-bar-button");
        this.getStyleClass().addAll("title-bar", "transparent-background");

        // Button Actions
        minimizeBtn.setOnAction(e -> stage.setIconified(true));
        closeBtn.setOnAction(e -> stage.close());

        // Spacer to push buttons to the right
        Region spacer = new Region();
        spacer.setPrefWidth(800);  // Adjust based on your window width
        this.setAlignment(Pos.CENTER_RIGHT);

        // Add components to title bar
        this.getChildren().addAll(spacer, minimizeBtn, closeBtn);
        this.setStyle("-fx-padding: 5px;");

        // Make Window Draggable
        this.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });

        this.setOnMouseDragged(event -> {
            stage.setX(event.getScreenX() - xOffset);
            stage.setY(event.getScreenY() - yOffset);
        });

        // Change cursor when hovering over the title bar
        this.setOnMouseEntered(e -> this.setCursor(Cursor.HAND));
        this.setOnMouseExited(e -> this.setCursor(Cursor.DEFAULT));
        this.getStyleClass().add("custom-title-bar");
    }
}