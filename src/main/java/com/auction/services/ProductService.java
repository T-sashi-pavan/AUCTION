package com.auction.services;

import com.auction.models.Product;
import com.auction.exceptions.DatabaseException;
import java.util.List;

/**
 * Interface for product-related services
 */
public interface ProductService {
    
    /**
     * Saves a product to the database
     * @param product the product to save
     * @return true if save successful, false otherwise
     * @throws DatabaseException if database operation fails
     */
    boolean saveProduct(Product product) throws DatabaseException;
    
    /**
     * Updates a product in the database
     * @param product the product to update
     * @return true if update successful, false otherwise
     * @throws DatabaseException if database operation fails
     */
    boolean updateProduct(Product product) throws DatabaseException;
    
    /**
     * Deletes a product from the database
     * @param productId the product ID to delete
     * @return true if deletion successful, false otherwise
     * @throws DatabaseException if database operation fails
     */
    boolean deleteProduct(String productId) throws DatabaseException;
    
    /**
     * Finds a product by ID
     * @param productId the product ID
     * @return product if found, null otherwise
     * @throws DatabaseException if database operation fails
     */
    Product findProductById(String productId) throws DatabaseException;
    
    /**
     * Gets all products
     * @return list of all products
     * @throws DatabaseException if database operation fails
     */
    List<Product> getAllProducts() throws DatabaseException;
    
    /**
     * Gets products by category
     * @param category the product category
     * @return list of products in the category
     * @throws DatabaseException if database operation fails
     */
    List<Product> getProductsByCategory(String category) throws DatabaseException;
    
    /**
     * Gets products by seller ID
     * @param sellerId the seller ID
     * @return list of products by the seller
     * @throws DatabaseException if database operation fails
     */
    List<Product> getProductsBySellerId(String sellerId) throws DatabaseException;
    
    /**
     * Gets available products
     * @return list of available products
     * @throws DatabaseException if database operation fails
     */
    List<Product> getAvailableProducts() throws DatabaseException;
    
    /**
     * Gets sold products
     * @return list of sold products
     * @throws DatabaseException if database operation fails
     */
    List<Product> getSoldProducts() throws DatabaseException;
    
    /**
     * Marks a product as sold
     * @param productId the product ID
     * @return true if update successful, false otherwise
     * @throws DatabaseException if database operation fails
     */
    boolean markProductAsSold(String productId) throws DatabaseException;
    
    /**
     * Gets all product categories
     * @return list of product categories
     * @throws DatabaseException if database operation fails
     */
    List<String> getAllCategories() throws DatabaseException;
}
