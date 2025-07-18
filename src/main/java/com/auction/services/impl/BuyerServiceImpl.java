package com.auction.services.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.auction.exceptions.AuctionException;
import com.auction.exceptions.DatabaseException;
import com.auction.models.Auction;
import com.auction.models.Bid;
import com.auction.models.Buyer;
import com.auction.models.Product;
import com.auction.models.Seller;
import com.auction.models.Transaction;
import com.auction.services.AuctionService;
import com.auction.services.BuyerService;
import com.auction.services.ProductService;
import com.auction.services.TransactionService;
import com.auction.services.UserService;
import com.auction.utils.InputUtils;

/**
 * Implementation of BuyerService interface
 */
public class BuyerServiceImpl implements BuyerService {
    
    private final ProductService productService;
    private final TransactionService transactionService;
    private final UserService userService;
    private final AuctionService auctionService;
    
    public BuyerServiceImpl() {
        this.productService = new ProductServiceImpl();
        this.transactionService = new TransactionServiceImpl();
        this.userService = new UserServiceImpl();
        this.auctionService = new AuctionServiceImpl();
    }
    
    @Override
    public void browseProductsByCategory(Buyer buyer) throws DatabaseException {
        try {
            List<String> categories = productService.getAllCategories();
            
            if (categories.isEmpty()) {
                System.out.println("No categories available.");
                return;
            }
            
            System.out.println("\n=== BROWSE PRODUCTS BY CATEGORY ===");
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
            
            // Filter only available products
            List<Product> availableProducts = products.stream()
                    .filter(Product::isAvailable)
                    .filter(p -> !p.isSold())
                    .collect(Collectors.toList());
            
            System.out.println("\nAvailable Products in '" + selectedCategory + "':");
            
            if (availableProducts.isEmpty()) {
                System.out.println("No available products in this category.");
                return;
            }
            
            System.out.printf("%-15s %-20s %-16s %-18s %-12s %-15s%n", 
                "ID", "Name", "Price", "Condition", "Quantity", "Seller");
            System.out.println("=" .repeat(110));
            
            for (Product product : availableProducts) {
                // Get seller name
                Seller seller = (Seller) userService.findUserById(product.getSellerId().toString());
                String sellerName = seller != null ? seller.getFullName() : "Unknown";
                
                System.out.printf("%-15s %-20s $%-15.2f %-18s %-12d %-15s%n",
                    product.getId().toString().substring(0, 8) + "...",
                    product.getName().length() > 18 ? product.getName().substring(0, 15) + "..." : product.getName(),
                    product.getPrice(),
                    product.getCondition(),
                    product.getQuantity(),
                    sellerName.length() > 13 ? sellerName.substring(0, 10) + "..." : sellerName);
            }
            
            System.out.println("\nTotal available products: " + availableProducts.size());
            
        } catch (Exception e) {
            throw new DatabaseException("Failed to browse products by category: " + e.getMessage(), e);
        }
    }
    
    @Override
    public void viewAllSellers(Buyer buyer) throws DatabaseException {
        try {
            System.out.println("\n=== ALL SELLERS ===");
            
            // Get all sellers
            List<com.auction.models.User> allUsers = userService.getAllUsers();
            List<Seller> sellers = allUsers.stream()
                    .filter(u -> u instanceof Seller)
                    .map(u -> (Seller) u)
                    .collect(Collectors.toList());
            
            if (sellers.isEmpty()) {
                System.out.println("No sellers found.");
                return;
            }
            
            System.out.println("ID\t\tName\t\t\tEmail\t\t\tPhone\t\t\tProducts\tRating\tRegistration Date");
            System.out.println("=" .repeat(120));
            
            for (Seller seller : sellers) {
                List<Product> products = productService.getProductsBySellerId(seller.getId().toString());
                
                System.out.printf("%-15s %-20s %-25s %-15s %-24d %.1f/5.0\t%s%n",
                    seller.getId().toString().substring(0, 8) + "...",
                    seller.getFullName(),
                    seller.getEmail(),
                    seller.getPhoneNumber(),
                    products.size(),
                    seller.getRating(),
                    InputUtils.formatDateTime(seller.getRegistrationDate()));
            }
            
            System.out.println("\nTotal Sellers: " + sellers.size());
            
            // Option to view products by specific seller
            String viewProducts = InputUtils.readString("\nWould you like to view products by a specific seller? (y/n): ");
            
            if (viewProducts.equalsIgnoreCase("y")) {
                String sellerId = InputUtils.readString("Enter seller ID (first 8 characters): ");
                
                // Find seller by partial ID
                Seller selectedSeller = null;
                for (Seller seller : sellers) {
                    if (seller.getId().toString().startsWith(sellerId)) {
                        selectedSeller = seller;
                        break;
                    }
                }
                
                if (selectedSeller != null) {
                    viewProductsBySeller(selectedSeller);
                } else {
                    System.out.println("Seller not found.");
                }
            }
            
        } catch (Exception e) {
            throw new DatabaseException("Failed to view all sellers: " + e.getMessage(), e);
        }
    }
    
