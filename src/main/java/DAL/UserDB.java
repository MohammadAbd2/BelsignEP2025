package DAL;

import BE.Admin;
import BE.Operator;
import BE.QA;
import BE.User;
import DAL.Interfaces.IUserDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDB implements IUserDB {

    private final DBConnector dbConnector;

    public UserDB() {
        this.dbConnector = new DBConnector();
    }

    public User createUser(User user) {
        String sql = "INSERT INTO QC_Belsign_schema.[user] (name, role) VALUES (?, ?)";

        try (Connection conn = dbConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, user.getName());
            stmt.setString(2, user.getRole());
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                int id = rs.getInt(1);
                return createSpecificUser(id, user.getName(), user.getRole());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public User getUserById(int id) {
        String sql = "SELECT * FROM QC_Belsign_schema.[user] WHERE id = ?";
        try (Connection conn = dbConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String name = rs.getString("name");
                String role = rs.getString("role");
                return createSpecificUser(id, name, role);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<User> getAllUsersByRole(String role) {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM QC_Belsign_schema.[user] WHERE role = ?";

        try (Connection conn = dbConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, role);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                users.add(createSpecificUser(id, name, role));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    public void updateUser(User user) {
        String sql = "UPDATE QC_Belsign_schema.[user] SET name = ?, role = ? WHERE id = ?";

        try (Connection conn = dbConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, user.getName());
            stmt.setString(2, user.getRole());
            stmt.setInt(3, user.getId());
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteUserById(int id) {
        String sql = "DELETE FROM QC_Belsign_schema.[user] WHERE id = ?";

        try (Connection conn = dbConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private User createSpecificUser(int id, String name, String role) {
        switch (role.toLowerCase()) {
            case "admin":
                return new Admin(id, name);
            case "operator":
                return new Operator(id, name);
            case "qa":
                return new QA(id, name);
            default:
                return new User(id, name, role); // fallback
        }
    }
}
