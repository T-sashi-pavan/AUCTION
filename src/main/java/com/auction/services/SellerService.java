package com.auction.services;

import com.auction.models.Seller;
import com.auction.models.Product;
import com.auction.models.Transaction;
import com.auction.exceptions.DatabaseException;
import java.util.List;

/**
 * Interface for seller-specific services
 */
public interface SellerService {
    
    /**
     * Adds a new product for the seller
     * @param seller the seller adding the product
     * @throws DatabaseException if database operation fails
     */
    void addNewProduct(Seller seller) throws DatabaseException;
    
    /**
     * Views all products of the seller
     * @param seller the seller
     * @throws DatabaseException if database operation fails
     */
    void viewMyProducts(Seller seller) throws DatabaseException;
    
    /**
     * Removes a product from the seller's inventory
     * @param seller the seller
     * @throws DatabaseException if database operation fails
     */
    void removeProduct(Seller seller) throws DatabaseException;
    
    /**
     * Views sold products of the seller
     * @param seller the seller
     * @throws DatabaseException if database operation fails
     */
    void viewSoldProducts(Seller seller) throws DatabaseException;
    
    /**
     * Views transaction history of the seller
     * @param seller the seller
     * @throws DatabaseException if database operation fails
     */
    void viewTransactionHistory(Seller seller) throws DatabaseException;
    
    /**
     * Updates personal details of the seller
     * @param seller the seller
     * @throws DatabaseException if database operation fails
     */
    void updatePersonalDetails(Seller seller) throws DatabaseException;
    
    /**
     * Views profile summary of the seller
     * @param seller the seller
     * @throws DatabaseException if database operation fails
     */
    void viewProfileSummary(Seller seller) throws DatabaseException;
    
    /**
     * Deletes the seller's account
     * @param seller the seller
     * @return true if deletion successful, false otherwise
     * @throws DatabaseException if database operation fails
     */
    boolean deleteAccount(Seller seller) throws DatabaseException;
    
    /**
     * Gets all products by seller ID
     * @param sellerId the seller ID
     * @return list of products
     * @throws DatabaseException if database operation fails
     */
    List<Product> getProductsBySellerId(String sellerId) throws DatabaseException;
    
    /**
     * Gets all transactions by seller ID
     * @param sellerId the seller ID
     * @return list of transactions
     * @throws DatabaseException if database operation fails
     */
    List<Transaction> getTransactionsBySellerId(String sellerId) throws DatabaseException;
}