    @Override
    public void purchaseProduct(Buyer buyer) throws DatabaseException {
        try {
            List<Product> availableProducts = productService.getAvailableProducts();
            
            if (availableProducts.isEmpty()) {
                System.out.println("No products available for purchase.");
                return;
            }
            
            System.out.println("\n=== PURCHASE PRODUCT ===");
            System.out.println("Available Products:");
            System.out.printf("%-4s %-15s %-20s %-15s %-16s %-18s %-8s%n", 
                "No.", "ID", "Name", "Category", "Price", "Condition", "Quantity");
            System.out.println("=" .repeat(130));
            
            for (int i = 0; i < availableProducts.size(); i++) {
                Product product = availableProducts.get(i);
                System.out.printf("%-4d %-15s %-20s %-15s $%-15.2f %-18s %-8d%n",
                    i + 1,
                    product.getId().toString().substring(0, 8) + "...",
                    product.getName().length() > 18 ? product.getName().substring(0, 15) + "..." : product.getName(),
                    product.getCategory(),
                    product.getPrice(),
                    product.getCondition(),
                    product.getQuantity());
            }
            
            System.out.println("\n💡 You can select multiple products by entering numbers separated by commas or spaces (e.g., 1,3,4 or 1 3 4)");
            List<Integer> selectedIndices = InputUtils.readMultipleProductSelections(
                "Select product(s) to purchase (1-" + availableProducts.size() + "): ", 
                availableProducts.size()
            );
            
            // Convert to selected products
            List<Product> selectedProducts = new ArrayList<>();
            for (int index : selectedIndices) {
                selectedProducts.add(availableProducts.get(index - 1));
            }
            
            // Display selected products and collect purchase details
            System.out.println("\n=== SELECTED PRODUCTS ===");
            List<PurchaseItem> purchaseItems = new ArrayList<>();
            double totalAmount = 0.0;
            
            for (int i = 0; i < selectedProducts.size(); i++) {
                Product product = selectedProducts.get(i);
                System.out.println("\n--- Product " + (i + 1) + " ---");
                System.out.println("Name: " + product.getName());
                System.out.println("Description: " + product.getDescription());
                System.out.println("Category: " + product.getCategory());
                System.out.println("Price: $" + product.getPrice());
                System.out.println("Condition: " + product.getCondition());
                System.out.println("Available Quantity: " + product.getQuantity());
                
                // Get seller details
                Seller seller = (Seller) userService.findUserById(product.getSellerId().toString());
                if (seller != null) {
                    System.out.println("Seller: " + seller.getFullName());
                    System.out.println("Seller Rating: " + seller.getRating() + "/5.0");
                }
                
                // Get quantity to purchase
                int quantityToPurchase = 1;
                if (product.getQuantity() > 1) {
                    quantityToPurchase = InputUtils.readInt("Enter quantity to purchase (1-" + product.getQuantity() + "): ");
                    if (quantityToPurchase < 1 || quantityToPurchase > product.getQuantity()) {
                        System.out.println("Invalid quantity. Skipping this product.");
                        continue;
                    }
                }
                
                double itemTotal = product.getPrice() * quantityToPurchase;
                totalAmount += itemTotal;
                
                // Create purchase item
                PurchaseItem purchaseItem = new PurchaseItem();
                purchaseItem.product = product;
                purchaseItem.quantity = quantityToPurchase;
                purchaseItem.itemTotal = itemTotal;
                purchaseItem.seller = seller;
                purchaseItems.add(purchaseItem);
                
                System.out.println("Item Total: $" + String.format("%.2f", itemTotal));
            }
            
            if (purchaseItems.isEmpty()) {
                System.out.println("No valid items to purchase.");
                return;
            }
            
            // Display purchase summary
            System.out.println("\n=== PURCHASE SUMMARY ===");
            System.out.println("Item\t\tQuantity\tUnit Price\tTotal");
            System.out.println("=" .repeat(60));
            for (PurchaseItem item : purchaseItems) {
                System.out.printf("%-15s %d\t\t$%-10.2f $%-10.2f%n",
                    item.product.getName().length() > 15 ? item.product.getName().substring(0, 12) + "..." : item.product.getName(),
                    item.quantity,
                    item.product.getPrice(),
                    item.itemTotal);
            }
            System.out.println("=" .repeat(60));
            System.out.println("TOTAL AMOUNT: $" + String.format("%.2f", totalAmount));
            
            // Payment method selection
            System.out.println("\nPayment Methods:");
            System.out.println("1. Credit Card");
            System.out.println("2. Debit Card");
            System.out.println("3. PayPal");
            System.out.println("4. Bank Transfer");
            
            int paymentChoice = InputUtils.readInt("Select payment method (1-4): ");
            String paymentMethod = getPaymentMethodByChoice(paymentChoice);
            
            String confirm = InputUtils.readString("Confirm purchase of " + purchaseItems.size() + " item(s) for $" + String.format("%.2f", totalAmount) + "? (y/n): ");
            
            if (confirm.equalsIgnoreCase("y")) {
                // Process each purchase item
                List<Transaction> completedTransactions = new ArrayList<>();
                boolean allSuccessful = true;
                
                for (PurchaseItem item : purchaseItems) {
                    try {
                        // Create transaction
                        Transaction transaction = new Transaction();
                        transaction.setBuyerId(buyer.getId());
                        transaction.setSellerId(item.product.getSellerId());
                        transaction.setProductId(item.product.getId());
                        transaction.setAmount(item.itemTotal);
                        transaction.setTransactionType("PURCHASE");
                        transaction.setPaymentMethod(paymentMethod);
                        transaction.setStatus("COMPLETED");
                        transaction.setCompletedDate(LocalDateTime.now());
                        transaction.setBuyerName(buyer.getFullName());
                        transaction.setSellerName(item.seller != null ? item.seller.getFullName() : "Unknown");
                        transaction.setProductName(item.product.getName());
                        transaction.setNotes("Multi-item purchase - Quantity: " + item.quantity);
                        
                        if (transactionService.createTransaction(transaction)) {
                            // Update product quantity or mark as sold
                            if (item.quantity == item.product.getQuantity()) {
                                item.product.setSold(true);
                                item.product.setAvailable(false);
                                item.product.setDateSold(LocalDateTime.now());
                            } else {
                                item.product.setQuantity(item.product.getQuantity() - item.quantity);
                            }
                            
                            productService.updateProduct(item.product);
                            
                            // Update buyer statistics
                            buyer.addPurchaseId(item.product.getId());
                            buyer.addTransactionId(transaction.getId());
                            buyer.updateSpent(item.itemTotal);
                            buyer.incrementPurchases();
                            
                            // Update seller statistics
                            if (item.seller != null) {
                                item.seller.addTransactionId(transaction.getId());
                                item.seller.updateEarnings(item.itemTotal);
                                item.seller.incrementProductsSold();
                                userService.updateUser(item.seller);
                            }
                            
                            completedTransactions.add(transaction);
                            System.out.println("✅ " + item.product.getName() + " purchased successfully!");
                        } else {
                            System.out.println("❌ Failed to purchase " + item.product.getName());
                            allSuccessful = false;
                        }
                    } catch (Exception e) {
                        System.out.println("❌ Error purchasing " + item.product.getName() + ": " + e.getMessage());
                        allSuccessful = false;
                    }
                }
                
                // Update buyer statistics (final update)
                userService.updateUser(buyer);
                
                // Display final results
                System.out.println("\n=== PURCHASE COMPLETED ===");
                System.out.println("Successfully purchased " + completedTransactions.size() + " out of " + purchaseItems.size() + " items.");
                System.out.println("Total amount: $" + String.format("%.2f", 
                    completedTransactions.stream().mapToDouble(Transaction::getAmount).sum()));
                
                if (allSuccessful) {
                    System.out.println("🎉 All items purchased successfully!");
                } else {
                    System.out.println("⚠️  Some items could not be purchased. Please check your transaction history.");
                }
            } else {
                System.out.println("Purchase cancelled.");
            }
        } catch (Exception e) {
            throw new DatabaseException("Failed to purchase product: " + e.getMessage(), e);
        }
    }
    
