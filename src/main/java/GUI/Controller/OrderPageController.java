package GUI.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.layout.GridPane;

public class OrderPageController {

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