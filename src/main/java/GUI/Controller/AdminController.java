package GUI.Controller;

import GUI.View.SceneManager;
import Utils.LoggedInUser;
import javafx.fxml.FXML;

import java.io.IOException;

public class AdminController {

    @FXML
    public void initialize() throws IOException {
        LoggedInUser.setLoggedInRole("Admin");
    }
}
