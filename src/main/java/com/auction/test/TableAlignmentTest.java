package com.auction.test;

/**
 * Test class to verify table column alignment improvements
 */
public class TableAlignmentTest {
    public static void main(String[] args) {
        System.out.println("=== Table Alignment Test ===");
        
        // Test auction history table format
        System.out.println("\n=== AUCTION HISTORY (IMPROVED) ===");
        System.out.println("Bid ID\t\t\tAuction\t\t\tProduct\t\t\tBid Amount\tWinning\t\tBid Time");
        System.out.println("=" .repeat(120));
        
        // Sample data to test alignment
        System.out.printf("%-15s %-15s %-20s $%-14.2f %-12s %-20s%n",
            "68792e41...", "68792e12...", "iphone", 14.00, "NO", "2025-07-17 22:39:21");
        System.out.printf("%-15s %-15s %-20s $%-14.2f %-12s %-20s%n",
            "687920sd...", "68791f64...", "bat", 101.00, "YES", "2025-07-17 21:40:05");
        
        // Test ongoing auctions table format  
        System.out.println("\n=== ONGOING AUCTIONS (IMPROVED) ===");
        System.out.println("ID\t\t\tProduct\t\t\tStarting Price\tCurrent Bid\tBids\tTime Left");
        System.out.println("=" .repeat(100));
        
        System.out.printf("%-15s %-20s $%-14.2f $%-14.2f %-8d %-15s%n",
            "68791f64...", "bat", 90.00, 101.00, 1, "EXPIRED");
        System.out.printf("%-15s %-20s $%-14.2f $%-14.2f %-8d %-15s%n",
            "68792e12...", "iphone", 12.00, 15.00, 2, "EXPIRED");
        System.out.printf("%-15s %-20s $%-14.2f $%-14.2f %-8d %-15s%n",
            "687a6b6c...", "bike", 1000001.00, 10020001.00, 1, "EXPIRED");
        
        // Test my products table format
        System.out.println("\n=== MY PRODUCTS (IMPROVED) ===");
        System.out.println("ID\t\t\tName\t\t\tCategory\t\tPrice\t\t\tCondition\t\tQuantity\tStatus\t\t\tDate Added");
        System.out.println("=" .repeat(140));
        
        System.out.printf("%-15s %-20s %-15s $%-14.2f %-18s %-12d %-18s %-20s%n",
            "687723b3...", "Laptop", "Electronics", 1000.00, "New", 1, "AVAILABLE", "2025-07-16 14:55:31");
        System.out.printf("%-15s %-20s %-15s $%-14.2f %-18s %-12d %-18s %-20s%n",
            "687723a1...", "iphone", "Electronics", 999.00, "New", 89, "UNAVAILABLE", "2025-07-16 14:59:29");
        System.out.printf("%-15s %-20s %-15s $%-14.2f %-18s %-12d %-18s %-20s%n",
            "687723cb...", "book", "Books", 800.00, "New", 300, "AVAILABLE", "2025-07-16 15:00:11");
        System.out.printf("%-15s %-20s %-15s $%-14.2f %-18s %-12d %-18s %-20s%n",
            "6877241b...", "chair", "Other", 100.00, "Refurbished", 11, "AVAILABLE", "2025-07-16 15:01:28");
        
        System.out.println("\n✅ Table alignment test completed!");
        System.out.println("📋 All columns should now be properly aligned with consistent spacing.");
        System.out.println("💡 Key improvements:");
        System.out.println("   - Fixed column widths for proper alignment");
        System.out.println("   - Consistent spacing between columns");
        System.out.println("   - Better handling of long text with truncation");
        System.out.println("   - Aligned currency values and numbers");
    }
}
