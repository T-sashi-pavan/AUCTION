@echo off
cd "c:\Desktop\JAVA\New folder\AuctionSystem"
echo Compiling ProductServiceImpl.java...
javac -cp "target/classes;target/dependency/*" -d target/classes src/main/java/com/auction/services/impl/ProductServiceImpl.java
echo Compilation complete.
pause
