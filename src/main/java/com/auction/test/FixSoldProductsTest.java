package com.auction.test;

import com.auction.database.DatabaseConnection;
import com.auction.services.impl.ProductServiceImpl;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import static com.mongodb.client.model.Filters.eq;

public class FixSoldProductsTest {
    public static void main(String[] args) {
        try {
            System.out.println("=== Fixing Sold Products ===");
            
            // Get database
            MongoDatabase database = DatabaseConnection.getDatabase();
            MongoCollection<Document> transactions = database.getCollection("transactions");
            MongoCollection<Document> products = database.getCollection("products");
            
            // Get all product IDs from transactions
            Set<ObjectId> soldProductIds = new HashSet<>();
            for (Document transaction : transactions.find(eq("transactionType", "PURCHASE"))) {
                ObjectId productId = transaction.getObjectId("productId");
                if (productId != null) {
                    soldProductIds.add(productId);
                }
            }
            
            System.out.println("Found " + soldProductIds.size() + " products that should be marked as sold");
            
            // Update each product
            ProductServiceImpl productService = new ProductServiceImpl();
            for (ObjectId productId : soldProductIds) {
                System.out.println("Marking product " + productId + " as sold...");
                try {
                    boolean success = productService.markProductAsSold(productId.toString());
                    System.out.println("  Result: " + (success ? "SUCCESS" : "FAILED"));
                } catch (Exception e) {
                    System.out.println("  Error: " + e.getMessage());
                }
            }
            
            System.out.println("\nVerifying updates...");
            
            // Verify the updates
            for (ObjectId productId : soldProductIds) {
                Document product = products.find(eq("_id", productId)).first();
                if (product != null) {
                    System.out.println("Product " + product.getString("name") + ":");
                    System.out.println("  isSold: " + product.getBoolean("isSold", false));
                    System.out.println("  isAvailable: " + product.getBoolean("isAvailable", true));
                    System.out.println("  dateSold: " + product.getDate("dateSold"));
                }
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
