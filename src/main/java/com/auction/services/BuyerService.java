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
   
    void browseProductsByCategory(Buyer buyer) throws DatabaseException;
    
  
    void viewAllSellers(Buyer buyer) throws DatabaseException;
    
 
    void purchaseProduct(Buyer buyer) throws DatabaseException;
 
    void viewPurchaseHistory(Buyer buyer) throws DatabaseException;
 
    void requestRefund(Buyer buyer) throws DatabaseException;
    
    
    void viewTransactionHistory(Buyer buyer) throws DatabaseException;
    
   
    void viewOngoingAuctions(Buyer buyer) throws DatabaseException;
    
  
    void placeBid(Buyer buyer) throws DatabaseException;
    
   
    void viewAuctionHistory(Buyer buyer) throws DatabaseException;
    
  
    void updatePersonalDetails(Buyer buyer) throws DatabaseException;
  
    void viewProfileSummary(Buyer buyer) throws DatabaseException;
    
    
    boolean deleteAccount(Buyer buyer) throws DatabaseException;
    

    List<Product> getProductsByCategory(String category) throws DatabaseException;
    

    List<Transaction> getTransactionsByBuyerId(String buyerId) throws DatabaseException;
}
