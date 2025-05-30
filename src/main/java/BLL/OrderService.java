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

    private final OrderDB orderDB = new OrderDB();

    /**
     * Loads orders filtered based on the role of the logged-in user.
     * @return A list of orders relevant to the current user's role.
     */
    public List<Order> loadOrders() {
        String role = LoggedInUser.getLoggedInRole();
        List<Order> allOrders = orderDB.getAllOrders();
        List<Order> filteredOrders = new ArrayList<>();

        switch (role) {
            case "Operator":
                for (Order order : allOrders) {
                    if ("New".equals(order.getStatus()) || "Rejected".equals(order.getStatus())) {
                        filteredOrders.add(order);
                    }
                }
                break;

            case "QA":
                for (Order order : allOrders) {
                    if ("Pending".equals(order.getStatus())) {
                        filteredOrders.add(order);
                    }
                }
                break;

            default:
                filteredOrders = allOrders; // Admin or other roles get all orders
                break;
        }

        return filteredOrders;
    }

    /**
     * Loads an order by its ID and logs the access.
     * @param id The ID of the order.
     * @return The order object.
     */
    public Order loadOrderById(int id) {
        Order order = orderDB.getOrderById(id);
        if (order != null) {
            loggerManager.logInfo("Load Order By ID: " + order.getId() + ", by user: " + LoggedInUser.getInstance());
        }
        return order;
    }

    /**
     * Creates a new order and logs the action.
     * @param order The order to create.
     */
    public void createOrder(Order order) {
        Order createdOrder = orderDB.createOrder(order);
        if (createdOrder != null) {
            String user = LoggedInUser.getInstance().toString();
            String logMessage = String.format("ORDER_ADDED | ID: %s | User: %s | Time: %s",
                    createdOrder.getId(), user, LocalDateTime.now());
            loggerManager.logInfo(logMessage);
        }
    }

    /**
     * Updates an existing order and logs the action.
     * @param order The order to update.
     */
    public void updateOrder(Order order) {
        orderDB.updateOrder(order);
        loggerManager.logInfo("Order updated: ID = " + order.getId() + ", by user: " + LoggedInUser.getInstance());
    }

    /**
     * Deletes an order by ID and logs the action.
     * @param orderId The ID of the order to delete.
     */
    public void deleteOrder(int orderId) {
        orderDB.deleteOrder(orderId);
        loggerManager.logInfo("Order deleted: ID = " + orderId + ", by user: " + LoggedInUser.getInstance());
    }
}
