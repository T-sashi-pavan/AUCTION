package com.auction.test;

import com.auction.database.DatabaseConnection;
import com.auction.services.UserService;
import com.auction.services.impl.UserServiceImpl;
import com.auction.models.User;
import com.auction.exceptions.AuthenticationException;

/**
 * Test authentication for specific user
 */
public class AuthenticationTest {
    
    public static void main(String[] args) {
        System.out.println("=== Testing Authentication Fix ===");
        
        try {
            // Test database connection
            System.out.println("1. Testing database connection...");
            DatabaseConnection.getDatabase();
            System.out.println("✓ Database connected successfully");
            
            // Test user service
            System.out.println("2. Testing user service...");
            UserService userService = new UserServiceImpl();
            
            // Test authentication with your user
            System.out.println("3. Testing authentication for user 'messi'...");
            try {
                User user = userService.authenticateUser("messi", "messi");
                System.out.println("✓ Authentication successful!");
                System.out.println("  - User: " + user.getFullName());
                System.out.println("  - Role: " + user.getRole());
                System.out.println("  - Email: " + user.getEmail());
                System.out.println("  - Registration Date: " + user.getRegistrationDate());
                System.out.println("  - Active: " + user.isActive());
                
                if (user instanceof com.auction.models.Seller) {
                    com.auction.models.Seller seller = (com.auction.models.Seller) user;
                    System.out.println("  - Products: " + seller.getProductIds().size());
                    System.out.println("  - Total Earnings: $" + seller.getTotalEarnings());
                }
                
            } catch (AuthenticationException e) {
                System.out.println("✗ Authentication failed: " + e.getMessage());
                e.printStackTrace();
            }
            
            // Test finding user by username
            System.out.println("4. Testing find user by username...");
            try {
                User foundUser = userService.findUserByUsername("messi");
                if (foundUser != null) {
                    System.out.println("✓ User found: " + foundUser.getFullName());
                } else {
                    System.out.println("✗ User not found");
                }
            } catch (Exception e) {
                System.out.println("✗ Error finding user: " + e.getMessage());
                e.printStackTrace();
            }
            
            System.out.println("\n=== Test Complete ===");
            
        } catch (Exception e) {
            System.err.println("✗ Test failed: " + e.getMessage());
            e.printStackTrace();
        } finally {
            DatabaseConnection.closeConnection();
        }
    }
}
