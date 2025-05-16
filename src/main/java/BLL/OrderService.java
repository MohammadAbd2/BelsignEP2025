package BLL;

import BE.Order;
import BLL.LoggerBLL.loggerManager;
import DAL.OrderDB;
import Utils.LoggedInUser;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class OrderService {
    OrderDB orderDB = new OrderDB();
    List<Order> filteredOrders = new ArrayList<>();
    public List<Order> loadOrders() {
        switch (LoggedInUser.getLoggedInRole()){

            case "Operator": {
                orderDB.getAllOrders().stream().filter(order -> {
                    if(Objects.equals(order.getStatus(), "New")){
                        filteredOrders.add(order);

                    } else if (Objects.equals(order.getStatus(), "Rejected")) {
                        filteredOrders.add(order);
                    }
                    System.out.println("FilteredOrders are : " + filteredOrders);
                    return true;
                });
            }
            case "QC": {
                orderDB.getAllOrders().stream().filter(order -> {
                    if(Objects.equals(order.getStatus(), "Pending")){
                        filteredOrders.add(order);
                    }
                    return true;
                });

            }
        }

        return orderDB.getAllOrders();
    }

    public Order loadOrderById(int id){
        Order order = orderDB.getOrderById(id);
        loggerManager.logInfo("Load Order By ID : " + order.getId() + ", by user: " + LoggedInUser.getInstance());
        return order;
    }

    public void createOrder(Order order) {
        // Handle order creation logic here
        loggerManager.logInfo("Order created: ID = " + order.getId() + ", by user: " + order.getId());

        // Log order creation event
        String logMessage = String.format("ORDER_ADDED | ID: %s | User: %s | Time: %s",
                order.getId(), order.getId(), LocalDateTime.now());
                loggerManager.logInfo(logMessage);
    }

    public void updateOrder(Order order) {
        orderDB.updateOrder(order);
        loggerManager.logInfo("Order updated: ID = " + order.getId() + ", by user: " + order.getId());
    }

    public void deleteOrder(int orderId) {
        // Handle order deletion logic
        orderDB.deleteOrder(orderId);
        loggerManager.logInfo("Order deleted: ID = " + orderId + ", by user: " + LoggedInUser.getInstance());
    }
}