    @Override
    public void viewPurchaseHistory(Buyer buyer) throws DatabaseException {
        try {
            List<Transaction> transactions = transactionService.getTransactionsByBuyerId(buyer.getId().toString());
            
            System.out.println("\n=== PURCHASE HISTORY ===");
            
            if (transactions.isEmpty()) {
                System.out.println("No purchase history found.");
                return;
            }
            
            System.out.println("ID\t\tProduct\t\tSeller\t\tAmount\t\tType\t\tStatus\t\tDate");
            System.out.println("=" .repeat(120));
            
            for (Transaction transaction : transactions) {
                System.out.printf("%-15s %-15s %-15s $%-10.2f %-15s %-15s %s%n",
                    transaction.getId().toString().substring(0, 8) + "...",
                    transaction.getProductName() != null ? 
                        (transaction.getProductName().length() > 15 ? 
                            transaction.getProductName().substring(0, 12) + "..." : 
                            transaction.getProductName()) : "Unknown",
                    transaction.getSellerName() != null ? transaction.getSellerName() : "Unknown",
                    transaction.getAmount(),
                    transaction.getTransactionType(),
                    transaction.getStatus(),
                    InputUtils.formatDateTime(transaction.getTransactionDate()));
            }
            
            System.out.println("\nTotal Purchases: " + transactions.size());
            
            // Calculate total spent
            double totalSpent = transactions.stream()
                    .filter(t -> "COMPLETED".equals(t.getStatus()))
                    .mapToDouble(Transaction::getAmount)
                    .sum();
            
            System.out.println("Total Spent: $" + String.format("%.2f", totalSpent));
            
        } catch (Exception e) {
            throw new DatabaseException("Failed to view purchase history: " + e.getMessage(), e);
        }
    }
    
