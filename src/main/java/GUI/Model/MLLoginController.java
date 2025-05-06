
package GUI.Model;

import BLL.LoginBLL.LoginChoice;
import Utils.LoggedInUser;

import java.io.IOException;

public class MLLoginController {
    public LoginChoice loginChoice = new LoginChoice();

    public void adminLogin() throws IOException {
        LoggedInUser.setAuthenticated(true);
        loginChoice.adminLogin();
    }

    public void operatorLogin() throws IOException {
        LoggedInUser.setAuthenticated(true);
        loginChoice.operatorLogin();
    }

    public void qaLogin(){
        LoggedInUser.setAuthenticated(true);
        loginChoice.qaLogin();
    }
}
