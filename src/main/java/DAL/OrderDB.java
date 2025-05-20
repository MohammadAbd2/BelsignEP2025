package DAL;

import BE.Order;
import DAL.Interfaces.IOrderDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderDB implements IOrderDB {

    private final DBConnector dbConnector;

    public OrderDB() {
        this.dbConnector = new DBConnector();
        initializeStatusConstraint();
    }

    private void initializeStatusConstraint() {
        try (Connection conn = dbConnector.getConnection()) {
            // First check if the column exists
            String checkColumnSQL =
                    "IF NOT EXISTS (SELECT * FROM sys.columns " +
                            "WHERE Name = 'status' AND Object_ID = Object_ID('QC_Belsign_schema.[order]')) " +
                            "BEGIN " +
                            "    ALTER TABLE QC_Belsign_schema.[order] ADD status VARCHAR(10) NOT NULL DEFAULT 'New'; " +
                            "END";

            // Then ensure the constraint exists
            String checkConstraintSQL =
                    "IF NOT EXISTS (SELECT * FROM sys.check_constraints " +
                            "WHERE OBJECT_NAME(parent_object_id) = 'order' AND name = 'CHK_Order_Status') " +
                            "BEGIN " +
                            "    ALTER TABLE QC_Belsign_schema.[order] " +
                            "    ADD CONSTRAINT CHK_Order_Status " +
                            "    CHECK (status IN ('New', 'Approved', 'Pending', 'Rejected')); " +
                            "END";

            // Update any null values to 'New'
            String updateNullSQL =
                    "UPDATE QC_Belsign_schema.[order] " +
                            "SET status = 'New' " +
                            "WHERE status IS NULL";

            try (Statement stmt = conn.createStatement()) {
                stmt.executeUpdate(checkColumnSQL);
                stmt.executeUpdate(checkConstraintSQL);
                stmt.executeUpdate(updateNullSQL);
                System.out.println("Status column and constraint initialized successfully");
            }
        } catch (SQLException e) {
            System.err.println("Error initializing status constraint: " + e.getMessage());
            e.printStackTrace();
        }
    }


    private String normalizeStatus(String status) {
        if (status == null) return "New";
        return status.substring(0, 1).toUpperCase() + status.substring(1).toLowerCase();
    }

    @Override
    public Order createOrder(Order order) {
        String sql = "INSERT INTO QC_Belsign_schema.[order] (orderNumber, image, notes, status, order_name) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = dbConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            String normalizedStatus = normalizeStatus(order.getStatus());
            System.out.println("Creating order with status: " + normalizedStatus);

            stmt.setString(1, order.getOrder_number());
            stmt.setString(2, order.getImage());
            stmt.setString(3, order.getNotes());
            stmt.setString(4, normalizedStatus);
            stmt.setString(5, order.getOrder_name());

            int rowsAffected = stmt.executeUpdate();
            System.out.println("Rows affected by insert: " + rowsAffected);

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                int id = rs.getInt(1);
                order.setId(id);
                return order;
            }

        } catch (SQLException e) {
            System.err.println("Error creating order: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Order getOrderById(int orderId) {
        String sql = "SELECT * FROM QC_Belsign_schema.[order] WHERE id = ?";
        try (Connection conn = dbConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, orderId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Order order = new Order(
                    rs.getInt("id"),
                    rs.getString("order_number"),
                    rs.getString("image"),
                    rs.getString("notes"),
                    rs.getString("status"),
                    rs.getString("order_name")
                );
                System.out.println("Retrieved order " + orderId + " with status: " + order.getStatus());
                return order;
            }

        } catch (SQLException e) {
            System.err.println("Error retrieving order: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Order> getAllOrders() {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT * FROM QC_Belsign_schema.[order]";
        try (Connection conn = dbConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                orders.add(new Order(
                    rs.getInt("id"),
                    rs.getString("order_number"),
                    rs.getString("image"),
                    rs.getString("notes"),
                    rs.getString("status"),
                    rs.getString("order_name")
                ));
            }
            System.out.println("Retrieved " + orders.size() + " orders from database");

        } catch (SQLException e) {
            System.err.println("Error retrieving all orders: " + e.getMessage());
            e.printStackTrace();
        }
        return orders;
    }

    @Override
    public void updateOrder(Order order) {
        String sql = "UPDATE QC_Belsign_schema.[order] SET status = ?, notes = ? WHERE id = ?";
        try (Connection conn = dbConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            String normalizedStatus = normalizeStatus(order.getStatus());
            System.out.println("Updating order " + order.getId() + " with status: " + normalizedStatus);

            stmt.setString(1, normalizedStatus);
            stmt.setString(2, order.getNotes());
            stmt.setInt(3, order.getId());

            int rowsAffected = stmt.executeUpdate();
            System.out.println("Rows affected by update: " + rowsAffected);

        } catch (SQLException e) {
            System.err.println("Error updating order: " + e.getMessage());
            e.printStackTrace();
        }
    }


    @Override
    public void deleteOrder(int orderId) {
        String sql = "DELETE FROM QC_Belsign_schema.[order] WHERE id = ?";
        try (Connection conn = dbConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, orderId);
            int rowsAffected = stmt.executeUpdate();
            System.out.println("Deleted order " + orderId + ". Rows affected: " + rowsAffected);

        } catch (SQLException e) {
            System.err.println("Error deleting order: " + e.getMessage());
            e.printStackTrace();
        }
    }
}