package com.auction.test;

import java.util.List;

import com.auction.exceptions.DatabaseException;
import com.auction.models.Product;
import com.auction.services.impl.ProductServiceImpl;

/**
 * Test to debug the sold products issue
 */
public class SoldProductsDebugTest {
    
    public static void main(String[] args) {
        ProductServiceImpl productService = new ProductServiceImpl();
        
        try {
            System.out.println("=== DEBUGGING SOLD PRODUCTS ISSUE ===\n");
            
            // Get all products by a specific seller (using the seller ID from the screenshots)
            // Based on the dashboard showing "Products Sold: 2", let's find that seller
            List<Product> allProducts = productService.getAllProducts();
            
            System.out.println("All products in database:");
            System.out.println("ID\t\t\t\tName\t\t\tSeller ID\t\t\tisSold\tisAvailable");
            System.out.println("=" .repeat(100));
            
            for (Product product : allProducts) {
                System.out.printf("%-24s %-20s %-24s %-8s %-8s%n",
                    product.getId().toString(),
                    product.getName().length() > 20 ? product.getName().substring(0, 17) + "..." : product.getName(),
                    product.getSellerId().toString(),
                    product.isSold(),
                    product.isAvailable());
            }
            
            System.out.println("\n=== PRODUCTS BY SELLER ===");
            
            // Group products by seller
            allProducts.stream()
                .collect(java.util.stream.Collectors.groupingBy(Product::getSellerId))
                .forEach((sellerId, products) -> {
                    long soldCount = products.stream().filter(Product::isSold).count();
                    System.out.println("\nSeller ID: " + sellerId);
                    System.out.println("Total products: " + products.size());
                    System.out.println("Sold products: " + soldCount);
                    System.out.println("Available products: " + products.stream().filter(Product::isAvailable).count());
                    
                    if (soldCount > 0) {
                        System.out.println("Sold products details:");
                        products.stream()
                            .filter(Product::isSold)
                            .forEach(p -> System.out.println("  - " + p.getName() + " (ID: " + p.getId() + ")"));
                    }
                });
            
            System.out.println("\n=== USING getSoldProducts() METHOD ===");
            List<Product> soldProducts = productService.getSoldProducts();
            System.out.println("Total sold products from getSoldProducts(): " + soldProducts.size());
            
            for (Product product : soldProducts) {
                System.out.printf("%-24s %-20s %-24s%n",
                    product.getId().toString(),
                    product.getName(),
                    product.getSellerId().toString());
            }
            
        } catch (DatabaseException e) {
            System.err.println("Database error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
