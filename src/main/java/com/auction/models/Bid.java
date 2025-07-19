package com.auction.models;

import java.time.LocalDateTime;

import org.bson.types.ObjectId;


public class Bid {
    private ObjectId id;
    private ObjectId auctionId;
    private ObjectId bidderId;
    private double bidAmount;
    private LocalDateTime bidTime;
    private boolean isWinning;
    private String bidderName;
    
    // Default constructor
    public Bid() {
        this.id = new ObjectId();
        this.bidTime = LocalDateTime.now();
        this.isWinning = false;
    }
    
    // Constructor with parameters
    public Bid(ObjectId auctionId, ObjectId bidderId, double bidAmount, String bidderName) {
        this();
        this.auctionId = auctionId;
        this.bidderId = bidderId;
        this.bidAmount = bidAmount;
        this.bidderName = bidderName;
    }
    
    // Getters and setters
    public ObjectId getId() {
        return id;
    }
    
    public void setId(ObjectId id) {
        this.id = id;
    }
    
    public ObjectId getAuctionId() {
        return auctionId;
    }
    
    public void setAuctionId(ObjectId auctionId) {
        this.auctionId = auctionId;
    }
    
    public ObjectId getBidderId() {
        return bidderId;
    }
    
    public void setBidderId(ObjectId bidderId) {
        this.bidderId = bidderId;
    }
    
    public double getBidAmount() {
        return bidAmount;
    }
    
    public void setBidAmount(double bidAmount) {
        this.bidAmount = bidAmount;
    }
    
    public LocalDateTime getBidTime() {
        return bidTime;
    }
    
    public void setBidTime(LocalDateTime bidTime) {
        this.bidTime = bidTime;
    }
    
    public boolean isWinning() {
        return isWinning;
    }
    
    public void setWinning(boolean winning) {
        isWinning = winning;
    }
    
    public String getBidderName() {
        return bidderName;
    }
    
    public void setBidderName(String bidderName) {
        this.bidderName = bidderName;
    }
    
    @Override
    public String toString() {
        return "Bid{" +
                "id=" + id +
                ", auctionId=" + auctionId +
                ", bidderId=" + bidderId +
                ", bidAmount=" + bidAmount +
                ", bidTime=" + bidTime +
                ", isWinning=" + isWinning +
                ", bidderName='" + bidderName + '\'' +
                '}';
    }
}
