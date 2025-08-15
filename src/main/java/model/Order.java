package model;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class Order {
    private int id;
    private int customerId;
    private Timestamp orderDate;
    private BigDecimal totalAmount;
    private String status;
    private String shippingAddress;
    private String paymentMethod;
    private String notes;
    private List<OrderItem> orderItems;
    
    public Order() {
        this.orderItems = new ArrayList<>();
    }
    
    public Order(int customerId, BigDecimal totalAmount, String shippingAddress, String paymentMethod) {
        this.customerId = customerId;
        this.totalAmount = totalAmount;
        this.shippingAddress = shippingAddress;
        this.paymentMethod = paymentMethod;
        this.status = "PENDING";
        this.orderItems = new ArrayList<>();
    }
    
    public Order(int id, int customerId, Timestamp orderDate, BigDecimal totalAmount, 
                 String status, String shippingAddress, String paymentMethod, String notes) {
        this.id = id;
        this.customerId = customerId;
        this.orderDate = orderDate;
        this.totalAmount = totalAmount;
        this.status = status;
        this.shippingAddress = shippingAddress;
        this.paymentMethod = paymentMethod;
        this.notes = notes;
        this.orderItems = new ArrayList<>();
    }
    
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public int getCustomerId() {
        return customerId;
    }
    
    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }
    
    public Timestamp getOrderDate() {
        return orderDate;
    }
    
    public void setOrderDate(Timestamp orderDate) {
        this.orderDate = orderDate;
    }
    
    public BigDecimal getTotalAmount() {
        return totalAmount;
    }
    
    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public String getShippingAddress() {
        return shippingAddress;
    }
    
    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }
    
    public String getPaymentMethod() {
        return paymentMethod;
    }
    
    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
    
    public String getNotes() {
        return notes;
    }
    
    public void setNotes(String notes) {
        this.notes = notes;
    }
    
    public List<OrderItem> getOrderItems() {
        return orderItems;
    }
    
    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }
    
    public void addOrderItem(OrderItem item) {
        this.orderItems.add(item);
    }
    
    public void calculateTotal() {
        BigDecimal total = BigDecimal.ZERO;
        for (OrderItem item : orderItems) {
            total = total.add(item.getSubtotal());
        }
        this.totalAmount = total;
    }
    
    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", customerId=" + customerId +
                ", orderDate=" + orderDate +
                ", totalAmount=" + totalAmount +
                ", status='" + status + '\'' +
                ", shippingAddress='" + shippingAddress + '\'' +
                ", paymentMethod='" + paymentMethod + '\'' +
                '}';
    }
}