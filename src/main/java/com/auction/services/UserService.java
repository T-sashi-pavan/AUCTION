package com.auction.services;

import com.auction.models.User;
import com.auction.exceptions.AuthenticationException;
import com.auction.exceptions.DatabaseException;
import java.util.List;

/**
 * Interface for user authentication and management services
 */
public interface UserService {
    
    /**
     * Registers a new user
     * @param user the user to register
     * @return true if registration successful, false otherwise
     * @throws DatabaseException if database operation fails
     */
    boolean registerUser(User user) throws DatabaseException;
    
    /**
     * Authenticates a user with username and password
     * @param username the username
     * @param password the password
     * @return authenticated user object
     * @throws AuthenticationException if authentication fails
     */
    User authenticateUser(String username, String password) throws AuthenticationException;
    
    /**
     * Updates user information
     * @param user the user to update
     * @return true if update successful, false otherwise
     * @throws DatabaseException if database operation fails
     */
    boolean updateUser(User user) throws DatabaseException;
    
    /**
     * Deletes a user account
     * @param userId the user ID to delete
     * @return true if deletion successful, false otherwise
     * @throws DatabaseException if database operation fails
     */
    boolean deleteUser(String userId) throws DatabaseException;
    
    /**
     * Finds a user by ID
     * @param userId the user ID
     * @return user object if found, null otherwise
     * @throws DatabaseException if database operation fails
     */
    User findUserById(String userId) throws DatabaseException;
    
    /**
     * Finds a user by username
     * @param username the username
     * @return user object if found, null otherwise
     * @throws DatabaseException if database operation fails
     */
    User findUserByUsername(String username) throws DatabaseException;
    
    /**
     * Checks if username exists
     * @param username the username to check
     * @return true if exists, false otherwise
     * @throws DatabaseException if database operation fails
     */
    boolean usernameExists(String username) throws DatabaseException;
    
    /**
     * Checks if email exists
     * @param email the email to check
     * @return true if exists, false otherwise
     * @throws DatabaseException if database operation fails
     */
    boolean emailExists(String email) throws DatabaseException;
    
    /**
     * Gets all users
     * @return list of all users
     * @throws DatabaseException if database operation fails
     */
    List<User> getAllUsers() throws DatabaseException;
}
