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
    
   
    void viewMyProducts(Seller seller) throws DatabaseException;
    
   
    void removeProduct(Seller seller) throws DatabaseException;
    
 
    void viewSoldProducts(Seller seller) throws DatabaseException;
   
    void viewTransactionHistory(Seller seller) throws DatabaseException;
    
    
    void updatePersonalDetails(Seller seller) throws DatabaseException;
    
   
    void viewProfileSummary(Seller seller) throws DatabaseException;
   
    boolean deleteAccount(Seller seller) throws DatabaseException;
   
    List<Product> getProductsBySellerId(String sellerId) throws DatabaseException;
    
   
    List<Transaction> getTransactionsBySellerId(String sellerId) throws DatabaseException;
}
