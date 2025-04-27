//package MajorProject.Controllers;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.jdbc.core.RowMapper;
//import org.springframework.util.StringUtils;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.multipart.MultipartFile;
//
//import jakarta.annotation.PostConstruct;
//
//import java.io.File;
//import java.io.IOException;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.text.SimpleDateFormat;
//import java.util.*;
//
//@RestController
//@CrossOrigin
//public class TableBookingController {
//
//    @Autowired
//    private JdbcTemplate jdbcTemplate;
//
//    // Endpoint for getting available time slots
//    @GetMapping("/availableTimes")
//    public ResponseEntity<Map<String, Object>> getAvailableTimes(@RequestParam String date) {
//        Map<String, Object> response = new HashMap<>();
//        Map<String, Integer> availableTimes = new HashMap<>();
//        
//        // Define all time slots
//        List<String> allTimeSlots = Arrays.asList(
//            "12:00 PM", "12:30 PM", "1:00 PM", "1:30 PM", "2:00 PM", "2:30 PM",
//            "6:00 PM", "6:30 PM", "7:00 PM", "7:30 PM", "8:00 PM", "8:30 PM", "9:00 PM"
//        );
//        
//        // Get reservation counts for each time slot
//        for (String timeSlot : allTimeSlots) {
//            Integer count = jdbcTemplate.queryForObject(
//                "SELECT COUNT(*) FROM reservation WHERE date = ? AND time_slot = ? AND status != 'Cancelled'",
//                Integer.class, date, timeSlot
//            );
//            
//            // Mark as available (1) if less than 5 reservations, otherwise unavailable (0)
//            availableTimes.put(timeSlot, count < 5 ? 1 : 0);
//        }
//        
//        response.put("success", true);
//        response.put("availableTimes", availableTimes);
//        return ResponseEntity.ok(response);
//    }
//
//    // Endpoint for table reservation
//    @PostMapping("/bookTable")
//    public ResponseEntity<Map<String, Object>> bookTable(@RequestBody Map<String, Object> reservationData) {
//        Map<String, Object> response = new HashMap<>();
//        
//        try {
//            String name = (String) reservationData.get("name");
//            String email = (String) reservationData.get("email");
//            String phone = (String) reservationData.get("phone");
//            int guests = Integer.parseInt(reservationData.get("guests").toString());
//            String date = (String) reservationData.get("date");
//            String time = (String) reservationData.get("time");
//            String specialRequests = (String) reservationData.get("specialRequests");
//            String paymentMethod = (String) reservationData.get("paymentMethod");
//            
//            // Generate a reservation ID
//            String reservationId = generateReservationId();
//            
//            // Insert into DB
//            jdbcTemplate.update(
//                "INSERT INTO reservation (reservation_id, name, email, phone, guests, date, time_slot, " +
//                "special_requests, payment_method, payment_status, status, created_at) " +
//                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, NOW())",
//                reservationId, name, email, phone, guests, date, time, 
//                specialRequests, paymentMethod, "Pending", "Confirmed"
//            );
//            
//            response.put("success", true);
//            response.put("message", "Reservation created successfully");
//            response.put("reservationId", reservationId);
//        } catch (Exception e) {
//            response.put("success", false);
//            response.put("message", "Error creating reservation: " + e.getMessage());
//        }
//        
//        return ResponseEntity.ok(response);
//    }
//    
//    // Process payment for online reservations
//    @PostMapping("/processPayment")
//    public ResponseEntity<Map<String, Object>> processPayment(@RequestBody Map<String, Object> paymentData) {
//        Map<String, Object> response = new HashMap<>();
//        
//        try {
//            String reservationId = (String) paymentData.get("reservationId");
//            String cardNumber = (String) paymentData.get("cardNumber");
//            String cardName = (String) paymentData.get("cardName");
//            String expiryDate = (String) paymentData.get("expiryDate");
//            
//            // In a production environment, you would integrate with a payment gateway
//            // For this example, we'll just simulate successful payment
//            
//            // Store payment info in a separate table
//            jdbcTemplate.update(
//                "INSERT INTO payment (reservation_id, card_name, card_number_last4, expiry_date, amount, status, processed_at) " +
//                "VALUES (?, ?, ?, ?, ?, ?, NOW())",
//                reservationId, cardName, cardNumber.substring(cardNumber.length() - 4), 
//                expiryDate, 200.00, "Completed"
//            );
//            
//            // Update reservation payment status
//            jdbcTemplate.update(
//                "UPDATE reservation SET payment_status = ? WHERE reservation_id = ?",
//                "Paid", reservationId
//            );
//            
//            response.put("success", true);
//            response.put("message", "Payment processed successfully");
//        } catch (Exception e) {
//            response.put("success", false);
//            response.put("message", "Error processing payment: " + e.getMessage());
//        }
//        
//        return ResponseEntity.ok(response);
//    }
//    
//    @PostMapping("/uploadScreenshot")
//    public ResponseEntity<Map<String, Object>> uploadScreenshot(
//            @RequestParam String reservationId,
//            @RequestParam MultipartFile screenshot) throws IOException {
//
//        Map<String, Object> response = new HashMap<>();
//
//        if (screenshot != null && !screenshot.isEmpty()) {
//
//            // Create a unique file name
//            String fileName = reservationId + "_" + System.currentTimeMillis() + "_" +
//                    StringUtils.cleanPath(screenshot.getOriginalFilename());
//
//            // Get absolute path for uploads/payments directory
//            String uploadDir = new File("uploads/payments").getAbsolutePath();
//
//            // Ensure directory exists
//            File dir = new File(uploadDir);
//            if (!dir.exists()) {
//                dir.mkdirs();
//            }
//
//            // Save the file
//            File destFile = new File(uploadDir, fileName);
//            screenshot.transferTo(destFile);
//
//            // Build relative path for storing in DB or accessing from frontend
//            String screenshotPath = "uploads/payments/" + fileName;
//
//            // Update reservation record in DB
//            jdbcTemplate.update(
//                "UPDATE reservation SET screenshot_path = ? WHERE reservation_id = ?",
//                screenshotPath, reservationId
//            );
//
//            response.put("success", true);
//            response.put("message", "Screenshot uploaded successfully");
//        } else {
//            response.put("success", false);
//            response.put("message", "No screenshot provided");
//        }
//
//        return ResponseEntity.ok(response);
//    }
//    
//    // Get reservation details
//    @GetMapping("/reservation/{id}")
//    public ResponseEntity<Map<String, Object>> getReservation(@PathVariable String id) {
//        Map<String, Object> response = new HashMap<>();
//        
//        try {
//            Map<String, Object> reservation = jdbcTemplate.queryForMap(
//                "SELECT * FROM reservation WHERE reservation_id = ?",
//                id
//            );
//            
//            response.put("success", true);
//            response.put("reservation", reservation);
//        } catch (Exception e) {
//            response.put("success", false);
//            response.put("message", "Reservation not found: " + e.getMessage());
//        }
//        
//        return ResponseEntity.ok(response);
//    }
//    
// // Endpoint for cancelling a reservation
//    @PostMapping("/cancelReservation")
//    public ResponseEntity<Map<String, Object>> cancelReservation(@RequestBody Map<String, Object> cancelData) {
//        Map<String, Object> response = new HashMap<>();
//
//        try {
//            String reservationId = (String) cancelData.get("reservationId");
//            String email = (String) cancelData.get("email");
//
//            // Verify that the reservation exists and matches the email
//            Integer count = jdbcTemplate.queryForObject(
//                    "SELECT COUNT(*) FROM reservation WHERE reservation_id = ? AND email = ?",
//                    Integer.class, reservationId, email
//            );
//
//            if (count == null || count == 0) {
//                response.put("success", false);
//                response.put("message", "No reservation found with the provided ID and email");
//                return ResponseEntity.ok(response);
//            }
//
//            // Get reservation details
//            Map<String, Object> reservation = jdbcTemplate.queryForMap(
//                    "SELECT date, time_slot, payment_status, status FROM reservation WHERE reservation_id = ?",
//                    reservationId
//            );
//
//            // Check if already cancelled
//            if ("Cancelled".equals(reservation.get("status"))) {
//                response.put("success", false);
//                response.put("message", "This reservation has already been cancelled");
//                return ResponseEntity.ok(response);
//            }
//
//            // Get and format the date
//            Date sqlDate = (Date) reservation.get("date");
//            String date = sqlDate.toString(); // yyyy-MM-dd
//
//            String timeSlot = (String) reservation.get("time_slot");
//
//            // Combine and parse the datetime
//            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm a");
//            Date reservationDateTime = sdf.parse(date + " " + timeSlot);
//            Date now = new Date();
//
//            if (reservationDateTime.before(now)) {
//                response.put("success", false);
//                response.put("message", "Cannot cancel a reservation that has already passed");
//                return ResponseEntity.ok(response);
//            }
//
//            // Update reservation status
//            jdbcTemplate.update(
//                    "UPDATE reservation SET status = ?, cancelled_at = NOW() WHERE reservation_id = ?",
//                    "Cancelled", reservationId
//            );
//
//            // Handle refund if paid
//            String paymentStatus = (String) reservation.get("payment_status");
//            if ("Paid".equalsIgnoreCase(paymentStatus)) {
//                jdbcTemplate.update(
//                        "INSERT INTO refund (reservation_id, amount, status, processed_at) VALUES (?, ?, ?, NOW())",
//                        reservationId, 200.00, "Pending"
//                );
//                response.put("refundStatus", "A refund of ₹200 has been initiated and will be processed within 5-7 business days");
//            }
//
//            response.put("success", true);
//            response.put("message", "Reservation cancelled successfully");
//
//        } catch (Exception e) {
//            response.put("success", false);
//            response.put("message", "Error cancelling reservation: " + e.getMessage());
//        }
//
//        return ResponseEntity.ok(response);
//    }
// // Helper method to generate unique reservation ID
//    private String generateReservationId() {
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
//        String dateStr = sdf.format(new Date());
//        
//        // Get count of reservations for today
//        Integer count = jdbcTemplate.queryForObject(
//            "SELECT COUNT(*) FROM reservation WHERE DATE(created_at) = CURDATE()",
//            Integer.class
//        );
//        
//        // Format: HU-YYYYMMDD-XXXX (HU for Hotel Umesh, date, sequential number)
//        return "HU-" + dateStr + "-" + String.format("%04d", count + 1);
//    }
//    
// // Get all reservations or filter by email
//    @GetMapping("/allReservations")
//    public ResponseEntity<Map<String, Object>> getAllReservations(@RequestParam(required = false) String email) {
//        Map<String, Object> response = new HashMap<>();
//        List<Map<String, Object>> reservations;
//
//        try {
//            if (email != null && !email.isEmpty()) {
//                reservations = jdbcTemplate.queryForList(
//                    "SELECT * FROM reservation WHERE email = ? ORDER BY created_at DESC",
//                    email
//                );
//            } else {
//                reservations = jdbcTemplate.queryForList(
//                    "SELECT * FROM reservation ORDER BY created_at DESC"
//                );
//            }
//
//            response.put("success", true);
//            response.put("reservations", reservations);
//        } catch (Exception e) {
//            response.put("success", false);
//            response.put("message", "Error fetching reservations: " + e.getMessage());
//        }
//
//        return ResponseEntity.ok(response);
//    }
//
//}

