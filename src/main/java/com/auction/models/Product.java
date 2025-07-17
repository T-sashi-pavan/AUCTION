package com.auction.models;

import org.bson.types.ObjectId;
import java.time.LocalDateTime;

/**
 * Product POJO class representing products in the auction system
 */
public class Product {
    private ObjectId id;
    private String name;
    private String description;
    private String category;
    private double price;
    private ObjectId sellerId;
    private boolean isAvailable;
    private boolean isSold;
    private LocalDateTime dateAdded;
    private LocalDateTime dateSold;
    private String imageUrl;
    private String condition;
    private int quantity;
    
    // Default constructor
    public Product() {
        this.id = new ObjectId();
        this.dateAdded = LocalDateTime.now();
        this.isAvailable = true;
        this.isSold = false;
        this.quantity = 1;
    }
    
    // Constructor with parameters
    public Product(String name, String description, String category, double price, 
                   ObjectId sellerId, String condition, int quantity) {
        this();
        this.name = name;
        this.description = description;
        this.category = category;
        this.price = price;
        this.sellerId = sellerId;
        this.condition = condition;
        this.quantity = quantity;
    }
    
    // Getters and setters
    public ObjectId getId() {
        return id;
    }
    
    public void setId(ObjectId id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getCategory() {
        return category;
    }
    
    public void setCategory(String category) {
        this.category = category;
    }
    
    public double getPrice() {
        return price;
    }
    
    public void setPrice(double price) {
        this.price = price;
    }
    
    public ObjectId getSellerId() {
        return sellerId;
    }
    
    public void setSellerId(ObjectId sellerId) {
        this.sellerId = sellerId;
    }
    
    public boolean isAvailable() {
        return isAvailable;
    }
    
    public void setAvailable(boolean available) {
        isAvailable = available;
    }
    
    public boolean isSold() {
        return isSold;
    }
    
    public void setSold(boolean sold) {
        isSold = sold;
    }
    
    public LocalDateTime getDateAdded() {
        return dateAdded;
    }
    
    public void setDateAdded(LocalDateTime dateAdded) {
        this.dateAdded = dateAdded;
    }
    
    public LocalDateTime getDateSold() {
        return dateSold;
    }
    
    public void setDateSold(LocalDateTime dateSold) {
        this.dateSold = dateSold;
    }
    
    public String getImageUrl() {
        return imageUrl;
    }
    
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
    
    public String getCondition() {
        return condition;
    }
    
    public void setCondition(String condition) {
        this.condition = condition;
    }
    
    public int getQuantity() {
        return quantity;
    }
    
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    
    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", category='" + category + '\'' +
                ", price=" + price +
                ", sellerId=" + sellerId +
                ", isAvailable=" + isAvailable +
                ", isSold=" + isSold +
                ", dateAdded=" + dateAdded +
                ", dateSold=" + dateSold +
                ", condition='" + condition + '\'' +
                ", quantity=" + quantity +
                '}';
    }
}
