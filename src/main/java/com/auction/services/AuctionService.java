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
    
    /**
     * Updates an auction
     * @param auction the auction to update
     * @return true if update successful, false otherwise
     * @throws DatabaseException if database operation fails
     */
    boolean updateAuction(Auction auction) throws DatabaseException;
    
    /**
     * Deletes an auction
     * @param auctionId the auction ID to delete
     * @return true if deletion successful, false otherwise
     * @throws DatabaseException if database operation fails
     */
    boolean deleteAuction(String auctionId) throws DatabaseException;
    
    /**
     * Finds an auction by ID
     * @param auctionId the auction ID
     * @return auction if found, null otherwise
     * @throws DatabaseException if database operation fails
     */
    Auction findAuctionById(String auctionId) throws DatabaseException;
    
    /**
     * Gets all auctions
     * @return list of all auctions
     * @throws DatabaseException if database operation fails
     */
    List<Auction> getAllAuctions() throws DatabaseException;
    
    /**
     * Gets active auctions
     * @return list of active auctions
     * @throws DatabaseException if database operation fails
     */
    List<Auction> getActiveAuctions() throws DatabaseException;
    
    /**
     * Gets completed auctions
     * @return list of completed auctions
     * @throws DatabaseException if database operation fails
     */
    List<Auction> getCompletedAuctions() throws DatabaseException;
    
    /**
     * Gets auctions by seller ID
     * @param sellerId the seller ID
     * @return list of auctions by the seller
     * @throws DatabaseException if database operation fails
     */
    List<Auction> getAuctionsBySellerId(String sellerId) throws DatabaseException;
    
    /**
     * Places a bid in an auction
     * @param bid the bid to place
     * @return true if bid placed successfully, false otherwise
     * @throws DatabaseException if database operation fails
     * @throws AuctionException if auction-related validation fails
     */
    boolean placeBid(Bid bid) throws DatabaseException, AuctionException;
    
    /**
     * Gets all bids for an auction
     * @param auctionId the auction ID
     * @return list of bids
     * @throws DatabaseException if database operation fails
     */
    List<Bid> getBidsByAuctionId(String auctionId) throws DatabaseException;
    
    /**
     * Gets all bids by bidder ID
     * @param bidderId the bidder ID
     * @return list of bids
     * @throws DatabaseException if database operation fails
     */
    List<Bid> getBidsByBidderId(String bidderId) throws DatabaseException;
    
    /**
     * Ends an auction and determines the winner
     * @param auctionId the auction ID
     * @return true if auction ended successfully, false otherwise
     * @throws DatabaseException if database operation fails
     * @throws AuctionException if auction-related validation fails
     */
    boolean endAuction(String auctionId) throws DatabaseException, AuctionException;
    
    /**
     * Checks and updates expired auctions
     * @throws DatabaseException if database operation fails
     */
    void checkAndUpdateExpiredAuctions() throws DatabaseException;
}
