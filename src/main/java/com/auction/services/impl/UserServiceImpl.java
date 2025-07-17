package com.auction.services.impl;

import com.auction.services.UserService;
import com.auction.models.User;
import com.auction.models.Admin;
import com.auction.models.Seller;
import com.auction.models.Buyer;
import com.auction.exceptions.AuthenticationException;
import com.auction.exceptions.DatabaseException;
import com.auction.database.DatabaseConnection;
import com.auction.utils.PasswordUtils;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.List;
import java.util.ArrayList;

import static com.mongodb.client.model.Filters.*;

/**
 * Implementation of UserService interface
 */
public class UserServiceImpl implements UserService {
    
    private MongoCollection<Document> getUserCollection() throws DatabaseException {
        MongoDatabase database = DatabaseConnection.getDatabase();
        return database.getCollection("users");
    }
    
    @Override
    public boolean registerUser(User user) throws DatabaseException {
        try {
            MongoCollection<Document> collection = getUserCollection();
            
            // Check if username or email already exists
            if (usernameExists(user.getUsername())) {
                throw new DatabaseException("Username already exists");
            }
            
            if (emailExists(user.getEmail())) {
                throw new DatabaseException("Email already exists");
            }
            
            // Encrypt password
            String encryptedPassword = PasswordUtils.encryptPassword(user.getPassword());
            user.setPassword(encryptedPassword);
            
            // Convert user to document
            Document userDoc = userToDocument(user);
            collection.insertOne(userDoc);
            
            return true;
        } catch (Exception e) {
            throw new DatabaseException("Failed to register user: " + e.getMessage(), e);
        }
    }
    
    @Override
    public User authenticateUser(String username, String password) throws AuthenticationException {
        try {
            MongoCollection<Document> collection = getUserCollection();
            Document userDoc = collection.find(eq("username", username)).first();
            
            if (userDoc == null) {
                throw new AuthenticationException("Invalid username or password");
            }
            
            String storedPassword = userDoc.getString("password");
            if (!PasswordUtils.verifyPassword(password, storedPassword)) {
                throw new AuthenticationException("Invalid username or password");
            }
            
            // Check if user is active
            if (!userDoc.getBoolean("isActive", true)) {
                throw new AuthenticationException("Account is deactivated");
            }
            
            return documentToUser(userDoc);
            
        } catch (AuthenticationException e) {
            throw e;
        } catch (Exception e) {
            throw new AuthenticationException("Authentication failed: " + e.getMessage(), e);
        }
    }
    
    @Override
    public boolean updateUser(User user) throws DatabaseException {
        try {
            MongoCollection<Document> collection = getUserCollection();
            Document userDoc = userToDocument(user);
            
            collection.replaceOne(eq("_id", user.getId()), userDoc);
            return true;
        } catch (Exception e) {
            throw new DatabaseException("Failed to update user: " + e.getMessage(), e);
        }
    }
    
    @Override
    public boolean deleteUser(String userId) throws DatabaseException {
        try {
            MongoCollection<Document> collection = getUserCollection();
            collection.deleteOne(eq("_id", new ObjectId(userId)));
            return true;
        } catch (Exception e) {
            throw new DatabaseException("Failed to delete user: " + e.getMessage(), e);
        }
    }
    
    @Override
    public User findUserById(String userId) throws DatabaseException {
        try {
            MongoCollection<Document> collection = getUserCollection();
            Document userDoc = collection.find(eq("_id", new ObjectId(userId))).first();
            
            return userDoc != null ? documentToUser(userDoc) : null;
        } catch (Exception e) {
            throw new DatabaseException("Failed to find user: " + e.getMessage(), e);
        }
    }
    
    @Override
    public User findUserByUsername(String username) throws DatabaseException {
        try {
            MongoCollection<Document> collection = getUserCollection();
            Document userDoc = collection.find(eq("username", username)).first();
            
            return userDoc != null ? documentToUser(userDoc) : null;
        } catch (Exception e) {
            throw new DatabaseException("Failed to find user: " + e.getMessage(), e);
        }
    }
    
    @Override
    public boolean usernameExists(String username) throws DatabaseException {
        try {
            MongoCollection<Document> collection = getUserCollection();
            return collection.countDocuments(eq("username", username)) > 0;
        } catch (Exception e) {
            throw new DatabaseException("Failed to check username: " + e.getMessage(), e);
        }
    }
    
    @Override
    public boolean emailExists(String email) throws DatabaseException {
        try {
            MongoCollection<Document> collection = getUserCollection();
            return collection.countDocuments(eq("email", email)) > 0;
        } catch (Exception e) {
            throw new DatabaseException("Failed to check email: " + e.getMessage(), e);
        }
    }
    