package MajorProject.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.annotation.PostConstruct;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@CrossOrigin
public class TableBookingController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

     //Endpoint for getting available time slots
    @GetMapping("/availableTimes")
    public ResponseEntity<Map<String, Object>> getAvailableTimes(@RequestParam String date) {
        Map<String, Object> response = new HashMap<>();
        Map<String, Integer> availableTimes = new HashMap<>();
        
        // Define all time slots
        List<String> allTimeSlots = Arrays.asList(
             "12:00 PM", "12:30 PM", "1:00 PM", "1:30 PM", "2:00 PM", "2:30 PM",
            "6:00 PM", "6:30 PM", "7:00 PM", "7:30 PM", "8:00 PM", "8:30 PM", "9:00 PM"
        );
        
        // Get reservation counts for each time slot
        for (String timeSlot : allTimeSlots) {
            Integer count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM reservationed WHERE date = ? AND time_slot = ? AND status != 'Cancelled'",
                Integer.class, date, timeSlot
            );
            
            // Mark as available (1) if less than 5 reservations, otherwise unavailable (0)
            availableTimes.put(timeSlot, count < 5 ? 1 : 0);
        }
        
        response.put("success", true);
        response.put("availableTimes", availableTimes);
        return ResponseEntity.ok(response);
    }

    
