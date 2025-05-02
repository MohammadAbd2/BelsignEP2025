package DAL;

import BE.Order;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderDB implements IOrderDB {

    private final DBConnector dbConnector;

    public OrderDB() {
        this.dbConnector = new DBConnector();
    }

    @Override
    public Order createOrder(Order order) {
        String sql = "INSERT INTO QC_Belsign_schema.[order] (orderNumber, image, notes, status) VALUES (?, ?, ?, ?)";
        try (Connection conn = dbConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, order.getOrderNumber());
            stmt.setString(2, order.getImage());
            stmt.setString(3, order.getNotes());
            stmt.setString(4, order.getStatus());
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
                        rs.getString("orderNumber"),
                        rs.getString("image"),
                        rs.getString("notes"),
                        rs.getString("status")
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
                        rs.getString("orderNumber"),
                        rs.getString("image"),
                        rs.getString("notes"),
                        rs.getString("status")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orders;
    }

    @Override
    public void updateOrder(Order order) {
        String sql = "UPDATE QC_Belsign_schema.[order] SET orderNumber = ?, image = ?, notes = ?, status = ? WHERE id = ?";
        try (Connection conn = dbConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, order.getOrderNumber());
            stmt.setString(2, order.getImage());
            stmt.setString(3, order.getNotes());
            stmt.setString(4, order.getStatus());
            stmt.setInt(5, order.getId());
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
