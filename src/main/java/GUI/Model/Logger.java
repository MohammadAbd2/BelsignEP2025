package GUI.Model;

import BE.Order;
import BLL.OrderService;
import BLL.LoggerBLL.LogAnalyzer;

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
        if (AlertType != 1 || AlertType != 2 || AlertType != 3) {
            System.out.println("You need to select a valid alert type!!");
        }else if (log.isEmpty()) {
            System.out.println("log should not be Empty!");
        }else{
            switch (AlertType) {
                case 1: {System.out.println("Register Info Logs : " + log );}
                    break;
                case 2: System.out.println("Register Warnings Logs : " + log );
                    break;
                case 3: System.out.println("Register Errors Logs : " + log );
                    break;
            }
        }

    }
}
