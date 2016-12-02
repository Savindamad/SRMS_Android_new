package Beans;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by savinda on 11/4/16.
 */

public class Order implements Serializable {
    String orderId;
    ArrayList<OrderItem> orderItems;
    String status;

    public Order(String orderId, ArrayList<OrderItem> orderItems, String status) {
        this.orderId = orderId;
        this.orderItems = orderItems;
        this.status = status;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void addOrderItem(OrderItem orderItem){
        orderItems.add(orderItem);
    }

    public String getOrderId() {
        return orderId;
    }

    public ArrayList<OrderItem> getOrderItems() {
        return orderItems;
    }

    public String getStatus() {
        return status;
    }
}
