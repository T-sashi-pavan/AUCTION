package com.auction.services.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.bson.Document;

import com.auction.database.DatabaseConnection;
import com.auction.exceptions.DatabaseException;
import com.auction.models.Auction;
import com.auction.models.Product;
import com.auction.models.Transaction;
import com.auction.models.User;
import com.auction.services.AdminService;
import com.auction.services.AuctionService;
import com.auction.services.ProductService;
import com.auction.services.TransactionService;
import com.auction.services.UserService;
import com.auction.services.impl.UserServiceImpl;
import com.auction.utils.InputUtils;
import com.auction.utils.WinnerAnnouncementUtils;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import static com.mongodb.client.model.Filters.eq;

/**
 * Implementation of AdminService interface
 */
public class AdminServiceImpl implements AdminService {
    
    private final UserService userService;
    private final ProductService productService;
    private final AuctionService auctionService;
    private final TransactionService transactionService;
    //CONSTRUCTOR
    public AdminServiceImpl() {
        this.userService = new UserServiceImpl();
        this.productService = new ProductServiceImpl();
        this.auctionService = new AuctionServiceImpl();
        this.transactionService = new TransactionServiceImpl();
    }
    
    @Override
    public void viewRegisteredUsers() throws DatabaseException {
        try {
            MongoDatabase database = DatabaseConnection.getDatabase();
            MongoCollection<Document> collection = database.getCollection("users");
            
            System.out.println("\n=== REGISTERED USERS ===");
            
            // Get buyers
            List<User> buyers = getUsersByRole("BUYER");
            System.out.println("\nBUYERS (" + buyers.size() + "):");
            System.out.println("ID\t\tUsername\t\tName\t\t\tEmail\t\t\tPhone\t\t\tRegistration Date");
            System.out.println("=" .repeat(120));
            
            for (User buyer : buyers) {
                System.out.printf("%-15s %-20s %-18s %-30s %-38s %-42s%n",
                    buyer.getId().toString().substring(0, 8) + "...",
                    buyer.getUsername(),
                    buyer.getFullName(),
                    buyer.getEmail(),
                    buyer.getPhoneNumber(),
                    InputUtils.formatDateTime(buyer.getRegistrationDate()));
            }
            
            // Get sellers
            List<User> sellers = getUsersByRole("SELLER");
            System.out.println("\nSELLERS (" + sellers.size() + "):");
            System.out.println("ID\t\tUsername\t\tName\t\t\tEmail\t\t\tPhone\t\t\tRegistration Date");
            System.out.println("=" .repeat(120));
            
            for (User seller : sellers) {
                System.out.printf("%-15s %-20s %-18s %-30s %-38s %-42s%n",
                    seller.getId().toString().substring(0, 8) + "...",
                    seller.getUsername(),
                    seller.getFullName(),
                    seller.getEmail(),
                    seller.getPhoneNumber(),
                    InputUtils.formatDateTime(seller.getRegistrationDate()));
            }
            
            System.out.println("\nTotal Users: " + (buyers.size() + sellers.size()));
            
        } catch (Exception e) {
            throw new DatabaseException("Failed to view registered users: " + e.getMessage(), e);
        }
    }
    
    @Override
    public void viewAllProducts() throws DatabaseException {
        try {
            List<Product> products = productService.getAllProducts();
            
            System.out.println("\n=== ALL PRODUCTS ===");
            System.out.printf("%-15s %-20s %-15s %-16s %-18s %-20s%n", 
                "ID", "Name", "Category", "Price", "Status", "Date Added");
            System.out.println("=" .repeat(120));
            
            for (Product product : products) {
                String status = product.isSold() ? "SOLD" : (product.isAvailable() ? "AVAILABLE" : "UNAVAILABLE");
                System.out.printf("%-15s %-20s %-15s $%-16f %-18s %-20s%n",
                    product.getId().toString().substring(0, 8) + "...",
                    product.getName().length() > 18 ? product.getName().substring(0, 15) + "..." : product.getName(),
                    product.getCategory(),
                    product.getPrice(),
                    status,
                    InputUtils.formatDateTime(product.getDateAdded()));
            }
            
            System.out.println("\nTotal Products: " + products.size());
            
        } catch (Exception e) {
            throw new DatabaseException("Failed to view all products: " + e.getMessage(), e);
        }
    }
    
