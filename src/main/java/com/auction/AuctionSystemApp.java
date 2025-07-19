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
            
            AuthenticationUI authUI = new AuthenticationUI();
            
            // Start the authentication UI - it will handle admin setup automatically
            authUI.start();
            
        } catch (Exception e) {
            System.err.println("Application failed to start: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
