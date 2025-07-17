package com.auction;

import com.auction.ui.AuthenticationUI;

/**
 * Main application class for the Automated Auction System
 * Entry point of the application
 */
public class AuctionSystemApp {
    
    public static void main(String[] args) {
        try {
            System.out.println("Starting Automated Auction System...");
            
            // Create authentication UI
            AuthenticationUI authUI = new AuthenticationUI();
            
            // Create default admin account if it doesn't exist
            authUI.createDefaultAdmin();
            
            // Start the application
            authUI.start();
            
        } catch (Exception e) {
            System.err.println("Application failed to start: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
