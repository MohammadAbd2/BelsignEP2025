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
            // First drop any existing constraints
            String dropConstraintSQL =
                    "IF EXISTS (SELECT * FROM sys.check_constraints WHERE name = 'CHK_Order_Status') " +
                            "ALTER TABLE QC_Belsign_schema.[order] DROP CONSTRAINT CHK_Order_Status";

            // Drop and recreate the status column
            String dropColumnSQL =
                    "IF EXISTS (SELECT * FROM sys.columns WHERE Name = 'status' AND Object_ID = Object_ID('QC_Belsign_schema.[order]')) " +
                            "ALTER TABLE QC_Belsign_schema.[order] DROP COLUMN status";

            String addColumnSQL =
                    "IF NOT EXISTS (SELECT * FROM sys.columns WHERE Name = 'status' AND Object_ID = Object_ID('QC_Belsign_schema.[order]')) " +
                            "ALTER TABLE QC_Belsign_schema.[order] ADD status VARCHAR(10) NULL";

            // Add the check constraint
            String checkConstraintSQL =
                    "ALTER TABLE QC_Belsign_schema.[order] " +
                            "ADD CONSTRAINT CHK_Order_Status " +
                            "CHECK (status IN ('New', 'Approved', 'Pending', 'Rejected'))";

            try (Statement stmt = conn.createStatement()) {
                // Execute each statement in sequence
                stmt.executeUpdate(dropConstraintSQL);
                System.out.println("Existing constraint dropped (if any)");

                stmt.executeUpdate(dropColumnSQL);
                System.out.println("Status column dropped (if exists)");

                stmt.executeUpdate(addColumnSQL);
                System.out.println("Status column added with correct size");

                stmt.executeUpdate(checkConstraintSQL);
                System.out.println("Check constraint added");
            }
        } catch (SQLException e) {
            System.err.println("Error initializing status constraint: " + e.getMessage());
            e.printStackTrace();
        }
    }



    private String normalizeStatus(String status) {
        if (status == null) return null;
        String normalized = status.toLowerCase();
        return normalized.substring(0, 1).toUpperCase() + normalized.substring(1);
    }

    @Override
    public Order createOrder(Order order) {
        String sql = "INSERT INTO QC_Belsign_schema.[order] (orderNumber, image, notes, status, order_name) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = dbConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, order.getOrder_number());
            stmt.setString(2, order.getImage());
            stmt.setString(3, order.getNotes());
            stmt.setString(4, normalizeStatus(order.getStatus()));
            stmt.setString(5, order.getOrder_name());

            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                int id = rs.getInt(1);
                order.setId(id);
                return order;
            }

        } catch (SQLException e) {
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
                return new Order(
                        rs.getInt("id"),
                        rs.getString("order_number"),
                        rs.getString("image"),
                        rs.getString("notes"),
                        rs.getString("status"),
                        rs.getString("order_name")
                );
            }

        } catch (SQLException e) {
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

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orders;
    }

    @Override
    public void updateOrder(Order order) {
        String sql = "UPDATE QC_Belsign_schema.[order] SET orderNumber = ?, image = ?, notes = ?, status = ?, order_name = ? WHERE id = ?";
        try (Connection conn = dbConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, order.getOrder_number());
            stmt.setString(2, order.getImage());
            stmt.setString(3, order.getNotes());
            stmt.setString(4, normalizeStatus(order.getStatus()));
            stmt.setString(5, order.getOrder_name());
            stmt.setInt(6, order.getId());
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteOrder(int orderId) {
        String sql = "DELETE FROM QC_Belsign_schema.[order] WHERE id = ?";
        try (Connection conn = dbConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, orderId);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}


