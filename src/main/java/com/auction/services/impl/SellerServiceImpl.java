package com.auction.services.impl;

import java.util.List;

import com.auction.exceptions.DatabaseException;
import com.auction.models.Product;
import com.auction.models.Seller;
import com.auction.models.Transaction;
import com.auction.services.ProductService;
import com.auction.services.SellerService;
import com.auction.services.TransactionService;
import com.auction.services.UserService;
import com.auction.utils.InputUtils;

/**
 * Implementation of SellerService interface
 */
public class SellerServiceImpl implements SellerService {
    
    private final ProductService productService;
    private final TransactionService transactionService;
    private final UserService userService;
    
    public SellerServiceImpl() {
        this.productService = new ProductServiceImpl();
        this.transactionService = new TransactionServiceImpl();
        this.userService = new UserServiceImpl();
    }
    
    @Override
    public void addNewProduct(Seller seller) throws DatabaseException {
        try {
            System.out.println("\n=== ADD NEW PRODUCT ===");
            
            String name = InputUtils.readString("Enter product name: ");
            String description = InputUtils.readString("Enter product description: ");
            
            // Display categories
            System.out.println("\nAvailable categories:");
            System.out.println("1. Electronics");
            System.out.println("2. Clothing");
            System.out.println("3. Books");
            System.out.println("4. Home & Garden");
            System.out.println("5. Sports");
            System.out.println("6. Automotive");
            System.out.println("7. Jewelry");
            System.out.println("8. Art & Collectibles");
            System.out.println("9. Other");
            
            int categoryChoice = InputUtils.readInt("Select category (1-9): ");
            String category = getCategoryByChoice(categoryChoice);
            
            double price = InputUtils.readDouble("Enter price: $");
            
            // Display conditions
            System.out.println("\nCondition:");
            System.out.println("1. New");
            System.out.println("2. Used");
            System.out.println("3. Refurbished");
            
            int conditionChoice = InputUtils.readInt("Select condition (1-3): ");
            String condition = getConditionByChoice(conditionChoice);
            
            int quantity = InputUtils.readInt("Enter quantity: ");
            
            // Create product
            Product product = new Product(name, description, category, price, seller.getId(), condition, quantity);
            
            if (productService.saveProduct(product)) {
                // Update seller's product list
                seller.addProductId(product.getId());
                userService.updateUser(seller);
                
                System.out.println("Product added successfully!");
                System.out.println("Product ID: " + product.getId());
                System.out.println("Name: " + product.getName());
                System.out.println("Category: " + product.getCategory());
                System.out.println("Price: $" + product.getPrice());
                System.out.println("Condition: " + product.getCondition());
                System.out.println("Quantity: " + product.getQuantity());
            } else {
                System.out.println("Failed to add product.");
            }
            
        } catch (Exception e) {
            throw new DatabaseException("Failed to add new product: " + e.getMessage(), e);
        }
    }
    
    @Override
    public void viewMyProducts(Seller seller) throws DatabaseException {
        try {
            List<Product> products = productService.getProductsBySellerId(seller.getId().toString());
            
            System.out.println("\n=== MY PRODUCTS ===");
            
            if (products.isEmpty()) {
                System.out.println("No products found.");
                return;
            }
            
            System.out.printf("%-15s %-20s %-15s %-16s %-18s %-12s %-18s %-20s%n", 
                "ID", "Name", "Category", "Price", "Condition", "Quantity", "Status", "Date Added");
            System.out.println("=" .repeat(140));
            
            for (Product product : products) {
                String status = product.isSold() ? "SOLD" : (product.isAvailable() ? "AVAILABLE" : "UNAVAILABLE");
                System.out.printf("%-15s %-20s %-15s $%-15.2f %-18s %-12d %-18s %-20s%n",
                    product.getId().toString().substring(0, 8) + "...",
                    product.getName().length() > 18 ? product.getName().substring(0, 15) + "..." : product.getName(),
                    product.getCategory(),
                    product.getPrice(),
                    product.getCondition(),
                    product.getQuantity(),
                    status,
                    InputUtils.formatDateTime(product.getDateAdded()));
            }
            
            System.out.println("\nTotal Products: " + products.size());
            
        } catch (Exception e) {
            throw new DatabaseException("Failed to view products: " + e.getMessage(), e);
        }
    }
    
