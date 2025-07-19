package com.auction.services;

import com.auction.models.Auction;
import com.auction.models.Bid;
import com.auction.exceptions.DatabaseException;
import com.auction.exceptions.AuctionException;
import java.util.List;

/**
 * Interface for auction-related services
 */
public interface AuctionService {
    
    /**
     * Creates a new auction
     * @param auction the auction to create
     * @return true if creation successful, false otherwise
     * @throws DatabaseException if database operation fails
     */
    boolean createAuction(Auction auction) throws DatabaseException;
    
   
    boolean updateAuction(Auction auction) throws DatabaseException;
    
   
    boolean deleteAuction(String auctionId) throws DatabaseException;
    
   
    Auction findAuctionById(String auctionId) throws DatabaseException;
    
   
    List<Auction> getAllAuctions() throws DatabaseException;
    
   
    List<Auction> getActiveAuctions() throws DatabaseException;
    
   
    List<Auction> getCompletedAuctions() throws DatabaseException;
    
   
    List<Auction> getAuctionsBySellerId(String sellerId) throws DatabaseException;
   
    boolean placeBid(Bid bid) throws DatabaseException, AuctionException;
    
    
    List<Bid> getBidsByAuctionId(String auctionId) throws DatabaseException;
   
    List<Bid> getBidsByBidderId(String bidderId) throws DatabaseException;
 
    boolean endAuction(String auctionId) throws DatabaseException, AuctionException;
    
   
    void checkAndUpdateExpiredAuctions() throws DatabaseException;
    
    /**
     * Checks for expired auctions and returns newly completed ones
     * @return List of newly completed auctions
     * @throws DatabaseException if database operation fails
     */
    List<Auction> checkAndGetNewlyCompletedAuctions() throws DatabaseException;
}
