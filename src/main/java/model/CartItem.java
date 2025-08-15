package model;

import java.math.BigDecimal;

public class CartItem {
    private Book book;
    private int quantity;
    private BigDecimal subtotal;
    
    public CartItem() {
    }
    
    public CartItem(Book book, int quantity) {
        this.book = book;
        this.quantity = quantity;
        calculateSubtotal();
    }
    
    public Book getBook() {
        return book;
    }
    
    public void setBook(Book book) {
        this.book = book;
        calculateSubtotal();
    }
    
    public int getQuantity() {
        return quantity;
    }
    
    public void setQuantity(int quantity) {
        this.quantity = quantity;
        calculateSubtotal();
    }
    
    public BigDecimal getSubtotal() {
        return subtotal;
    }
    
    public void increaseQuantity() {
        this.quantity++;
        calculateSubtotal();
    }
    
    public void decreaseQuantity() {
        if (this.quantity > 1) {
            this.quantity--;
            calculateSubtotal();
        }
    }
    
    private void calculateSubtotal() {
        if (book != null && book.getPrice() != null) {
            this.subtotal = book.getPrice().multiply(BigDecimal.valueOf(quantity));
        }
    }
    
    @Override
    public String toString() {
        return "CartItem{" +
                "book=" + book +
                ", quantity=" + quantity +
                ", subtotal=" + subtotal +
                '}';
    }
}