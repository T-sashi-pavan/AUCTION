package com.auction.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * Utility class for input operations and formatting
 */
public class InputUtils {
    private static final Scanner scanner = new Scanner(System.in);
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    // Validation patterns
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
        "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$"
    );
    private static final Pattern PHONE_PATTERN = Pattern.compile(
        "^[+]?[0-9]{10,15}$"
    );
    private static final Pattern PASSWORD_PATTERN = Pattern.compile(
        "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$"
    );
    private static final Pattern NAME_PATTERN = Pattern.compile(
        "^[a-zA-Z\\s]{2,30}$"
    );
    private static final Pattern USERNAME_PATTERN = Pattern.compile(
        "^[a-zA-Z0-9_]{3,20}$"
    );
  
    public static String readRequiredString(String prompt, String fieldName) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            
            if (input.isEmpty()) {
                System.out.println("❌ " + fieldName + " cannot be empty. Please enter a valid " + fieldName.toLowerCase() + ".");
                continue;
            }
            
            return input;
        }
    }
    
    /**
     * Reads a simple string input (basic method for compatibility)
     */
    public static String readString(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }
    
    /**
     * Reads an integer input with validation
     * @param prompt the prompt message
     * @return user input as integer
     */
    public static int readInt(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                String input = scanner.nextLine().trim();
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid number format. Please enter a valid integer.");
            }
        }
    }
    
    /**
     * Reads a double input with validation
     * @param prompt the prompt message
     * @return user input as double
     */
    public static double readDouble(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                String input = scanner.nextLine().trim();
                return Double.parseDouble(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid number format. Please enter a valid decimal number.");
            }
        }
    }
    
    /**
     * Reads a validated username
     * @param prompt the prompt message
     * @return validated username
     */
    public static String readUsername(String prompt) {
        while (true) {
            System.out.print(prompt);
            String username = scanner.nextLine().trim();
            
            if (username.isEmpty()) {
                System.out.println("Username cannot be empty.");
                continue;
            }
            
            if (!USERNAME_PATTERN.matcher(username).matches()) {
                System.out.println("Username must be 3-20 characters long and contain only letters, numbers, and underscores.");
                continue;
            }
            
            return username;
        }
    }
    
    /**
     * Reads a validated email address
     * @param prompt the prompt message
     * @return validated email address
     */
    public static String readEmail(String prompt) {
        while (true) {
            System.out.print(prompt);
            String email = scanner.nextLine().trim();
            
            if (email.isEmpty()) {
                System.out.println("Email cannot be empty.");
                continue;
            }
            
            if (!EMAIL_PATTERN.matcher(email).matches()) {
                System.out.println("Please enter a valid email address (e.g., user@example.com).");
                continue;
            }
            
            return email;
        }
    }
    
    /**
     * Reads a validated password
     * @param prompt the prompt message
     * @return validated password
     */
    public static String readPassword(String prompt) {
        while (true) {
            System.out.print(prompt);
            String password = scanner.nextLine().trim();
            
            if (password.isEmpty()) {
                System.out.println("Password cannot be empty.");
                continue;
            }
            
            if (password.length() < 8) {
                System.out.println("Password must be at least 8 characters long.");
                continue;
            }
            
            if (!PASSWORD_PATTERN.matcher(password).matches()) {
                System.out.println(" Password must contain:");
                System.out.println("   • At least one uppercase letter (A-Z)");
                System.out.println("   • At least one lowercase letter (a-z)");
                System.out.println("   • At least one digit (0-9)");
                System.out.println("   • At least one special character (@$!%*?&)");
                System.out.println("   • Minimum 8 characters");
                continue;
            }
            
            return password;
        }
    }
    
    /**
     * Reads password confirmation
     * @param originalPassword the original password to match
     * @return confirmed password
     */
    public static String readPasswordConfirmation(String originalPassword) {
        while (true) {
            System.out.print("Confirm Password: ");
            String confirmPassword = scanner.nextLine().trim();
            
            if (confirmPassword.isEmpty()) {
                System.out.println(" Password confirmation cannot be empty.");
                continue;
            }
            
            if (!confirmPassword.equals(originalPassword)) {
                System.out.println("Passwords do not match. Please try again.");
                continue;
            }
            
            return confirmPassword;
        }
    }
    
    /**
     * Reads a validated name (first name or last name)
     * @param prompt the prompt message
     * @param fieldName the name of the field
     * @return validated name
     */
    public static String readName(String prompt, String fieldName) {
        while (true) {
            System.out.print(prompt);
            String name = scanner.nextLine().trim();
            
            if (name.isEmpty()) {
                System.out.println(" " + fieldName + " cannot be empty.");
                continue;
            }
            
            if (!NAME_PATTERN.matcher(name).matches()) {
                System.out.println(" " + fieldName + " must be 2-30 characters long and contain only letters and spaces.");
                continue;
            }
            
            return name;
        }
    }
    
    /**
     * Reads a validated phone number
     * @param prompt the prompt message
     * @return validated phone number
     */
    public static String readPhoneNumber(String prompt) {
        while (true) {
            System.out.print(prompt);
            String phone = scanner.nextLine().trim();
            
            if (phone.isEmpty()) {
                System.out.println(" Phone number cannot be empty.");
                continue;
            }
            
            // Remove spaces and dashes for validation
            String cleanPhone = phone.replaceAll("[\\s-]", "");
            
            if (!PHONE_PATTERN.matcher(cleanPhone).matches()) {
                System.out.println(" Please enter a valid phone number (10-15 digits, optionally starting with +).");
                System.out.println("   Examples: +1234567890, 1234567890, +91 98765 43210");
                continue;
            }
            
            return cleanPhone;
        }
    }
    
    /**
     * Reads multiple product selections (e.g., "1,3,4" or "1 3 4")
     * @param prompt the prompt message
     * @param maxProducts the maximum number of products available
     * @return list of selected product indices
     */
    public static List<Integer> readMultipleProductSelections(String prompt, int maxProducts) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            
            if (input.isEmpty()) {
                System.out.println("❌ Please enter at least one product selection.");
                continue;
            }
            
            try {
                List<Integer> selections = new ArrayList<>();
                
                // Split by both comma and space, handling mixed separators
                String[] parts = input.split("[,\\s]+");
                
                for (String part : parts) {
                    if (part.trim().isEmpty()) continue; // Skip empty parts
                    
                    int productIndex = Integer.parseInt(part.trim());
                    
                    if (productIndex < 1 || productIndex > maxProducts) {
                        System.out.println("❌ Product selection " + productIndex + " is out of range (1-" + maxProducts + ").");
                        selections.clear();
                        break;
                    }
                    
                    if (selections.contains(productIndex)) {
                        System.out.println("❌ Product " + productIndex + " is selected multiple times.");
                        selections.clear();
                        break;
                    }
                    
                    selections.add(productIndex);
                }
                
                if (!selections.isEmpty()) {
                    return selections;
                }
                
            } catch (NumberFormatException e) {
                System.out.println("❌ Invalid input format. Please enter numbers separated by commas or spaces (e.g., 1,3,4 or 1 3 4).");
            }
        }
    }
    
    /**
     * Formats LocalDateTime to string
     * @param dateTime the LocalDateTime object
     * @return formatted date string
     */
    public static String formatDateTime(LocalDateTime dateTime) {
        return dateTime.format(formatter);
    }
    
    /**
     * Parses string to LocalDateTime
     * @param dateString the date string
     * @return LocalDateTime object
     */
    public static LocalDateTime parseDateTime(String dateString) {
        return LocalDateTime.parse(dateString, formatter);
    }
    
    /**
     * Clears the console screen
     */
    public static void clearScreen() {
        try {
            if (System.getProperty("os.name").contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                System.out.print("\033[2J\033[H");
            }
        } catch (Exception e) {
            // If clearing fails, just print empty lines
            for (int i = 0; i < 50; i++) {
                System.out.println();
            }
        }
    }
    
    /**
     * Pauses execution until user presses Enter
     */
    public static void pause() {
        System.out.print("\nPress Enter to continue...");
        scanner.nextLine();
    }
}
