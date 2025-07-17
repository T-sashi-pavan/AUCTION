package com.auction.models;

import com.auction.services.BuyerService;
import com.auction.services.impl.BuyerServiceImpl;
import com.auction.utils.InputUtils;
import java.util.ArrayList;
import java.util.List;
import org.bson.types.ObjectId;

/**
 * Buyer class extending User - demonstrates inheritance
 */
public class Buyer extends User {
    private List<ObjectId> purchaseIds;
    private List<ObjectId> transactionIds;
    private List<ObjectId> bidIds;
    private double totalSpent;
    private int totalPurchases;
    private BuyerService buyerService;
    
    public Buyer() {
        super();
        this.setRole("BUYER");
        this.purchaseIds = new ArrayList<>();
        this.transactionIds = new ArrayList<>();
        this.bidIds = new ArrayList<>();
        this.totalSpent = 0.0;
        this.totalPurchases = 0;
        this.buyerService = new BuyerServiceImpl();
    }
    
    public Buyer(String username, String email, String password, 
                 String firstName, String lastName, String phoneNumber) {
        super(username, email, password, "BUYER", firstName, lastName, phoneNumber);
        this.purchaseIds = new ArrayList<>();
        this.transactionIds = new ArrayList<>();
        this.bidIds = new ArrayList<>();
        this.totalSpent = 0.0;
        this.totalPurchases = 0;
        this.buyerService = new BuyerServiceImpl();
    }
    
    @Override
    public void showDashboard() {
        System.out.println("\n=== BUYER DASHBOARD ===");
        System.out.println("Welcome, " + getFullName() + "!");
        System.out.println("Total Purchases: " + totalPurchases);
        System.out.println("Total Spent: $" + String.format("%.2f", totalSpent));
        System.out.println("========================");
        System.out.println("1. Browse Products by Category");
        System.out.println("2. View All Sellers");
        System.out.println("3. Purchase a Product");
        System.out.println("4. View Purchase History");
        System.out.println("5. Request Refund");
        System.out.println("6. View Transaction History");
        System.out.println("7. View Ongoing Auctions");
        System.out.println("8. Place Bid in Auction");
        System.out.println("9. View Auction History");
        System.out.println("10. Update Personal Details");
        System.out.println("11. View Profile Summary");
        System.out.println("12. Delete Account");
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
                        buyerService.browseProductsByCategory(this);
                        break;
                    case 2:
                        buyerService.viewAllSellers(this);
                        break;
                    case 3:
                        buyerService.purchaseProduct(this);
                        break;
                    case 4:
                        buyerService.viewPurchaseHistory(this);
                        break;
                    case 5:
                        buyerService.requestRefund(this);
                        break;
                    case 6:
                        buyerService.viewTransactionHistory(this);
                        break;
                    case 7:
                        buyerService.viewOngoingAuctions(this);
                        break;
                    case 8:
                        buyerService.placeBid(this);
                        break;
                    case 9:
                        buyerService.viewAuctionHistory(this);
                        break;
                    case 10:
                        buyerService.updatePersonalDetails(this);
                        break;
                    case 11:
                        buyerService.viewProfileSummary(this);
                        break;
                    case 12:
                        if (buyerService.deleteAccount(this)) {
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
    public List<ObjectId> getPurchaseIds() {
        return purchaseIds;
    }
    
    public void setPurchaseIds(List<ObjectId> purchaseIds) {
        this.purchaseIds = purchaseIds;
    }
    
    public List<ObjectId> getTransactionIds() {
        return transactionIds;
    }
    
    public void setTransactionIds(List<ObjectId> transactionIds) {
        this.transactionIds = transactionIds;
    }
    
    public List<ObjectId> getBidIds() {
        return bidIds;
    }
    
    public void setBidIds(List<ObjectId> bidIds) {
        this.bidIds = bidIds;
    }
    
    public double getTotalSpent() {
        return totalSpent;
    }
    
    public void setTotalSpent(double totalSpent) {
        this.totalSpent = totalSpent;
    }
    
    public int getTotalPurchases() {
        return totalPurchases;
    }
    
    public void setTotalPurchases(int totalPurchases) {
        this.totalPurchases = totalPurchases;
    }
    
    public void addPurchaseId(ObjectId purchaseId) {
        this.purchaseIds.add(purchaseId);
    }
    
    public void addTransactionId(ObjectId transactionId) {
        this.transactionIds.add(transactionId);
    }
    
    public void addBidId(ObjectId bidId) {
        this.bidIds.add(bidId);
    }
    
    public void updateSpent(double amount) {
        this.totalSpent += amount;
    }
    
    public void incrementPurchases() {
        this.totalPurchases++;
    }
}
