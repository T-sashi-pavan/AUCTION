# Quick Testing Guide for Git Bash (MINGW64)

## Starting the Application

Since you're using Git Bash on Windows, use these commands:

```bash
# In Git Bash (MINGW64)
mvn clean compile
mvn exec:java -Dexec.mainClass="com.auction.AuctionSystemApp"
```

## Step-by-Step Testing

### 1. First Run - Test Registration
The application is now running. Follow these steps:

#### Register a Seller:
1. Choose option `2` (Register)
2. Select `1` (Seller) 
3. Enter details:
   - Username: `seller1`
   - Email: `seller1@test.com`
   - Password: `password123`
   - Confirm Password: `password123`
   - First Name: `John`
   - Last Name: `Seller`
   - Phone: `1234567890`

#### Register a Buyer:
1. After seller registration, logout (option `0`)
2. Choose option `2` (Register)
3. Select `2` (Buyer)
4. Enter details:
   - Username: `buyer1`
   - Email: `buyer1@test.com`
   - Password: `password123`
   - Confirm Password: `password123`
   - First Name: `Jane`
   - Last Name: `Buyer`
   - Phone: `0987654321`

### 2. Test Admin Features
1. Logout and login as admin:
   - Username: `admin`
   - Password: `admin123`
2. Test each option 1-10 in the admin dashboard

### 3. Test Seller Features
1. Login as seller1:
   - Username: `seller1`
   - Password: `password123`
2. Add products:
   - Option 1: Add products (Laptop, Book, Phone)
   - Test other seller options

### 4. Test Buyer Features
1. Login as buyer1:
   - Username: `buyer1`
   - Password: `password123`
2. Browse and purchase products
3. Test auction features

## MongoDB Connection

The application will try to connect to MongoDB at `mongodb://localhost:27017`. 

If MongoDB is not running, you might see connection errors. To start MongoDB:

### Option 1: Start MongoDB Service (as Administrator)
Open Command Prompt as Administrator and run:
```cmd
net start MongoDB
```

### Option 2: Start MongoDB Manually
```bash
mongod --dbpath "C:\data\db"
```

### Option 3: Use MongoDB Atlas
Update the connection string in:
`src/main/java/com/auction/database/DatabaseConnection.java`

## Testing Without MongoDB
If you can't start MongoDB, the application will show database connection errors, but you can still test the UI flow and see how the application handles database failures.

## Quick Commands Reference

```bash
# Compile and run
mvn clean compile exec:java -Dexec.mainClass="com.auction.AuctionSystemApp"

# Run tests
mvn exec:java -Dexec.mainClass="com.auction.test.BasicFunctionalityTest"

# Check MongoDB status
mongosh --eval "db.runCommand({ping: 1})"

# Start MongoDB (as admin)
net start MongoDB
```

The application is now ready for testing! Follow the phases in TESTING_GUIDE.md for comprehensive testing.
