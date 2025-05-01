package BLL;

import BE.Order;

import java.time.LocalDateTime;

public class orderService {

    public void createOrder(Order order) {
        // Handle order creation logic here
        loggerManager.logInfo("Order created: ID = " + order.getId() + ", by user: " + order.getOrderId());

        // Log order creation event
        String logMessage = String.format("ORDER_ADDED | ID: %s | User: %s | Time: %s",
                order.getId(), order.getOrderId(), LocalDateTime.now());
        loggerManager.logInfo(logMessage);
    }

    public void updateOrder(Order order) {
        // Handle order update logic
        loggerManager.logInfo("Order updated: ID = " + order.getId() + ", by user: " + order.getOrderId());
    }

    public void deleteOrder(int orderId, String user) {
        // Handle order deletion logic
        loggerManager.logInfo("Order deleted: ID = " + orderId + ", by user: " + user);
    }
}
