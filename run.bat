@echo off
REM Automated Auction System - Run Script for Windows
REM This script compiles and runs the Auction System application

echo ==================================
echo   Automated Auction System
echo ==================================
echo.

REM Check if Java is installed
java -version >nul 2>&1
if %errorlevel% neq 0 (
    echo Error: Java is not installed or not in PATH
    echo Please install Java 11 or higher
    pause
    exit /b 1
)

REM Check if Maven is installed
mvn -version >nul 2>&1
if %errorlevel% neq 0 (
    echo Error: Maven is not installed or not in PATH
    echo Please install Maven 3.6 or higher
    pause
    exit /b 1
)

echo Checking Java version...
java -version

echo.
echo Checking Maven version...
mvn -version

echo.
echo Cleaning previous builds...
mvn clean

echo.
echo Compiling the application...
mvn compile

if %errorlevel% equ 0 (
    echo.
    echo Starting Automated Auction System...
    echo ==================================
    mvn exec:java -Dexec.mainClass="com.auction.AuctionSystemApp"
) else (
    echo.
    echo Compilation failed. Please check the errors above.
    pause
    exit /b 1
)

pause
