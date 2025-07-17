package com.auction.test;

import com.auction.database.DatabaseConnection;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.types.ObjectId;

import static com.mongodb.client.model.Filters.eq;

public class DatabaseDebugTest {
    public static void main(String[] args) {
        try {
            System.out.println("=== Database Debug Test ===");
            
            // Get database
            MongoDatabase database = DatabaseConnection.getDatabase();
            
            // Check transactions
            MongoCollection<Document> transactions = database.getCollection("transactions");
            System.out.println("Total transactions: " + transactions.countDocuments());
            
            // Check products
            MongoCollection<Document> products = database.getCollection("products");
            System.out.println("Total products: " + products.countDocuments());
            
            // Check products with sellerId messi
            System.out.println("\nProducts by messi:");
            for (Document product : products.find(eq("sellerId", new ObjectId("68769ca4aa166a75626d30ed")))) {
                System.out.println("Product: " + product.getString("name"));
                System.out.println("  isSold: " + product.getBoolean("isSold", false));
                System.out.println("  isAvailable: " + product.getBoolean("isAvailable", true));
                System.out.println("  quantity: " + product.getInteger("quantity", 1));
                System.out.println("  dateSold: " + product.getDate("dateSold"));
                System.out.println("  ---");
            }
            
            // Check products with sellerId suarez
            System.out.println("\nProducts by suarez:");
            for (Document product : products.find(eq("sellerId", new ObjectId("68773faba06a195a3cf06b0e")))) {
                System.out.println("Product: " + product.getString("name"));
                System.out.println("  isSold: " + product.getBoolean("isSold", false));
                System.out.println("  isAvailable: " + product.getBoolean("isAvailable", true));
                System.out.println("  quantity: " + product.getInteger("quantity", 1));
                System.out.println("  dateSold: " + product.getDate("dateSold"));
                System.out.println("  ---");
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