    @Override
    public void createNewAuction() throws DatabaseException {
        try {
            // Get available products
            List<Product> availableProducts = productService.getAvailableProducts();
            
            if (availableProducts.isEmpty()) {
                System.out.println("No available products to create auction for.");
                return;
            }
            
            System.out.println("\n=== CREATE NEW AUCTION ===");
            System.out.println("Available Products:");
            System.out.printf("%-4s %-15s %-20s %-15s %-12s%n", 
                "No.", "ID", "Name", "Category", "Price");
            System.out.println("=" .repeat(90));
            
            for (int i = 0; i < availableProducts.size(); i++) {
                Product product = availableProducts.get(i);
                System.out.printf("%-4d %-15s %-20s %-15s $%-11.2f%n",
                    i + 1,
                    product.getId().toString().substring(0, 8) + "...",
                    product.getName().length() > 18 ? product.getName().substring(0, 15) + "..." : product.getName(),
                    product.getCategory(),
                    product.getPrice());
            }
            
            int choice = InputUtils.readInt("Select product (1-" + availableProducts.size() + "): ");
            
            if (choice < 1 || choice > availableProducts.size()) {
                System.out.println("Invalid selection.");
                return;
            }
            
            Product selectedProduct = availableProducts.get(choice - 1);
            
            double startingPrice = InputUtils.readDouble("Enter starting price: $");
            int durationMinutes = InputUtils.readInt("Enter auction duration (minutes): ");
            
            Auction auction = new Auction();
            auction.setProductId(selectedProduct.getId());
            auction.setSellerId(selectedProduct.getSellerId());
            auction.setStartingPrice(startingPrice);
            auction.setCurrentHighestBid(startingPrice);
            auction.setStartTime(LocalDateTime.now());
            auction.setEndTime(LocalDateTime.now().plusMinutes(durationMinutes));
            auction.setDurationMinutes(durationMinutes); // Store original duration for time reset feature
            auction.setActive(true);
            auction.setStatus("ACTIVE");
            
            if (auctionService.createAuction(auction)) {
                // Update product availability
                selectedProduct.setAvailable(false);
                productService.updateProduct(selectedProduct);
                
                System.out.println("Auction created successfully!");
                System.out.println("Auction ID: " + auction.getId());
                System.out.println("Product: " + selectedProduct.getName());
                System.out.println("Starting Price: $" + startingPrice);
                System.out.println("Duration: " + durationMinutes + " minutes");
                System.out.println("End Time: " + InputUtils.formatDateTime(auction.getEndTime()));
            } else {
                System.out.println("Failed to create auction.");
            }
            
        } catch (Exception e) {
            throw new DatabaseException("Failed to create auction: " + e.getMessage(), e);
        }
    }
    
