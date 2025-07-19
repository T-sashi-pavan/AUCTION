package com.auction.test;

import com.auction.exceptions.DatabaseException;
import com.auction.services.AdminService;
import com.auction.services.UserService;
import com.auction.services.impl.AdminServiceImpl;
import com.auction.services.impl.UserServiceImpl;
import com.auction.models.Admin;

/**
 * Test for admin registration functionality
 */
public class AdminRegistrationTest {
    
    public static void main(String[] args) {
        AdminService adminService = new AdminServiceImpl();
        UserService userService = new UserServiceImpl();
        
        try {
            System.out.println("=== ADMIN REGISTRATION TEST ===\n");
            
            // Check initial admin count
            System.out.println("Initial admin count: " + adminService.getAdminCount());
            System.out.println("Has any admin: " + adminService.hasAnyAdmin());
            
            // Test creating an admin
            Admin testAdmin = new Admin("testadmin", "test@admin.com", "securepassword", 
                                      "Test", "Administrator", "1234567890");
            
            System.out.println("\nTesting admin registration...");
            
            if (userService.registerUser(testAdmin)) {
                System.out.println("✅ Test admin registered successfully!");
                
                // Check admin count after registration
                System.out.println("Admin count after registration: " + adminService.getAdminCount());
                System.out.println("Has any admin: " + adminService.hasAnyAdmin());
                
                // Test authentication
                System.out.println("\nTesting admin authentication...");
                var authenticatedUser = userService.authenticateUser("testadmin", "securepassword");
                
                if (authenticatedUser != null && authenticatedUser instanceof Admin) {
                    System.out.println("✅ Admin authentication successful!");
                    System.out.println("Admin name: " + authenticatedUser.getFullName());
                    System.out.println("Admin role: " + authenticatedUser.getRole());
                } else {
                    System.out.println("❌ Admin authentication failed!");
                }
                
            } else {
                System.out.println("❌ Admin registration failed!");
            }
            
        } catch (DatabaseException e) {
            System.err.println("Database error: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
