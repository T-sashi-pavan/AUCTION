package com.auction.services.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.bson.types.ObjectId;

import com.auction.database.DatabaseConnection;
import com.auction.exceptions.AuctionException;
import com.auction.exceptions.DatabaseException;
import com.auction.models.Auction;
import com.auction.models.Bid;
import com.auction.services.AuctionService;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.lt;
import static com.mongodb.client.model.Filters.ne;
import static com.mongodb.client.model.Sorts.descending;

/**
 * Implementation of AuctionService interface
 */
public class AuctionServiceImpl implements AuctionService {
    
    private MongoCollection<Document> getAuctionCollection() throws DatabaseException {
        MongoDatabase database = DatabaseConnection.getDatabase();
        return database.getCollection("auctions");
    }
    
    private MongoCollection<Document> getBidCollection() throws DatabaseException {
        MongoDatabase database = DatabaseConnection.getDatabase();
        return database.getCollection("bids");
    }
    
    @Override
    public boolean createAuction(Auction auction) throws DatabaseException {
        try {
            MongoCollection<Document> collection = getAuctionCollection();
            Document auctionDoc = auctionToDocument(auction);
            collection.insertOne(auctionDoc);
            return true;
        } catch (Exception e) {
            throw new DatabaseException("Failed to create auction: " + e.getMessage(), e);
        }
    }
    
    @Override
    public boolean updateAuction(Auction auction) throws DatabaseException {
        try {
            MongoCollection<Document> collection = getAuctionCollection();
            Document auctionDoc = auctionToDocument(auction);
            collection.replaceOne(eq("_id", auction.getId()), auctionDoc);
            return true;
        } catch (Exception e) {
            throw new DatabaseException("Failed to update auction: " + e.getMessage(), e);
        }
    }
    
    @Override
    public boolean deleteAuction(String auctionId) throws DatabaseException {
        try {
            MongoCollection<Document> collection = getAuctionCollection();
            collection.deleteOne(eq("_id", new ObjectId(auctionId)));
            return true;
        } catch (Exception e) {
            throw new DatabaseException("Failed to delete auction: " + e.getMessage(), e);
        }
    }
    
    @Override
    public Auction findAuctionById(String auctionId) throws DatabaseException {
        try {
            MongoCollection<Document> collection = getAuctionCollection();
            Document auctionDoc = collection.find(eq("_id", new ObjectId(auctionId))).first();
            return auctionDoc != null ? documentToAuction(auctionDoc) : null;
        } catch (Exception e) {
            throw new DatabaseException("Failed to find auction: " + e.getMessage(), e);
        }
    }
    
    @Override
    public List<Auction> getAllAuctions() throws DatabaseException {
        try {
            MongoCollection<Document> collection = getAuctionCollection();
            List<Auction> auctions = new ArrayList<>();
            
            for (Document doc : collection.find().sort(descending("createdAt"))) {
                auctions.add(documentToAuction(doc));
            }
            
            return auctions;
        } catch (Exception e) {
            throw new DatabaseException("Failed to get all auctions: " + e.getMessage(), e);
        }
    }
    
    @Override
    public List<Auction> getActiveAuctions() throws DatabaseException {
        try {
            MongoCollection<Document> collection = getAuctionCollection();
            List<Auction> auctions = new ArrayList<>();
            
            for (Document doc : collection.find(and(eq("isActive", true), eq("status", "ACTIVE")))) {
                auctions.add(documentToAuction(doc));
            }
            
            return auctions;
        } catch (Exception e) {
            throw new DatabaseException("Failed to get active auctions: " + e.getMessage(), e);
        }
    }
    
    @Override
    public List<Auction> getCompletedAuctions() throws DatabaseException {
        try {
            MongoCollection<Document> collection = getAuctionCollection();
            List<Auction> auctions = new ArrayList<>();
            
            for (Document doc : collection.find(eq("status", "COMPLETED"))) {
                auctions.add(documentToAuction(doc));
            }
            
            return auctions;
        } catch (Exception e) {
            throw new DatabaseException("Failed to get completed auctions: " + e.getMessage(), e);
        }
    }
    
    @Override
    public List<Auction> getAuctionsBySellerId(String sellerId) throws DatabaseException {
        try {
            MongoCollection<Document> collection = getAuctionCollection();
            List<Auction> auctions = new ArrayList<>();
            
            for (Document doc : collection.find(eq("sellerId", new ObjectId(sellerId)))) {
                auctions.add(documentToAuction(doc));
            }
            
            return auctions;
        } catch (Exception e) {
            throw new DatabaseException("Failed to get auctions by seller: " + e.getMessage(), e);
        }
    }
    
