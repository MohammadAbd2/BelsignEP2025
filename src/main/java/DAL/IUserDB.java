package DAL;

import BE.User;
import java.util.List;

public interface IUserDB {
    User createUser(User user);
    User getUserById(int id);
    List<User> getAllUsersByRole(String role);
    void updateUser(User user);
    void deleteUserById(int id);
}