    @Override
    public void removeProduct(Seller seller) throws DatabaseException {
        try {
            List<Product> products = productService.getProductsBySellerId(seller.getId().toString());
            
            if (products.isEmpty()) {
                System.out.println("No products to remove.");
                return;
            }
            
            System.out.println("\n=== REMOVE PRODUCT ===");
            System.out.println("Your Products:");
            System.out.println("No.\tID\t\tName\t\t\tCategory\t\tPrice\t\tStatus");
            System.out.println("=" .repeat(100));
            
            for (int i = 0; i < products.size(); i++) {
                Product product = products.get(i);
                String status = product.isSold() ? "SOLD" : (product.isAvailable() ? "AVAILABLE" : "UNAVAILABLE");
                System.out.printf("%d.\t%-15s %-20s %-15s $%-10.2f %s%n",
                    i + 1,
                    product.getId().toString().substring(0, 8) + "...",
                    product.getName().length() > 20 ? product.getName().substring(0, 17) + "..." : product.getName(),
                    product.getCategory(),
                    product.getPrice(),
                    status);
            }
            
            int choice = InputUtils.readInt("Select product to remove (1-" + products.size() + "): ");
            
            if (choice < 1 || choice > products.size()) {
                System.out.println("Invalid selection.");
                return;
            }
            
            Product selectedProduct = products.get(choice - 1);
            
            if (selectedProduct.isSold()) {
                System.out.println("Cannot remove sold product.");
                return;
            }
            
            String confirm = InputUtils.readString("Are you sure you want to remove '" + selectedProduct.getName() + "'? (y/n): ");
            
            if (confirm.equalsIgnoreCase("y") || confirm.equalsIgnoreCase("yes")) {
                if (productService.deleteProduct(selectedProduct.getId().toString())) {
                    // Update seller's product list
                    seller.removeProductId(selectedProduct.getId());
                    userService.updateUser(seller);
                    
                    System.out.println("Product removed successfully!");
                } else {
                    System.out.println("Failed to remove product.");
                }
            } else {
                System.out.println("Product removal cancelled.");
            }
            
        } catch (Exception e) {
            throw new DatabaseException("Failed to remove product: " + e.getMessage(), e);
        }
    }
    
    @Override
    public void viewSoldProducts(Seller seller) throws DatabaseException {
        try {
            List<Product> allProducts = productService.getProductsBySellerId(seller.getId().toString());
            List<Product> soldProducts = allProducts.stream()
                    .filter(Product::isSold)
                    .collect(java.util.stream.Collectors.toList());
            
            System.out.println("\n=== SOLD PRODUCTS ===");
            
            if (soldProducts.isEmpty()) {
                System.out.println("No sold products found.");
                return;
            }
            
            System.out.println("ID\t\tName\t\t\tCategory\t\tPrice\t\tDate Sold");
            System.out.println("=" .repeat(100));
            
            for (Product product : soldProducts) {
                System.out.printf("%-15s %-20s %-15s $%-10.2f %s%n",
                    product.getId().toString().substring(0, 8) + "...",
                    product.getName().length() > 20 ? product.getName().substring(0, 17) + "..." : product.getName(),
                    product.getCategory(),
                    product.getPrice(),
                    product.getDateSold() != null ? InputUtils.formatDateTime(product.getDateSold()) : "Unknown");
            }
            
            System.out.println("\nTotal Sold Products: " + soldProducts.size());
            
        } catch (Exception e) {
            throw new DatabaseException("Failed to view sold products: " + e.getMessage(), e);
        }
    }
    
