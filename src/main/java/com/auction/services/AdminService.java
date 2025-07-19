package com.auction.services;

import com.auction.models.User;
import com.auction.exceptions.DatabaseException;
import java.util.List;

/**
 * Interface for admin-specific services
 */
public interface AdminService {
    
   
    void viewRegisteredUsers() throws DatabaseException;
    
    
    void viewAllProducts() throws DatabaseException;
    
   
    void createNewAuction() throws DatabaseException;
    
    
    void viewAuctionHistory() throws DatabaseException;
    
   
    void viewItemsStatus() throws DatabaseException;
    
    
    void filterItemsByCategory() throws DatabaseException;
    
  
    void viewAllTransactions() throws DatabaseException;
    
    void searchTransactionsByDate() throws DatabaseException;

    void searchTransactionById() throws DatabaseException;
    
   
    void showSystemStatistics() throws DatabaseException;
    
    /**
     * Get all users by role
     */
    List<User> getUsersByRole(String role) throws DatabaseException;
    
    /**
     * Check if any admin user exists in the system
     */
    boolean hasAnyAdmin() throws DatabaseException;
    
    /**
     * Get count of admin users
     */
    long getAdminCount() throws DatabaseException;
}
