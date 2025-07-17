package com.auction.models;

import com.auction.services.AdminService;
import com.auction.services.impl.AdminServiceImpl;
import com.auction.utils.InputUtils;

/**
 * Admin class extending User - demonstrates inheritance
 */
public class Admin extends User {
    private AdminService adminService;
    
    public Admin() {
        super();
        this.setRole("ADMIN");
        this.adminService = new AdminServiceImpl();
    }
    
    public Admin(String username, String email, String password, 
                 String firstName, String lastName, String phoneNumber) {
        super(username, email, password, "ADMIN", firstName, lastName, phoneNumber);
        this.adminService = new AdminServiceImpl();
    }
    
    @Override
    public void showDashboard() {
        System.out.println("\n=== ADMIN DASHBOARD ===");
        System.out.println("Welcome, " + getFullName() + "!");
        System.out.println("1. View Registered Users");
        System.out.println("2. View All Products");
        System.out.println("3. Create New Auction");
        System.out.println("4. View Auction History");
        System.out.println("5. View Items (Sold/Unsold)");
        System.out.println("6. Filter Items by Category");
        System.out.println("7. View All Transactions");
        System.out.println("8. Search Transactions by Date");
        System.out.println("9. Search Transaction by ID");
        System.out.println("10. System Statistics");
        System.out.println("0. Logout");
        System.out.println("========================");
    }
    
    @Override
    public void handleActions() {
        boolean running = true;
        
        while (running) {
            try {
                InputUtils.clearScreen();
                showDashboard();
                
                int choice = InputUtils.readInt("Enter your choice: ");
                
                switch (choice) {
                    case 1:
                        adminService.viewRegisteredUsers();
                        break;
                    case 2:
                        adminService.viewAllProducts();
                        break;
                    case 3:
                        adminService.createNewAuction();
                        break;
                    case 4:
                        adminService.viewAuctionHistory();
                        break;
                    case 5:
                        adminService.viewItemsStatus();
                        break;
                    case 6:
                        adminService.filterItemsByCategory();
                        break;
                    case 7:
                        adminService.viewAllTransactions();
                        break;
                    case 8:
                        adminService.searchTransactionsByDate();
                        break;
                    case 9:
                        adminService.searchTransactionById();
                        break;
                    case 10:
                        adminService.showSystemStatistics();
                        break;
                    case 0:
                        System.out.println("Logging out...");
                        running = false;
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
                
                if (running) {
                    InputUtils.pause();
                }
                
            } catch (Exception e) {
                System.err.println("Error: " + e.getMessage());
                InputUtils.pause();
            }
        }
    }
}
