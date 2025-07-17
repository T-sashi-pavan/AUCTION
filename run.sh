#!/bin/bash

# Automated Auction System - Run Script
# This script compiles and runs the Auction System application

echo "=================================="
echo "  Automated Auction System"
echo "=================================="
echo

# Check if Java is installed
if ! command -v java &> /dev/null; then
    echo "Error: Java is not installed or not in PATH"
    echo "Please install Java 11 or higher"
    exit 1
fi

# Check if Maven is installed
if ! command -v mvn &> /dev/null; then
    echo "Error: Maven is not installed or not in PATH"
    echo "Please install Maven 3.6 or higher"
    exit 1
fi

echo "Checking Java version..."
java -version

echo
echo "Checking Maven version..."
mvn -version

echo
echo "Cleaning previous builds..."
mvn clean

echo
echo "Compiling the application..."
mvn compile

if [ $? -eq 0 ]; then
    echo
    echo "Starting Automated Auction System..."
    echo "=================================="
    mvn exec:java -Dexec.mainClass="com.auction.AuctionSystemApp"
else
    echo
    echo "Compilation failed. Please check the errors above."
    exit 1
fi
