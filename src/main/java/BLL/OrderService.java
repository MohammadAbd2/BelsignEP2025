package BLL;

import BE.Order;
import BLL.LoggerBLL.loggerManager;
import DAL.OrderDB;
import Utils.LoggedInUser;

import java.time.LocalDateTime;
import java.util.List;

public class OrderService {
    OrderDB orderDB = new OrderDB();
    public List<Order> loadOrders() {
        return orderDB.getAllOrders();
    }

    public Order loadOrderById(int id){
        Order order = orderDB.getOrderById(id);
        loggerManager.logInfo("Load Order By ID : " + order.getId() + ", by user: " + LoggedInUser.getInstance());
        return order;
    }

    public void createOrder(Order order) {
        // Handle order creation logic here
        loggerManager.logInfo("Order created: ID = " + order.getId() + ", by user: " + order.getOrderId());

        // Log order creation event
        String logMessage = String.format("ORDER_ADDED | ID: %s | User: %s | Time: %s",
                order.getId(), order.getOrderId(), LocalDateTime.now());
                loggerManager.logInfo(logMessage);
    }

    public void updateOrder(Order order) {
        orderDB.updateOrder(order);
        loggerManager.logInfo("Order updated: ID = " + order.getId() + ", by user: " + order.getOrderId());
    }

    public void deleteOrder(int orderId) {
        // Handle order deletion logic
        orderDB.deleteOrder(orderId);
        loggerManager.logInfo("Order deleted: ID = " + orderId + ", by user: " + LoggedInUser.getInstance());
    }
}
