package BLL.LoginBLL;

import BE.Admin;
import BE.Operator;
import BE.QA;
import GUI.Model.Logger;

public class LoginChoice {

//    private final Admin admin = new Admin(1 , "Admin User");      // Represents admin role
//    private final Operator operator = new Operator(2 , "Operator User");  // Represents operator role
//    private final QA qa = new QA(3 , "QA User");                // Represents qc role

    public void adminLogin() {
        System.out.println("Logged in as Admin.");
         Logger.displayOrders();
        // Additional logic for admin-specific access can go here
    }

    public void operatorLogin() {
        System.out.println("Logged in as Operator.");
        // Additional logic for operator-specific access can go here
    }

    public void qaLogin() {
        System.out.println("Logged in as QA.");
        // Additional logic for QA-specific access can go here
    }
}