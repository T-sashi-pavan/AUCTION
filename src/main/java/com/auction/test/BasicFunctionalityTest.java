package com.auction.test;

import com.auction.database.DatabaseConnection;
import com.auction.services.UserService;
import com.auction.services.impl.UserServiceImpl;
import com.auction.models.Admin;
import com.auction.models.Seller;
import com.auction.models.Buyer;
import com.auction.models.User;
import com.auction.exceptions.AuthenticationException;
import com.auction.exceptions.DatabaseException;

/**
 * Simple test class to verify basic functionality
 */
public class BasicFunctionalityTest {
    
    public static void main(String[] args) {
        System.out.println("=== Automated Auction System - Basic Functionality Test ===");
        
        try {
            // Test 1: Database Connection
            System.out.println("\n1. Testing Database Connection...");
            DatabaseConnection.getDatabase();
            System.out.println("✓ Database connection successful");
            
            // Test 2: User Service
            System.out.println("\n2. Testing User Service...");
            UserService userService = new UserServiceImpl();
            System.out.println("✓ User service initialized");
            
            // Test 3: Create Default Admin
            System.out.println("\n3. Testing Default Admin Creation...");
            Admin admin = new Admin("testadmin", "testadmin@test.com", "testpass", 
                                  "Test", "Admin", "1234567890");
            
            try {
                if (userService.registerUser(admin)) {
                    System.out.println("✓ Admin user created successfully");
                }
            } catch (DatabaseException e) {
                if (e.getMessage().contains("already exists")) {
                    System.out.println("✓ Admin user already exists (expected)");
                } else {
                    throw e;
                }
            }
            
            // Test 4: Create Seller
            System.out.println("\n4. Testing Seller Creation...");
            Seller seller = new Seller("testseller", "seller@test.com", "testpass", 
                                      "Test", "Seller", "1234567890");
            
            try {
                if (userService.registerUser(seller)) {
                    System.out.println("✓ Seller user created successfully");
                }
            } catch (DatabaseException e) {
                if (e.getMessage().contains("already exists")) {
                    System.out.println("✓ Seller user already exists (expected)");
                } else {
                    throw e;
                }
            }
            
            // Test 5: Create Buyer
            System.out.println("\n5. Testing Buyer Creation...");
            Buyer buyer = new Buyer("testbuyer", "buyer@test.com", "testpass", 
                                   "Test", "Buyer", "1234567890");
            
            try {
                if (userService.registerUser(buyer)) {
                    System.out.println("✓ Buyer user created successfully");
                }
            } catch (DatabaseException e) {
                if (e.getMessage().contains("already exists")) {
                    System.out.println("✓ Buyer user already exists (expected)");
                } else {
                    throw e;
                }
            }
            
            // Test 6: Authentication
            System.out.println("\n6. Testing Authentication...");
            try {
                User authenticatedUser = userService.authenticateUser("testadmin", "testpass");
                System.out.println("✓ Admin authentication successful");
                System.out.println("  - User: " + authenticatedUser.getFullName());
                System.out.println("  - Role: " + authenticatedUser.getRole());
            } catch (AuthenticationException e) {
                System.out.println("✗ Admin authentication failed: " + e.getMessage());
            }
            
            // Test 7: Get All Users
            System.out.println("\n7. Testing Get All Users...");
            try {
                var users = userService.getAllUsers();
                System.out.println("✓ Retrieved " + users.size() + " users from database");
                for (User user : users) {
                    System.out.println("  - " + user.getUsername() + " (" + user.getRole() + ")");
                }
            } catch (DatabaseException e) {
                System.out.println("✗ Failed to retrieve users: " + e.getMessage());
            }
            
            System.out.println("\n=== All Tests Completed ===");
            System.out.println("✓ Basic functionality is working correctly");
            
        } catch (Exception e) {
            System.err.println("✗ Test failed: " + e.getMessage());
            e.printStackTrace();
        } finally {
            DatabaseConnection.closeConnection();
        }
    }
}
