package GUI.View;

import BE.Order;
import BLL.OrderService;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.util.List;


public class Orders {
    static OrderService orderService = new OrderService();
    public static VBox loadOrdersComponent() {

        VBox mainContainer = new VBox();
        mainContainer.setPadding(new Insets(15));
        mainContainer.setPrefWidth(700);
        mainContainer.setAlignment(Pos.TOP_RIGHT);

        // Set width after the scene is initialized
        Platform.runLater(() -> {
            Scene scene = mainContainer.getScene();
            if (scene != null) {
                mainContainer.prefWidthProperty().bind(((Scene) scene).widthProperty().multiply(0.7));
            }
        });

        // TextField for searching orders
        TextField searchBox = new TextField();
        searchBox.setStyle("-fx-background-color: white;");
        searchBox.setPromptText("Search orders...");
        searchBox.setPrefWidth(300);


        // Filter options
        HBox filterBox = new HBox(10);
        filterBox.setStyle("-fx-background-color: transparent;");
        filterBox.setAlignment(Pos.TOP_RIGHT);
        Button sortByBtn = new Button("Sort By");
        sortByBtn.setStyle("-fx-background-color: #DF6800; -fx-text-fill: white;");
        Button priceLowBtn = new Button("Price: Lower");
        Button priceHighBtn = new Button("Price: Highest");
        Button latestBtn = new Button("Latest");
        filterBox.getChildren().addAll(sortByBtn, priceLowBtn, priceHighBtn, latestBtn);
        filterBox.setPadding(new Insets(5));

        // Top bar containing the search box and filter box
        HBox topBar = new HBox();
        topBar.setStyle("-fx-background-color: transparent;");
        topBar.getChildren().addAll(searchBox, filterBox);
        topBar.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(filterBox, Priority.ALWAYS);

        // ScrollPane to hold the order cards
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setStyle("-fx-background-color: #0abae0 !important;");
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefHeight(600);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        // FlowPane to display orders
        FlowPane orderPane = new FlowPane();
        orderPane.setStyle("-fx-background-color: linear-gradient(to right, #87CEFA, #0b48cd, #0d80ad);");
        orderPane.setHgap(20);
        orderPane.setVgap(20);
        orderPane.setPrefWrapLength(600);

        List<Order> orders = orderService.loadOrders();
        for (Order order : orders) {
            orderPane.getChildren().add(createorderCard(order));
        }

        scrollPane.setContent(orderPane);
        mainContainer.getChildren().addAll(topBar, scrollPane);
        mainContainer.setStyle("-fx-background-color: linear-gradient(to right, #87CEFA, #0b48cd, #0d80ad);");
        return mainContainer;
    }



    public static VBox createorderCard(Order order) {
        VBox orderCard = new VBox();
        orderCard.setStyle("-fx-background-color: transparent;");
        orderCard.setSpacing(8);
        orderCard.setPadding(new Insets(10));
        orderCard.setPrefWidth(160);
        orderCard.setAlignment(Pos.CENTER);

        orderCard.setOnMouseClicked(orderclicked -> {
            System.out.println("order Clicked" + order);

        });
        ImageView orderImage = new ImageView(new Image("/img/logo.png"));
        orderImage.setFitWidth(160);
        orderImage.setFitHeight(120);

        Text orderDescription = new Text("Description : " + order.getId() + order.getOrder_number());

        orderCard.getChildren().addAll(orderImage, orderDescription);
        return orderCard;
    }


}
