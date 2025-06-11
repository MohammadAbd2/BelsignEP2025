package GUI.Controller;

import BE.Order;
import DAL.OrderDB;
import GUI.View.SceneManager;
import Utils.LoggedInUser;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class OrdersController {
    private OrderDB orderDB;
    private int currentPage = 1;
    private int ordersPerPage = 10; // 5 columns × 2 rows
    private List<Order> currentOrders; // Store current filter state


    @FXML
    private TextField searchField;
    @FXML
    private ToggleButton ordersNew, ordersApproved, ordersPending, ordersRejected;
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
    @FXML
    private HBox buttonContainer;


    public OrdersController() {
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
        checkURole();

        // Hook up filter actions
        ordersNew.setOnAction(e -> applyFilters());
        ordersApproved.setOnAction(e -> applyFilters());
        ordersPending.setOnAction(e -> applyFilters());
        ordersRejected.setOnAction(e -> applyFilters());

        searchField.setOnKeyReleased(e -> filterByText(searchField.getText()));

        // Setup pagination controls
        prevButton.setOnAction(e -> goToPreviousPage());
        nextButton.setOnAction(e -> goToNextPage());
        page1Button.setOnAction(e -> goToPage(1));
        page2Button.setOnAction(e -> goToPage(2));
        page3Button.setOnAction(e -> goToPage(3));

        System.out.println("About to load orders...");
        loadOrders();
        System.out.println("Orders loaded and displayed");
    }

    public void checkURole(){
        System.out.println("OrdersController initialized!");
        System.out.println(LoggedInUser.getLoggedInRole() != null ? LoggedInUser.getLoggedInRole() : "No logged in user");
        if (LoggedInUser.getLoggedInRole() != null) {
            setButtonVisibility(buttonContainer, ordersNew, LoggedInUser.getLoggedInRole().equals("Operator") || LoggedInUser.getLoggedInRole().equals("Admin"));
            setButtonVisibility(buttonContainer, ordersApproved, LoggedInUser.getLoggedInRole().equals("QA") || LoggedInUser.getLoggedInRole().equals("Admin"));
            setButtonVisibility(buttonContainer, ordersPending, LoggedInUser.getLoggedInRole().equals("QA") || LoggedInUser.getLoggedInRole().equals("Admin"));
            setButtonVisibility(buttonContainer, ordersRejected, LoggedInUser.getLoggedInRole().equals("Operator") || LoggedInUser.getLoggedInRole().equals("Admin"));
        }

        // Filter which buttons are visible and usable based on role
        if ("Operator".equalsIgnoreCase(LoggedInUser.getLoggedInRole())) {
            // Show only 'New' and 'Rejected'
            ordersApproved.setVisible(false);
            ordersPending.setVisible(false);
            System.out.println(LoggedInUser.getLoggedInRole());
        } else if ("QA".equalsIgnoreCase(LoggedInUser.getLoggedInRole())) {
            // Show only 'Pending' and 'Approved'
            ordersNew.setVisible(false);
            ordersRejected.setVisible(false);
            System.out.println(LoggedInUser.getLoggedInRole());
        } else if ("Admin".equalsIgnoreCase(LoggedInUser.getLoggedInRole())) {
            // Admin Role showing all the filter
            ordersNew.setVisible(true);
            ordersApproved.setVisible(true);
            ordersPending.setVisible(true);
            ordersRejected.setVisible(true);
            System.out.println(LoggedInUser.getLoggedInRole());
        }
        // Update status labels with actual counts
        updateStatusLabels();
    }

    private void applyFilters() {
        checkURole();
        List<Order> allOrders = orderDB.getAllOrders();
        List<Order> filteredOrders = new ArrayList<>();

        if (!ordersNew.isSelected() && !ordersApproved.isSelected() &&
                !ordersPending.isSelected() && !ordersRejected.isSelected()) {
            // No filter selected: show all
            switch (LoggedInUser.getLoggedInRole()){
                case "Admin" : {
                    for (Order order : allOrders) {
                        if ((ordersNew.isSelected() && order.getStatus().equalsIgnoreCase("new")) ||
                                (ordersApproved.isSelected() && order.getStatus().equalsIgnoreCase("approved")) ||
                                (ordersPending.isSelected() && order.getStatus().equalsIgnoreCase("pending")) ||
                                (ordersRejected.isSelected() && order.getStatus().equalsIgnoreCase("rejected"))) {
                            filteredOrders.add(order);
                        } else if (((!ordersNew.isSelected() && order.getStatus().equalsIgnoreCase("new")) ||
                                (!ordersApproved.isSelected() && order.getStatus().equalsIgnoreCase("approved")) ||
                                (!ordersPending.isSelected() && order.getStatus().equalsIgnoreCase("pending")) ||
                                (!ordersRejected.isSelected() && order.getStatus().equalsIgnoreCase("rejected")))) {
                            filteredOrders.add(order);
                        }
                    }
                }
                case "Operator" : {
                    for (Order order : allOrders) {
                        if ((!ordersNew.isSelected() && order.getStatus().equalsIgnoreCase("new")) ||
                                (!ordersRejected.isSelected() && order.getStatus().equalsIgnoreCase("rejected"))) {
                            filteredOrders.add(order);
                        }
                    }
                }
                case "QA" : {
                    for (Order order : allOrders) {
                        if ((!ordersPending.isSelected() && order.getStatus().equalsIgnoreCase("pending")) ||
                                (!ordersApproved.isSelected() && order.getStatus().equalsIgnoreCase("approved"))) {
                            filteredOrders.add(order);
                        }
                    }
                }
            }

        } else {
            for (Order order : allOrders) {
                if ((ordersNew.isSelected() && order.getStatus().equalsIgnoreCase("new")) ||
                        (ordersApproved.isSelected() && order.getStatus().equalsIgnoreCase("approved")) ||
                        (ordersPending.isSelected() && order.getStatus().equalsIgnoreCase("pending")) ||
                        (ordersRejected.isSelected() && order.getStatus().equalsIgnoreCase("rejected"))) {
                    filteredOrders.add(order);
                }
            }
        }

        currentOrders = filteredOrders;
        currentPage = 1;
        updatePaginationControls();
        displayCurrentPage();
    }


    private void setButtonVisibility(HBox container, ToggleButton button, boolean shouldBeVisible) {
        if (shouldBeVisible) {
            if (!container.getChildren().contains(button)) {
                container.getChildren().add(button);
            }
        } else {
            container.getChildren().remove(button);
        }
    }


    private void loadOrders() {
        checkURole();
        List<Order> allOrders = orderDB.getAllOrders();
        String userRole = LoggedInUser.getLoggedInRole();

        if (userRole == null) {
            currentOrders = allOrders;
            System.out.println("No user role found, showing all orders");
            updateStatusLabels();
            updatePaginationControls();
            displayCurrentPage();
            return;
        }

        // Filter orders based on role
        switch (userRole) {
            case "Operator" -> {
                // Operators can only see New and Rejected orders
                currentOrders = allOrders.stream()
                        .filter(order ->
                                order.getStatus().equalsIgnoreCase("New") ||
                                        order.getStatus().equalsIgnoreCase("Rejected"))
                        .collect(Collectors.toList());
            }
            case "QA" -> {
                // QA can see Pending and Approved orders
                currentOrders = allOrders.stream()
                        .filter(order ->
                                order.getStatus().equalsIgnoreCase("Approved") ||
                                        order.getStatus().equalsIgnoreCase("Pending"))
                        .collect(Collectors.toList());
            }
            case "Admin" -> {
                // Admin can see all orders
                currentOrders = allOrders.stream()
                        .filter(order ->
                                order.getStatus().equalsIgnoreCase("New") ||
                                        order.getStatus().equalsIgnoreCase("Approved") ||
                                        order.getStatus().equalsIgnoreCase("Pending") ||
                                        order.getStatus().equalsIgnoreCase("Rejected")
                        )
                        .collect(Collectors.toList());
            }
            default -> currentOrders = new ArrayList<>();
        }

        System.out.println("Number of orders loaded for " + userRole + ": " + currentOrders.size());

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
        if (orders != null) {
            for (Order order : orders) {
                System.out.println("Order " + order.getId() + " has " +
                        (order.getImages() == null ? "null" : order.getImages().size()) + " images");
                if (order.getImages() != null && !order.getImages().isEmpty()) {
                    System.out.println("First image: " + order.getImages().get(0).substring(0, Math.min(50, order.getImages().get(0).length())) + "...");
                }
            }
        }

        checkURole();
        try {
            System.out.println("Starting to populate grid");
            ordersGrid.getChildren().clear();
            int columns = 5;
            int rows = 2;
            int maxCards = columns * rows;

            // Set fixed width for the grid
            ordersGrid.setMinWidth(870);
            ordersGrid.setPrefWidth(880);
//            ordersGrid.setMaxWidth(1160);
            ordersGrid.setMinHeight(350);

            List<Order> displayOrders = orders.stream()
                    .limit(maxCards)
                    .toList();
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
                    orderCard.setStyle("-fx-border-color: rgba(4,108,191,0.98); -fx-border-width: 1px; -fx-border-style: solid; ");

                    controller.setOrderData(order);
                    orderCard.setOnMouseClicked(e -> {
                        switch (LoggedInUser.getLoggedInRole()){
                            case "Operator": {
                                OperatorController operatorController = new OperatorController();
                                try {
                                    System.out.println("Operator Clicked on order card");
                                    OperatorController.setSelectedOrder(order);
                                    SceneManager.loadScene("navbar", "/View/Navbar.fxml");
                                    SceneManager.loadScene("operatorPage", "/View/Operator.fxml");
                                    SceneManager.loadScene("QA", "/View/QA.fxml");
                                    List<String> loginScenes = List.of(
                                            "navbar",
                                            "operatorPage"
                                    );
                                    SceneManager.composeScene(loginScenes, "ComposedOperator");
                                    SceneManager.switchScene("ComposedOperator");
                                } catch (IOException ex) {
                                    throw new RuntimeException(ex);
                                }

                            break;
                            }
                            case "QA" : {
                                try {
                                    System.out.println("QA Clicked on order card");
                                    QAController.setSelectedOrder(order);

                                    SceneManager.loadScene("navbar", "/View/Navbar.fxml");
                                    SceneManager.loadScene("QA", "/View/QA.fxml");

                                    List<String> loginScenes = List.of(
                                            "navbar",
                                            "QA"
                                    );
                                    SceneManager.composeScene(loginScenes, "ComposedQA");
                                    SceneManager.switchScene("ComposedQA");
                                } catch (IOException ex) {
                                    throw new RuntimeException(ex);
                                }
                            break;
                            }
                        };
                    });

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
        checkURole();
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
        checkURole();
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
