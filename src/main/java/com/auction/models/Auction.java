package com.auction.models;

import org.bson.types.ObjectId;
import java.time.LocalDateTime;

/**
 * Auction POJO class representing auctions in the system
 */
public class Auction {
    private ObjectId id;
    private ObjectId productId;
    private ObjectId sellerId;
    private double startingPrice;
    private double currentHighestBid;
    private ObjectId currentHighestBidderId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private boolean isActive;
    private boolean isCompleted;
    private String status; // PENDING, ACTIVE, COMPLETED, CANCELLED
    private LocalDateTime createdAt;
    private int totalBids;
    
    // Default constructor
    public Auction() {
        this.id = new ObjectId();
        this.createdAt = LocalDateTime.now();
        this.isActive = false;
        this.isCompleted = false;
        this.status = "PENDING";
        this.totalBids = 0;
    }
    
    // Constructor with parameters
    public Auction(ObjectId productId, ObjectId sellerId, double startingPrice, 
                   LocalDateTime startTime, LocalDateTime endTime) {
        this();
        this.productId = productId;
        this.sellerId = sellerId;
        this.startingPrice = startingPrice;
        this.currentHighestBid = startingPrice;
        this.startTime = startTime;
        this.endTime = endTime;
    }
    
    // Getters and setters
    public ObjectId getId() {
        return id;
    }
    
    public void setId(ObjectId id) {
        this.id = id;
    }
    
    public ObjectId getProductId() {
        return productId;
    }
    
    public void setProductId(ObjectId productId) {
        this.productId = productId;
    }
    
    public ObjectId getSellerId() {
        return sellerId;
    }
    
    public void setSellerId(ObjectId sellerId) {
        this.sellerId = sellerId;
    }
    
    public double getStartingPrice() {
        return startingPrice;
    }
    
    public void setStartingPrice(double startingPrice) {
        this.startingPrice = startingPrice;
    }
    
    public double getCurrentHighestBid() {
        return currentHighestBid;
    }
    
    public void setCurrentHighestBid(double currentHighestBid) {
        this.currentHighestBid = currentHighestBid;
    }
    
    public ObjectId getCurrentHighestBidderId() {
        return currentHighestBidderId;
    }
    
    public void setCurrentHighestBidderId(ObjectId currentHighestBidderId) {
        this.currentHighestBidderId = currentHighestBidderId;
    }
    
    public LocalDateTime getStartTime() {
        return startTime;
    }
    
    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }
    
    public LocalDateTime getEndTime() {
        return endTime;
    }
    
    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }
    
    public boolean isActive() {
        return isActive;
    }
    
    public void setActive(boolean active) {
        isActive = active;
    }
    
    public boolean isCompleted() {
        return isCompleted;
    }
    
    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public int getTotalBids() {
        return totalBids;
    }
    
    public void setTotalBids(int totalBids) {
        this.totalBids = totalBids;
    }
    
    /**
     * Checks if auction is currently running
     * @return true if auction is active and within time bounds
     */
    public boolean isRunning() {
        LocalDateTime now = LocalDateTime.now();
        return isActive && now.isAfter(startTime) && now.isBefore(endTime);
    }
    
    /**
     * Checks if auction has ended
     * @return true if auction has ended
     */
    public boolean hasEnded() {
        return LocalDateTime.now().isAfter(endTime);
    }
    
    @Override
    public String toString() {
        return "Auction{" +
                "id=" + id +
                ", productId=" + productId +
                ", sellerId=" + sellerId +
                ", startingPrice=" + startingPrice +
                ", currentHighestBid=" + currentHighestBid +
                ", currentHighestBidderId=" + currentHighestBidderId +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", isActive=" + isActive +
                ", isCompleted=" + isCompleted +
                ", status='" + status + '\'' +
                ", createdAt=" + createdAt +
                ", totalBids=" + totalBids +
                '}';
    }
}
