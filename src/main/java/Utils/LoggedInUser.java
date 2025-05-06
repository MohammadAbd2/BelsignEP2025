package Utils;

import BE.User;

public class LoggedInUser {
    private static LoggedInUser instance;

    private static String Role;
    private static boolean isAuthenticated = false;

    public static String getLoggedInRole(){
        return Role;
    }

    public static void setLoggedInRole(String role){
        Role = role;
    }

    public static LoggedInUser getInstance() {
        if (instance == null)
            instance = new LoggedInUser();
        return instance;
    }

    public static void setAuthenticated(boolean authenticated) {
        isAuthenticated = authenticated;
    }

    public static boolean isAuthenticated() {
        return isAuthenticated;
    }
    }
