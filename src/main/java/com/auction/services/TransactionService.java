package com.auction.services;

import com.auction.models.Transaction;
import com.auction.exceptions.DatabaseException;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Interface for transaction-related services
 */
public interface TransactionService {
    
    /**
     * Creates a new transaction
     * @param transaction the transaction to create
     * @return true if creation successful, false otherwise
     * @throws DatabaseException if database operation fails
     */
    boolean createTransaction(Transaction transaction) throws DatabaseException;
    
  
    boolean updateTransaction(Transaction transaction) throws DatabaseException;
    
    
    boolean deleteTransaction(String transactionId) throws DatabaseException;
    
 
    Transaction findTransactionById(String transactionId) throws DatabaseException;
    

    List<Transaction> getAllTransactions() throws DatabaseException;
    
   
    List<Transaction> getTransactionsByBuyerId(String buyerId) throws DatabaseException;
    
   
    List<Transaction> getTransactionsBySellerId(String sellerId) throws DatabaseException;
    
   
    List<Transaction> getTransactionsByDateRange(LocalDateTime startDate, LocalDateTime endDate) throws DatabaseException;
    
    
    List<Transaction> getTransactionsByType(String transactionType) throws DatabaseException;
    
   
    List<Transaction> getTransactionsByStatus(String status) throws DatabaseException;
    
   
    boolean processRefund(String transactionId) throws DatabaseException;
    
    
    boolean completeTransaction(String transactionId) throws DatabaseException;
}
