#!/bin/bash
# MongoDB Setup Script for Automated Auction System

echo "=== MongoDB Setup for Automated Auction System ==="
echo

# Check if MongoDB is installed
if ! command -v mongod &> /dev/null; then
    echo "MongoDB is not installed. Please install MongoDB Community Edition."
    echo "Visit: https://www.mongodb.com/try/download/community"
    exit 1
fi

echo "MongoDB is installed."

# Start MongoDB service
echo "Starting MongoDB service..."
if [[ "$OSTYPE" == "darwin"* ]]; then
    # macOS
    brew services start mongodb/brew/mongodb-community
elif [[ "$OSTYPE" == "linux-gnu"* ]]; then
    # Linux
    sudo systemctl start mongod
    sudo systemctl enable mongod
else
    echo "Please start MongoDB manually on your system."
fi

echo "MongoDB service started."

# Wait for MongoDB to be ready
echo "Waiting for MongoDB to be ready..."
sleep 5

# Connect to MongoDB and create database
echo "Setting up auction_system database..."
mongosh --eval "
use auction_system;
db.createCollection('users');
db.createCollection('products');
db.createCollection('auctions');
db.createCollection('bids');
db.createCollection('transactions');
console.log('Database and collections created successfully!');
"

echo "Database setup complete!"
echo
echo "You can now run the Automated Auction System:"
echo "Windows: run.bat"
echo "Linux/Mac: ./run.sh"
