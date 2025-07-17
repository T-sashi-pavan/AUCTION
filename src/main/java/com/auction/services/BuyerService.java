package com.auction.services;

import com.auction.models.Buyer;
import com.auction.models.Product;
import com.auction.models.Transaction;
import com.auction.models.Auction;
import com.auction.models.Bid;
import com.auction.models.Seller;
import com.auction.exceptions.DatabaseException;
import java.util.List;

/**
 * Interface for buyer-specific services
 */
public interface BuyerService {
    
    /**
     * Browses products by category
     * @param buyer the buyer
     * @throws DatabaseException if database operation fails
     */
    void browseProductsByCategory(Buyer buyer) throws DatabaseException;
    
    /**
     * Views all sellers in the system
     * @param buyer the buyer
     * @throws DatabaseException if database operation fails
     */
    void viewAllSellers(Buyer buyer) throws DatabaseException;
    
    /**
     * Purchases a product
     * @param buyer the buyer
     * @throws DatabaseException if database operation fails
     */
    void purchaseProduct(Buyer buyer) throws DatabaseException;
    
    /**
     * Views purchase history of the buyer
     * @param buyer the buyer
     * @throws DatabaseException if database operation fails
     */
    void viewPurchaseHistory(Buyer buyer) throws DatabaseException;
    
    /**
     * Requests a refund for a purchase
     * @param buyer the buyer
     * @throws DatabaseException if database operation fails
     */
    void requestRefund(Buyer buyer) throws DatabaseException;
    
    /**
     * Views transaction history of the buyer
     * @param buyer the buyer
     * @throws DatabaseException if database operation fails
     */
    void viewTransactionHistory(Buyer buyer) throws DatabaseException;
    
    /**
     * Views ongoing auctions
     * @param buyer the buyer
     * @throws DatabaseException if database operation fails
     */
    void viewOngoingAuctions(Buyer buyer) throws DatabaseException;
    
    /**
     * Places a bid in an auction
     * @param buyer the buyer
     * @throws DatabaseException if database operation fails
     */
    void placeBid(Buyer buyer) throws DatabaseException;
    
    /**
     * Views auction history
     * @param buyer the buyer
     * @throws DatabaseException if database operation fails
     */
    void viewAuctionHistory(Buyer buyer) throws DatabaseException;
    
    /**
     * Updates personal details of the buyer
     * @param buyer the buyer
     * @throws DatabaseException if database operation fails
     */
    void updatePersonalDetails(Buyer buyer) throws DatabaseException;
    
    /**
     * Views profile summary of the buyer
     * @param buyer the buyer
     * @throws DatabaseException if database operation fails
     */
    void viewProfileSummary(Buyer buyer) throws DatabaseException;
    
    /**
     * Deletes the buyer's account
     * @param buyer the buyer
     * @return true if deletion successful, false otherwise
     * @throws DatabaseException if database operation fails
     */
    boolean deleteAccount(Buyer buyer) throws DatabaseException;
    
    /**
     * Gets all products by category
     * @param category the product category
     * @return list of products
     * @throws DatabaseException if database operation fails
     */
    List<Product> getProductsByCategory(String category) throws DatabaseException;
    
    /**
     * Gets all transactions by buyer ID
     * @param buyerId the buyer ID
     * @return list of transactions
     * @throws DatabaseException if database operation fails
     */
    List<Transaction> getTransactionsByBuyerId(String buyerId) throws DatabaseException;
}
