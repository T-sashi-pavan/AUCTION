package com.auction.models;

import org.bson.types.ObjectId;
import java.time.LocalDateTime;

/**
 * Abstract base class for all users in the system
 * Demonstrates abstraction and inheritance principles
 */
public abstract class User {
    private ObjectId id;
    private String username;
    private String email;
    private String password;
    private String role;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private LocalDateTime registrationDate;
    private boolean isActive;
    
    // Default constructor
    public User() {
        this.id = new ObjectId();
        this.registrationDate = LocalDateTime.now();
        this.isActive = true;
    }
    
    // Constructor with parameters
    public User(String username, String email, String password, String role, 
                String firstName, String lastName, String phoneNumber) {
        this();
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
    }
    
    // Abstract methods that subclasses must implement
    public abstract void showDashboard();
    public abstract void handleActions();
    
    // Getters and setters (Encapsulation)
    public ObjectId getId() {
        return id;
    }
    
    public void setId(ObjectId id) {
        this.id = id;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getRole() {
        return role;
    }
    
    public void setRole(String role) {
        this.role = role;
    }
    
    public String getFirstName() {
        return firstName;
    }
    
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    
    public String getLastName() {
        return lastName;
    }
    
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    
    public String getPhoneNumber() {
        return phoneNumber;
    }
    
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    
    public LocalDateTime getRegistrationDate() {
        return registrationDate;
    }
    
    public void setRegistrationDate(LocalDateTime registrationDate) {
        this.registrationDate = registrationDate;
    }
    
    public boolean isActive() {
        return isActive;
    }
    
    public void setActive(boolean active) {
        isActive = active;
    }
    
    public String getFullName() {
        return firstName + " " + lastName;
    }
    
    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", role='" + role + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", registrationDate=" + registrationDate +
                ", isActive=" + isActive +
                '}';
    }
}