    @Override
    public void requestRefund(Buyer buyer) throws DatabaseException {
        try {
            List<Transaction> transactions = transactionService.getTransactionsByBuyerId(buyer.getId().toString());
            
            // Filter completed transactions only
            List<Transaction> completedTransactions = transactions.stream()
                    .filter(t -> "COMPLETED".equals(t.getStatus()))
                    .collect(Collectors.toList());
            
            if (completedTransactions.isEmpty()) {
                System.out.println("No completed transactions found for refund.");
                return;
            }
            
            System.out.println("\n=== REQUEST REFUND ===");
            System.out.println("Completed Transactions:");
            System.out.println("No.\tID\t\tProduct\t\tAmount\t\tDate");
            System.out.println("=" .repeat(80));
            
            for (int i = 0; i < completedTransactions.size(); i++) {
                Transaction transaction = completedTransactions.get(i);
                System.out.printf("%d.\t%-15s %-15s $%-10.2f %s%n",
                    i + 1,
                    transaction.getId().toString().substring(0, 8) + "...",
                    transaction.getProductName() != null ? 
                        (transaction.getProductName().length() > 15 ? 
                            transaction.getProductName().substring(0, 12) + "..." : 
                            transaction.getProductName()) : "Unknown",
                    transaction.getAmount(),
                    InputUtils.formatDateTime(transaction.getTransactionDate()));
            }
            
            int choice = InputUtils.readInt("Select transaction for refund (1-" + completedTransactions.size() + "): ");
            
            if (choice < 1 || choice > completedTransactions.size()) {
                System.out.println("Invalid selection.");
                return;
            }
            
            Transaction selectedTransaction = completedTransactions.get(choice - 1);
            
            System.out.println("\nTransaction Details:");
            System.out.println("ID: " + selectedTransaction.getId());
            System.out.println("Product: " + selectedTransaction.getProductName());
            System.out.println("Amount: $" + selectedTransaction.getAmount());
            System.out.println("Date: " + InputUtils.formatDateTime(selectedTransaction.getTransactionDate()));
            
            String reason = InputUtils.readString("Enter reason for refund: ");
            String confirm = InputUtils.readString("Confirm refund request? (y/n): ");
            
            if (confirm.equalsIgnoreCase("y")) {
                if (transactionService.processRefund(selectedTransaction.getId().toString())) {
                    System.out.println("\nRefund request processed successfully!");
                    System.out.println("Transaction ID: " + selectedTransaction.getId());
                    System.out.println("Refund Amount: $" + selectedTransaction.getAmount());
                    System.out.println("Reason: " + reason);
                    System.out.println("Status: REFUNDED");
                    System.out.println("The refund will be processed within 3-5 business days.");
                } else {
                    System.out.println("Failed to process refund request.");
                }
            } else {
                System.out.println("Refund request cancelled.");
            }
            
        } catch (Exception e) {
            throw new DatabaseException("Failed to request refund: " + e.getMessage(), e);
        }
    }
    
