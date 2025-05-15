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
    private GridPane ordersGrid;

    public void initialize() {
        System.out.println("OrderPageController initialized!");

        // Hook up filter actions
        ordersNew.setOnAction(e -> filterByStatus("new"));
        ordersApproved.setOnAction(e -> filterByStatus("approved"));
        ordersPending.setOnAction(e -> filterByStatus("pending"));
        ordersRejected.setOnAction(e -> filterByStatus("rejected"));

        searchField.setOnKeyReleased(e -> filterByText(searchField.getText()));

        // Get the orders and populate the grid
        List<Order> orders = getSampleOrders();
        populateOrdersGrid(orders);
    }

    private void populateOrdersGrid(List<Order> orders) {
        // Clear existing items
        ordersGrid.getChildren().clear();

        // Create 5x2 grid
        int columns = 5;
        int rows = 2;

        for (int i = 0; i < (columns * rows); i++) {
            try {
                // Calculate grid position
                int column = i % columns;
                int row = i / columns;

                // Load the OrderCard.fxml layout
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/OrderCard.fxml"));
                Node orderCard = loader.load();

                // Get the controller and set the order data (might be null)
                OrderCardController controller = loader.getController();
                Order order = (i < orders.size()) ? orders.get(i) : null;
                controller.setOrderData(order);

                // Add to grid at calculated position
                ordersGrid.add(orderCard, column, row);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private List<Order> getSampleOrders() {
        List<Order> orders = new ArrayList<>(10); // Initialize with capacity of 10
        // Pre-fill the list with 10 null elements
        for (int i = 0; i < 10; i++) {
            orders.add(null);
        }

        // Add actual orders at specific positions (indices 0-9)
        orders.set(0, new Order(1, "Order #001", "", "", "new", "Sample Product 1", null));
        orders.set(1, new Order(2, "Order #002", "", "", "approved", "Sample Product 2", null));
        orders.set(2, new Order(3, "Order #003", "", "", "pending", "Sample Product 3", null));

        // The remaining positions (3-9) will stay null, creating empty spaces in the grid
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