//    @GetMapping("/availableTimes")
//    public ResponseEntity<Map<String, Object>> getAvailableTimes(@RequestParam String date) {
//        Map<String, Object> response = new HashMap<>();
//        Map<String, Integer> availableTimes = new HashMap<>();
//
//        // Fetch all slots from DB
//        List<Map<String, Object>> slots = jdbcTemplate.queryForList("SELECT slot_time, capacity FROM time_slot");
//
//        for (Map<String, Object> slot : slots) {
//            String time = (String) slot.get("slot_time");
//            int capacity = (int) slot.get("capacity");
//
//            Integer bookedCount = jdbcTemplate.queryForObject(
//                "SELECT COUNT(*) FROM reservationed WHERE date = ? AND time_slot = ? AND status != 'Cancelled'",
//                Integer.class, date, time
//            );
//
//            availableTimes.put(time, bookedCount < capacity ? 1 : 0);
//        }
//
//        response.put("success", true);
//        response.put("availableTimes", availableTimes);
//        return ResponseEntity.ok(response);
//    }

    // Endpoint for table reservation
    @PostMapping("/bookTable")
    public ResponseEntity<Map<String, Object>> bookTable(@RequestBody Map<String, Object> reservationData) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            String name = (String) reservationData.get("name");
            String email = (String) reservationData.get("email");
            String phone = (String) reservationData.get("phone");
            int guests = Integer.parseInt(reservationData.get("guests").toString());
            String date = (String) reservationData.get("date");
            String time = (String) reservationData.get("time");
            String specialRequests = (String) reservationData.get("specialRequests");
            String paymentMethod = (String) reservationData.get("paymentMethod");
            
            // Generate a reservation ID
            String reservationId = generateReservationId();
            
            jdbcTemplate.update(
            	    "INSERT INTO reservationed (reservation_id, name, email, phone, guests, date, time_slot, " +
            	    "special_requests, payment_method, payment_status, status) " +
            	    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
            	    reservationId, name, email, phone, guests, date, time, 
            	    specialRequests, paymentMethod, "Pending", "Confirmed"
            	);

            
            response.put("success", true);
            response.put("message", "Reservation created successfully");
            response.put("reservationId", reservationId);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error creating reservation: " + e.getMessage());
        }
        
        return ResponseEntity.ok(response);
    }
    
    // Process payment for online reservations
    @PostMapping("/processPayment")
    public ResponseEntity<Map<String, Object>> processPayment(@RequestBody Map<String, Object> paymentData) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            String reservationId = (String) paymentData.get("reservationId");
            String cardNumber = (String) paymentData.get("cardNumber");
            String cardName = (String) paymentData.get("cardName");
            String expiryDate = (String) paymentData.get("expiryDate");
            
            // In a production environment, you would integrate with a payment gateway
            // For this example, we'll just simulate successful payment
            
            // Store payment info in a separate table
            jdbcTemplate.update(
                "INSERT INTO payment (reservation_id, card_name, card_number_last4, expiry_date, amount, status, processed_at) " +
                "VALUES (?, ?, ?, ?, ?, ?, NOW())",
                reservationId, cardName, cardNumber.substring(cardNumber.length() - 4), 
                expiryDate, 200.00, "Completed"
            );
            
            // Update reservation payment status
            jdbcTemplate.update(
                "UPDATE reservationed SET payment_status = ? WHERE reservation_id = ?",
                "Paid", reservationId
            );
            
            response.put("success", true);
            response.put("message", "Payment processed successfully");
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error processing payment: " + e.getMessage());
        }
        
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/uploadScreenshot")
    public ResponseEntity<Map<String, Object>> uploadScreenshot(
            @RequestParam String reservationId,
            @RequestParam MultipartFile screenshot) throws IOException {

        Map<String, Object> response = new HashMap<>();

        if (screenshot != null && !screenshot.isEmpty()) {

            // Create a unique file name
            String fileName = reservationId + "_" + System.currentTimeMillis() + "_" +
                    StringUtils.cleanPath(screenshot.getOriginalFilename());

            // Get absolute path for uploads/payments directory
            String uploadDir = new File("uploads/payments").getAbsolutePath();

            // Ensure directory exists
            File dir = new File(uploadDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            // Save the file
            File destFile = new File(uploadDir, fileName);
            screenshot.transferTo(destFile);

            // Build relative path for storing in DB or accessing from frontend
            String screenshotPath = "uploads/payments/" + fileName;

            // Update reservation record in DB
            jdbcTemplate.update(
                "UPDATE reservationed SET screenshot_path = ? WHERE reservation_id = ?",
                screenshotPath, reservationId
            );

            response.put("success", true);
            response.put("message", "Screenshot uploaded successfully");
        } else {
            response.put("success", false);
            response.put("message", "No screenshot provided");
        }

        return ResponseEntity.ok(response);
    }
    
    // Get reservation details
    @GetMapping("/reservation/{id}")
    public ResponseEntity<Map<String, Object>> getReservation(@PathVariable String id) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            Map<String, Object> reservation = jdbcTemplate.queryForMap(
                "SELECT * FROM reservationed WHERE reservation_id = ?",
                id
            );
            
            response.put("success", true);
            response.put("reservation", reservation);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Reservation not found: " + e.getMessage());
        }
        
        return ResponseEntity.ok(response);
    }
    

    
    
    
    // Helper method to generate unique reservation ID
    private String generateReservationId() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String dateStr = sdf.format(new Date());
        
        // Get count of reservations for today
        Integer count = jdbcTemplate.queryForObject(
            "SELECT COUNT(*) FROM reservationed WHERE DATE(created_at) = CURDATE()",
            Integer.class
        );
        
        // Format: HU-YYYYMMDD-XXXX (HU for Hotel Umesh, date, sequential number)
        return "HU-" + dateStr + "-" + String.format("%04d", count + 1);
    }
    
   // Get all reservations or filter by email
    @GetMapping("/allReservations")
    public ResponseEntity<Map<String, Object>> getAllReservations(@RequestParam(required = false) String email) {
        Map<String, Object> response = new HashMap<>();
        List<Map<String, Object>> reservations;

        try {
            if (email != null && !email.isEmpty()) {
                reservations = jdbcTemplate.queryForList(
                    "SELECT * FROM reservationed WHERE email = ? ORDER BY created_at DESC",
                    email
                );
            } else {
                reservations = jdbcTemplate.queryForList(
                    "SELECT * FROM reservationed ORDER BY created_at DESC"
                );
            }

            response.put("success", true);
            response.put("reservations", reservations);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error fetching reservations: " + e.getMessage());
        }

        return ResponseEntity.ok(response);
    }
}