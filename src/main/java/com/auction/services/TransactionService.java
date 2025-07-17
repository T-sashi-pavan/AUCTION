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
    
    /**
     * Updates a transaction
     * @param transaction the transaction to update
     * @return true if update successful, false otherwise
     * @throws DatabaseException if database operation fails
     */
    boolean updateTransaction(Transaction transaction) throws DatabaseException;
    
    /**
     * Deletes a transaction
     * @param transactionId the transaction ID to delete
     * @return true if deletion successful, false otherwise
     * @throws DatabaseException if database operation fails
     */
    boolean deleteTransaction(String transactionId) throws DatabaseException;
    
    /**
     * Finds a transaction by ID
     * @param transactionId the transaction ID
     * @return transaction if found, null otherwise
     * @throws DatabaseException if database operation fails
     */
    Transaction findTransactionById(String transactionId) throws DatabaseException;
    
    /**
     * Gets all transactions
     * @return list of all transactions
     * @throws DatabaseException if database operation fails
     */
    List<Transaction> getAllTransactions() throws DatabaseException;
    
    /**
     * Gets transactions by buyer ID
     * @param buyerId the buyer ID
     * @return list of transactions by the buyer
     * @throws DatabaseException if database operation fails
     */
    List<Transaction> getTransactionsByBuyerId(String buyerId) throws DatabaseException;
    
    /**
     * Gets transactions by seller ID
     * @param sellerId the seller ID
     * @return list of transactions by the seller
     * @throws DatabaseException if database operation fails
     */
    List<Transaction> getTransactionsBySellerId(String sellerId) throws DatabaseException;
    
    /**
     * Gets transactions by date range
     * @param startDate the start date
     * @param endDate the end date
     * @return list of transactions within the date range
     * @throws DatabaseException if database operation fails
     */
    List<Transaction> getTransactionsByDateRange(LocalDateTime startDate, LocalDateTime endDate) throws DatabaseException;
    
    /**
     * Gets transactions by type
     * @param transactionType the transaction type
     * @return list of transactions of the specified type
     * @throws DatabaseException if database operation fails
     */
    List<Transaction> getTransactionsByType(String transactionType) throws DatabaseException;
    
    /**
     * Gets transactions by status
     * @param status the transaction status
     * @return list of transactions with the specified status
     * @throws DatabaseException if database operation fails
     */
    List<Transaction> getTransactionsByStatus(String status) throws DatabaseException;
    
    /**
     * Processes a refund for a transaction
     * @param transactionId the transaction ID to refund
     * @return true if refund processed successfully, false otherwise
     * @throws DatabaseException if database operation fails
     */
    boolean processRefund(String transactionId) throws DatabaseException;
    
    /**
     * Completes a transaction
     * @param transactionId the transaction ID to complete
     * @return true if completion successful, false otherwise
     * @throws DatabaseException if database operation fails
     */
    boolean completeTransaction(String transactionId) throws DatabaseException;
}