    @Override
    public void viewAuctionHistory() throws DatabaseException {
        try {
            // Check for newly completed auctions and show winner announcements
            List<Auction> newlyCompleted = auctionService.checkAndGetNewlyCompletedAuctions();
            if (!newlyCompleted.isEmpty()) {
                System.out.println("\n*** AUCTION RESULTS JUST IN! ***");
                System.out.println("=".repeat(50));
                for (Auction auction : newlyCompleted) {
                    WinnerAnnouncementUtils.displaySimpleWinnerNotification(auction);
                }
                System.out.println("=".repeat(50));
                InputUtils.pause();
            }
            
            List<Auction> auctions = auctionService.getAllAuctions();
            
            System.out.println("\n=== AUCTION HISTORY ===");
            System.out.printf("%-12s %-18s %-14s %-14s %-12s %-18s %-18s%n", 
                "ID", "Product", "Start Price", "Current Bid", "Status", "Winner", "End Time");
            System.out.println("=" .repeat(130));
            
            for (Auction auction : auctions) {
                Product product = productService.findProductById(auction.getProductId().toString());
                String productName = product != null ? product.getName() : "Unknown";
                
                // Get winner information if auction is completed
                String winnerInfo = "N/A";
                if (auction.getStatus().equals("COMPLETED") && auction.getCurrentHighestBidderId() != null) {
                    try {
                        User winner = userService.findUserById(auction.getCurrentHighestBidderId().toString());
                        if (winner != null && auction.getCurrentHighestBid() > auction.getStartingPrice()) {
                            winnerInfo = winner.getFirstName() + " " + winner.getLastName().substring(0, 1) + ".";
                        } else {
                            winnerInfo = "No Winner";
                        }
                    } catch (Exception e) {
                        winnerInfo = "Unknown";
                    }
                }
                
                System.out.printf("%-12s %-18s $%-13.2f $%-13.2f %-12s %-18s %-18s%n",
                    auction.getId().toString().substring(0, 8) + "...",
                    productName.length() > 16 ? productName.substring(0, 13) + "..." : productName,
                    auction.getStartingPrice(),
                    auction.getCurrentHighestBid(),
                    auction.getStatus(),
                    winnerInfo,
                    InputUtils.formatDateTime(auction.getEndTime()));
            }
            
            // Show statistics
            long activeAuctions = auctions.stream().filter(a -> "ACTIVE".equals(a.getStatus())).count();
            long completedAuctions = auctions.stream().filter(a -> "COMPLETED".equals(a.getStatus())).count();
            long pendingAuctions = auctions.stream().filter(a -> "PENDING".equals(a.getStatus())).count();
            
            System.out.println("\n" + "=".repeat(130));
            System.out.println("📊 AUCTION STATISTICS:");
            System.out.println("Total Auctions: " + auctions.size());
            System.out.println("Active Auctions: " + activeAuctions);
            System.out.println("Completed Auctions: " + completedAuctions);
            System.out.println("Pending Auctions: " + pendingAuctions);
            
            // Offer to show detailed winner announcements for recently completed auctions
            if (completedAuctions > 0) {
                System.out.println("\nWould you like to see detailed winner announcements for recent auctions? (y/n)");
                String choice = InputUtils.readString("Choice: ");
                if (choice.equalsIgnoreCase("y")) {
                    showDetailedWinnerAnnouncements(auctions);
                }
            }
            
        } catch (Exception e) {
            throw new DatabaseException("Failed to view auction history: " + e.getMessage(), e);
        }
    }
    
    /**
     * Show detailed winner announcements for completed auctions
     */
    private void showDetailedWinnerAnnouncements(List<Auction> auctions) {
        System.out.println("\n*** DETAILED WINNER ANNOUNCEMENTS ***");
        System.out.println("=".repeat(70));
        
        int count = 0;
        for (Auction auction : auctions) {
            if ("COMPLETED".equals(auction.getStatus()) && count < 5) { // Show last 5
                WinnerAnnouncementUtils.displayWinnerAnnouncement(auction);
                count++;
            }
        }
        
        if (count == 0) {
            System.out.println("No completed auctions to display.");
        }
        
        InputUtils.pause();
    }
    
