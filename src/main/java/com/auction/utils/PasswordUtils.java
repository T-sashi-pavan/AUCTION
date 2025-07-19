package com.auction.utils;

import org.mindrot.jbcrypt.BCrypt;

/**
 * Utility class for password encryption and verification
 */
public class PasswordUtils {
    

    public static String encryptPassword(String plainPassword) {
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt());
    }
    
    /**
     * Verifies a plain text password against encrypted password
     * @param plainPassword the plain text password
     * @param encryptedPassword the encrypted password
     * @return true if passwords match, false otherwise
     */
    public static boolean verifyPassword(String plainPassword, String encryptedPassword) {
        return BCrypt.checkpw(plainPassword, encryptedPassword);
    }
}