    @Override
    public void viewTransactionHistory(Buyer buyer) throws DatabaseException {
        viewPurchaseHistory(buyer); // Same as purchase history for buyers
    }
    
    @Override
    public void viewOngoingAuctions(Buyer buyer) throws DatabaseException {
        try {
            List<Auction> activeAuctions = auctionService.getActiveAuctions();
            
            System.out.println("\n=== ONGOING AUCTIONS ===");
            
            if (activeAuctions.isEmpty()) {
                System.out.println("No ongoing auctions found.");
                return;
            }
            
            System.out.printf("%-15s %-20s %-16s %-16s %-8s %-15s%n", 
                "ID", "Product", "Starting Price", "Current Bid", "Bids", "Time Left");
            System.out.println("=" .repeat(100));
            
            for (Auction auction : activeAuctions) {
                Product product = productService.findProductById(auction.getProductId().toString());
                String productName = product != null ? product.getName() : "Unknown";
                
                // Calculate time left
                LocalDateTime now = LocalDateTime.now();
                String timeLeft = getTimeLeft(now, auction.getEndTime());
                
                System.out.printf("%-15s %-20s $%-15.2f $%-15.2f %-8d %-15s%n",
                    auction.getId().toString().substring(0, 8) + "...",
                    productName.length() > 18 ? productName.substring(0, 15) + "..." : productName,
                    auction.getStartingPrice(),
                    auction.getCurrentHighestBid(),
                    auction.getTotalBids(),
                    timeLeft);
            }
            
            System.out.println("\nTotal Active Auctions: " + activeAuctions.size());
            
        } catch (Exception e) {
            throw new DatabaseException("Failed to view ongoing auctions: " + e.getMessage(), e);
        }
    }
    