    @Override
    public boolean placeBid(Bid bid) throws DatabaseException, AuctionException {
        try {
            // First, validate the bid
            Auction auction = findAuctionById(bid.getAuctionId().toString());
            
            if (auction == null) {
                throw new AuctionException("Auction not found");
            }
            
            if (!auction.isActive()) {
                throw new AuctionException("Auction is not active");
            }
            
            if (auction.hasEnded()) {
                throw new AuctionException("Auction has ended");
            }
            
            if (bid.getBidAmount() <= auction.getCurrentHighestBid()) {
                throw new AuctionException("Bid amount must be higher than current highest bid");
            }
            
            // Save the bid
            MongoCollection<Document> bidCollection = getBidCollection();
            Document bidDoc = bidToDocument(bid);
            bidCollection.insertOne(bidDoc);
            
            // Update the auction with new highest bid
            auction.setCurrentHighestBid(bid.getBidAmount());
            auction.setCurrentHighestBidderId(bid.getBidderId());
            auction.setTotalBids(auction.getTotalBids() + 1);
            
            // **AUCTION TIME RESET FEATURE** - Reset auction end time when a bid is placed
            // Use the stored original duration instead of calculating it
            long originalDurationMinutes = auction.getDurationMinutes();
            
            // Set new end time to current time + original duration
            LocalDateTime newEndTime = LocalDateTime.now().plusMinutes(originalDurationMinutes);
            auction.setEndTime(newEndTime);
            
            // Mark previous bids as not winning
            bidCollection.updateMany(
                and(eq("auctionId", bid.getAuctionId()), ne("_id", bid.getId())),
                new Document("$set", new Document("isWinning", false))
            );
            
            // Mark current bid as winning
            bidCollection.updateOne(
                eq("_id", bid.getId()),
                new Document("$set", new Document("isWinning", true))
            );
            
            // Update auction
            updateAuction(auction);
            
            return true;
        } catch (AuctionException e) {
            throw e;
        } catch (Exception e) {
            throw new DatabaseException("Failed to place bid: " + e.getMessage(), e);
        }
    }
    
    @Override
    public List<Bid> getBidsByAuctionId(String auctionId) throws DatabaseException {
        try {
            MongoCollection<Document> collection = getBidCollection();
            List<Bid> bids = new ArrayList<>();
            
            for (Document doc : collection.find(eq("auctionId", new ObjectId(auctionId))).sort(descending("bidTime"))) {
                bids.add(documentToBid(doc));
            }
            
            return bids;
        } catch (Exception e) {
            throw new DatabaseException("Failed to get bids by auction: " + e.getMessage(), e);
        }
    }
    
    @Override
    public List<Bid> getBidsByBidderId(String bidderId) throws DatabaseException {
        try {
            MongoCollection<Document> collection = getBidCollection();
            List<Bid> bids = new ArrayList<>();
            
            for (Document doc : collection.find(eq("bidderId", new ObjectId(bidderId))).sort(descending("bidTime"))) {
                bids.add(documentToBid(doc));
            }
            
            return bids;
        } catch (Exception e) {
            throw new DatabaseException("Failed to get bids by bidder: " + e.getMessage(), e);
        }
    }
    
    @Override
    public boolean endAuction(String auctionId) throws DatabaseException, AuctionException {
        try {
            Auction auction = findAuctionById(auctionId);
            
            if (auction == null) {
                throw new AuctionException("Auction not found");
            }
            
            if (auction.isCompleted()) {
                throw new AuctionException("Auction is already completed");
            }
            
            // Update auction status
            auction.setActive(false);
            auction.setCompleted(true);
            auction.setStatus("COMPLETED");
            
            updateAuction(auction);
            
            return true;
        } catch (AuctionException e) {
            throw e;
        } catch (Exception e) {
            throw new DatabaseException("Failed to end auction: " + e.getMessage(), e);
        }
    }
    
    @Override
    public void checkAndUpdateExpiredAuctions() throws DatabaseException {
        try {
            MongoCollection<Document> collection = getAuctionCollection();
            LocalDateTime now = LocalDateTime.now();
            
            // Find expired auctions
            for (Document doc : collection.find(and(eq("isActive", true), lt("endTime", now)))) {
                Auction auction = documentToAuction(doc);
                auction.setActive(false);
                auction.setCompleted(true);
                auction.setStatus("COMPLETED");
                updateAuction(auction);
            }
        } catch (Exception e) {
            throw new DatabaseException("Failed to check expired auctions: " + e.getMessage(), e);
        }
    }
    
    @Override
    public List<Auction> checkAndGetNewlyCompletedAuctions() throws DatabaseException {
        try {
            MongoCollection<Document> collection = getAuctionCollection();
            LocalDateTime now = LocalDateTime.now();
            List<Auction> newlyCompleted = new ArrayList<>();
            
            // Find expired auctions that are still active
            for (Document doc : collection.find(and(eq("isActive", true), lt("endTime", now)))) {
                Auction auction = documentToAuction(doc);
                newlyCompleted.add(auction);
                
                // Update auction status
                auction.setActive(false);
                auction.setCompleted(true);
                auction.setStatus("COMPLETED");
                updateAuction(auction);
            }
            
            return newlyCompleted;
        } catch (Exception e) {
            throw new DatabaseException("Failed to check expired auctions: " + e.getMessage(), e);
        }
    }
    
