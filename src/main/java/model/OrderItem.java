package model;

import java.math.BigDecimal;

public class OrderItem {
    private int id;
    private int orderId;
    private int bookId;
    private int quantity;
    private BigDecimal price;
    private BigDecimal subtotal;
    private Book book;
    
    public OrderItem() {
    }
    
    public OrderItem(int bookId, int quantity, BigDecimal price) {
        this.bookId = bookId;
        this.quantity = quantity;
        this.price = price;
        this.subtotal = price.multiply(BigDecimal.valueOf(quantity));
    }
    
    public OrderItem(int id, int orderId, int bookId, int quantity, BigDecimal price, BigDecimal subtotal) {
        this.id = id;
        this.orderId = orderId;
        this.bookId = bookId;
        this.quantity = quantity;
        this.price = price;
        this.subtotal = subtotal;
    }
    
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public int getOrderId() {
        return orderId;
    }
    
    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }
    
    public int getBookId() {
        return bookId;
    }
    
    public void setBookId(int bookId) {
        this.bookId = bookId;
    }
    
    public int getQuantity() {
        return quantity;
    }
    
    public void setQuantity(int quantity) {
        this.quantity = quantity;
        calculateSubtotal();
    }
    
    public BigDecimal getPrice() {
        return price;
    }
    
    public void setPrice(BigDecimal price) {
        this.price = price;
        calculateSubtotal();
    }
    
    public BigDecimal getSubtotal() {
        return subtotal;
    }
    
    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }
    
    public Book getBook() {
        return book;
    }
    
    public void setBook(Book book) {
        this.book = book;
    }
    
    public void calculateSubtotal() {
        if (price != null && quantity > 0) {
            this.subtotal = price.multiply(BigDecimal.valueOf(quantity));
        }
    }
    
    @Override
    public String toString() {
        return "OrderItem{" +
                "id=" + id +
                ", orderId=" + orderId +
                ", bookId=" + bookId +
                ", quantity=" + quantity +
                ", price=" + price +
                ", subtotal=" + subtotal +
                '}';
    }
}