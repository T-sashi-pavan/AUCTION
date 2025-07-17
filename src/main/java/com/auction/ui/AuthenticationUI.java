package com.auction.ui;

import com.auction.database.DatabaseConnection;
import com.auction.exceptions.AuthenticationException;
import com.auction.exceptions.DatabaseException;
import com.auction.models.Admin;
import com.auction.models.Buyer;
import com.auction.models.Seller;
import com.auction.models.User;
import com.auction.services.UserService;
import com.auction.services.impl.UserServiceImpl;
import com.auction.utils.InputUtils;

/**
 * Main UI class for the Auction System
 * Handles user authentication and navigation
 */
public class AuthenticationUI {
    
    private final UserService userService;
    
    public AuthenticationUI() {
        this.userService = new UserServiceImpl();
    }
    
    /**
     * Main authentication loop
     */
    public void start() {
        try {
            // Initialize database connection and create indexes
            DatabaseConnection.getDatabase();
            DatabaseConnection.createIndexes();
            
            showWelcomeScreen();
            
            boolean running = true;
            while (running) {
                try {
                    InputUtils.clearScreen();
                    showMainMenu();
                    
                    int choice = InputUtils.readInt("Enter your choice: ");
                    
                    switch (choice) {
                        case 1:
                            login();
                            break;
                        case 2:
                            register();
                            break;
                        case 3:
                            showAbout();
                            break;
                        case 0:
                            System.out.println("Thank you for using Automated Auction System!");
                            running = false;
                            break;
                        default:
                            System.out.println("Invalid choice. Please try again.");
                            InputUtils.pause();
                    }
                } catch (Exception e) {
                    System.err.println("Error: " + e.getMessage());
                    InputUtils.pause();
                }
            }
            
        } catch (Exception e) {
            System.err.println("Failed to start application: " + e.getMessage());
        } finally {
            DatabaseConnection.closeConnection();
        }
    }
    