    @Override
    public void placeBid(Buyer buyer) throws DatabaseException {
        try {
            List<Auction> activeAuctions = auctionService.getActiveAuctions();
            
            if (activeAuctions.isEmpty()) {
                System.out.println("No active auctions available for bidding.");
                return;
            }
            
            System.out.println("\n=== PLACE BID ===");
            System.out.println("Active Auctions:");
            System.out.printf("%-4s %-15s %-20s %-16s %-8s %-15s%n", 
                "No.", "ID", "Product", "Current Bid", "Bids", "Time Left");
            System.out.println("=" .repeat(100));
            
            for (int i = 0; i < activeAuctions.size(); i++) {
                Auction auction = activeAuctions.get(i);
                Product product = productService.findProductById(auction.getProductId().toString());
                String productName = product != null ? product.getName() : "Unknown";
                
                // Calculate time left
                LocalDateTime now = LocalDateTime.now();
                String timeLeft = getTimeLeft(now, auction.getEndTime());
                
                System.out.printf("%-4d %-15s %-20s $%-15.2f %-8d %-15s%n",
                    i + 1,
                    auction.getId().toString().substring(0, 8) + "...",
                    productName.length() > 18 ? productName.substring(0, 15) + "..." : productName,
                    auction.getCurrentHighestBid(),
                    auction.getTotalBids(),
                    timeLeft);
            }
            
            int choice = InputUtils.readInt("Select auction to bid on (1-" + activeAuctions.size() + "): ");
            
            if (choice < 1 || choice > activeAuctions.size()) {
                System.out.println("Invalid selection.");
                return;
            }
            
            Auction selectedAuction = activeAuctions.get(choice - 1);
            
            // Display auction details
            Product product = productService.findProductById(selectedAuction.getProductId().toString());
            System.out.println("\nAuction Details:");
            System.out.println("Product: " + (product != null ? product.getName() : "Unknown"));
            System.out.println("Description: " + (product != null ? product.getDescription() : "N/A"));
            System.out.println("Starting Price: $" + selectedAuction.getStartingPrice());
            System.out.println("Current Highest Bid: $" + selectedAuction.getCurrentHighestBid());
            System.out.println("Total Bids: " + selectedAuction.getTotalBids());
            System.out.println("End Time: " + InputUtils.formatDateTime(selectedAuction.getEndTime()));
            
            double minBid = selectedAuction.getCurrentHighestBid() + 0.01;
            double bidAmount = InputUtils.readDouble("Enter your bid amount (minimum $" + String.format("%.2f", minBid) + "): $");
            
            if (bidAmount <= selectedAuction.getCurrentHighestBid()) {
                System.out.println("Bid amount must be higher than current highest bid.");
                return;
            }
            
            String confirm = InputUtils.readString("Confirm bid of $" + String.format("%.2f", bidAmount) + "? (y/n): ");
            
            if (confirm.equalsIgnoreCase("y")) {
                try {
                    // Create bid
                    Bid bid = new Bid();
                    bid.setAuctionId(selectedAuction.getId());
                    bid.setBidderId(buyer.getId());
                    bid.setBidAmount(bidAmount);
                    bid.setBidderName(buyer.getFullName());
                    
                    if (auctionService.placeBid(bid)) {
                        // Update buyer's bid list
                        buyer.addBidId(bid.getId());
                        userService.updateUser(buyer);
                        
                        // Get updated auction to show new end time
                        Auction updatedAuction = auctionService.findAuctionById(selectedAuction.getId().toString());
                        
                        System.out.println("\nBid placed successfully!");
                        System.out.println("Bid ID: " + bid.getId());
                        System.out.println("Auction: " + (product != null ? product.getName() : "Unknown"));
                        System.out.println("Bid Amount: $" + String.format("%.2f", bidAmount));
                        System.out.println("Bid Time: " + InputUtils.formatDateTime(bid.getBidTime()));
                        
                        // Show the updated auction end time (time has been reset)
                        if (updatedAuction != null) {
                            System.out.println("⏰ Auction time has been extended!");
                            System.out.println("New End Time: " + InputUtils.formatDateTime(updatedAuction.getEndTime()));
                        }
                        
                    } else {
                        System.out.println("Failed to place bid.");
                    }
                    
                } catch (AuctionException e) {
                    System.out.println("Bid failed: " + e.getMessage());
                }
            } else {
                System.out.println("Bid cancelled.");
            }
            
        } catch (Exception e) {
            throw new DatabaseException("Failed to place bid: " + e.getMessage(), e);
        }
    }
    
    @Override
    public void viewAuctionHistory(Buyer buyer) throws DatabaseException {
        try {
            List<Bid> bids = auctionService.getBidsByBidderId(buyer.getId().toString());
            
            System.out.println("\n=== AUCTION HISTORY ===");
            
            if (bids.isEmpty()) {
                System.out.println("No auction history found.");
                return;
            }
            
            System.out.printf("%-15s %-15s %-20s %-14s %-12s %-20s%n", 
                "Bid ID", "Auction", "Product", "Bid Amount", "Winning", "Bid Time");
            System.out.println("=" .repeat(120));
            
            for (Bid bid : bids) {
                Auction auction = auctionService.findAuctionById(bid.getAuctionId().toString());
                Product product = null;
                
                if (auction != null) {
                    product = productService.findProductById(auction.getProductId().toString());
                }
                
                String productName = product != null ? product.getName() : "Unknown";
                
                System.out.printf("%-15s %-15s %-20s $%-14.2f %-12s %-20s%n",
                    bid.getId().toString().substring(0, 8) + "...",
                    auction != null ? auction.getId().toString().substring(0, 8) + "..." : "Unknown",
                    productName.length() > 18 ? productName.substring(0, 15) + "..." : productName,
                    bid.getBidAmount(),
                    bid.isWinning() ? "YES" : "NO",
                    InputUtils.formatDateTime(bid.getBidTime()));
            }
            
            System.out.println("\nTotal Bids: " + bids.size());
            
            // Count winning bids
            long winningBids = bids.stream().filter(Bid::isWinning).count();
            System.out.println("Winning Bids: " + winningBids);
            
        } catch (Exception e) {
            throw new DatabaseException("Failed to view auction history: " + e.getMessage(), e);
        }
    }
    
