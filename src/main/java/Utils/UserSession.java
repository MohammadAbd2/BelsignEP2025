package Utils;

public class UserSession {
    private static boolean loggedIn = false;

    public static boolean isLoggedIn() {
        return loggedIn;
    }

    public static void setLoggedIn(boolean status) {
        loggedIn = status;
    }
}
