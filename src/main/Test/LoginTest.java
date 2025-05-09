package Test;
import Utils.LoggedInUser;
import org.testng.annotations.Test;


import static org.junit.jupiter.api.Assertions.*;

public class LoginTest {


    @Test
    public void TestUserLogin() {
       LoggedInUser.setAuthenticated(true);
       LoggedInUser.setLoggedInRole("admin");
       String logInAsAdmin = LoggedInUser.getLoggedInRole();
       assertEquals("admin", logInAsAdmin);
       assertTrue(LoggedInUser.isAuthenticated());
    }
}
