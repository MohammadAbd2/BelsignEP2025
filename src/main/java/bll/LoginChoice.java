package bll;

import be.Admin;
import be.Operator;
import be.QA;

public class LoginChoice {

    private final Admin admin = new Admin();      // Represents admin role
    private final Operator operator = new Operator();  // Represents operator role
    private final QA qa = new QA();                // Represents qc role

    public void adminLogin() {
        System.out.println("Logged in as Admin.");
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