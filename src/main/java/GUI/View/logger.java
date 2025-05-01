package GUI.View;

import BE.Order;
import BLL.orderService;
import BLL.logAnalyzer;

import java.util.List;

public class logger {

    public static void displayOrders() {
        // Create a new order
        Order newOrder = new Order(12, "mohammad");
        orderService service = new orderService();
        //service.createOrder(newOrder);

        // Fetch and display logs for orders added within the last week
        List<String> recentLogs = logAnalyzer.getOrdersAddedInLastWeek();
        for (String log : recentLogs) {
            System.out.println(log);
        }
    }
}
