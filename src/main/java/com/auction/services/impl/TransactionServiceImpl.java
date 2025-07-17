package com.auction.services.impl;

import com.auction.services.TransactionService;
import com.auction.models.Transaction;
import com.auction.exceptions.DatabaseException;
import com.auction.database.DatabaseConnection;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Sorts.descending;

/**
 * Implementation of TransactionService interface
 */
public class TransactionServiceImpl implements TransactionService {
    
    private MongoCollection<Document> getTransactionCollection() throws DatabaseException {
        MongoDatabase database = DatabaseConnection.getDatabase();
        return database.getCollection("transactions");
    }
    
    @Override
    public boolean createTransaction(Transaction transaction) throws DatabaseException {
        try {
            MongoCollection<Document> collection = getTransactionCollection();
            Document transactionDoc = transactionToDocument(transaction);
            collection.insertOne(transactionDoc);
            return true;
        } catch (Exception e) {
            throw new DatabaseException("Failed to create transaction: " + e.getMessage(), e);
        }
    }
    
    @Override
    public boolean updateTransaction(Transaction transaction) throws DatabaseException {
        try {
            MongoCollection<Document> collection = getTransactionCollection();
            Document transactionDoc = transactionToDocument(transaction);
            collection.replaceOne(eq("_id", transaction.getId()), transactionDoc);
            return true;
        } catch (Exception e) {
            throw new DatabaseException("Failed to update transaction: " + e.getMessage(), e);
        }
    }
    
    @Override
    public boolean deleteTransaction(String transactionId) throws DatabaseException {
        try {
            MongoCollection<Document> collection = getTransactionCollection();
            collection.deleteOne(eq("_id", new ObjectId(transactionId)));
            return true;
        } catch (Exception e) {
            throw new DatabaseException("Failed to delete transaction: " + e.getMessage(), e);
        }
    }
    
    @Override
    public Transaction findTransactionById(String transactionId) throws DatabaseException {
        try {
            MongoCollection<Document> collection = getTransactionCollection();
            Document transactionDoc = collection.find(eq("_id", new ObjectId(transactionId))).first();
            return transactionDoc != null ? documentToTransaction(transactionDoc) : null;
        } catch (Exception e) {
            throw new DatabaseException("Failed to find transaction: " + e.getMessage(), e);
        }
    }
    
    @Override
    public List<Transaction> getAllTransactions() throws DatabaseException {
        try {
            MongoCollection<Document> collection = getTransactionCollection();
            List<Transaction> transactions = new ArrayList<>();
            
            for (Document doc : collection.find().sort(descending("transactionDate"))) {
                transactions.add(documentToTransaction(doc));
            }
            
            return transactions;
        } catch (Exception e) {
            throw new DatabaseException("Failed to get all transactions: " + e.getMessage(), e);
        }
    }
    
    @Override
    public List<Transaction> getTransactionsByBuyerId(String buyerId) throws DatabaseException {
        try {
            MongoCollection<Document> collection = getTransactionCollection();
            List<Transaction> transactions = new ArrayList<>();
            
            for (Document doc : collection.find(eq("buyerId", new ObjectId(buyerId))).sort(descending("transactionDate"))) {
                transactions.add(documentToTransaction(doc));
            }
            
            return transactions;
        } catch (Exception e) {
            throw new DatabaseException("Failed to get transactions by buyer: " + e.getMessage(), e);
        }
    }
    
    @Override
    public List<Transaction> getTransactionsBySellerId(String sellerId) throws DatabaseException {
        try {
            MongoCollection<Document> collection = getTransactionCollection();
            List<Transaction> transactions = new ArrayList<>();
            
            for (Document doc : collection.find(eq("sellerId", new ObjectId(sellerId))).sort(descending("transactionDate"))) {
                transactions.add(documentToTransaction(doc));
            }
            
            return transactions;
        } catch (Exception e) {
            throw new DatabaseException("Failed to get transactions by seller: " + e.getMessage(), e);
        }
    }
    
    @Override
    public List<Transaction> getTransactionsByDateRange(LocalDateTime startDate, LocalDateTime endDate) throws DatabaseException {
        try {
            MongoCollection<Document> collection = getTransactionCollection();
            List<Transaction> transactions = new ArrayList<>();
            
            for (Document doc : collection.find(and(gte("transactionDate", startDate), lte("transactionDate", endDate))).sort(descending("transactionDate"))) {
                transactions.add(documentToTransaction(doc));
            }
            
            return transactions;
        } catch (Exception e) {
            throw new DatabaseException("Failed to get transactions by date range: " + e.getMessage(), e);
        }
    }
    