    @Override
    public List<User> getAllUsers() throws DatabaseException {
        try {
            MongoCollection<Document> collection = getUserCollection();
            List<User> users = new ArrayList<>();
            
            for (Document doc : collection.find()) {
                users.add(documentToUser(doc));
            }
            
            return users;
        } catch (Exception e) {
            throw new DatabaseException("Failed to get all users: " + e.getMessage(), e);
        }
    }
    
    /**
     * Converts User object to MongoDB Document
     */
    private Document userToDocument(User user) {
        Document doc = new Document()
                .append("_id", user.getId())
                .append("username", user.getUsername())
                .append("email", user.getEmail())
                .append("password", user.getPassword())
                .append("role", user.getRole())
                .append("firstName", user.getFirstName())
                .append("lastName", user.getLastName())
                .append("phoneNumber", user.getPhoneNumber())
                .append("isActive", user.isActive());
        
        // Convert LocalDateTime to Date for MongoDB storage
        if (user.getRegistrationDate() != null) {
            doc.append("registrationDate", java.util.Date.from(user.getRegistrationDate()
                .atZone(java.time.ZoneId.systemDefault()).toInstant()));
        } else {
            doc.append("registrationDate", new java.util.Date());
        }
        
        // Add role-specific fields
        if (user instanceof Seller) {
            Seller seller = (Seller) user;
            doc.append("productIds", seller.getProductIds())
               .append("transactionIds", seller.getTransactionIds())
               .append("totalEarnings", seller.getTotalEarnings())
               .append("totalProductsSold", seller.getTotalProductsSold())
               .append("rating", seller.getRating())
               .append("totalRatings", seller.getTotalRatings());
        } else if (user instanceof Buyer) {
            Buyer buyer = (Buyer) user;
            doc.append("purchaseIds", buyer.getPurchaseIds())
               .append("transactionIds", buyer.getTransactionIds())
               .append("bidIds", buyer.getBidIds())
               .append("totalSpent", buyer.getTotalSpent())
               .append("totalPurchases", buyer.getTotalPurchases());
        }
        
        return doc;
    }
    
    /**
     * Converts MongoDB Document to User object
     */
    private User documentToUser(Document doc) {
        String role = doc.getString("role");
        User user;
        
        switch (role) {
            case "ADMIN":
                user = new Admin();
                break;
            case "SELLER":
                user = new Seller();
                break;
            case "BUYER":
                user = new Buyer();
                break;
            default:
                throw new IllegalArgumentException("Unknown user role: " + role);
        }
        
        user.setId(doc.getObjectId("_id"));
        user.setUsername(doc.getString("username"));
        user.setEmail(doc.getString("email"));
        user.setPassword(doc.getString("password"));
        user.setRole(doc.getString("role"));
        user.setFirstName(doc.getString("firstName"));
        user.setLastName(doc.getString("lastName"));
        user.setPhoneNumber(doc.getString("phoneNumber"));
        
        // Handle date conversion properly
        java.util.Date registrationDate = doc.getDate("registrationDate");
        if (registrationDate != null) {
            user.setRegistrationDate(registrationDate.toInstant()
                .atZone(java.time.ZoneId.systemDefault())
                .toLocalDateTime());
        } else {
            user.setRegistrationDate(java.time.LocalDateTime.now());
        }
        
        user.setActive(doc.getBoolean("isActive", true));
        
        // Set role-specific fields
        if (user instanceof Seller) {
            Seller seller = (Seller) user;
            seller.setProductIds(doc.getList("productIds", ObjectId.class, new java.util.ArrayList<>()));
            seller.setTransactionIds(doc.getList("transactionIds", ObjectId.class, new java.util.ArrayList<>()));
            
            Double totalEarnings = doc.getDouble("totalEarnings");
            seller.setTotalEarnings(totalEarnings != null ? totalEarnings : 0.0);
            
            Integer totalProductsSold = doc.getInteger("totalProductsSold");
            seller.setTotalProductsSold(totalProductsSold != null ? totalProductsSold : 0);
            
            Double rating = doc.getDouble("rating");
            seller.setRating(rating != null ? rating : 0.0);
            
            Integer totalRatings = doc.getInteger("totalRatings");
            seller.setTotalRatings(totalRatings != null ? totalRatings : 0);
        } else if (user instanceof Buyer) {
            Buyer buyer = (Buyer) user;
            buyer.setPurchaseIds(doc.getList("purchaseIds", ObjectId.class, new java.util.ArrayList<>()));
            buyer.setTransactionIds(doc.getList("transactionIds", ObjectId.class, new java.util.ArrayList<>()));
            buyer.setBidIds(doc.getList("bidIds", ObjectId.class, new java.util.ArrayList<>()));
            
            Double totalSpent = doc.getDouble("totalSpent");
            buyer.setTotalSpent(totalSpent != null ? totalSpent : 0.0);
            
            Integer totalPurchases = doc.getInteger("totalPurchases");
            buyer.setTotalPurchases(totalPurchases != null ? totalPurchases : 0);
        }
        
        return user;
    }
}