    @Override
    public void updatePersonalDetails(Buyer buyer) throws DatabaseException {
        try {
            System.out.println("\n=== UPDATE PERSONAL DETAILS ===");
            System.out.println("Current Details:");
            System.out.println("Name: " + buyer.getFullName());
            System.out.println("Email: " + buyer.getEmail());
            System.out.println("Phone: " + buyer.getPhoneNumber());
            
            System.out.println("\nWhat would you like to update?");
            System.out.println("1. First Name");
            System.out.println("2. Last Name");
            System.out.println("3. Email");
            System.out.println("4. Phone Number");
            System.out.println("5. Password");
            System.out.println("0. Cancel");
            
            int choice = InputUtils.readInt("Enter your choice: ");
            
            switch (choice) {
                case 1:
                    String firstName = InputUtils.readString("Enter new first name: ");
                    buyer.setFirstName(firstName);
                    break;
                case 2:
                    String lastName = InputUtils.readString("Enter new last name: ");
                    buyer.setLastName(lastName);
                    break;
                case 3:
                    String email = InputUtils.readString("Enter new email: ");
                    if (userService.emailExists(email)) {
                        System.out.println("Email already exists. Please choose a different email.");
                        return;
                    }
                    buyer.setEmail(email);
                    break;
                case 4:
                    String phone = InputUtils.readString("Enter new phone number: ");
                    buyer.setPhoneNumber(phone);
                    break;
                case 5:
                    String oldPassword = InputUtils.readString("Enter current password: ");
                    if (!com.auction.utils.PasswordUtils.verifyPassword(oldPassword, buyer.getPassword())) {
                        System.out.println("Current password is incorrect.");
                        return;
                    }
                    String newPassword = InputUtils.readString("Enter new password: ");
                    String confirmPassword = InputUtils.readString("Confirm new password: ");
                    if (!newPassword.equals(confirmPassword)) {
                        System.out.println("Passwords do not match.");
                        return;
                    }
                    buyer.setPassword(com.auction.utils.PasswordUtils.encryptPassword(newPassword));
                    break;
                case 0:
                    System.out.println("Update cancelled.");
                    return;
                default:
                    System.out.println("Invalid choice.");
                    return;
            }
            
            if (userService.updateUser(buyer)) {
                System.out.println("Personal details updated successfully!");
            } else {
                System.out.println("Failed to update personal details.");
            }
            
        } catch (Exception e) {
            throw new DatabaseException("Failed to update personal details: " + e.getMessage(), e);
        }
    }
    
    @Override
    public void viewProfileSummary(Buyer buyer) throws DatabaseException {
        try {
            System.out.println("\n=== PROFILE SUMMARY ===");
            System.out.println("Name: " + buyer.getFullName());
            System.out.println("Username: " + buyer.getUsername());
            System.out.println("Email: " + buyer.getEmail());
            System.out.println("Phone: " + buyer.getPhoneNumber());
            System.out.println("Registration Date: " + InputUtils.formatDateTime(buyer.getRegistrationDate()));
            System.out.println("Account Status: " + (buyer.isActive() ? "Active" : "Inactive"));
            
            // Get statistics
            List<Transaction> transactions = transactionService.getTransactionsByBuyerId(buyer.getId().toString());
            List<Bid> bids = auctionService.getBidsByBidderId(buyer.getId().toString());
            
            double totalSpent = transactions.stream()
                    .filter(t -> "COMPLETED".equals(t.getStatus()))
                    .mapToDouble(Transaction::getAmount)
                    .sum();
            
            long winningBids = bids.stream().filter(Bid::isWinning).count();
            
            System.out.println("\n=== BUYING STATISTICS ===");
            System.out.println("Total Purchases: " + buyer.getTotalPurchases());
            System.out.println("Total Transactions: " + transactions.size());
            System.out.println("Total Spent: $" + String.format("%.2f", totalSpent));
            System.out.println("Average Purchase: $" + String.format("%.2f", 
                    buyer.getTotalPurchases() > 0 ? totalSpent / buyer.getTotalPurchases() : 0));
            System.out.println("Total Bids: " + bids.size());
            System.out.println("Winning Bids: " + winningBids);
            System.out.println("Bid Success Rate: " + String.format("%.1f", 
                    bids.size() > 0 ? (winningBids * 100.0 / bids.size()) : 0) + "%");
            
        } catch (Exception e) {
            throw new DatabaseException("Failed to view profile summary: " + e.getMessage(), e);
        }
    }
    
