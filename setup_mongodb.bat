@echo off
REM MongoDB Setup Script for Automated Auction System (Windows)

echo === MongoDB Setup for Automated Auction System ===
echo.

REM Check if MongoDB is installed
mongod --version >nul 2>&1
if %errorlevel% neq 0 (
    echo MongoDB is not installed. Please install MongoDB Community Edition.
    echo Visit: https://www.mongodb.com/try/download/community
    pause
    exit /b 1
)

echo MongoDB is installed.

REM Start MongoDB service
echo Starting MongoDB service...
net start MongoDB

echo MongoDB service started.

REM Wait for MongoDB to be ready
echo Waiting for MongoDB to be ready...
timeout /t 5 /nobreak >nul

REM Connect to MongoDB and create database
echo Setting up auction_system database...
mongosh --eval "use auction_system; db.createCollection('users'); db.createCollection('products'); db.createCollection('auctions'); db.createCollection('bids'); db.createCollection('transactions'); console.log('Database and collections created successfully!');"

echo Database setup complete!
echo.
echo You can now run the Automated Auction System:
echo run.bat
pause
