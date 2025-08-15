package model;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

public class Book {
    private int id;
    private String title;
    private String author;
    private String isbn;
    private BigDecimal price;
    private int quantity;
    private String category;
    private String description;
    private String imageUrl;
    private Date publishedDate;
    private Timestamp createdDate;
    
    public Book() {
    }
    
    public Book(String title, String author, String isbn, BigDecimal price, int quantity, String category) {
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.price = price;
        this.quantity = quantity;
        this.category = category;
    }
    
    public Book(int id, String title, String author, String isbn, BigDecimal price, int quantity, 
                String category, String description, String imageUrl, Date publishedDate, Timestamp createdDate) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.price = price;
        this.quantity = quantity;
        this.category = category;
        this.description = description;
        this.imageUrl = imageUrl;
        this.publishedDate = publishedDate;
        this.createdDate = createdDate;
    }
    
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getAuthor() {
        return author;
    }
    
    public void setAuthor(String author) {
        this.author = author;
    }
    
    public String getIsbn() {
        return isbn;
    }
    
    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }
    
    public BigDecimal getPrice() {
        return price;
    }
    
    public void setPrice(BigDecimal price) {
        this.price = price;
    }
    
    public int getQuantity() {
        return quantity;
    }
    
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    
    public String getCategory() {
        return category;
    }
    
    public void setCategory(String category) {
        this.category = category;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getImageUrl() {
        return imageUrl;
    }
    
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
    
    public Date getPublishedDate() {
        return publishedDate;
    }
    
    public void setPublishedDate(Date publishedDate) {
        this.publishedDate = publishedDate;
    }
    
    public Timestamp getCreatedDate() {
        return createdDate;
    }
    
    public void setCreatedDate(Timestamp createdDate) {
        this.createdDate = createdDate;
    }
    
    public boolean isAvailable() {
        return quantity > 0;
    }
    
    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", isbn='" + isbn + '\'' +
                ", price=" + price +
                ", quantity=" + quantity +
                ", category='" + category + '\'' +
                '}';
    }
}