#!/bin/bash

echo "=== Testing Date Conversion Fix ==="
echo "Compiling application..."

# Compile the application
mvn clean compile -q

if [ $? -eq 0 ]; then
    echo "✓ Compilation successful"
    echo "Starting application test..."
    
    # Start the application in background for testing
    timeout 10s mvn exec:java -Dexec.mainClass="com.auction.test.BasicFunctionalityTest" -q
    
    if [ $? -eq 0 ]; then
        echo "✓ Basic functionality test passed"
    else
        echo "✗ Basic functionality test failed"
    fi
else
    echo "✗ Compilation failed"
fi

echo "=== Test Complete ==="