    @Override
    public void viewTransactionHistory(Seller seller) throws DatabaseException {
        try {
            List<Transaction> transactions = transactionService.getTransactionsBySellerId(seller.getId().toString());
            
            System.out.println("\n=== TRANSACTION HISTORY ===");
            
            if (transactions.isEmpty()) {
                System.out.println("No transactions found.");
                return;
            }
            
            System.out.println("ID\t\tBuyer\t\tProduct\t\tAmount\t\tType\t\tStatus\t\tDate");
            System.out.println("=" .repeat(120));
            
            for (Transaction transaction : transactions) {
                System.out.printf("%-15s %-15s %-15s $%-10.2f %-15s %-15s %s%n",
                    transaction.getId().toString().substring(0, 8) + "...",
                    transaction.getBuyerName() != null ? transaction.getBuyerName() : "Unknown",
                    transaction.getProductName() != null ? transaction.getProductName().substring(0, Math.min(15, transaction.getProductName().length())) : "Unknown",
                    transaction.getAmount(),
                    transaction.getTransactionType(),
                    transaction.getStatus(),
                    InputUtils.formatDateTime(transaction.getTransactionDate()));
            }
            
            System.out.println("\nTotal Transactions: " + transactions.size());
            
            // Calculate total earnings
            double totalEarnings = transactions.stream()
                    .filter(t -> "COMPLETED".equals(t.getStatus()))
                    .mapToDouble(Transaction::getAmount)
                    .sum();
            
            System.out.println("Total Earnings: $" + String.format("%.2f", totalEarnings));
            
        } catch (Exception e) {
            throw new DatabaseException("Failed to view transaction history: " + e.getMessage(), e);
        }
    }
    
    @Override
    public void updatePersonalDetails(Seller seller) throws DatabaseException {
        try {
            System.out.println("\n=== UPDATE PERSONAL DETAILS ===");
            System.out.println("Current Details:");
            System.out.println("Name: " + seller.getFullName());
            System.out.println("Email: " + seller.getEmail());
            System.out.println("Phone: " + seller.getPhoneNumber());
            
            System.out.println("\nWhat would you like to update?");
            System.out.println("1. First Name");
            System.out.println("2. Last Name");
            System.out.println("3. Email");
            System.out.println("4. Phone Number");
            System.out.println("5. Password");
            System.out.println("0. Cancel");
            
            int choice = InputUtils.readInt("Enter your choice: ");
            
            switch (choice) {
                case 1:
                    String firstName = InputUtils.readString("Enter new first name: ");
                    seller.setFirstName(firstName);
                    break;
                case 2:
                    String lastName = InputUtils.readString("Enter new last name: ");
                    seller.setLastName(lastName);
                    break;
                case 3:
                    String email = InputUtils.readString("Enter new email: ");
                    if (userService.emailExists(email)) {
                        System.out.println("Email already exists. Please choose a different email.");
                        return;
                    }
                    seller.setEmail(email);
                    break;
                case 4:
                    String phone = InputUtils.readString("Enter new phone number: ");
                    seller.setPhoneNumber(phone);
                    break;
                case 5:
                    String oldPassword = InputUtils.readString("Enter current password: ");
                    if (!com.auction.utils.PasswordUtils.verifyPassword(oldPassword, seller.getPassword())) {
                        System.out.println("Current password is incorrect.");
                        return;
                    }
                    String newPassword = InputUtils.readString("Enter new password: ");
                    String confirmPassword = InputUtils.readString("Confirm new password: ");
                    if (!newPassword.equals(confirmPassword)) {
                        System.out.println("Passwords do not match.");
                        return;
                    }
                    seller.setPassword(com.auction.utils.PasswordUtils.encryptPassword(newPassword));
                    break;
                case 0:
                    System.out.println("Update cancelled.");
                    return;
                default:
                    System.out.println("Invalid choice.");
                    return;
            }
            
            if (userService.updateUser(seller)) {
                System.out.println("Personal details updated successfully!");
            } else {
                System.out.println("Failed to update personal details.");
            }
            
        } catch (Exception e) {
            throw new DatabaseException("Failed to update personal details: " + e.getMessage(), e);
        }
    }
    
