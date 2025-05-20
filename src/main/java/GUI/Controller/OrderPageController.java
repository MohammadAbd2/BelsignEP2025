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
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class OrderPageController {
    private OrderDB orderDB;
    private int currentPage = 1;
    private int ordersPerPage = 10; // 5 columns × 2 rows
    private List<Order> currentOrders; // Store current filter state


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
    private Button prevButton;

    @FXML
    private Button nextButton;

    @FXML
    private Button page1Button;

    @FXML
    private Button page2Button;

    @FXML
    private Button page3Button;

    @FXML
    private GridPane ordersGrid;

    public OrderPageController() {
        orderDB = new OrderDB();
    }

    private void updateStatusLabels() {
        int newCount = orderDB.countOrdersByStatus("New");
        int approvedCount = orderDB.countOrdersByStatus("Approved");
        int pendingCount = orderDB.countOrdersByStatus("Pending");
        int rejectedCount = orderDB.countOrdersByStatus("Rejected");

        ordersNew.setText("New : " + newCount);
        ordersApproved.setText("Approved : " + approvedCount);
        ordersPending.setText("Pending : " + pendingCount);
        ordersRejected.setText("Rejected : " + rejectedCount);
    }

    public void initialize() {
        System.out.println("OrderPageController initialized!");

        // Update status labels with actual counts
        updateStatusLabels();

        // Hook up filter actions
        ordersNew.setOnAction(e -> filterByStatus("new"));
        ordersApproved.setOnAction(e -> filterByStatus("approved"));
        ordersPending.setOnAction(e -> filterByStatus("pending"));
        ordersRejected.setOnAction(e -> filterByStatus("rejected"));

        searchField.setOnKeyReleased(e -> filterByText(searchField.getText()));

        // Setup pagination controls
        prevButton.setOnAction(e -> goToPreviousPage());
        nextButton.setOnAction(e -> goToNextPage());
        page1Button.setOnAction(e -> goToPage(1));
        page2Button.setOnAction(e -> goToPage(2));
        page3Button.setOnAction(e -> goToPage(3));


        // Debug print before loading orders
        System.out.println("About to load orders...");
        loadOrders();
        System.out.println("Orders loaded and displayed");
    }

    private void loadOrders() {
        currentOrders = orderDB.getAllOrders();
        System.out.println("Number of orders loaded from DB: " + currentOrders.size());

        // Add more detailed logging for status
        currentOrders.forEach(order ->
                System.out.println("Loaded order ID: " + order.getId() +
                        ", Number: " + order.getOrder_number() +
                        ", Status: " + order.getStatus())
        );

        updateStatusLabels();
        updatePaginationControls();
        displayCurrentPage();
    }


    private void populateOrdersGrid(List<Order> orders) {
        try {
            System.out.println("Starting to populate grid");
            ordersGrid.getChildren().clear();

            int columns = 5;
            int rows = 2;
            int maxCards = columns * rows;

            // Set fixed width for the grid
            ordersGrid.setMinWidth(1160);
            ordersGrid.setPrefWidth(1160);
            ordersGrid.setMaxWidth(1160);

            List<Order> displayOrders = orders.stream()
                    .limit(maxCards)
                    .collect(Collectors.toList());

            System.out.println("Displaying " + displayOrders.size() + " orders out of " + orders.size() + " total");

            for (int i = 0; i < maxCards; i++) {
                try {
                    int column = i % columns;
                    int row = i / columns;

                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/OrderCard.fxml"));
                    VBox orderCard = loader.load();  // Load as VBox since we know the root element is VBox

                    OrderCardController controller = loader.getController();
                    Order order = (i < displayOrders.size()) ? displayOrders.get(i) : null;

                    if (order != null) {
                        System.out.println("Setting order data for card: " + order.getOrder_number());
                    }

                    controller.setOrderData(order);
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


    private void updatePaginationControls() {
        int totalPages = (int) Math.ceil((double) currentOrders.size() / ordersPerPage);

        prevButton.setDisable(currentPage <= 1);
        nextButton.setDisable(currentPage >= totalPages);

        // Update page buttons
        page1Button.setDisable(false);
        page2Button.setDisable(false);
        page3Button.setDisable(false);

        // Highlight current page
        String defaultStyle = "-fx-background-radius: 5;";
        String selectedStyle = defaultStyle + "-fx-background-color: #0066AA;";

        page1Button.setStyle(currentPage == 1 ? selectedStyle : defaultStyle);
        page2Button.setStyle(currentPage == 2 ? selectedStyle : defaultStyle);
        page3Button.setStyle(currentPage == 3 ? selectedStyle : defaultStyle);

        // Update visibility
        page1Button.setVisible(totalPages >= 1);
        page2Button.setVisible(totalPages >= 2);
        page3Button.setVisible(totalPages >= 3);
    }

    private void displayCurrentPage() {
        int startIndex = (currentPage - 1) * ordersPerPage;
        int endIndex = Math.min(startIndex + ordersPerPage, currentOrders.size());

        List<Order> pageOrders = currentOrders.subList(startIndex, endIndex);
        populateOrdersGrid(pageOrders);

        // Log pagination info
        System.out.println("Displaying page " + currentPage +
                " (items " + (startIndex + 1) + "-" + endIndex +
                " of " + currentOrders.size() + ")");
    }

    private void goToPage(int page) {
        int totalPages = (int) Math.ceil((double) currentOrders.size() / ordersPerPage);
        if (page >= 1 && page <= totalPages) {
            currentPage = page;
            updatePaginationControls();
            displayCurrentPage();
        }
    }

    private void goToPreviousPage() {
        if (currentPage > 1) {
            currentPage--;
            updatePaginationControls();
            displayCurrentPage();
        }
    }

    private void goToNextPage() {
        int totalPages = (int) Math.ceil((double) currentOrders.size() / ordersPerPage);
        if (currentPage < totalPages) {
            currentPage++;
            updatePaginationControls();
            displayCurrentPage();
        }
    }

    private void filterByStatus(String status) {
        System.out.println("Filtering by status: " + status);
        List<Order> allOrders = orderDB.getAllOrders();
        currentOrders = allOrders.stream()
                .filter(order -> order.getStatus().equalsIgnoreCase(status))
                .collect(Collectors.toList());
        currentPage = 1; // Reset to first page when filtering
        updatePaginationControls();
        displayCurrentPage();
    }


    private void filterByText(String text) {
        if (text == null || text.trim().isEmpty()) {
            currentOrders = orderDB.getAllOrders();
        } else {
            List<Order> allOrders = orderDB.getAllOrders();
            currentOrders = allOrders.stream()
                    .filter(order -> order.getOrder_number().toLowerCase().contains(text.toLowerCase()))
                    .collect(Collectors.toList());
        }
        currentPage = 1; // Reset to first page when filtering
        updatePaginationControls();
        displayCurrentPage();
    }
}
