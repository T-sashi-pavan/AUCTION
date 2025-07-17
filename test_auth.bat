@echo off
echo === Testing Authentication Fix ===
echo.
echo Compiling project...
mvn clean compile -q
echo.
echo Running authentication test...
mvn exec:java -Dexec.mainClass="com.auction.test.AuthenticationTest" -q
echo.
echo Test complete.
pause
