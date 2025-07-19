package com.auction.utils;

import com.auction.models.Auction;
import com.auction.models.Bid;
import com.auction.models.Product;
import com.auction.models.User;
import com.auction.services.AuctionService;
import com.auction.services.ProductService;
import com.auction.services.UserService;
import com.auction.services.impl.AuctionServiceImpl;
import com.auction.services.impl.ProductServiceImpl;
import com.auction.services.impl.UserServiceImpl;
import com.auction.exceptions.DatabaseException;

import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Utility class for displaying auction winner announcements
 */
public class WinnerAnnouncementUtils {
    
    private static final AuctionService auctionService = new AuctionServiceImpl();
    private static final ProductService productService = new ProductServiceImpl();
    private static final UserService userService = new UserServiceImpl();
    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    /**
     * Check for newly completed auctions and display winner announcements
     */
    public static void checkAndDisplayWinnerAnnouncements() {
        try {
            List<Auction> newlyCompleted = auctionService.checkAndGetNewlyCompletedAuctions();
            
            if (!newlyCompleted.isEmpty()) {
                System.out.println("\n" + "=".repeat(50));
                System.out.println("          AUCTION RESULTS ANNOUNCEMENT");
                System.out.println("=".repeat(50));
                
                for (Auction auction : newlyCompleted) {
                    displayWinnerAnnouncement(auction);
                }
                
                System.out.println("=".repeat(50));
                InputUtils.pause();
            }
            
        } catch (Exception e) {
            // Silent handling - don't interrupt user experience
            System.err.println("Note: Unable to check for auction results at this time.");
        }
    }
    
    /**
     * Display winner announcement for a specific auction
     */
    public static void displayWinnerAnnouncement(Auction auction) {
        try {
            // Get product details
            Product product = productService.findProductById(auction.getProductId().toString());
            if (product == null) {
                System.out.println("❌ Product not found for auction: " + auction.getId());
                return;
            }
            
            System.out.println("\n*** AUCTION COMPLETED ***");
            System.out.println("=".repeat(60));
            System.out.printf("Product: %s%n", product.getName());
            System.out.printf("Category: %s%n", product.getCategory());
            System.out.printf("Starting Price: $%.2f%n", auction.getStartingPrice());
            System.out.printf("Ended: %s%n", auction.getEndTime().format(dateFormatter));
            
            // Check if there's a winner
            if (auction.getCurrentHighestBidderId() != null && auction.getCurrentHighestBid() > auction.getStartingPrice()) {
                // Get winner details
                User winner = userService.findUserById(auction.getCurrentHighestBidderId().toString());
                String winnerName = winner != null ? winner.getFullName() : "Unknown";
                
                System.out.println("\n*** WINNER ANNOUNCEMENT ***");
                System.out.println("-".repeat(40));
                System.out.printf("Winner: %s%n", winnerName);
                System.out.printf("Winning Bid: $%.2f%n", auction.getCurrentHighestBid());
                System.out.printf("Total Bids: %d%n", auction.getTotalBids());
                System.out.println("Congratulations to the winner!");
                
                // Show bid history for context
                displayRecentBids(auction);
                
            } else {
                System.out.println("\n*** NO WINNER ***");
                System.out.println("-".repeat(40));
                System.out.println("No bids were placed above the starting price");
                System.out.println("This item will be returned to available inventory");
            }
            
            System.out.println("=".repeat(60));
            
        } catch (Exception e) {
            System.err.println("Error displaying winner announcement: " + e.getMessage());
        }
    }
    
    /**
     * Display recent bids for an auction
     */
    private static void displayRecentBids(Auction auction) {
        try {
            List<Bid> bids = auctionService.getBidsByAuctionId(auction.getId().toString());
            
            if (bids.size() > 1) {
                System.out.println("\nRecent Bids:");
                System.out.println("-".repeat(40));
                
                int count = 0;
                for (Bid bid : bids) {
                    if (count >= 3) break; // Show only last 3 bids
                    
                    String status = bid.isWinning() ? "*** WINNING" : "    ";
                    System.out.printf("%s $%.2f by %s%n", 
                        status, 
                        bid.getBidAmount(), 
                        bid.getBidderName() != null ? bid.getBidderName() : "Anonymous");
                    count++;
                }
            }
            
        } catch (Exception e) {
            // Silent error handling
        }
    }
    
    /**
     * Display a simple winner notification (for dashboard integration)
     */
    public static void displaySimpleWinnerNotification(Auction auction) {
        try {
            Product product = productService.findProductById(auction.getProductId().toString());
            String productName = product != null ? product.getName() : "Unknown Item";
            
            if (auction.getCurrentHighestBidderId() != null && auction.getCurrentHighestBid() > auction.getStartingPrice()) {
                User winner = userService.findUserById(auction.getCurrentHighestBidderId().toString());
                String winnerName = winner != null ? winner.getFullName() : "Unknown";
                
                System.out.printf("*** WINNER: %s won '%s' for $%.2f%n", 
                    winnerName, productName, auction.getCurrentHighestBid());
            } else {
                System.out.printf("*** NO WINNER: '%s' received no qualifying bids%n", productName);
            }
            
        } catch (Exception e) {
            System.err.println("Error displaying winner notification");
        }
    }
    
    /**
     * Check if user should see winner announcements and display them
     */
    public static void showPendingWinnerAnnouncements(String userRole) {
        // Only show announcements for admin and buyers
        if ("ADMIN".equals(userRole) || "BUYER".equals(userRole)) {
            checkAndDisplayWinnerAnnouncements();
        }
    }
}
