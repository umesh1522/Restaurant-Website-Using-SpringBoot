//package MajorProject.Controllers;
//
//import java.io.IOException;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//import java.util.Collections;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.UUID;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.multipart.MultipartFile;
//
//@RestController
//@RequestMapping("/api")
//public class PaymentController {
//
//    private final Path uploadDir;
//    private final JdbcTemplate jdbcTemplate;
//
//    @Autowired
//    public PaymentController(JdbcTemplate jdbcTemplate) {
//        this.jdbcTemplate = jdbcTemplate;
//        this.uploadDir = Paths.get("uploads/payments");
//     //   private final Path uploadDir = Paths.get("src/main/resources/static/uploads");
//        
//        // Create upload directory if it doesn't exist
//        try {
//            if (!Files.exists(this.uploadDir)) {
//                Files.createDirectories(this.uploadDir);
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//    
//    
//
//    @PostMapping("/upload-screenshot")
//    public ResponseEntity<Map<String, String>> uploadPaymentScreenshot(@RequestParam("file") MultipartFile file) {
//        if (file.isEmpty()) {
//            return ResponseEntity.badRequest().body(Collections.singletonMap("error", "File is empty"));
//        }
//
//        try {
//            String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();
//            Path filePath = uploadDir.resolve(filename);
//            Files.write(filePath, file.getBytes());
//
//            String fileUrl = "/uploads/payments/" + filename;
//
//            Map<String, String> response = new HashMap<>();
//            response.put("url", fileUrl);
//            return ResponseEntity.ok(response);
//
//        } catch (IOException e) {
//            e.printStackTrace();
//            return ResponseEntity.badRequest().body(Collections.singletonMap("error", "File upload failed"));
//        }
//    }
//
//    @PostMapping("/payment/confirm")
//    public ResponseEntity<String> confirmPayment(@RequestBody Map<String, Object> paymentData) {
//        try {
//            String sessionId = (String) paymentData.get("sessionId");
//            Double totalAmount = Double.valueOf(paymentData.get("totalAmount").toString());
//            String paymentStatus = (String) paymentData.get("paymentStatus");
//            String name = (String) paymentData.get("name");
//            String phone = (String) paymentData.get("phone");
//            String address = (String) paymentData.get("address");
//            String paymentScreenshotUrl = (String) paymentData.get("paymentScreenshotUrl");
//            
//            // Validate essential fields
//            if (sessionId == null || totalAmount == null || paymentStatus == null) {
//                return ResponseEntity.badRequest().body("Session ID, total amount, and payment status are required");
//            }
//
//            // Handle screenshot payment method
//            if ("screenshot".equalsIgnoreCase(paymentStatus)) {
//                if (paymentScreenshotUrl == null || paymentScreenshotUrl.isEmpty()) {
//                    return ResponseEntity.badRequest().body("Payment screenshot URL is required for screenshot method");
//                }
//                
//                paymentStatus = "pending_review";
//            }
//
//            // Store payment details in database
//            String sql = "INSERT INTO payment_details (session_id, total_amount, payment_status, name, phone, address, payment_screenshot_url) " +
//                    "VALUES (?, ?, ?, ?, ?, ?, ?)";
//
//            jdbcTemplate.update(sql, sessionId, totalAmount, paymentStatus, name, phone, address, paymentScreenshotUrl);
//
//            return ResponseEntity.ok("Payment recorded successfully");
//            
//        } catch (Exception e) {
//            e.printStackTrace();
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body("Failed to process payment: " + e.getMessage());
//        }
//    }
//    
//    // Utility endpoint to help with debugging
//    @GetMapping("/payment/test")
//    public ResponseEntity<String> testConnection() {
//        return ResponseEntity.ok("Payment API is working");
//    }
//}







package MajorProject.Controllers;

import MajorProject.config.StaticResourceConfig;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api")
public class PaymentController {

    private final Path uploadDir;
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public PaymentController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        
        // Use the shared upload directory from config
        this.uploadDir = StaticResourceConfig.UPLOAD_BASE_DIR.resolve("payments");
        
        // Create upload directory if it doesn't exist
        try {
            if (!Files.exists(this.uploadDir)) {
                Files.createDirectories(this.uploadDir);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @PostMapping("/upload-screenshot")
    public ResponseEntity<Map<String, String>> uploadPaymentScreenshot(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("error", "File is empty"));
        }

        try {
            String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();
            Path filePath = uploadDir.resolve(filename);
            Files.write(filePath, file.getBytes());

            // URL will be /uploads/payments/filename
            String fileUrl = "/uploads/payments/" + filename;

            Map<String, String> response = new HashMap<>();
            response.put("url", fileUrl);
            return ResponseEntity.ok(response);

        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(Collections.singletonMap("error", "File upload failed"));
        }
    }

    @PostMapping("/payment/confirm")
    public ResponseEntity<String> confirmPayment(@RequestBody Map<String, Object> paymentData) {
        try {
            String sessionId = (String) paymentData.get("sessionId");
            Double totalAmount = Double.valueOf(paymentData.get("totalAmount").toString());
            String paymentStatus = (String) paymentData.get("paymentStatus");
            String name = (String) paymentData.get("name");
            String phone = (String) paymentData.get("phone");
            String address = (String) paymentData.get("address");
            String paymentScreenshotUrl = (String) paymentData.get("paymentScreenshotUrl");
            
            // Validate essential fields
            if (sessionId == null || totalAmount == null || paymentStatus == null) {
                return ResponseEntity.badRequest().body("Session ID, total amount, and payment status are required");
            }

            // Handle screenshot payment method
            if ("screenshot".equalsIgnoreCase(paymentStatus)) {
                if (paymentScreenshotUrl == null || paymentScreenshotUrl.isEmpty()) {
                    return ResponseEntity.badRequest().body("Payment screenshot URL is required for screenshot method");
                }
                
                paymentStatus = "pending_review";
            }

            // Store payment details in database
            String sql = "INSERT INTO payment_details (session_id, total_amount, payment_status, name, phone, address, payment_screenshot_url) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?)";

            jdbcTemplate.update(sql, sessionId, totalAmount, paymentStatus, name, phone, address, paymentScreenshotUrl);

            return ResponseEntity.ok("Payment recorded successfully");
            
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to process payment: " + e.getMessage());
        }
    }
    
    // Utility endpoint to help with debugging
    @GetMapping("/payment/test")
    public ResponseEntity<String> testConnection() {
        return ResponseEntity.ok("Payment API is working");
    }
}