# How to Test All Features - Automated Auction System

## Quick Start Guide

### 1. Prerequisites Setup
```bash
# Check if you have Java 11+ and Maven installed
java -version
mvn -version

# Install MongoDB locally (if not using Atlas)
# Windows: Download from https://www.mongodb.com/try/download/community
# Linux: sudo apt-get install mongodb
# macOS: brew install mongodb-community
```

### 2. Database Setup
```bash
# Windows
setup_mongodb.bat

# Linux/Mac
./setup_mongodb.sh
```

### 3. Run the Application
```bash
# Windows
run.bat

# Linux/Mac
./run.sh

# Or manually
mvn clean compile exec:java -Dexec.mainClass="com.auction.AuctionSystemApp"
```

### 4. Quick Functionality Test
```bash
# Test basic functionality
mvn exec:java -Dexec.mainClass="com.auction.test.BasicFunctionalityTest"
```

## Complete Feature Testing Walkthrough

### Phase 1: Initial Setup (5 minutes)
1. **Start Application**
   - Run the application
   - Verify database connection
   - Check default admin creation

2. **Test Registration**
   - Register as Seller: `seller1 / password123`
   - Register as Buyer: `buyer1 / password123`
   - Test duplicate username/email prevention

### Phase 2: Admin Features (10 minutes)
**Login as Admin: `admin / admin123`**

1. **User Management**
   - View Registered Users → Should show seller1, buyer1
   - Verify user roles and details

2. **Product Management**
   - View All Products → Initially empty
   - Check after sellers add products

3. **System Monitoring**
   - System Statistics → User counts, system health
   - View Items Status → Track sold/unsold items

### Phase 3: Seller Features (15 minutes)
**Login as Seller: `seller1 / password123`**

1. **Product Management**
   ```
   Add Products:
   - Laptop | Electronics | $1000 | New | Gaming laptop
   - Book | Books | $50 | Used | Java programming guide
   - Phone | Electronics | $800 | New | Latest smartphone
   ```

2. **Sales Management**
   - View inventory
   - Check sold products (after purchases)
   - Monitor transaction history

3. **Profile Management**
   - Update personal details
   - View profile summary
   - Check earnings and statistics

### Phase 4: Buyer Features (20 minutes)
**Login as Buyer: `buyer1 / password123`**

1. **Product Browsing**
   - Browse by Category → Electronics, Books
   - View All Sellers → See seller1's products
   - Search and filter products

2. **Purchase Testing**
   ```
   Purchase Flow:
   1. Select "Laptop" from seller1
   2. Complete purchase transaction
   3. Verify transaction recorded
   4. Check purchase history
   ```

3. **Auction Features**
   - View ongoing auctions (after admin creates them)
   - Place bids on auctions
   - Monitor auction results

4. **Customer Service**
   - Request refund for purchased item
   - View refund status
   - Check transaction history

### Phase 5: Advanced Features (15 minutes)

1. **Auction System**
   - **As Admin**: Create auction for seller's product
   - **As Buyer**: Place multiple bids
   - **Monitor**: Real-time auction updates
   - **Verify**: Winner determination and transaction

2. **Transaction Management**
   - **As Admin**: View all transactions
   - **Search**: By date range, transaction ID
   - **Filter**: By user, product, status
   - **Refund**: Process refund requests

3. **Reporting and Analytics**
   - System statistics
   - User activity reports
   - Sales performance
   - Auction analytics

## Testing Checklist

### ✅ Core Functionality
- [ ] Database connection
- [ ] User authentication
- [ ] Password encryption
- [ ] Role-based access

### ✅ Admin Features
- [ ] User management
- [ ] Product oversight
- [ ] Auction creation
- [ ] Transaction monitoring
- [ ] System statistics

### ✅ Seller Features
- [ ] Product CRUD operations
- [ ] Sales tracking
- [ ] Profile management
- [ ] Earnings monitoring

### ✅ Buyer Features
- [ ] Product browsing
- [ ] Purchase transactions
- [ ] Auction participation
- [ ] Refund requests
- [ ] Order history

### ✅ Error Handling
- [ ] Invalid login attempts
- [ ] Duplicate registrations
- [ ] Database connectivity issues
- [ ] Invalid input handling

## Sample Test Scenarios

### Scenario 1: Complete Purchase Flow
```
1. Seller adds "Laptop" for $1000
2. Buyer browses products
3. Buyer purchases laptop
4. Transaction recorded
5. Seller sees sold product
6. Buyer sees purchase history
7. Admin views transaction
```

### Scenario 2: Auction Flow
```
1. Admin creates auction for seller's product
2. Multiple buyers place bids
3. Auction ends (time-based)
4. Highest bidder wins
5. Transaction automatically created
6. Product transferred to winner
```

### Scenario 3: Refund Process
```
1. Buyer requests refund for purchase
2. Admin reviews refund request
3. Admin approves/denies refund
4. Transaction updated
5. Buyer notified of status
```

## Performance Testing

### Load Testing
1. **Create 50+ users** of each type
2. **Add 100+ products** across categories
3. **Simulate concurrent purchases**
4. **Test multiple auction participation**

### Database Performance
1. **Monitor query execution times**
2. **Check index usage**
3. **Verify connection pooling**
4. **Test with large datasets**

## Troubleshooting

### Common Issues
1. **MongoDB Connection Failed**
   - Check if MongoDB service is running
   - Verify connection string
   - Test network connectivity

2. **Compilation Errors**
   - Ensure Java 11+ is installed
   - Check Maven dependencies
   - Clean and rebuild project

3. **Authentication Issues**
   - Verify password encryption
   - Check user existence in database
   - Validate credentials format

### Debug Commands
```bash
# Check MongoDB connection
mongosh --eval "db.adminCommand('ping')"

# View database collections
mongosh auction_system --eval "show collections"

# Check user data
mongosh auction_system --eval "db.users.find()"

# Compile with verbose output
mvn clean compile -X
```

## Expected Results

After completing all tests, you should have:
- ✅ 1 Admin user (default)
- ✅ 1+ Seller users with products
- ✅ 1+ Buyer users with purchases
- ✅ Multiple transactions recorded
- ✅ Functional auction system
- ✅ Complete audit trail

## Test Data Summary

### Users Created
```
Admin: admin / admin123
Seller: seller1 / password123
Buyer: buyer1 / password123
```

### Products Added
```
1. Laptop - Electronics - $1000 - New
2. Book - Books - $50 - Used
3. Phone - Electronics - $800 - New
```

### Transactions Expected
```
1. buyer1 → Laptop purchase → $1000
2. buyer1 → Book purchase → $50
3. Refund request → Book → $50
```

This comprehensive testing approach ensures all features work correctly and provides confidence in the system's reliability and functionality.
