package BLL;

import BE.User;
import DAL.UserDB;

import java.util.List;

public class UserManager {
    private final UserDB userDB;

    public UserManager() {
        userDB = new UserDB();
    }

    // Get all users
    public List<User> getAllUsers() {
        return userDB.getAllUsers();
    }

    // Get users by role
    public List<User> getUsersByRole(String role) {
        return userDB.getAllUsersByRole(role);
    }

    // Get user by ID
    public User getUserById(int id) {
        return userDB.getUserById(id);
    }

    // Create user
    public User createUser(User user) {
        return userDB.createUser(user);
    }

    // Update user
    public void updateUser(User user) {
        userDB.updateUser(user);
    }

    // Delete user
    public void deleteUserById(int id) {
        userDB.deleteUserById(id);
    }
}
