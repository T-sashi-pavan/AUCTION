package com.auction.services.impl;

import com.auction.services.ProductService;
import com.auction.models.Product;
import com.auction.exceptions.DatabaseException;
import com.auction.database.DatabaseConnection;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.mongodb.client.model.Filters.*;

/**
 * Implementation of ProductService interface
 */
public class ProductServiceImpl implements ProductService {
    
    private MongoCollection<Document> getProductCollection() throws DatabaseException {
        MongoDatabase database = DatabaseConnection.getDatabase();
        return database.getCollection("products");
    }
    
    @Override
    public boolean saveProduct(Product product) throws DatabaseException {
        try {
            MongoCollection<Document> collection = getProductCollection();
            Document productDoc = productToDocument(product);
            collection.insertOne(productDoc);
            return true;
        } catch (Exception e) {
            throw new DatabaseException("Failed to save product: " + e.getMessage(), e);
        }
    }
    
    @Override
    public boolean updateProduct(Product product) throws DatabaseException {
        try {
            MongoCollection<Document> collection = getProductCollection();
            Document productDoc = productToDocument(product);
            collection.replaceOne(eq("_id", product.getId()), productDoc);
            return true;
        } catch (Exception e) {
            throw new DatabaseException("Failed to update product: " + e.getMessage(), e);
        }
    }
    
    @Override
    public boolean deleteProduct(String productId) throws DatabaseException {
        try {
            MongoCollection<Document> collection = getProductCollection();
            collection.deleteOne(eq("_id", new ObjectId(productId)));
            return true;
        } catch (Exception e) {
            throw new DatabaseException("Failed to delete product: " + e.getMessage(), e);
        }
    }
    
    @Override
    public Product findProductById(String productId) throws DatabaseException {
        try {
            MongoCollection<Document> collection = getProductCollection();
            Document productDoc = collection.find(eq("_id", new ObjectId(productId))).first();
            return productDoc != null ? documentToProduct(productDoc) : null;
        } catch (Exception e) {
            throw new DatabaseException("Failed to find product: " + e.getMessage(), e);
        }
    }
    
    @Override
    public List<Product> getAllProducts() throws DatabaseException {
        try {
            MongoCollection<Document> collection = getProductCollection();
            List<Product> products = new ArrayList<>();
            
            for (Document doc : collection.find()) {
                products.add(documentToProduct(doc));
            }
            
            return products;
        } catch (Exception e) {
            throw new DatabaseException("Failed to get all products: " + e.getMessage(), e);
        }
    }
    
    @Override
    public List<Product> getProductsByCategory(String category) throws DatabaseException {
        try {
            MongoCollection<Document> collection = getProductCollection();
            List<Product> products = new ArrayList<>();
            
            for (Document doc : collection.find(eq("category", category))) {
                products.add(documentToProduct(doc));
            }
            
            return products;
        } catch (Exception e) {
            throw new DatabaseException("Failed to get products by category: " + e.getMessage(), e);
        }
    }
    
    @Override
    public List<Product> getProductsBySellerId(String sellerId) throws DatabaseException {
        try {
            MongoCollection<Document> collection = getProductCollection();
            List<Product> products = new ArrayList<>();
            
            for (Document doc : collection.find(eq("sellerId", new ObjectId(sellerId)))) {
                products.add(documentToProduct(doc));
            }
            
            return products;
        } catch (Exception e) {
            throw new DatabaseException("Failed to get products by seller: " + e.getMessage(), e);
        }
    }
    
    @Override
    public List<Product> getAvailableProducts() throws DatabaseException {
        try {
            MongoCollection<Document> collection = getProductCollection();
            List<Product> products = new ArrayList<>();
            
            for (Document doc : collection.find(and(eq("isAvailable", true), eq("isSold", false)))) {
                products.add(documentToProduct(doc));
            }
            
            return products;
        } catch (Exception e) {
            throw new DatabaseException("Failed to get available products: " + e.getMessage(), e);
        }
    }
    
