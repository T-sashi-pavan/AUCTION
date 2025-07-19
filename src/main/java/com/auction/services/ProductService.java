package com.auction.services;

import com.auction.models.Product;
import com.auction.exceptions.DatabaseException;
import java.util.List;

/**
 * Interface for product-related services
 */
public interface ProductService {
    
    
    boolean saveProduct(Product product) throws DatabaseException;
    
  
    boolean updateProduct(Product product) throws DatabaseException;
    
  
    boolean deleteProduct(String productId) throws DatabaseException;
    
   
    Product findProductById(String productId) throws DatabaseException;
    
   
    List<Product> getAllProducts() throws DatabaseException;
    
    
    List<Product> getProductsByCategory(String category) throws DatabaseException;
    
    
    List<Product> getProductsBySellerId(String sellerId) throws DatabaseException;
   
    List<Product> getAvailableProducts() throws DatabaseException;
    
   
    List<Product> getSoldProducts() throws DatabaseException;
    
   
    boolean markProductAsSold(String productId) throws DatabaseException;
    
   
    List<String> getAllCategories() throws DatabaseException;
}
