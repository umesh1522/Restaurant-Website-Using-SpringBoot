////package MajorProject.Controllers;
////
////import org.springframework.beans.factory.annotation.Autowired;
////import org.springframework.http.ResponseEntity;
////import org.springframework.jdbc.core.JdbcTemplate;
////import org.springframework.jdbc.core.RowMapper;
////import org.springframework.web.bind.annotation.*;
////import org.springframework.web.multipart.MultipartFile;
////
////import jakarta.annotation.PostConstruct;
////import java.io.IOException;
////import java.nio.file.*;
////import java.sql.ResultSet;
////import java.sql.SQLException;
////import java.util.*;
////
////@RestController
////@RequestMapping("/api/food")
////@CrossOrigin(origins = "*")
////public class FoodItemController {
////
////    @Autowired
////    private JdbcTemplate jdbcTemplate;
////
////    private final Path uploadDir = Paths.get("src/main/resources/static/uploads");
////
////    @PostConstruct
////    public void init() {
////        try {
////            if (!Files.exists(uploadDir)) {
////                Files.createDirectories(uploadDir);
////            }
////        } catch (IOException e) {
////            e.printStackTrace();
////        }
////    }
////
////    @PostMapping("/add")
////    public ResponseEntity<String> addFoodItem(
////            @RequestParam("name") String name,
////            @RequestParam("price") Double price,
////            @RequestParam("category") String category,
////            @RequestParam("description") String description,
////            @RequestParam("image") MultipartFile imageFile) throws IOException {
////        
////        String filename = UUID.randomUUID() + "_" + imageFile.getOriginalFilename();
////        Path path = uploadDir.resolve(filename);
////        Files.write(path, imageFile.getBytes());
////        
////        String imageUrl = "/uploads/" + filename;
////        
////        String sql = "INSERT INTO food_items (name, price, category, description, image_url) VALUES (?, ?, ?, ?, ?)";
////        
////        jdbcTemplate.update(sql, name, price, category, description, imageUrl);
////        
////        return ResponseEntity.ok("Food item added successfully");
////    }
////    
////    @GetMapping("/list")
////    public List<Map<String, Object>> getAllFoodItems() {
////        String sql = "SELECT * FROM food_items ORDER BY category, name";
////        
////        return jdbcTemplate.query(sql, new RowMapper<Map<String, Object>>() {
////            @Override
////            public Map<String, Object> mapRow(ResultSet rs, int rowNum) throws SQLException {
////                Map<String, Object> item = new HashMap<>();
////                item.put("id", rs.getInt("id"));
////                item.put("name", rs.getString("name"));
////                item.put("price", rs.getDouble("price"));
////                item.put("category", rs.getString("category"));
////                item.put("description", rs.getString("description"));
////                item.put("image_url", rs.getString("image_url"));
////                return item;
////            }
////        });
////    }
////    
////    @GetMapping("/get/{id}")
////    public Map<String, Object> getFoodItemById(@PathVariable int id) {
////        String sql = "SELECT * FROM food_items WHERE id = ?";
////        
////        return jdbcTemplate.queryForObject(sql, new Object[]{id}, new RowMapper<Map<String, Object>>() {
////            @Override
////            public Map<String, Object> mapRow(ResultSet rs, int rowNum) throws SQLException {
////                Map<String, Object> item = new HashMap<>();
////                item.put("id", rs.getInt("id"));
////                item.put("name", rs.getString("name"));
////                item.put("price", rs.getDouble("price"));
////                item.put("category", rs.getString("category"));
////                item.put("description", rs.getString("description"));
////                item.put("image_url", rs.getString("image_url"));
////                return item;
////            }
////        });
////    }
////    
////    @PutMapping("/update/{id}")
////    public ResponseEntity<String> updateFoodItem(
////            @PathVariable int id,
////            @RequestParam("name") String name,
////            @RequestParam("price") Double price,
////            @RequestParam("category") String category,
////            @RequestParam("description") String description,
////            @RequestParam(value = "image", required = false) MultipartFile imageFile) throws IOException {
////        
////        if (imageFile != null && !imageFile.isEmpty()) {
////            String filename = UUID.randomUUID() + "_" + imageFile.getOriginalFilename();
////            Path path = uploadDir.resolve(filename);
////            Files.write(path, imageFile.getBytes());
////            String imageUrl = "/uploads/" + filename;
////            
////            // Update with new image
////            String sql = "UPDATE food_items SET name = ?, price = ?, category = ?, description = ?, image_url = ? WHERE id = ?";
////            jdbcTemplate.update(sql, name, price, category, description, imageUrl, id);
////        } else {
////            // Update without changing image
////            String sql = "UPDATE food_items SET name = ?, price = ?, category = ?, description = ? WHERE id = ?";
////            jdbcTemplate.update(sql, name, price, category, description, id);
////        }
////        
////        return ResponseEntity.ok("Food item updated successfully");
////    }
////    
////    @DeleteMapping("/delete/{id}")
////    public ResponseEntity<String> deleteFoodItem(@PathVariable int id) {
////        // First, get the image URL to delete the file
////        String getImageSql = "SELECT image_url FROM food_items WHERE id = ?";
////        String imageUrl = jdbcTemplate.queryForObject(getImageSql, new Object[]{id}, String.class);
////        
////        if (imageUrl != null && imageUrl.startsWith("/uploads/")) {
////            try {
////                String filename = imageUrl.substring("/uploads/".length());
////                Files.deleteIfExists(uploadDir.resolve(filename));
////            } catch (IOException e) {
////                e.printStackTrace();
////                // Continue with deletion even if file removal fails
////            }
////        }
////        
////        // Delete the database record
////        String sql = "DELETE FROM food_items WHERE id = ?";
////        jdbcTemplate.update(sql, id);
////        
////        return ResponseEntity.ok("Food item deleted successfully");
////    }
////}
//
//
//
//
//
//
//
//
//package MajorProject.Controllers;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.multipart.MultipartFile;
//
//import jakarta.annotation.PostConstruct;
//import java.io.IOException;
//import java.nio.file.*;
//import java.util.*;
//import java.sql.ResultSet;
//
//
//@RestController
//@RequestMapping("/api/food")
//@CrossOrigin(origins = "*")
//public class FoodItemController {
//
//    @Autowired
//    private JdbcTemplate jdbcTemplate;
//
//    private final Path uploadDir = Paths.get("src/main/resources/static/uploads");
//
//    @PostConstruct
//    public void init() {
//        try {
//            if (!Files.exists(uploadDir)) {
//                Files.createDirectories(uploadDir);
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    // ==================== ADD FOOD ====================
//    @PostMapping("/add")
//    public ResponseEntity<String> addFoodItem(@RequestParam("name") String name,
//                                              @RequestParam("price") Double price,
//                                              @RequestParam("category") String category,
//                                              @RequestParam("description") String description,
//                                              @RequestParam("image") MultipartFile imageFile) throws IOException {
//
//        String filename = UUID.randomUUID() + "_" + imageFile.getOriginalFilename();
//        Path path = uploadDir.resolve(filename);
//        Files.write(path, imageFile.getBytes());
//
//        String imageUrl = "/uploads/" + filename;
//        String sql = "INSERT INTO food_items (name, price, category, description, image_url) VALUES (?, ?, ?, ?, ?)";
//        jdbcTemplate.update(sql, name, price, category, description, imageUrl);
//
//        return ResponseEntity.ok("Food item added successfully");
//    }
//
//    // ==================== LIST FOOD ITEMS ====================
////    @GetMapping("/list")
////    public List<Map<String, Object>> getAllFoodItems() {
////        String sql = "SELECT * FROM food_items ORDER BY category, name";
////        return jdbcTemplate.query(sql, (rs, rowNum) -> {
////            Map<String, Object> item = new HashMap<>();
////            item.put("id", rs.getInt("id"));
////            item.put("name", rs.getString("name"));
////            item.put("price", rs.getDouble("price"));
////            item.put("category", rs.getString("category"));
////            item.put("description", rs.getString("description"));
////            item.put("image_url", rs.getString("image_url"));
////            return item;
////        });
////    }
//
//    
//    @GetMapping("/list")
//    public List<Map<String, Object>> getAllFoodItems() {
//        String sql = "SELECT * FROM food_items ORDER BY category, name";
//        
//        return jdbcTemplate.query(sql, (rs, rowNum) -> {
//            Map<String, Object> item = new HashMap<>();
//            item.put("id", rs.getInt("id"));
//            item.put("name", rs.getString("name"));
//            item.put("price", rs.getDouble("price"));
//            item.put("category", rs.getString("category"));
//            item.put("description", rs.getString("description"));
//            
//            // Change this line to include the full URL path
//            String imageUrl = rs.getString("image_url");
//            item.put("image_url", "http://localhost:8080" + imageUrl);
//            
//            return item;
//        });
//    }
//    // ==================== SAVE CART ====================
//    @PostMapping("/cart/save")
//    public ResponseEntity<String> saveCart(@RequestBody Map<String, Object> cartData) {
//        String sessionId = (String) cartData.get("sessionId");
//        List<Map<String, Object>> items = (List<Map<String, Object>>) cartData.get("items");
//
//        // Delete any existing cart items for the session
//        jdbcTemplate.update("DELETE FROM cart_items WHERE session_id = ?", sessionId);
//
//        // Save new cart items
//        for (Map<String, Object> item : items) {
//            Integer foodId = (Integer) item.get("foodId");
//            Integer quantity = (Integer) item.get("quantity");
//            jdbcTemplate.update("INSERT INTO cart_items (session_id, food_id, quantity) VALUES (?, ?, ?)",
//                    sessionId, foodId, quantity);
//        }
//
//        return ResponseEntity.ok("Cart saved successfully");
//    }
//
////   @PostMapping("/payment/confirm")
////    public ResponseEntity<String> confirmPayment(@RequestBody Map<String, Object> paymentData) {
////        String sessionId = (String) paymentData.get("sessionId");
////        Double totalAmount = (Double) paymentData.get("totalAmount");
////        String paymentStatus = (String) paymentData.get("paymentStatus");
////
////        // ✅ New fields: name, phone, address
////        String name = (String) paymentData.get("name");
////        String phone = (String) paymentData.get("phone");
////        String address = (String) paymentData.get("address");
////
////        // If payment is screenshot upload, handle file storage
////        if ("screenshot".equals(paymentStatus)) {
////            MultipartFile paymentScreenshot = (MultipartFile) paymentData.get("paymentScreenshot");
////            if (paymentScreenshot != null && !paymentScreenshot.isEmpty()) {
////                String filename = UUID.randomUUID() + "_" + paymentScreenshot.getOriginalFilename();
////                Path path = uploadDir.resolve(filename);
////                try {
////                    Files.write(path, paymentScreenshot.getBytes());
////                } catch (IOException e) {
////                    e.printStackTrace();
////                    return ResponseEntity.status(500).body("Error uploading payment screenshot");
////                }
////                paymentStatus = "paid"; // Set payment status to "paid" if screenshot is uploaded successfully
////            }
////        }
////
////        // ✅ Updated SQL with extra fields
////        String sql = "INSERT INTO payment_details (session_id, total_amount, payment_status, name, phone, address) VALUES (?, ?, ?, ?, ?, ?)";
////        jdbcTemplate.update(sql, sessionId, totalAmount, paymentStatus, name, phone, address);
////
////        return ResponseEntity.ok("Payment recorded successfully");
////    }
//    
//
////    @PostMapping("/payment/confirm")
////    public ResponseEntity<String> confirmPayment(@RequestBody Map<String, Object> paymentData) {
////        String sessionId = (String) paymentData.get("sessionId");
////        Double totalAmount = Double.valueOf(paymentData.get("totalAmount").toString());
////        String paymentStatus = (String) paymentData.get("paymentStatus");
////        
////        String name = (String) paymentData.get("name");
////        String phone = (String) paymentData.get("phone");
////        String address = (String) paymentData.get("address");
////        
////        String paymentScreenshotUrl = (String) paymentData.get("paymentScreenshotUrl");
////        
////        if ("screenshot".equalsIgnoreCase(paymentStatus)) {
////            if (paymentScreenshotUrl != null && !paymentScreenshotUrl.isEmpty()) {
////                paymentStatus = "pending_review";
////            } else {
////                return ResponseEntity.badRequest().body("Payment screenshot URL is required for screenshot method");
////            }
////        }
////        
////        String sql = "INSERT INTO payment_details (session_id, total_amount, payment_status, name, phone, address, payment_screenshot_url) " +
////                "VALUES (?, ?, ?, ?, ?, ?, ?)";
////        
////        jdbcTemplate.update(sql, sessionId, totalAmount, paymentStatus, name, phone, address, paymentScreenshotUrl);
////        
////        return ResponseEntity.ok("Payment recorded successfully");
////    }
//
//
////    @PostMapping("/payment/confirm")
////    public ResponseEntity<String> confirmPayment(
////            @RequestParam("sessionId") String sessionId,
////            @RequestParam("totalAmount") Double totalAmount,
////            @RequestParam("paymentStatus") String paymentStatus,
////            @RequestParam("name") String name,
////            @RequestParam("phone") String phone,
////            @RequestParam("address") String address,
////            @RequestParam(value = "paymentScreenshot", required = false) MultipartFile paymentScreenshot
////    ) {
////        String screenshotUrl = null;
////
////        // ✅ Handle screenshot upload if provided
////        if ("screenshot".equals(paymentStatus) && paymentScreenshot != null && !paymentScreenshot.isEmpty()) {
////            try {
////                String filename = UUID.randomUUID() + "_" + paymentScreenshot.getOriginalFilename();
////                Path path = uploadDir.resolve(filename);
////                Files.write(path, paymentScreenshot.getBytes());
////                screenshotUrl = "/uploads/" + filename; // ✅ Store the relative URL for later use
////                paymentStatus = "paid"; // ✅ Mark payment as successful
////            } catch (IOException e) {
////                e.printStackTrace();
////                return ResponseEntity.status(500).body("Error uploading payment screenshot");
////            }
////        }
////
////        // ✅ Save all details to the database including the screenshot URL
////        String sql = "INSERT INTO payment_details (session_id, total_amount, payment_status, name, phone, address, payment_screenshot_url) VALUES (?, ?, ?, ?, ?, ?, ?)";
////        jdbcTemplate.update(sql, sessionId, totalAmount, paymentStatus, name, phone, address, screenshotUrl);
////
////        return ResponseEntity.ok("Payment recorded successfully");
////    }
//
//    // ==================== GET SINGLE FOOD ITEM ====================
//    @GetMapping("/get/{id}")
//    public Map<String, Object> getFoodItemById(@PathVariable int id) {
//        String sql = "SELECT * FROM food_items WHERE id = ?";
//        return jdbcTemplate.queryForObject(sql, new Object[]{id}, (rs, rowNum) -> {
//            Map<String, Object> item = new HashMap<>();
//            item.put("id", rs.getInt("id"));
//            item.put("name", rs.getString("name"));
//            item.put("price", rs.getDouble("price"));
//            item.put("category", rs.getString("category"));
//            item.put("description", rs.getString("description"));
//            item.put("image_url", rs.getString("image_url"));
//            return item;
//        });
//    }
//    
//    
//
//   
//
//        // ==================== UPDATE FOOD ITEM ====================
//        @PutMapping("/update/{id}")
//        public ResponseEntity<String> updateFoodItem(@PathVariable int id,
//                                                     @RequestParam("name") String name,
//                                                     @RequestParam("price") Double price,
//                                                     @RequestParam("category") String category,
//                                                     @RequestParam("description") String description,
//                                                     @RequestParam(value = "image", required = false) MultipartFile imageFile) throws IOException {
//
//            if (imageFile != null && !imageFile.isEmpty()) {
//                // Save new image
//                String filename = UUID.randomUUID() + "_" + imageFile.getOriginalFilename();
//                Path path = uploadDir.resolve(filename);
//                Files.write(path, imageFile.getBytes());
//                String imageUrl = "/uploads/" + filename;
//
//                // Update food item with new image
//                String sql = "UPDATE food_items SET name = ?, price = ?, category = ?, description = ?, image_url = ? WHERE id = ?";
//                jdbcTemplate.update(sql, name, price, category, description, imageUrl, id);
//            } else {
//                // Update food item without changing image
//                String sql = "UPDATE food_items SET name = ?, price = ?, category = ?, description = ? WHERE id = ?";
//                jdbcTemplate.update(sql, name, price, category, description, id);
//            }
//
//            return ResponseEntity.ok("Food item updated successfully");
//        }
//
//        // ==================== DELETE FOOD ITEM ====================
//        @DeleteMapping("/delete/{id}")
//        public ResponseEntity<String> deleteFoodItem(@PathVariable int id) {
//            try {
//                // Get the image URL (safely)
//                String getImageSql = "SELECT image_url FROM food_items WHERE id = ?";
//                List<String> imageUrls = jdbcTemplate.query(getImageSql, new Object[]{id}, (rs, rowNum) -> rs.getString("image_url"));
//
//                if (!imageUrls.isEmpty()) {
//                    String imageUrl = imageUrls.get(0);
//                    if (imageUrl != null && imageUrl.startsWith("/uploads/")) {
//                        try {
//                            String filename = imageUrl.substring("/uploads/".length());
//                            Files.deleteIfExists(uploadDir.resolve(filename));
//                        } catch (IOException e) {
//                            e.printStackTrace(); // Log error but proceed
//                        }
//                    }
//                }
//
//                // Delete from the database (check affected rows)
//                String sql = "DELETE FROM food_items WHERE id = ?";
//                int rowsAffected = jdbcTemplate.update(sql, id);
//
//                if (rowsAffected > 0) {
//                    return ResponseEntity.ok("Food item deleted successfully");
//                } else {
//                    return ResponseEntity.status(404).body("Food item not found");
//                }
//
//            } catch (Exception e) {
//                e.printStackTrace();
//                return ResponseEntity.status(500).body("Error deleting food item: " + e.getMessage());
//            }
//        }
//    
//
//    
//    
// // ==================== GET ALL ORDERS & PAYMENTS ====================
//        @GetMapping("/orders")
//        public List<Map<String, Object>> getAllOrders() {
//            String sql = """
//                SELECT 
//                    pd.id AS payment_id,
//                    pd.session_id,
//                    pd.total_amount,
//                    pd.payment_status,
//                    pd.name,
//                    pd.address,
//                    pd.phone,
//                    pd.payment_screenshot_url,
//                    pd.created_at,
//                    ci.food_id,
//                    fi.name AS food_name,
//                    fi.price,
//                    ci.quantity
//                FROM payment_details pd
//                JOIN cart_items ci ON pd.session_id = ci.session_id
//                JOIN food_items fi ON ci.food_id = fi.id
//                ORDER BY pd.created_at DESC
//            """;
//
//            return jdbcTemplate.query(sql, (ResultSet rs, int rowNum) -> {
//                Map<String, Object> order = new HashMap<>();
//                order.put("paymentId", rs.getInt("payment_id"));
//                order.put("sessionId", rs.getString("session_id"));
//                order.put("totalAmount", rs.getDouble("total_amount"));
//                order.put("paymentStatus", rs.getString("payment_status"));
//                order.put("name", rs.getString("name"));
//                order.put("address", rs.getString("address"));
//                order.put("phone", rs.getString("phone"));
//                order.put("paymentScreenshotUrl", rs.getString("payment_screenshot_url"));
//                order.put("createdAt", rs.getTimestamp("created_at"));
//                order.put("foodId", rs.getInt("food_id"));
//                order.put("foodName", rs.getString("food_name"));
//                order.put("price", rs.getDouble("price"));
//                order.put("quantity", rs.getInt("quantity"));
//                return order;
//            });
//        }
//
//        @DeleteMapping("/orders/{sessionId}")
//        public ResponseEntity<String> deleteOrder(@PathVariable String sessionId) {
//            int cartDeleted = jdbcTemplate.update("DELETE FROM cart_items WHERE session_id = ?", sessionId);
//            int paymentDeleted = jdbcTemplate.update("DELETE FROM payment_details WHERE session_id = ?", sessionId);
//
//            if (cartDeleted > 0 || paymentDeleted > 0) {
//                return ResponseEntity.ok("Order deleted successfully for sessionId: " + sessionId);
//            } else {
//                return ResponseEntity.status(404).body("No order found for sessionId: " + sessionId);
//            }
//        }
//
//
//}