    @Override
    public List<Product> getSoldProducts() throws DatabaseException {
        try {
            MongoCollection<Document> collection = getProductCollection();
            List<Product> products = new ArrayList<>();
            
            for (Document doc : collection.find(eq("isSold", true))) {
                products.add(documentToProduct(doc));
            }
            
            return products;
        } catch (Exception e) {
            throw new DatabaseException("Failed to get sold products: " + e.getMessage(), e);
        }
    }
    
    @Override
    public boolean markProductAsSold(String productId) throws DatabaseException {
        try {
            MongoCollection<Document> collection = getProductCollection();
            
            // Convert LocalDateTime to Date for MongoDB storage
            LocalDateTime now = LocalDateTime.now();
            java.util.Date dateSold = java.util.Date.from(now.atZone(java.time.ZoneId.systemDefault()).toInstant());
            
            collection.updateOne(
                eq("_id", new ObjectId(productId)),
                new Document("$set", new Document()
                    .append("isSold", true)
                    .append("isAvailable", false)
                    .append("dateSold", dateSold))
            );
            return true;
        } catch (Exception e) {
            throw new DatabaseException("Failed to mark product as sold: " + e.getMessage(), e);
        }
    }
    
    @Override
    public List<String> getAllCategories() throws DatabaseException {
        try {
            MongoCollection<Document> collection = getProductCollection();
            List<String> categories = new ArrayList<>();
            
            for (Document doc : collection.find().projection(new Document("category", 1))) {
                String category = doc.getString("category");
                if (category != null && !categories.contains(category)) {
                    categories.add(category);
                }
            }
            
            return categories.stream().sorted().collect(Collectors.toList());
        } catch (Exception e) {
            throw new DatabaseException("Failed to get all categories: " + e.getMessage(), e);
        }
    }
    
    /**
     * Converts Product object to MongoDB Document
     */
    private Document productToDocument(Product product) {
        Document doc = new Document()
                .append("_id", product.getId())
                .append("name", product.getName())
                .append("description", product.getDescription())
                .append("category", product.getCategory())
                .append("price", product.getPrice())
                .append("sellerId", product.getSellerId())
                .append("isAvailable", product.isAvailable())
                .append("isSold", product.isSold())
                .append("imageUrl", product.getImageUrl())
                .append("condition", product.getCondition())
                .append("quantity", product.getQuantity());
        
        // Convert LocalDateTime to Date for MongoDB storage
        if (product.getDateAdded() != null) {
            doc.append("dateAdded", java.util.Date.from(product.getDateAdded()
                .atZone(java.time.ZoneId.systemDefault()).toInstant()));
        }
        
        if (product.getDateSold() != null) {
            doc.append("dateSold", java.util.Date.from(product.getDateSold()
                .atZone(java.time.ZoneId.systemDefault()).toInstant()));
        } else {
            doc.append("dateSold", null);
        }
        
        return doc;
    }
    
    /**
     * Converts MongoDB Document to Product object
     */
    private Product documentToProduct(Document doc) {
        Product product = new Product();
        
        product.setId(doc.getObjectId("_id"));
        product.setName(doc.getString("name"));
        product.setDescription(doc.getString("description"));
        product.setCategory(doc.getString("category"));
        product.setPrice(doc.getDouble("price"));
        product.setSellerId(doc.getObjectId("sellerId"));
        product.setAvailable(doc.getBoolean("isAvailable", true));
        product.setSold(doc.getBoolean("isSold", false));
        product.setImageUrl(doc.getString("imageUrl"));
        product.setCondition(doc.getString("condition"));
        product.setQuantity(doc.getInteger("quantity", 1));
        
        // Handle date conversion properly
        java.util.Date dateAdded = doc.getDate("dateAdded");
        if (dateAdded != null) {
            product.setDateAdded(dateAdded.toInstant()
                .atZone(java.time.ZoneId.systemDefault())
                .toLocalDateTime());
        }
        
        java.util.Date dateSold = doc.getDate("dateSold");
        if (dateSold != null) {
            product.setDateSold(dateSold.toInstant()
                .atZone(java.time.ZoneId.systemDefault())
                .toLocalDateTime());
        }
        
        return product;
    }
}
