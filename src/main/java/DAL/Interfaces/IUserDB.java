package DAL.Interfaces;

import BE.User;

import java.util.List;

public interface IUserDB extends IDBConnector {
    User createUser(User user);
    User getUserById(int id);
    List<User> getAllUsers();
    List<User> getAllUsersByRole(String role);
    void updateUser(User user);
    void deleteUserById(int id);
}
