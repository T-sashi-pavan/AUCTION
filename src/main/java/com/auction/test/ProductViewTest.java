package com.auction.test;

import com.auction.database.DatabaseConnection;
import com.auction.services.ProductService;
import com.auction.services.impl.ProductServiceImpl;
import com.auction.models.Product;
import com.auction.exceptions.DatabaseException;
import org.bson.types.ObjectId;

import java.util.List;

/**
 * Test product viewing functionality
 */
public class ProductViewTest {
    
    public static void main(String[] args) {
        System.out.println("=== Testing Product View Fix ===");
        
        try {
            // Test database connection
            System.out.println("1. Testing database connection...");
            DatabaseConnection.getDatabase();
            System.out.println("✓ Database connected successfully");
            
            // Test product service
            System.out.println("2. Testing product service...");
            ProductService productService = new ProductServiceImpl();
            
            // Test getting products by seller ID (messi's ID from your database)
            System.out.println("3. Testing get products by seller...");
            String sellerId = "68769ca4aa166a75626d30ed"; // messi's ID
            
            try {
                List<Product> products = productService.getProductsBySellerId(sellerId);
                System.out.println("✓ Retrieved " + products.size() + " products successfully");
                
                for (Product product : products) {
                    System.out.println("  - " + product.getName() + " | $" + product.getPrice() + 
                                     " | " + product.getCategory() + " | " + product.getCondition());
                    System.out.println("    Added: " + product.getDateAdded());
                    System.out.println("    Available: " + product.isAvailable());
                    System.out.println("    Sold: " + product.isSold());
                    System.out.println();
                }
                
            } catch (Exception e) {
                System.out.println("✗ Failed to get products: " + e.getMessage());
            }
            
            // Test getting all products
            System.out.println("4. Testing get all products...");
            try {
                List<Product> allProducts = productService.getAllProducts();
                System.out.println("✓ Retrieved " + allProducts.size() + " total products");
                
            } catch (Exception e) {
                System.out.println("✗ Failed to get all products: " + e.getMessage());
            }
            
            System.out.println("\n=== Test Complete ===");
            
        } catch (Exception e) {
            System.err.println("✗ Test failed: " + e.getMessage());
        } finally {
            DatabaseConnection.closeConnection();
        }
    }
}
