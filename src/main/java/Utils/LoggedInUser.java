package Utils;

public class LoggedInUser {
    private static LoggedInUser instance;
    private boolean isAuthenticated = false;


    public static LoggedInUser getInstance() {
        if (instance == null)
            instance = new LoggedInUser();
        return instance;
    }

    public void setAuthenticated(boolean authenticated) {
        this.isAuthenticated = authenticated;
    }

    public boolean isAuthenticated() {
        return isAuthenticated;
    }
}
