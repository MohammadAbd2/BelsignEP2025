
package GUI.Model;

import BLL.LoginBLL.LoginChoice;
public class MLLoginController {
    public LoginChoice loginChoice = new LoginChoice();

    public void adminLogin(){
        loginChoice.adminLogin();
    }

    public void operatorLogin(){
        loginChoice.operatorLogin();
    }

    public void qaLogin(){
        loginChoice.qaLogin();
    }
}
