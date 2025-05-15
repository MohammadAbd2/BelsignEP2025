package GUI.Controller;

import BE.Order;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.layout.GridPane;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class OrderPageController {

    @FXML
    public VBox ordersList;
    @FXML
    private VBox ordersBody;

    @FXML
    private TextField searchField;

    @FXML
    private Button ordersNew;

    @FXML
    private Button ordersApproved;

    @FXML
    private Button ordersPending;

    @FXML
    private Button ordersRejected;

    @FXML
    private GridPane ordersGrid; // You'll need to assign fx:id to the GridPane in FXML

    public void initialize() {
        System.out.println("OrderPageController initialized!");

        // Hook up filter actions
        ordersNew.setOnAction(e -> filterByStatus("new"));
        ordersApproved.setOnAction(e -> filterByStatus("approved"));
        ordersPending.setOnAction(e -> filterByStatus("pending"));
        ordersRejected.setOnAction(e -> filterByStatus("rejected"));

        searchField.setOnKeyReleased(e -> filterByText(searchField.getText()));


        // TODO: change to fetch from DB
        List<Order> orders = getSampleOrders();

        for (Order order : orders) {
            try {
                // Load the OrderCard.fxml layout
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/OrderCard.fxml"));
                Node orderCard = loader.load();

                // Get the controller for the loaded FXML
                OrderCardController controller = loader.getController();

                // Pass the Order object to the card
                controller.setOrderData(order);

                // Add the card to the VBox
                ordersList.getChildren().add(orderCard);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private List<Order> getSampleOrders() {
        List<Order> orders = new ArrayList<>();
        // Sample data
        orders.add(new Order(1, "Order #001", "", "", "new"));
        orders.add(new Order(2, "Order #002", "", "", "approved"));
        orders.add(new Order(3, "Order #003", "", "", "pending"));
        return orders;
    }

    private void filterByStatus(String status) {
        System.out.println("Filtering by status: " + status);
        // TODO: Implement filter logic
    }

    private void filterByText(String text) {
        System.out.println("Filtering by text: " + text);
        // TODO: Implement search filter
    }
}