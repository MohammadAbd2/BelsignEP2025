package GUI.Model;

import BE.Order;
import BLL.OrderService;
import BLL.LoggerBLL.LogAnalyzer;
import javafx.scene.control.Alert;

import java.util.List;

public class Logger {

    public static void displayOrders() {
        // Create a new order
        Order newOrder = new Order(12);
        OrderService service = new OrderService();
        //service.createOrder(newOrder);

        // Fetch and display logs for orders added within the last week
        List<String> recentLogs = LogAnalyzer.getOrdersAddedInLastWeek();
        for (String log : recentLogs) {
            System.out.println(log);
        }
    }

    public static void RegisterLog(String log, int AlertType) {
        if (AlertType < 1 ||AlertType > 3) {
            System.out.println("You need to select a valid alert type!!");
        }else if (log.isEmpty()) {
            System.out.println("log should not be Empty!");
        }else{
            switch (AlertType) {
                case 1, 3, 2: {
                    LogAnalyzer.RegisterLog(log, AlertType);
                    break;
                }
            }
        }

    }

    public static List<String> displayLogsByType(int AlertType) {
        if (AlertType < 1 || AlertType > 3) {  // تحقق من أن AlertType بين 1 و 3
            System.out.println("You need to select a valid alert type!!");
            return null;  // إرجاع null في حالة عدم تطابق AlertType
        }

        switch (AlertType) {
            case 1: {
                return LogAnalyzer.getLogsByType("INFO");  // get all Info logs
            }
            case 2: {
                return LogAnalyzer.getLogsByType("WARNING");  // get all warnings logs
            }
            case 3: {
                return LogAnalyzer.getLogsByType("SEVERE");  // Severe or error
            }
            default: {
                return null;  // if not exist return null
            }
        }
    }

}