    @Override
    public boolean deleteAccount(Buyer buyer) throws DatabaseException {
        try {
            System.out.println("\n=== DELETE ACCOUNT ===");
            System.out.println("WARNING: This action cannot be undone!");
            System.out.println("All your purchase history and bids will be removed.");
            
            String confirm1 = InputUtils.readString("Are you sure you want to delete your account? (yes/no): ");
            
            if (!confirm1.equalsIgnoreCase("yes")) {
                System.out.println("Account deletion cancelled.");
                return false;
            }
            
            String confirm2 = InputUtils.readString("Type 'DELETE' to confirm: ");
            
            if (!confirm2.equals("DELETE")) {
                System.out.println("Account deletion cancelled.");
                return false;
            }
            
            String password = InputUtils.readString("Enter your password to confirm: ");
            
            if (!com.auction.utils.PasswordUtils.verifyPassword(password, buyer.getPassword())) {
                System.out.println("Incorrect password. Account deletion cancelled.");
                return false;
            }
            
            // Delete user account
            if (userService.deleteUser(buyer.getId().toString())) {
                System.out.println("Account deleted successfully!");
                return true;
            } else {
                System.out.println("Failed to delete account.");
                return false;
            }
            
        } catch (Exception e) {
            throw new DatabaseException("Failed to delete account: " + e.getMessage(), e);
        }
    }
    
    @Override
    public List<Product> getProductsByCategory(String category) throws DatabaseException {
        return productService.getProductsByCategory(category);
    }
    
    @Override
    public List<Transaction> getTransactionsByBuyerId(String buyerId) throws DatabaseException {
        return transactionService.getTransactionsByBuyerId(buyerId);
    }
    
    // Helper methods
    private void viewProductsBySeller(Seller seller) throws DatabaseException {
        List<Product> products = productService.getProductsBySellerId(seller.getId().toString());
        
        System.out.println("\nProducts by " + seller.getFullName() + ":");
        
        if (products.isEmpty()) {
            System.out.println("No products found.");
            return;
        }
        
        System.out.println("ID\t\tName\t\t\tCategory\t\tPrice\t\tCondition\tStatus");
        System.out.println("=" .repeat(100));
        
        for (Product product : products) {
            String status = product.isSold() ? "SOLD" : (product.isAvailable() ? "AVAILABLE" : "UNAVAILABLE");
            System.out.printf("%-15s %-20s %-15s $%-10.2f %-15s %s%n",
                product.getId().toString().substring(0, 8) + "...",
                product.getName().length() > 20 ? product.getName().substring(0, 17) + "..." : product.getName(),
                product.getCategory(),
                product.getPrice(),
                product.getCondition(),
                status);
        }
    }
    
    private String getPaymentMethodByChoice(int choice) {
        switch (choice) {
            case 1: return "Credit Card";
            case 2: return "Debit Card";
            case 3: return "PayPal";
            case 4: return "Bank Transfer";
            default: return "Credit Card";
        }
    }
    
    private String getTimeLeft(LocalDateTime now, LocalDateTime endTime) {
        if (now.isAfter(endTime)) {
            return "EXPIRED";
        }
        
        long minutes = java.time.Duration.between(now, endTime).toMinutes();
        
        if (minutes < 60) {
            return minutes + " min";
        } else if (minutes < 1440) { // 24 hours
            return (minutes / 60) + "h " + (minutes % 60) + "m";
        } else {
            long days = minutes / 1440;
            long hours = (minutes % 1440) / 60;
            return days + "d " + hours + "h";
        }
    }
}

/**
 * Helper class for managing purchase items
 */
class PurchaseItem {
    Product product;
    int quantity;
    double itemTotal;
    Seller seller;
}
