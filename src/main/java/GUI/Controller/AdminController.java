package GUI.Controller;

import BE.User;
import BLL.UserManager;
import Utils.LoggedInUser;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.IOException;
import java.util.List;

public class AdminController {

    @FXML
    private TableView<User> tblUsers;
    @FXML
    private TableColumn<User, Integer> colId;
    @FXML
    private TableColumn<User, String> colName;
    @FXML
    private TableColumn<User, String> colRole;

    @FXML
    private Label lblId;               // Label for displaying user ID
    @FXML
    private TextField txtName;        // TextField for displaying/editing the user name
    @FXML
    private ComboBox<String> cmbRole; // ComboBox for user role

    private final UserManager userManager = new UserManager();
    private ObservableList<User> usersObservable;

    @FXML
    public void initialize() throws IOException {
        LoggedInUser.setLoggedInRole("Admin");

        // Initialize columns
        colId.setCellValueFactory(data -> new javafx.beans.property.SimpleIntegerProperty(data.getValue().getId()).asObject());
        colName.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getName()));
        colRole.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getRole()));

        // Load all users from DB
        loadUsers();

        // Initialize ComboBox items (if you want predefined roles)
        cmbRole.setItems(FXCollections.observableArrayList("Admin", "QA", "Operator"));

        // Add listener for row click
        tblUsers.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                displayUserDetails(newSelection);
            }
        });
    }

    private void loadUsers() {
        List<User> userList = userManager.getAllUsers();
        usersObservable = FXCollections.observableArrayList(userList);
        tblUsers.setItems(usersObservable);
    }

    private void displayUserDetails(User user) {
        lblId.setText(String.valueOf(user.getId()));  // Display user ID
        txtName.setText(user.getName());             // Display user name
        cmbRole.setValue(user.getRole());            // Set selected role in ComboBox
    }
}