    @Override
    public List<Transaction> getTransactionsByType(String transactionType) throws DatabaseException {
        try {
            MongoCollection<Document> collection = getTransactionCollection();
            List<Transaction> transactions = new ArrayList<>();
            
            for (Document doc : collection.find(eq("transactionType", transactionType)).sort(descending("transactionDate"))) {
                transactions.add(documentToTransaction(doc));
            }
            
            return transactions;
        } catch (Exception e) {
            throw new DatabaseException("Failed to get transactions by type: " + e.getMessage(), e);
        }
    }
    
    @Override
    public List<Transaction> getTransactionsByStatus(String status) throws DatabaseException {
        try {
            MongoCollection<Document> collection = getTransactionCollection();
            List<Transaction> transactions = new ArrayList<>();
            
            for (Document doc : collection.find(eq("status", status)).sort(descending("transactionDate"))) {
                transactions.add(documentToTransaction(doc));
            }
            
            return transactions;
        } catch (Exception e) {
            throw new DatabaseException("Failed to get transactions by status: " + e.getMessage(), e);
        }
    }
    
    @Override
    public boolean processRefund(String transactionId) throws DatabaseException {
        try {
            MongoCollection<Document> collection = getTransactionCollection();
            
            // Update transaction status to refunded
            collection.updateOne(
                eq("_id", new ObjectId(transactionId)),
                new Document("$set", new Document()
                    .append("status", "REFUNDED")
                    .append("completedDate", LocalDateTime.now())
                    .append("notes", "Refund processed on " + LocalDateTime.now()))
            );
            
            return true;
        } catch (Exception e) {
            throw new DatabaseException("Failed to process refund: " + e.getMessage(), e);
        }
    }
    
    @Override
    public boolean completeTransaction(String transactionId) throws DatabaseException {
        try {
            MongoCollection<Document> collection = getTransactionCollection();
            
            // Update transaction status to completed
            collection.updateOne(
                eq("_id", new ObjectId(transactionId)),
                new Document("$set", new Document()
                    .append("status", "COMPLETED")
                    .append("completedDate", LocalDateTime.now()))
            );
            
            return true;
        } catch (Exception e) {
            throw new DatabaseException("Failed to complete transaction: " + e.getMessage(), e);
        }
    }
    
    /**
     * Converts Transaction object to MongoDB Document
     */
    private Document transactionToDocument(Transaction transaction) {
        Document doc = new Document()
                .append("_id", transaction.getId())
                .append("buyerId", transaction.getBuyerId())
                .append("sellerId", transaction.getSellerId())
                .append("productId", transaction.getProductId())
                .append("auctionId", transaction.getAuctionId())
                .append("amount", transaction.getAmount())
                .append("transactionType", transaction.getTransactionType())
                .append("paymentMethod", transaction.getPaymentMethod())
                .append("status", transaction.getStatus())
                .append("buyerName", transaction.getBuyerName())
                .append("sellerName", transaction.getSellerName())
                .append("productName", transaction.getProductName())
                .append("notes", transaction.getNotes());
        
        // Convert LocalDateTime to Date for MongoDB storage
        if (transaction.getTransactionDate() != null) {
            doc.append("transactionDate", java.util.Date.from(transaction.getTransactionDate()
                .atZone(java.time.ZoneId.systemDefault()).toInstant()));
        }
        
        if (transaction.getCompletedDate() != null) {
            doc.append("completedDate", java.util.Date.from(transaction.getCompletedDate()
                .atZone(java.time.ZoneId.systemDefault()).toInstant()));
        }
        
        return doc;
    }
    
    /**
     * Converts MongoDB Document to Transaction object
     */
    private Transaction documentToTransaction(Document doc) {
        Transaction transaction = new Transaction();
        
        transaction.setId(doc.getObjectId("_id"));
        transaction.setBuyerId(doc.getObjectId("buyerId"));
        transaction.setSellerId(doc.getObjectId("sellerId"));
        transaction.setProductId(doc.getObjectId("productId"));
        transaction.setAuctionId(doc.getObjectId("auctionId"));
        transaction.setAmount(doc.getDouble("amount"));
        transaction.setTransactionType(doc.getString("transactionType"));
        transaction.setPaymentMethod(doc.getString("paymentMethod"));
        transaction.setStatus(doc.getString("status"));
        
        // Handle date conversion properly
        java.util.Date transactionDate = doc.getDate("transactionDate");
        if (transactionDate != null) {
            transaction.setTransactionDate(transactionDate.toInstant()
                .atZone(java.time.ZoneId.systemDefault())
                .toLocalDateTime());
        }
        
        java.util.Date completedDate = doc.getDate("completedDate");
        if (completedDate != null) {
            transaction.setCompletedDate(completedDate.toInstant()
                .atZone(java.time.ZoneId.systemDefault())
                .toLocalDateTime());
        }
        
        transaction.setBuyerName(doc.getString("buyerName"));
        transaction.setSellerName(doc.getString("sellerName"));
        transaction.setProductName(doc.getString("productName"));
        transaction.setNotes(doc.getString("notes"));
        
        return transaction;
    }
}
