package Utils;

import BE.Order;
import BLL.OrderService;
import BLL.LogAnalyzer;

import java.util.List;

public class Logger {

    public static void displayOrders() {
        // Create a new order
        Order newOrder = new Order(12, "mohammad");
        OrderService service = new OrderService();
        //service.createOrder(newOrder);

        // Fetch and display logs for orders added within the last week
        List<String> recentLogs = LogAnalyzer.getOrdersAddedInLastWeek();
        for (String log : recentLogs) {
            System.out.println(log);
        }
    }
}