    @Override
    public void viewItemsStatus() throws DatabaseException {
        try {
            List<Product> allProducts = productService.getAllProducts();
            List<Product> soldProducts = productService.getSoldProducts();
            List<Product> availableProducts = productService.getAvailableProducts();
            
            System.out.println("\n=== ITEMS STATUS ===");
            System.out.println("Total Products: " + allProducts.size());
            System.out.println("Sold Products: " + soldProducts.size());
            System.out.println("Available Products: " + availableProducts.size());
            System.out.println("Unavailable Products: " + (allProducts.size() - soldProducts.size() - availableProducts.size()));
            
            System.out.println("\nSOLD PRODUCTS:");
            System.out.println("ID\t\tName\t\t\tCategory\t\tPrice\t\tDate Sold");
            System.out.println("=" .repeat(100));
            
            for (Product product : soldProducts) {
                System.out.printf("%-15s %-20s %-18s $%-10.2f %s%n",
                    product.getId().toString().substring(0, 8) + "...",
                    product.getName(),
                    product.getCategory(),
                    product.getPrice(),
                    product.getDateSold() != null ? InputUtils.formatDateTime(product.getDateSold()) : "Unknown");
            }
            
            System.out.println("\nAVAILABLE PRODUCTS:");
            System.out.println("ID\t\tName\t\t\tCategory\t\tPrice\t\tDate Added");
            System.out.println("=" .repeat(100));
            
            for (Product product : availableProducts) {
                System.out.printf("%-15s %-20s %-15s $%-10.2f %s%n",
                    product.getId().toString().substring(0, 8) + "...",
                    product.getName(),
                    product.getCategory(),
                    product.getPrice(),
                    InputUtils.formatDateTime(product.getDateAdded()));
            }
            
        } catch (Exception e) {
            throw new DatabaseException("Failed to view items status: " + e.getMessage(), e);
        }
    }
    
    @Override
    public void filterItemsByCategory() throws DatabaseException {
        try {
            List<String> categories = productService.getAllCategories();
            
            if (categories.isEmpty()) {
                System.out.println("No categories found.");
                return;
            }
            
            System.out.println("\n=== FILTER ITEMS BY CATEGORY ===");
            System.out.println("Available Categories:");
            
            for (int i = 0; i < categories.size(); i++) {
                System.out.println((i + 1) + ". " + categories.get(i));
            }
            
            int choice = InputUtils.readInt("Select category (1-" + categories.size() + "): ");
            
            if (choice < 1 || choice > categories.size()) {
                System.out.println("Invalid selection.");
                return;
            }
            
            String selectedCategory = categories.get(choice - 1);
            List<Product> products = productService.getProductsByCategory(selectedCategory);
            
            System.out.println("\nProducts in category '" + selectedCategory + "':");
            System.out.println("ID\t\tName\t\t\tPrice\t\tStatus\t\tDate Added");
            System.out.println("=" .repeat(100));
            
            for (Product product : products) {
                String status = product.isSold() ? "SOLD" : (product.isAvailable() ? "AVAILABLE" : "UNAVAILABLE");
                System.out.printf("%-15s %-20s $%-10.2f %-15s %s%n",
                    product.getId().toString().substring(0, 8) + "...",
                    product.getName(),
                    product.getPrice(),
                    status,
                    InputUtils.formatDateTime(product.getDateAdded()));
            }
            
            System.out.println("\nTotal items in category: " + products.size());
            
        } catch (Exception e) {
            throw new DatabaseException("Failed to filter items by category: " + e.getMessage(), e);
        }
    }
    
    @Override
    public void viewAllTransactions() throws DatabaseException {
        try {
            List<Transaction> transactions = transactionService.getAllTransactions();
            
            System.out.println("\n=== ALL TRANSACTIONS ===");
            System.out.println("ID\t\tBuyer\t\tSeller\t\tProduct\t\tAmount\t\tType\t\tStatus\t\tDate");
            System.out.println("=" .repeat(140));
            
            for (Transaction transaction : transactions) {
                System.out.printf("%-15s %-15s %-15s %-15s $%-10.2f %-15s %-15s %s%n",
                    transaction.getId().toString().substring(0, 8) + "...",
                    transaction.getBuyerName() != null ? transaction.getBuyerName() : "Unknown",
                    transaction.getSellerName() != null ? transaction.getSellerName() : "Unknown",
                    transaction.getProductName() != null ? transaction.getProductName() : "Unknown",
                    transaction.getAmount(),
                    transaction.getTransactionType(),
                    transaction.getStatus(),
                    InputUtils.formatDateTime(transaction.getTransactionDate()));
            }
            
            System.out.println("\nTotal Transactions: " + transactions.size());
            
        } catch (Exception e) {
            throw new DatabaseException("Failed to view all transactions: " + e.getMessage(), e);
        }
    }
    
