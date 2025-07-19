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
   
    User authenticateUser(String username, String password) throws AuthenticationException;
    
   
    boolean updateUser(User user) throws DatabaseException;
    
  
    boolean deleteUser(String userId) throws DatabaseException;
    
  
    User findUserById(String userId) throws DatabaseException;
    
   
    User findUserByUsername(String username) throws DatabaseException;
    boolean usernameExists(String username) throws DatabaseException;
    
   
    boolean emailExists(String email) throws DatabaseException;
    
   
    List<User> getAllUsers() throws DatabaseException;
}
