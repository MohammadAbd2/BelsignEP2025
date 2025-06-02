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

    // ========================
    // FXML Components
    // ========================
    @FXML private TableView<User> tblUsers;
    @FXML private TableColumn<User, Integer> colId;
    @FXML private TableColumn<User, String> colName;
    @FXML private TableColumn<User, String> colRole;

    @FXML private Label lblId;                      // Displays selected user ID
    @FXML private TextField txtName;                // Displays/edits selected user name
    @FXML private ComboBox<String> cmbRole;         // Displays/edits selected user role
    @FXML private Button btnEdit;                   // Button to enable editing
    @FXML private Button btnSave;                   // Button to save changes

    @FXML private TextField searchBox;              // Search field for filtering users by name
    @FXML private ToggleButton btnFilterAdmin;      // Filter toggle for Admin role
    @FXML private ToggleButton btnFilterQA;         // Filter toggle for QA role
    @FXML private ToggleButton btnFilterOperator;   // Filter toggle for Operator role

    // ========================
    // Data Fields
    // ========================
    private final UserManager userManager = new UserManager();
    private ObservableList<User> usersObservable;   // List of users from DB

    // ========================
    // Initialization
    // ========================
    @FXML
    public void initialize() throws IOException {
        // Set the logged-in role for context
        LoggedInUser.setLoggedInRole("Admin");

        // Initialize table columns with User properties
        colId.setCellValueFactory(data -> new javafx.beans.property.SimpleIntegerProperty(data.getValue().getId()).asObject());
        colName.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getName()));
        colRole.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getRole()));

        // Initialize predefined roles in ComboBox
        cmbRole.setItems(FXCollections.observableArrayList("Admin", "QA", "Operator"));

        // Load users from DB and populate table
        loadUsers();

        // Add listener for row selection in TableView
        tblUsers.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                displayUserDetails(newSelection);
            }
        });

        // Add search functionality
        searchBox.textProperty().addListener((observable, oldValue, newValue) -> filterUsers());

        // Add filter toggle buttons functionality
        btnFilterAdmin.setOnAction(e -> filterUsers());
        btnFilterQA.setOnAction(e -> filterUsers());
        btnFilterOperator.setOnAction(e -> filterUsers());

        // Disable editing fields and Save button initially
        txtName.setDisable(true);
        cmbRole.setDisable(true);
        btnSave.setDisable(true);
    }

    // ========================
    // Load Users from Database
    // ========================
    private void loadUsers() {
        List<User> userList = userManager.getAllUsers();
        usersObservable = FXCollections.observableArrayList(userList);
        tblUsers.setItems(usersObservable);
    }

    // ========================
    // Display Selected User Details
    // ========================
    private void displayUserDetails(User user) {
        lblId.setText(String.valueOf(user.getId()));      // Display ID
        txtName.setText(user.getName());                 // Display Name
        cmbRole.setValue(user.getRole());                // Display Role
    }

    // ========================
    // Handle Edit Button Click
    // ========================
    @FXML
    private void handleEdit() {
        User selectedUser = tblUsers.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            // Enable fields for editing
            txtName.setDisable(false);
            cmbRole.setDisable(false);
            btnSave.setDisable(false);
            btnEdit.setDisable(true);

            System.out.println("Editing user: " + selectedUser.getName());
        }
    }

    // ========================
    // Handle Save Button Click
    // ========================
    @FXML
    private void handleSave() {
        User selectedUser = tblUsers.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            String newName = txtName.getText();
            String newRole = cmbRole.getValue();

            // Update user details in DB
            selectedUser.setName(newName);
            selectedUser.setRole(newRole);
            userManager.updateUser(selectedUser);

            // Refresh table to reflect changes
            tblUsers.refresh();

            // Reset editing fields
            txtName.setDisable(true);
            cmbRole.setDisable(true);
            btnSave.setDisable(true);
            btnEdit.setDisable(false);

            System.out.println("Saved user: " + selectedUser.getName());
        }
    }

    // ========================
    // Filter Users Based on Search and Toggles
    // ========================
    private void filterUsers() {
        String searchText = searchBox.getText().toLowerCase();
        ObservableList<User> filteredList = FXCollections.observableArrayList();

        for (User user : usersObservable) {
            boolean matchesSearch = user.getName().toLowerCase().contains(searchText);
            boolean matchesFilter =
                    (!btnFilterAdmin.isSelected() && !btnFilterQA.isSelected() && !btnFilterOperator.isSelected()) || // No filters selected
                            (btnFilterAdmin.isSelected() && user.getRole().equals("Admin")) ||
                            (btnFilterQA.isSelected() && user.getRole().equals("QA")) ||
                            (btnFilterOperator.isSelected() && user.getRole().equals("Operator"));

            if (matchesSearch && matchesFilter) {
                filteredList.add(user);
            }
        }

        // Update table with filtered list
        tblUsers.setItems(filteredList);
    }
}
