package com.auction.models;

import java.time.LocalDateTime;

import org.bson.types.ObjectId;

/**
 * Transaction POJO class representing transactions in the system
 */
public class Transaction {
    private ObjectId id;
    private ObjectId buyerId;
    private ObjectId sellerId;
    private ObjectId productId;
    private ObjectId auctionId; 
    private double amount;
    private String transactionType; 
    private String paymentMethod;
    private String status; 
    private LocalDateTime transactionDate;
    private LocalDateTime completedDate;
    private String buyerName;
    private String sellerName;
    private String productName;
    private String notes;
    
    // Default constructor
    public Transaction() {
        this.id = new ObjectId();
        this.transactionDate = LocalDateTime.now();
        this.status = "PENDING";
    }
    
    // Constructor with parameters
    public Transaction(ObjectId buyerId, ObjectId sellerId, ObjectId productId, 
                      double amount, String transactionType, String paymentMethod) {
        this();
        this.buyerId = buyerId;
        this.sellerId = sellerId;
        this.productId = productId;
        this.amount = amount;
        this.transactionType = transactionType;
        this.paymentMethod = paymentMethod;
    }
    
    // Getters and setters
    public ObjectId getId() {
        return id;
    }
    
    public void setId(ObjectId id) {
        this.id = id;
    }
    
    public ObjectId getBuyerId() {
        return buyerId;
    }
    
    public void setBuyerId(ObjectId buyerId) {
        this.buyerId = buyerId;
    }
    
    public ObjectId getSellerId() {
        return sellerId;
    }
    
    public void setSellerId(ObjectId sellerId) {
        this.sellerId = sellerId;
    }
    
    public ObjectId getProductId() {
        return productId;
    }
    
    public void setProductId(ObjectId productId) {
        this.productId = productId;
    }
    
    public ObjectId getAuctionId() {
        return auctionId;
    }
    
    public void setAuctionId(ObjectId auctionId) {
        this.auctionId = auctionId;
    }
    
    public double getAmount() {
        return amount;
    }
    
    public void setAmount(double amount) {
        this.amount = amount;
    }
    
    public String getTransactionType() {
        return transactionType;
    }
    
    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }
    
    public String getPaymentMethod() {
        return paymentMethod;
    }
    
    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public LocalDateTime getTransactionDate() {
        return transactionDate;
    }
    
    public void setTransactionDate(LocalDateTime transactionDate) {
        this.transactionDate = transactionDate;
    }
    
    public LocalDateTime getCompletedDate() {
        return completedDate;
    }
    
    public void setCompletedDate(LocalDateTime completedDate) {
        this.completedDate = completedDate;
    }
    
    public String getBuyerName() {
        return buyerName;
    }
    
    public void setBuyerName(String buyerName) {
        this.buyerName = buyerName;
    }
    
    public String getSellerName() {
        return sellerName;
    }
    
    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }
    
    public String getProductName() {
        return productName;
    }
    
    public void setProductName(String productName) {
        this.productName = productName;
    }
    
    public String getNotes() {
        return notes;
    }
    
    public void setNotes(String notes) {
        this.notes = notes;
    }
    
    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + id +
                ", buyerId=" + buyerId +
                ", sellerId=" + sellerId +
                ", productId=" + productId +
                ", auctionId=" + auctionId +
                ", amount=" + amount +
                ", transactionType='" + transactionType + '\'' +
                ", paymentMethod='" + paymentMethod + '\'' +
                ", status='" + status + '\'' +
                ", transactionDate=" + transactionDate +
                ", completedDate=" + completedDate +
                ", buyerName='" + buyerName + '\'' +
                ", sellerName='" + sellerName + '\'' +
                ", productName='" + productName + '\'' +
                ", notes='" + notes + '\'' +
                '}';
    }
}
