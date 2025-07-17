package com.auction.services;

import com.auction.models.User;
import com.auction.exceptions.DatabaseException;
import java.util.List;

/**
 * Interface for admin-specific services
 */
public interface AdminService {
    
    /**
     * Views all registered users
     * @throws DatabaseException if database operation fails
     */
    void viewRegisteredUsers() throws DatabaseException;
    
    /**
     * Views all products in the system
     * @throws DatabaseException if database operation fails
     */
    void viewAllProducts() throws DatabaseException;
    
    /**
     * Creates a new auction
     * @throws DatabaseException if database operation fails
     */
    void createNewAuction() throws DatabaseException;
    
    /**
     * Views auction history
     * @throws DatabaseException if database operation fails
     */
    void viewAuctionHistory() throws DatabaseException;
    
    /**
     * Views items status (sold/unsold)
     * @throws DatabaseException if database operation fails
     */
    void viewItemsStatus() throws DatabaseException;
    
    /**
     * Filters items by category
     * @throws DatabaseException if database operation fails
     */
    void filterItemsByCategory() throws DatabaseException;
    
    /**
     * Views all transactions
     * @throws DatabaseException if database operation fails
     */
    void viewAllTransactions() throws DatabaseException;
    
    /**
     * Searches transactions by date range
     * @throws DatabaseException if database operation fails
     */
    void searchTransactionsByDate() throws DatabaseException;
    
    /**
     * Searches transaction by ID
     * @throws DatabaseException if database operation fails
     */
    void searchTransactionById() throws DatabaseException;
    
    /**
     * Shows system statistics
     * @throws DatabaseException if database operation fails
     */
    void showSystemStatistics() throws DatabaseException;
    
    /**
     * Gets all users by role
     * @param role the user role
     * @return list of users
     * @throws DatabaseException if database operation fails
     */
    List<User> getUsersByRole(String role) throws DatabaseException;
}