    @Override
    public void viewProfileSummary(Seller seller) throws DatabaseException {
        try {
            System.out.println("\n=== PROFILE SUMMARY ===");
            System.out.println("Name: " + seller.getFullName());
            System.out.println("Username: " + seller.getUsername());
            System.out.println("Email: " + seller.getEmail());
            System.out.println("Phone: " + seller.getPhoneNumber());
            System.out.println("Registration Date: " + InputUtils.formatDateTime(seller.getRegistrationDate()));
            System.out.println("Account Status: " + (seller.isActive() ? "Active" : "Inactive"));
            
            // Get statistics
            List<Product> products = productService.getProductsBySellerId(seller.getId().toString());
            List<Transaction> transactions = transactionService.getTransactionsBySellerId(seller.getId().toString());
            
            long soldProducts = products.stream().filter(Product::isSold).count();
            long availableProducts = products.stream().filter(Product::isAvailable).count();
            
            double totalEarnings = transactions.stream()
                    .filter(t -> "COMPLETED".equals(t.getStatus()))
                    .mapToDouble(Transaction::getAmount)
                    .sum();
            
            System.out.println("\n=== SELLING STATISTICS ===");
            System.out.println("Total Products Listed: " + products.size());
            System.out.println("Products Sold: " + soldProducts);
            System.out.println("Available Products: " + availableProducts);
            System.out.println("Total Transactions: " + transactions.size());
            System.out.println("Total Earnings: $" + String.format("%.2f", totalEarnings));
            System.out.println("Average Sale Price: $" + String.format("%.2f", 
                    soldProducts > 0 ? totalEarnings / soldProducts : 0));
            System.out.println("Rating: " + String.format("%.1f", seller.getRating()) + "/5.0 (" + seller.getTotalRatings() + " ratings)");
            
        } catch (Exception e) {
            throw new DatabaseException("Failed to view profile summary: " + e.getMessage(), e);
        }
    }
    
    @Override
    public boolean deleteAccount(Seller seller) throws DatabaseException {
        try {
            System.out.println("\n=== DELETE ACCOUNT ===");
            System.out.println("WARNING: This action cannot be undone!");
            System.out.println("All your products and transaction history will be removed.");
            
            String confirm1 = InputUtils.readString("Are you sure you want to delete your account? (yes/no): ");
            
            if (!confirm1.equalsIgnoreCase("yes")) {
                System.out.println("Account deletion cancelled.");
                return false;
            }
            
            String confirm2 = InputUtils.readString("Type 'DELETE' to confirm: ");
            
            if (!confirm2.equals("DELETE")) {
                System.out.println("Account deletion cancelled.");
                return false;
            }
            
            String password = InputUtils.readString("Enter your password to confirm: ");
            
            if (!com.auction.utils.PasswordUtils.verifyPassword(password, seller.getPassword())) {
                System.out.println("Incorrect password. Account deletion cancelled.");
                return false;
            }
            
            // Delete all products
            List<Product> products = productService.getProductsBySellerId(seller.getId().toString());
            for (Product product : products) {
                if (!product.isSold()) {
                    productService.deleteProduct(product.getId().toString());
                }
            }
            
            // Delete user account
            if (userService.deleteUser(seller.getId().toString())) {
                System.out.println("Account deleted successfully!");
                return true;
            } else {
                System.out.println("Failed to delete account.");
                return false;
            }
            
        } catch (Exception e) {
            throw new DatabaseException("Failed to delete account: " + e.getMessage(), e);
        }
    }
    
    @Override
    public List<Product> getProductsBySellerId(String sellerId) throws DatabaseException {
        return productService.getProductsBySellerId(sellerId);
    }
    
    @Override
    public List<Transaction> getTransactionsBySellerId(String sellerId) throws DatabaseException {
        return transactionService.getTransactionsBySellerId(sellerId);
    }
    
    // Helper methods
    private String getCategoryByChoice(int choice) {
        switch (choice) {
            case 1: return "Electronics";
            case 2: return "Clothing";
            case 3: return "Books";
            case 4: return "Home & Garden";
            case 5: return "Sports";
            case 6: return "Automotive";
            case 7: return "Jewelry";
            case 8: return "Art & Collectibles";
            case 9: return "Other";
            default: return "Other";
        }
    }
    
    private String getConditionByChoice(int choice) {
        switch (choice) {
            case 1: return "New";
            case 2: return "Used";
            case 3: return "Refurbished";
            default: return "Used";
        }
    }
}