    @Override
    public void searchTransactionsByDate() throws DatabaseException {
        try {
            System.out.println("\n=== SEARCH TRANSACTIONS BY DATE ===");
            String startDateStr = InputUtils.readString("Enter start date (YYYY-MM-DD HH:MM:SS): ");
            String endDateStr = InputUtils.readString("Enter end date (YYYY-MM-DD HH:MM:SS): ");
            
            LocalDateTime startDate = InputUtils.parseDateTime(startDateStr);
            LocalDateTime endDate = InputUtils.parseDateTime(endDateStr);
            
            List<Transaction> transactions = transactionService.getTransactionsByDateRange(startDate, endDate);
            
            System.out.println("\nTransactions from " + startDateStr + " to " + endDateStr + ":");
            System.out.println("ID\t\tBuyer\t\tSeller\t\tAmount\t\tType\t\tStatus\t\tDate");
            System.out.println("=" .repeat(120));
            
            for (Transaction transaction : transactions) {
                System.out.printf("%-15s %-15s %-15s $%-10.2f %-15s %-15s %s%n",
                    transaction.getId().toString().substring(0, 8) + "...",
                    transaction.getBuyerName() != null ? transaction.getBuyerName() : "Unknown",
                    transaction.getSellerName() != null ? transaction.getSellerName() : "Unknown",
                    transaction.getAmount(),
                    transaction.getTransactionType(),
                    transaction.getStatus(),
                    InputUtils.formatDateTime(transaction.getTransactionDate()));
            }
            
            System.out.println("\nFound " + transactions.size() + " transactions.");
            
        } catch (Exception e) {
            throw new DatabaseException("Failed to search transactions by date: " + e.getMessage(), e);
        }
    }
    
    @Override
    public void searchTransactionById() throws DatabaseException {
        try {
            System.out.println("\n=== SEARCH TRANSACTION BY ID ===");
            String transactionId = InputUtils.readString("Enter transaction ID: ");
            
            Transaction transaction = transactionService.findTransactionById(transactionId);
            
            if (transaction == null) {
                System.out.println("Transaction not found.");
                return;
            }
            
            System.out.println("\nTransaction Details:");
            System.out.println("ID: " + transaction.getId());
            System.out.println("Buyer: " + transaction.getBuyerName());
            System.out.println("Seller: " + transaction.getSellerName());
            System.out.println("Product: " + transaction.getProductName());
            System.out.println("Amount: $" + transaction.getAmount());
            System.out.println("Type: " + transaction.getTransactionType());
            System.out.println("Status: " + transaction.getStatus());
            System.out.println("Payment Method: " + transaction.getPaymentMethod());
            System.out.println("Transaction Date: " + InputUtils.formatDateTime(transaction.getTransactionDate()));
            
            if (transaction.getCompletedDate() != null) {
                System.out.println("Completed Date: " + InputUtils.formatDateTime(transaction.getCompletedDate()));
            }
            
            if (transaction.getNotes() != null && !transaction.getNotes().isEmpty()) {
                System.out.println("Notes: " + transaction.getNotes());
            }
            
        } catch (Exception e) {
            throw new DatabaseException("Failed to search transaction by ID: " + e.getMessage(), e);
        }
    }
    
