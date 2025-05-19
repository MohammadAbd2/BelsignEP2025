package GUI.Controller;

import BE.Order;
import BLL.OrderService;
import DAL.OrderDB;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class OrderPageController {
    private OrderDB orderDB;

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

    public OrderPageController() {
        orderDB = new OrderDB();
    }

    public void initialize() {
        System.out.println("OrderPageController initialized!");

        // Hook up filter actions
        ordersNew.setOnAction(e -> filterByStatus("new"));
        ordersApproved.setOnAction(e -> filterByStatus("approved"));
        ordersPending.setOnAction(e -> filterByStatus("pending"));
        ordersRejected.setOnAction(e -> filterByStatus("rejected"));

        searchField.setOnKeyReleased(e -> filterByText(searchField.getText()));

        // Debug print before loading orders
        System.out.println("About to load orders...");
        loadOrders();
        System.out.println("Orders loaded and displayed");
    }

    private void loadOrders() {
        List<Order> orders = orderDB.getAllOrders();
        System.out.println("Number of orders loaded from DB: " + orders.size());

        // Add more detailed logging for status
        orders.forEach(order ->
                System.out.println("Loaded order ID: " + order.getId() +
                        ", Number: " + order.getOrder_number() +
                        ", Status: " + order.getStatus())
        );




        populateOrdersGrid(orders);
    }

    private void populateOrdersGrid(List<Order> orders) {
        try {
            System.out.println("Starting to populate grid");
            // Clear existing items
            ordersGrid.getChildren().clear();

            // Create 5x2 grid
            int columns = 5;
            int rows = 2;
            int maxCards = columns * rows;

            // Limit the number of orders to prevent overflow
            List<Order> displayOrders = orders.stream()
                    .limit(maxCards)
                    .collect(Collectors.toList());

            System.out.println("Displaying " + displayOrders.size() + " orders out of " + orders.size() + " total");

            for (int i = 0; i < maxCards; i++) {
                try {
                    // Calculate grid position
                    int column = i % columns;
                    int row = i / columns;

                    System.out.println("Loading card for position: row=" + row + ", column=" + column);

                    // Load the OrderCard.fxml layout
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/OrderCard.fxml"));
                    Node orderCard = loader.load();

                    // Get the controller and set the order data (might be null)
                    OrderCardController controller = loader.getController();
                    Order order = (i < displayOrders.size()) ? displayOrders.get(i) : null;

                    if (order != null) {
                        System.out.println("Setting order data for card: " + order.getOrder_number());
                    }

                    controller.setOrderData(order);

                    // Add to grid at calculated position
                    ordersGrid.add(orderCard, column, row);

                } catch (IOException e) {
                    System.err.println("Error loading OrderCard.fxml: " + e.getMessage());
                    e.printStackTrace();
                }
            }
            System.out.println("Grid population completed");

        } catch (Exception e) {
            System.err.println("Unexpected error in populateOrdersGrid: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void filterByStatus(String status) {
        System.out.println("Filtering by status: " + status);
        List<Order> allOrders = orderDB.getAllOrders();
        List<Order> filteredOrders = allOrders.stream()
                .filter(order -> order.getStatus().equalsIgnoreCase(status))
                .collect(Collectors.toList());
        System.out.println("Found " + filteredOrders.size() + " orders with status: " + status);
        populateOrdersGrid(filteredOrders);
    }

    private void filterByText(String text) {
        if (text == null || text.trim().isEmpty()) {
            loadOrders();
            return;
        }

        System.out.println("Filtering by text: " + text);
        List<Order> allOrders = orderDB.getAllOrders();
        List<Order> filteredOrders = allOrders.stream()
                .filter(order -> order.getOrder_number().toLowerCase().contains(text.toLowerCase()))
                .collect(Collectors.toList());
        System.out.println("Found " + filteredOrders.size() + " orders matching text: " + text);
        populateOrdersGrid(filteredOrders);
    }
}
