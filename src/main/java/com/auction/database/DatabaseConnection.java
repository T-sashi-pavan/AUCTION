package com.auction.database;

import com.auction.exceptions.DatabaseException;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

/**
 * MongoDB Atlas connection manager
 */
public class DatabaseConnection {
    private static final String CONNECTION_STRING = System.getProperty("mongodb.uri", "mongodb://localhost:27017");
    private static final String DATABASE_NAME = "auction_system";
    
    private static MongoClient mongoClient;
    private static MongoDatabase database;
    
    /**
     * Establishes connection to MongoDB
     * @return MongoDatabase instance
     * @throws DatabaseException if connection fails
     */
    public static MongoDatabase getDatabase() throws DatabaseException {
        if (database == null) {
            try {
                System.out.println("Connecting to MongoDB at: " + CONNECTION_STRING);
                mongoClient = MongoClients.create(CONNECTION_STRING);
                database = mongoClient.getDatabase(DATABASE_NAME);
                
                database.runCommand(new org.bson.Document("ping", 1));
                System.out.println("Connected to MongoDB successfully!");
                
            } catch (Exception e) {
                throw new DatabaseException("Failed to connect to MongoDB: " + e.getMessage(), e);
            }
        }
        return database;
    }
    
  
    public static void closeConnection() {
        if (mongoClient != null) {
            mongoClient.close();
            mongoClient = null;
            database = null;
        }
    }
    
   
    public static void createIndexes() {
        try {
            MongoDatabase db = getDatabase();
            
            db.getCollection("users").createIndex(new org.bson.Document("username", 1));
            db.getCollection("users").createIndex(new org.bson.Document("email", 1));
            
            db.getCollection("products").createIndex(new org.bson.Document("category", 1));
            db.getCollection("products").createIndex(new org.bson.Document("sellerId", 1));
            
            db.getCollection("auctions").createIndex(new org.bson.Document("productId", 1));
            db.getCollection("auctions").createIndex(new org.bson.Document("endTime", 1));
            
            db.getCollection("bids").createIndex(new org.bson.Document("auctionId", 1));
            db.getCollection("bids").createIndex(new org.bson.Document("bidderId", 1));
            
            db.getCollection("transactions").createIndex(new org.bson.Document("buyerId", 1));
            db.getCollection("transactions").createIndex(new org.bson.Document("sellerId", 1));
            db.getCollection("transactions").createIndex(new org.bson.Document("transactionDate", 1));
            
        } catch (Exception e) {
            System.err.println("Failed to create indexes: " + e.getMessage());
        }
    }
}
