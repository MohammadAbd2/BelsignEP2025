package DAL;

import BE.Order;
import java.util.List;

public interface IOrderDB {
    Order createOrder(Order order);
    Order getOrderById(int orderId);
    List<Order> getAllOrders();
    void updateOrder(Order order);
    void deleteOrder(int orderId);
}