    @Override
    public void showSystemStatistics() throws DatabaseException {
        try {
            System.out.println("\n=== SYSTEM STATISTICS ===");
            
            // User statistics
            List<User> buyers = getUsersByRole("BUYER");
            List<User> sellers = getUsersByRole("SELLER");
            
            System.out.println("USER STATISTICS:");
            System.out.println("Total Buyers: " + buyers.size());
            System.out.println("Total Sellers: " + sellers.size());
            System.out.println("Total Users: " + (buyers.size() + sellers.size()));
            
            // Product statistics
            List<Product> allProducts = productService.getAllProducts();
            List<Product> soldProducts = productService.getSoldProducts();
            List<Product> availableProducts = productService.getAvailableProducts();
            
            System.out.println("\nPRODUCT STATISTICS:");
            System.out.println("Total Products: " + allProducts.size());
            System.out.println("Sold Products: " + soldProducts.size());
            System.out.println("Available Products: " + availableProducts.size());
            
            // Auction statistics
            List<Auction> allAuctions = auctionService.getAllAuctions();
            List<Auction> activeAuctions = auctionService.getActiveAuctions();
            List<Auction> completedAuctions = auctionService.getCompletedAuctions();
            
            System.out.println("\nAUCTION STATISTICS:");
            System.out.println("Total Auctions: " + allAuctions.size());
            System.out.println("Active Auctions: " + activeAuctions.size());
            System.out.println("Completed Auctions: " + completedAuctions.size());
            
            // Transaction statistics
            List<Transaction> allTransactions = transactionService.getAllTransactions();
            List<Transaction> completedTransactions = transactionService.getTransactionsByStatus("COMPLETED");
            List<Transaction> pendingTransactions = transactionService.getTransactionsByStatus("PENDING");
            
            System.out.println("\nTRANSACTION STATISTICS:");
            System.out.println("Total Transactions: " + allTransactions.size());
            System.out.println("Completed Transactions: " + completedTransactions.size());
            System.out.println("Pending Transactions: " + pendingTransactions.size());
            
            // Calculate total revenue
            double totalRevenue = completedTransactions.stream()
                    .mapToDouble(Transaction::getAmount)
                    .sum();
            
            System.out.println("\nREVENUE STATISTICS:");
            System.out.println("Total Revenue: $" + String.format("%.2f", totalRevenue));
            System.out.println("Average Transaction: $" + String.format("%.2f", 
                    completedTransactions.size() > 0 ? totalRevenue / completedTransactions.size() : 0));
            
        } catch (Exception e) {
            throw new DatabaseException("Failed to show system statistics: " + e.getMessage(), e);
        }
    }
    
    @Override
    public List<User> getUsersByRole(String role) throws DatabaseException {
        try {
            MongoDatabase database = DatabaseConnection.getDatabase();
            MongoCollection<Document> collection = database.getCollection("users");
            
            List<User> users = new ArrayList<>();
            
            for (Document doc : collection.find(eq("role", role))) {
                users.add(documentToUser(doc));
            }
            
            return users;
            
        } catch (Exception e) {
            throw new DatabaseException("Failed to get users by role: " + e.getMessage(), e);
        }
    }
    
    /**
     * Converts MongoDB Document to User object
     */
    private User documentToUser(Document doc) {
        // We'll use reflection or create a method to convert document to user
        // For now, we'll create a simple implementation
        
        String role = doc.getString("role");
        User user;
        
        switch (role) {
            case "ADMIN":
                user = new com.auction.models.Admin();
                break;
            case "SELLER":
                user = new com.auction.models.Seller();
                break;
            case "BUYER":
                user = new com.auction.models.Buyer();
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
        }
        
        user.setActive(doc.getBoolean("isActive", true));
        
        return user;
    }
    
    @Override
    public boolean hasAnyAdmin() throws DatabaseException {
        try {
            List<User> admins = getUsersByRole("ADMIN");
            return !admins.isEmpty();
        } catch (Exception e) {
            throw new DatabaseException("Failed to check for admin users: " + e.getMessage(), e);
        }
    }
    
    @Override
    public long getAdminCount() throws DatabaseException {
        try {
            List<User> admins = getUsersByRole("ADMIN");
            return admins.size();
        } catch (Exception e) {
            throw new DatabaseException("Failed to get admin count: " + e.getMessage(), e);
        }
    }
}
