package bll;

import be.Admin;
import be.Operator;
import be.QC;

public class LoginChoice {

    private final Admin admin = new Admin();      // Represents admin role
    private final Operator operator = new Operator();  // Represents operator role
    private final QC qc = new QC();                // Represents qc role

    public void adminLogin() {
        System.out.println("Admin role activated.");
        // Additional logic for admin-specific access can go here
    }

    public void operatorLogin() {
        System.out.println("Operator role activated.");
        // Additional logic for operator-specific access can go here
    }

    public void qcLogin() {
        System.out.println("QC role activated.");
        // Additional logic for QC-specific access can go here
    }
}