    /**
     * Shows welcome screen
     */
    private void showWelcomeScreen() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("          WELCOME TO AUTOMATED AUCTION SYSTEM");
        System.out.println("=".repeat(60));
        System.out.println("  A comprehensive platform for buying and selling");
        System.out.println("  through auctions and direct purchases");
        System.out.println("=".repeat(60));
        System.out.println();
        InputUtils.pause();
    }
    
    /**
     * Shows main menu
     */
    private void showMainMenu() {
        System.out.println("\n=== AUTOMATED AUCTION SYSTEM ===");
        System.out.println("1. Login");
        System.out.println("2. Register");
        System.out.println("3. About");
        System.out.println("0. Exit");
        System.out.println("=================================");
    }
    
    /**
     * Handles user login
     */
    private void login() {
        try {
            System.out.println("\n=== LOGIN ===");
            
            String username = InputUtils.readString("Username: ");
            String password = InputUtils.readString("Password: ");
            
            User user = userService.authenticateUser(username, password);
            
            System.out.println("\nLogin successful!");
            System.out.println("Welcome, " + user.getFullName() + "!");
            
            InputUtils.pause();
            
            // Navigate to appropriate dashboard based on user role
            navigateToUserDashboard(user);
            
        } catch (AuthenticationException e) {
            System.err.println("Login failed: " + e.getMessage());
            InputUtils.pause();
        } catch (Exception e) {
            System.err.println("Login error: " + e.getMessage());
            InputUtils.pause();
        }
    }
    
    /**
     * Handles user registration
     */
    private void register() {
        try {
            System.out.println("\n=== REGISTER ===");
            
            System.out.println("Select user type:");
            System.out.println("1. Seller");
            System.out.println("2. Buyer");
            System.out.println("0. Cancel");
            
            int choice = InputUtils.readInt("Enter your choice: ");
            
            if (choice == 0) {
                return;
            }
            
            if (choice != 1 && choice != 2) {
                System.out.println("Invalid choice.");
                InputUtils.pause();
                return;
            }
            
            String role = choice == 1 ? "SELLER" : "BUYER";
            
            System.out.println("\n📝 Enter your details:");
            System.out.println("Note: All fields are required and will be validated.");
            
            String username;
            while (true) {
                username = InputUtils.readUsername("Username: ");
                
                // Check if username already exists
                if (userService.usernameExists(username)) {
                    System.out.println("❌ Username already exists. Please choose a different username.");
                    continue;
                }
                break;
            }
            
            String email;
            while (true) {
                email = InputUtils.readEmail("Email: ");
                
                // Check if email already exists
                if (userService.emailExists(email)) {
                    System.out.println("❌ Email already exists. Please choose a different email.");
                    continue;
                }
                break;
            }
            
            String password = InputUtils.readPassword("Password: ");
            String confirmPassword = InputUtils.readPasswordConfirmation(password);
            
            String firstName = InputUtils.readName("First Name: ", "First Name");
            String lastName = InputUtils.readName("Last Name: ", "Last Name");
            String phoneNumber = InputUtils.readPhoneNumber("Phone Number: ");
            
            System.out.println("\nAll details validated successfully!");
            
            // Create user object based on role
            User user;
            if (role.equals("SELLER")) {
                user = new Seller(username, email, password, firstName, lastName, phoneNumber);
            } else {
                user = new Buyer(username, email, password, firstName, lastName, phoneNumber);
            }
            
            // Register user
            if (userService.registerUser(user)) {
                System.out.println("\nRegistration successful!");
                System.out.println("Welcome to Automated Auction System, " + user.getFullName() + "!");
                System.out.println("You can now login with your credentials.");
                
                InputUtils.pause();
                
                // Automatically login the user
                User authenticatedUser = userService.authenticateUser(username, password);
                navigateToUserDashboard(authenticatedUser);
                
            } else {
                System.out.println("Registration failed. Please try again.");
                InputUtils.pause();
            }
            
        } catch (DatabaseException e) {
            System.err.println("Registration failed: " + e.getMessage());
            InputUtils.pause();
        } catch (Exception e) {
            System.err.println("Registration error: " + e.getMessage());
            InputUtils.pause();
        }
    }
    
    /**
     * Navigates to appropriate user dashboard
     */
    private void navigateToUserDashboard(User user) {
        try {
            switch (user.getRole()) {
                case "ADMIN":
                    Admin admin = (Admin) user;
                    admin.handleActions();
                    break;
                case "SELLER":
                    Seller seller = (Seller) user;
                    seller.handleActions();
                    break;
                case "BUYER":
                    Buyer buyer = (Buyer) user;
                    buyer.handleActions();
                    break;
                default:
                    System.out.println("Unknown user role: " + user.getRole());
            }
        } catch (Exception e) {
            System.err.println("Dashboard error: " + e.getMessage());
            InputUtils.pause();
        }
    }
    
    /**
     * Shows about information
     */
    private void showAbout() {
        System.out.println("\n=== ABOUT AUTOMATED AUCTION SYSTEM ===");
        System.out.println();
        System.out.println("Version: 1.0.0");
        System.out.println("Developer: Java OOP Project");
        System.out.println("Database: MongoDB Atlas");
        System.out.println();
        System.out.println("FEATURES:");
        System.out.println("• User Authentication with encrypted passwords");
        System.out.println("• Role-based access (Admin, Seller, Buyer)");
        System.out.println("• Product listing and management");
        System.out.println("• Real-time auction system");
        System.out.println("• Transaction processing");
        System.out.println("• Purchase history and refunds");
        System.out.println("• Comprehensive reporting");
        System.out.println();
        System.out.println("USER TYPES:");
        System.out.println("• ADMIN: System management and oversight");
        System.out.println("• SELLER: List products and manage sales");
        System.out.println("• BUYER: Browse products, purchase, and bid");
        System.out.println();
        System.out.println("TECHNOLOGIES:");
        System.out.println("• Java 11+ with OOP principles");
        System.out.println("• MongoDB for data persistence");
        System.out.println("• BCrypt for password encryption");
        System.out.println("• Jackson for JSON processing");
        System.out.println();
        System.out.println("© 2025 Automated Auction System. All rights reserved.");
        System.out.println("===============================================");
        
        InputUtils.pause();
    }
    
    /**
     * Creates a default admin account if none exists
     */
    public void createDefaultAdmin() {
        try {
            // Check if admin already exists
            User existingAdmin = userService.findUserByUsername("admin");
            
            if (existingAdmin == null) {
                System.out.println("Creating default admin account...");
                
                Admin admin = new Admin("admin", "admin@auction.com", "admin123", 
                                      "System", "Administrator", "1234567890");
                
                if (userService.registerUser(admin)) {
                    System.out.println("Default admin account created successfully!");
                    System.out.println("Username: admin");
                    System.out.println("Password: admin123");
                    System.out.println("Please change the default password after first login.");
                } else {
                    System.out.println("Failed to create default admin account.");
                }
            }
        } catch (Exception e) {
            System.err.println("Error creating default admin: " + e.getMessage());
        }
    }
}