package MajorProject.Controllers;

import MajorProject.config.StaticResourceConfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.sql.ResultSet;


@RestController
@RequestMapping("/api/food")
@CrossOrigin(origins = "*")
public class FoodItemController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // Use the shared upload directory from config
    private final Path uploadDir = StaticResourceConfig.UPLOAD_BASE_DIR.resolve("food");

    @PostConstruct
    public void init() {
        try {
            if (!Files.exists(uploadDir)) {
                Files.createDirectories(uploadDir);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // ==================== ADD FOOD ====================
    @PostMapping("/add")
    public ResponseEntity<String> addFoodItem(@RequestParam("name") String name,
                                              @RequestParam("price") Double price,
                                              @RequestParam("category") String category,
                                              @RequestParam("description") String description,
                                              @RequestParam("image") MultipartFile imageFile) throws IOException {

        String filename = UUID.randomUUID() + "_" + imageFile.getOriginalFilename();
        Path path = uploadDir.resolve(filename);
        Files.write(path, imageFile.getBytes());

        // URL will be /uploads/food/filename
        String imageUrl = "/uploads/food/" + filename;
        String sql = "INSERT INTO food_items (name, price, category, description, image_url) VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, name, price, category, description, imageUrl);

        return ResponseEntity.ok("Food item added successfully");
    }

    // ==================== LIST FOOD ITEMS ====================
    @GetMapping("/list")
    public List<Map<String, Object>> getAllFoodItems() {
        String sql = "SELECT * FROM food_items ORDER BY category, name";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            Map<String, Object> item = new HashMap<>();
            item.put("id", rs.getInt("id"));
            item.put("name", rs.getString("name"));
            item.put("price", rs.getDouble("price"));
            item.put("category", rs.getString("category"));
            item.put("description", rs.getString("description"));
            item.put("image_url", rs.getString("image_url"));
            return item;
        });
    }

    // ==================== SAVE CART ====================
    @PostMapping("/cart/save")
    public ResponseEntity<String> saveCart(@RequestBody Map<String, Object> cartData) {
        String sessionId = (String) cartData.get("sessionId");
        List<Map<String, Object>> items = (List<Map<String, Object>>) cartData.get("items");

        // Delete any existing cart items for the session
        jdbcTemplate.update("DELETE FROM cart_items WHERE session_id = ?", sessionId);

        // Save new cart items
        for (Map<String, Object> item : items) {
            Integer foodId = (Integer) item.get("foodId");
            Integer quantity = (Integer) item.get("quantity");
            jdbcTemplate.update("INSERT INTO cart_items (session_id, food_id, quantity) VALUES (?, ?, ?)",
                    sessionId, foodId, quantity);
        }

        return ResponseEntity.ok("Cart saved successfully");
    }

    // ==================== GET SINGLE FOOD ITEM ====================
    @GetMapping("/get/{id}")
    public Map<String, Object> getFoodItemById(@PathVariable int id) {
        String sql = "SELECT * FROM food_items WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{id}, (rs, rowNum) -> {
            Map<String, Object> item = new HashMap<>();
            item.put("id", rs.getInt("id"));
            item.put("name", rs.getString("name"));
            item.put("price", rs.getDouble("price"));
            item.put("category", rs.getString("category"));
            item.put("description", rs.getString("description"));
            item.put("image_url", rs.getString("image_url"));
            return item;
        });
    }

    // ==================== UPDATE FOOD ITEM ====================
    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateFoodItem(@PathVariable int id,
                                                 @RequestParam("name") String name,
                                                 @RequestParam("price") Double price,
                                                 @RequestParam("category") String category,
                                                 @RequestParam("description") String description,
                                                 @RequestParam(value = "image", required = false) MultipartFile imageFile) throws IOException {

        if (imageFile != null && !imageFile.isEmpty()) {
            // Get old image to delete
            String getOldImageSql = "SELECT image_url FROM food_items WHERE id = ?";
            List<String> oldImageUrls = jdbcTemplate.query(getOldImageSql, new Object[]{id}, (rs, rowNum) -> rs.getString("image_url"));
            
            // Delete old image if exists
            if (!oldImageUrls.isEmpty() && oldImageUrls.get(0) != null) {
                String oldImagePath = oldImageUrls.get(0);
                if (oldImagePath.startsWith("/uploads/food/")) {
                    try {
                        String oldFilename = oldImagePath.substring("/uploads/food/".length());
                        Files.deleteIfExists(uploadDir.resolve(oldFilename));
                    } catch (IOException e) {
                        e.printStackTrace(); // Log but continue
                    }
                }
            }
            
            // Save new image
            String filename = UUID.randomUUID() + "_" + imageFile.getOriginalFilename();
            Path path = uploadDir.resolve(filename);
            Files.write(path, imageFile.getBytes());
            String imageUrl = "/uploads/food/" + filename;

            // Update food item with new image
            String sql = "UPDATE food_items SET name = ?, price = ?, category = ?, description = ?, image_url = ? WHERE id = ?";
            jdbcTemplate.update(sql, name, price, category, description, imageUrl, id);
        } else {
            // Update food item without changing image
            String sql = "UPDATE food_items SET name = ?, price = ?, category = ?, description = ? WHERE id = ?";
            jdbcTemplate.update(sql, name, price, category, description, id);
        }

        return ResponseEntity.ok("Food item updated successfully");
    }

    // ==================== DELETE FOOD ITEM ====================
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteFoodItem(@PathVariable int id) {
        try {
            // Get the image URL
            String getImageSql = "SELECT image_url FROM food_items WHERE id = ?";
            List<String> imageUrls = jdbcTemplate.query(getImageSql, new Object[]{id}, (rs, rowNum) -> rs.getString("image_url"));

            if (!imageUrls.isEmpty()) {
                String imageUrl = imageUrls.get(0);
                if (imageUrl != null && imageUrl.startsWith("/uploads/food/")) {
                    try {
                        String filename = imageUrl.substring("/uploads/food/".length());
                        Files.deleteIfExists(uploadDir.resolve(filename));
                    } catch (IOException e) {
                        e.printStackTrace(); // Log error but proceed
                    }
                }
            }

            // Delete from the database
            String sql = "DELETE FROM food_items WHERE id = ?";
            int rowsAffected = jdbcTemplate.update(sql, id);

            if (rowsAffected > 0) {
                return ResponseEntity.ok("Food item deleted successfully");
            } else {
                return ResponseEntity.status(404).body("Food item not found");
            }

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error deleting food item: " + e.getMessage());
        }
    }

    // ==================== GET ALL ORDERS & PAYMENTS ====================
    @GetMapping("/orders")
    public List<Map<String, Object>> getAllOrders() {
        String sql = """
            SELECT 
                pd.id AS payment_id,
                pd.session_id,
                pd.total_amount,
                pd.payment_status,
                pd.name,
                pd.address,
                pd.phone,
                pd.payment_screenshot_url,
                pd.created_at,
                ci.food_id,
                fi.name AS food_name,
                fi.price,
                ci.quantity
            FROM payment_details pd
            JOIN cart_items ci ON pd.session_id = ci.session_id
            JOIN food_items fi ON ci.food_id = fi.id
            ORDER BY pd.created_at DESC
        """;

        return jdbcTemplate.query(sql, (ResultSet rs, int rowNum) -> {
            Map<String, Object> order = new HashMap<>();
            order.put("paymentId", rs.getInt("payment_id"));
            order.put("sessionId", rs.getString("session_id"));
            order.put("totalAmount", rs.getDouble("total_amount"));
            order.put("paymentStatus", rs.getString("payment_status"));
            order.put("name", rs.getString("name"));
            order.put("address", rs.getString("address"));
            order.put("phone", rs.getString("phone"));
            order.put("paymentScreenshotUrl", rs.getString("payment_screenshot_url"));
            order.put("createdAt", rs.getTimestamp("created_at"));
            order.put("foodId", rs.getInt("food_id"));
            order.put("foodName", rs.getString("food_name"));
            order.put("price", rs.getDouble("price"));
            order.put("quantity", rs.getInt("quantity"));
            return order;
        });
    }

    @DeleteMapping("/orders/{sessionId}")
    public ResponseEntity<String> deleteOrder(@PathVariable String sessionId) {
        int cartDeleted = jdbcTemplate.update("DELETE FROM cart_items WHERE session_id = ?", sessionId);
        int paymentDeleted = jdbcTemplate.update("DELETE FROM payment_details WHERE session_id = ?", sessionId);

        if (cartDeleted > 0 || paymentDeleted > 0) {
            return ResponseEntity.ok("Order deleted successfully for sessionId: " + sessionId);
        } else {
            return ResponseEntity.status(404).body("No order found for sessionId: " + sessionId);
        }
    }
}
