package com.auction.test;

import com.auction.database.DatabaseConnection;
import com.auction.services.impl.ProductServiceImpl;
import com.auction.models.Product;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.types.ObjectId;

import static com.mongodb.client.model.Filters.eq;

public class ProductUpdateTest {
    public static void main(String[] args) {
        try {
            System.out.println("=== Product Update Test ===");
            
            // Get database
            MongoDatabase database = DatabaseConnection.getDatabase();
            MongoCollection<Document> collection = database.getCollection("products");
            
            // Find a test product
            Document firstProduct = collection.find().first();
            if (firstProduct != null) {
                System.out.println("Found product: " + firstProduct.getString("name"));
                System.out.println("Current isSold: " + firstProduct.getBoolean("isSold", false));
                System.out.println("Current isAvailable: " + firstProduct.getBoolean("isAvailable", true));
                
                // Test update
                ObjectId productId = firstProduct.getObjectId("_id");
                ProductServiceImpl productService = new ProductServiceImpl();
                
                // Get the product as object
                Product product = productService.findProductById(productId.toString());
                if (product != null) {
                    System.out.println("Product object isSold: " + product.isSold());
                    System.out.println("Product object isAvailable: " + product.isAvailable());
                    
                    // Update the product
                    product.setSold(true);
                    product.setAvailable(false);
                    
                    boolean updated = productService.updateProduct(product);
                    System.out.println("Update result: " + updated);
                    
                    // Check if it's actually updated
                    Document updatedDoc = collection.find(eq("_id", productId)).first();
                    if (updatedDoc != null) {
                        System.out.println("After update - isSold: " + updatedDoc.getBoolean("isSold", false));
                        System.out.println("After update - isAvailable: " + updatedDoc.getBoolean("isAvailable", true));
                    }
                }
            } else {
                System.out.println("No products found in database");
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
