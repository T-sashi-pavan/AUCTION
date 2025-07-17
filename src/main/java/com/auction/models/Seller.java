package com.auction.models;

import com.auction.services.SellerService;
import com.auction.services.impl.SellerServiceImpl;
import com.auction.utils.InputUtils;
import java.util.ArrayList;
import java.util.List;
import org.bson.types.ObjectId;

/**
 * Seller class extending User - demonstrates inheritance
 */
public class Seller extends User {
    private List<ObjectId> productIds;
    private List<ObjectId> transactionIds;
    private double totalEarnings;
    private int totalProductsSold;
    private double rating;
    private int totalRatings;
    private SellerService sellerService;
    
    public Seller() {
        super();
        this.setRole("SELLER");
        this.productIds = new ArrayList<>();
        this.transactionIds = new ArrayList<>();
        this.totalEarnings = 0.0;
        this.totalProductsSold = 0;
        this.rating = 0.0;
        this.totalRatings = 0;
        this.sellerService = new SellerServiceImpl();
    }
    
    public Seller(String username, String email, String password, 
                  String firstName, String lastName, String phoneNumber) {
        super(username, email, password, "SELLER", firstName, lastName, phoneNumber);
        this.productIds = new ArrayList<>();
        this.transactionIds = new ArrayList<>();
        this.totalEarnings = 0.0;
        this.totalProductsSold = 0;
        this.rating = 0.0;
        this.totalRatings = 0;
        this.sellerService = new SellerServiceImpl();
    }
    
    @Override
    public void showDashboard() {
        System.out.println("\n=== SELLER DASHBOARD ===");
        System.out.println("Welcome, " + getFullName() + "!");
        System.out.println("Total Products: " + productIds.size());
        System.out.println("Total Earnings: $" + String.format("%.2f", totalEarnings));
        System.out.println("Products Sold: " + totalProductsSold);
        System.out.println("Rating: " + String.format("%.1f", rating) + "/5.0");
        System.out.println("=========================");
        System.out.println("1. Add New Product");
        System.out.println("2. View My Products");
        System.out.println("3. Remove Product");
        System.out.println("4. View Sold Products");
        System.out.println("5. View Transaction History");
        System.out.println("6. Update Personal Details");
        System.out.println("7. View Profile Summary");
        System.out.println("8. Delete Account");
        System.out.println("0. Logout");
        System.out.println("=========================");
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
                        sellerService.addNewProduct(this);
                        break;
                    case 2:
                        sellerService.viewMyProducts(this);
                        break;
                    case 3:
                        sellerService.removeProduct(this);
                        break;
                    case 4:
                        sellerService.viewSoldProducts(this);
                        break;
                    case 5:
                        sellerService.viewTransactionHistory(this);
                        break;
                    case 6:
                        sellerService.updatePersonalDetails(this);
                        break;
                    case 7:
                        sellerService.viewProfileSummary(this);
                        break;
                    case 8:
                        if (sellerService.deleteAccount(this)) {
                            System.out.println("Account deleted successfully!");
                            running = false;
                        }
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
    
    // Getters and setters
    public List<ObjectId> getProductIds() {
        return productIds;
    }
    
    public void setProductIds(List<ObjectId> productIds) {
        this.productIds = productIds;
    }
    
    public List<ObjectId> getTransactionIds() {
        return transactionIds;
    }
    
    public void setTransactionIds(List<ObjectId> transactionIds) {
        this.transactionIds = transactionIds;
    }
    
    public double getTotalEarnings() {
        return totalEarnings;
    }
    
    public void setTotalEarnings(double totalEarnings) {
        this.totalEarnings = totalEarnings;
    }
    
    public int getTotalProductsSold() {
        return totalProductsSold;
    }
    
    public void setTotalProductsSold(int totalProductsSold) {
        this.totalProductsSold = totalProductsSold;
    }
    
    public double getRating() {
        return rating;
    }
    
    public void setRating(double rating) {
        this.rating = rating;
    }
    
    public int getTotalRatings() {
        return totalRatings;
    }
    
    public void setTotalRatings(int totalRatings) {
        this.totalRatings = totalRatings;
    }
    
    public void addProductId(ObjectId productId) {
        this.productIds.add(productId);
    }
    
    public void removeProductId(ObjectId productId) {
        this.productIds.remove(productId);
    }
    
    public void addTransactionId(ObjectId transactionId) {
        this.transactionIds.add(transactionId);
    }
    
    public void updateEarnings(double amount) {
        this.totalEarnings += amount;
    }
    
    public void incrementProductsSold() {
        this.totalProductsSold++;
    }
}