    /**
     * Converts Auction object to MongoDB Document
     */
    private Document auctionToDocument(Auction auction) {
        Document doc = new Document()
                .append("_id", auction.getId())
                .append("productId", auction.getProductId())
                .append("sellerId", auction.getSellerId())
                .append("startingPrice", auction.getStartingPrice())
                .append("currentHighestBid", auction.getCurrentHighestBid())
                .append("currentHighestBidderId", auction.getCurrentHighestBidderId())
                .append("isActive", auction.isActive())
                .append("isCompleted", auction.isCompleted())
                .append("status", auction.getStatus())
                .append("totalBids", auction.getTotalBids())
                .append("durationMinutes", auction.getDurationMinutes());
        
        // Convert LocalDateTime to Date for MongoDB storage
        if (auction.getStartTime() != null) {
            doc.append("startTime", java.util.Date.from(auction.getStartTime()
                .atZone(java.time.ZoneId.systemDefault()).toInstant()));
        }
        
        if (auction.getEndTime() != null) {
            doc.append("endTime", java.util.Date.from(auction.getEndTime()
                .atZone(java.time.ZoneId.systemDefault()).toInstant()));
        }
        
        if (auction.getCreatedAt() != null) {
            doc.append("createdAt", java.util.Date.from(auction.getCreatedAt()
                .atZone(java.time.ZoneId.systemDefault()).toInstant()));
        }
        
        return doc;
    }
    
    /**
     * Converts MongoDB Document to Auction object
     */
    private Auction documentToAuction(Document doc) {
        Auction auction = new Auction();
        
        auction.setId(doc.getObjectId("_id"));
        auction.setProductId(doc.getObjectId("productId"));
        auction.setSellerId(doc.getObjectId("sellerId"));
        auction.setStartingPrice(doc.getDouble("startingPrice"));
        auction.setCurrentHighestBid(doc.getDouble("currentHighestBid"));
        auction.setCurrentHighestBidderId(doc.getObjectId("currentHighestBidderId"));
        
        // Handle date conversion properly
        java.util.Date startTime = doc.getDate("startTime");
        if (startTime != null) {
            auction.setStartTime(startTime.toInstant()
                .atZone(java.time.ZoneId.systemDefault())
                .toLocalDateTime());
        }
        
        java.util.Date endTime = doc.getDate("endTime");
        if (endTime != null) {
            auction.setEndTime(endTime.toInstant()
                .atZone(java.time.ZoneId.systemDefault())
                .toLocalDateTime());
        }
        
        auction.setActive(doc.getBoolean("isActive", false));
        auction.setCompleted(doc.getBoolean("isCompleted", false));
        auction.setStatus(doc.getString("status"));
        
        java.util.Date createdAt = doc.getDate("createdAt");
        if (createdAt != null) {
            auction.setCreatedAt(createdAt.toInstant()
                .atZone(java.time.ZoneId.systemDefault())
                .toLocalDateTime());
        }
        
        auction.setTotalBids(doc.getInteger("totalBids", 0));
        
        // Handle durationMinutes with proper type conversion
        Object durationObj = doc.get("durationMinutes");
        if (durationObj instanceof Number) {
            auction.setDurationMinutes(((Number) durationObj).longValue());
        } else {
            auction.setDurationMinutes(60L); // Default to 60 minutes if not set
        }
        
        return auction;
    }
    
    /**
     * Converts Bid object to MongoDB Document
     */
    private Document bidToDocument(Bid bid) {
        Document doc = new Document()
                .append("_id", bid.getId())
                .append("auctionId", bid.getAuctionId())
                .append("bidderId", bid.getBidderId())
                .append("bidAmount", bid.getBidAmount())
                .append("isWinning", bid.isWinning())
                .append("bidderName", bid.getBidderName());
        
        // Convert LocalDateTime to Date for MongoDB storage
        if (bid.getBidTime() != null) {
            doc.append("bidTime", java.util.Date.from(bid.getBidTime()
                .atZone(java.time.ZoneId.systemDefault()).toInstant()));
        }
        
        return doc;
    }
    
    /**
     * Converts MongoDB Document to Bid object
     */
    private Bid documentToBid(Document doc) {
        Bid bid = new Bid();
        
        bid.setId(doc.getObjectId("_id"));
        bid.setAuctionId(doc.getObjectId("auctionId"));
        bid.setBidderId(doc.getObjectId("bidderId"));
        bid.setBidAmount(doc.getDouble("bidAmount"));
        
        // Handle date conversion properly
        java.util.Date bidTime = doc.getDate("bidTime");
        if (bidTime != null) {
            bid.setBidTime(bidTime.toInstant()
                .atZone(java.time.ZoneId.systemDefault())
                .toLocalDateTime());
        }
        
        bid.setWinning(doc.getBoolean("isWinning", false));
        bid.setBidderName(doc.getString("bidderName"));
        
        return bid;
    }
}
