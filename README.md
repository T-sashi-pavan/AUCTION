# Automated Auction System

A comprehensive Java console-based auction system that facilitates product buying and selling through auctions and direct purchases. Built with Java OOP principles and MongoDB Atlas integration.

## Features

### 🔐 User Authentication
- Secure user registration and login
- Password encryption using BCrypt
- Role-based access control (Admin, Seller, Buyer)
- Account management and deletion

### 👤 Admin Features
- View all registered users (buyers and sellers)
- View all products in the system
- Create new auctions
- View auction history
- Monitor items status (sold/unsold)
- Filter items by category
- View all transactions
- Search transactions by date range or ID
- System statistics and reporting

### 🧑 Seller Features
- Register as a seller
- Add new products to inventory
- Remove products from inventory
- View sold product history
- View personal transaction history
- Update personal details
- View profile summary with statistics
- Delete account

### 🛒 Buyer Features
- Register as a buyer
- Browse products by category
- View all sellers and their products
- Purchase products directly
- View purchase history
- Request refunds for purchases
- View personal transaction history
- View ongoing auctions
- Place bids in auctions
- View auction history and bid results
- Update personal details
- View profile summary with statistics
- Delete account

## Technology Stack

- **Java 11+** - Core programming language
- **MongoDB Atlas** - Database for data persistence
- **MongoDB Java Driver** - Database connectivity
- **BCrypt** - Password encryption
- **Jackson** - JSON processing
- **Maven** - Build and dependency management

## Project Structure

```
AuctionSystem/
├── src/main/java/com/auction/
│   ├── models/                 # POJO classes
│   │   ├── User.java          # Abstract base class
│   │   ├── Admin.java         # Admin user type
│   │   ├── Seller.java        # Seller user type
│   │   ├── Buyer.java         # Buyer user type
│   │   ├── Product.java       # Product entity
│   │   ├── Auction.java       # Auction entity
│   │   ├── Bid.java           # Bid entity
│   │   └── Transaction.java   # Transaction entity
│   ├── services/              # Service interfaces
│   │   ├── UserService.java
│   │   ├── AdminService.java
│   │   ├── SellerService.java
│   │   ├── BuyerService.java
│   │   ├── ProductService.java
│   │   ├── AuctionService.java
│   │   └── TransactionService.java
│   ├── services/impl/         # Service implementations
│   │   ├── UserServiceImpl.java
│   │   ├── AdminServiceImpl.java
│   │   ├── SellerServiceImpl.java
│   │   ├── BuyerServiceImpl.java
│   │   ├── ProductServiceImpl.java
│   │   ├── AuctionServiceImpl.java
│   │   └── TransactionServiceImpl.java
│   ├── database/              # Database connection
│   │   └── DatabaseConnection.java
│   ├── utils/                 # Utility classes
│   │   ├── InputUtils.java
│   │   └── PasswordUtils.java
│   ├── ui/                    # User interface
│   │   └── AuthenticationUI.java
│   ├── exceptions/            # Custom exceptions
│   │   ├── AuthenticationException.java
│   │   ├── DatabaseException.java
│   │   └── AuctionException.java
│   └── AuctionSystemApp.java  # Main application class
└── pom.xml                    # Maven configuration
```

## OOP Principles Implemented

### 1. **Encapsulation**
- All model classes use private fields with public getters/setters
- Data hiding and controlled access to object properties

### 2. **Inheritance**
- `User` abstract base class extended by `Admin`, `Seller`, and `Buyer`
- Common properties and methods shared through inheritance

### 3. **Polymorphism**
- Method overriding in `showDashboard()` and `handleActions()` methods
- Different implementations for each user type

### 4. **Abstraction**
- Abstract `User` class with abstract methods
- Service interfaces abstracting implementation details
- Clean separation between interface and implementation


## Database Schema

### Collections:
- **users** - User accounts (Admin, Seller, Buyer)
- **products** - Product listings
- **auctions** - Auction records
- **bids** - Bid records for auctions
- **transactions** - Transaction history

## Setup Instructions

### Prerequisites
- Java 11 or higher
- Maven 3.6 or higher
- MongoDB Atlas account (or local MongoDB instance)

### Installation

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd AuctionSystem
   ```

2. **Configure Database Connection**
   Update the connection string in `DatabaseConnection.java`:
   ```java
   private static final String CONNECTION_STRING = "your-mongodb-atlas-connection-string";
   ```

3. **Build the project**
   ```bash
   mvn clean compile
   ```

4. **Run the application**
   ```bash
   mvn exec:java -Dexec.mainClass="com.auction.AuctionSystemApp"
   ```

## Usage

### First Time Setup
1. Run the application
2. A default admin account will be created:
   - Username: `admin`
   - Password: `admin123`

### User Registration
1. Select "Register" from the main menu
2. Choose user type (Seller or Buyer)
3. Fill in required information
4. Login with your credentials

### Navigation
- **Admin Dashboard**: Complete system management
- **Seller Dashboard**: Product and sales management
- **Buyer Dashboard**: Shopping and bidding interface

## Default Admin Account

- **Username**: admin
- **Password**: admin123
- **Role**: ADMIN

*Please change the default password after first login for security.*

## Exception Handling

The application includes comprehensive exception handling:
- `AuthenticationException` - Login/authentication failures
- `DatabaseException` - Database operation failures
- `AuctionException` - Auction-related errors
- Input validation and error recovery

## Features Highlights

### Security
- Password encryption using BCrypt
- User session management
- Role-based access control

### Data Management
- MongoDB Atlas integration
- CRUD operations for all entities
- Data validation and integrity

### User Experience
- Intuitive console interface
- Clear navigation and feedback
- Comprehensive error messages

### Scalability
- Modular architecture
- Service-oriented design
- Extensible codebase

## License

This project is developed as an educational demonstration of Java OOP principles and MongoDB integration.

## Contributing

This is an educational project. Feel free to fork and extend for learning purposes.

## Support

For questions or issues, please refer to the code documentation or create an issue in the repository.
