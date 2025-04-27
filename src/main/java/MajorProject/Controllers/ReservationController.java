package MajorProject.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
public class ReservationController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // Endpoint for cancelling a reservation
    @PostMapping("/cancelReservation")
    public ResponseEntity<Map<String, Object>> cancelReservation(@RequestBody Map<String, Object> cancelData) {
        Map<String, Object> response = new HashMap<>();

        try {
            String reservationId = (String) cancelData.get("reservationId");
            String email = (String) cancelData.get("email");

            // Verify that the reservation exists and matches the email
            Integer count = jdbcTemplate.queryForObject(
                    "SELECT COUNT(*) FROM reservationed WHERE reservation_id = ? AND email = ?",
                    Integer.class, reservationId, email
            );

            if (count == 0) {
                response.put("success", false);
                response.put("message", "No reservation found with the provided ID and email");
                return ResponseEntity.ok(response);
            }

            // Get reservation details for validation
            Map<String, Object> reservation = jdbcTemplate.queryForMap(
                    "SELECT date, time_slot, payment_status, status FROM reservationed WHERE reservation_id = ?",
                    reservationId
            );

            // Check if the reservation is already cancelled
            if ("Cancelled".equals(reservation.get("status"))) {
                response.put("success", false);
                response.put("message", "This reservation has already been cancelled");
                return ResponseEntity.ok(response);
            }

            // Get reservation date and time for comparison
            java.sql.Date sqlDate = (java.sql.Date) reservation.get("date"); // Get the date as java.sql.Date
            String timeSlot = (String) reservation.get("time_slot");

            // Convert java.sql.Date to String (the format should match your DB date format)
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String formattedDate = sdf.format(sqlDate); // Convert SQL date to formatted String

            // Now create the full reservation date-time string
            SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm a");
            Date reservationDateTime = dateTimeFormat.parse(formattedDate + " " + timeSlot);
            Date now = new Date();

//            // Optional: Check if the reservation time has passed (you can comment out this block to allow cancellations even after the reservation has passed)
//            if (reservationDateTime.before(now)) {
//                response.put("success", false);
//                response.put("message", "Cannot cancel a reservation that has already passed");
//                return ResponseEntity.ok(response);
//            }

            // Update the reservation status to cancelled
            jdbcTemplate.update(
                    "UPDATE reservationed SET status = ?, cancelled_at = NOW() WHERE reservation_id = ?",
                    "Cancelled", reservationId
            );

            // If payment was made, handle refund logic (this would connect to a payment gateway in a real production system)
            String paymentStatus = (String) reservation.get("payment_status");
            if ("Paid".equals(paymentStatus)) {
                // In a real system, refund would be processed through a payment gateway
                jdbcTemplate.update(
                        "INSERT INTO refund (reservation_id, amount, status, processed_at) " +
                                "VALUES (?, ?, ?, NOW())",
                        reservationId, 200.00, "Pending"
                );

                response.put("refundStatus", "A refund of ₹200 has been initiated and will be processed within 5-7 business days");
            }

            response.put("success", true);
            response.put("message", "Reservation cancelled successfully");
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error cancelling reservation: " + e.getMessage());
        }

        return ResponseEntity.ok(response);
    }
    
    
    @DeleteMapping("/deleteReservation")
    public ResponseEntity<Map<String, Object>> deleteReservation(@RequestBody Map<String, Object> data) {
        Map<String, Object> response = new HashMap<>();

        try {
            String reservationId = (String) data.get("reservationId");

            // Check if reservation exists
            Integer count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM reservationed WHERE reservation_id = ?",
                Integer.class,
                reservationId
            );

            if (count == null || count == 0) {
                response.put("success", false);
                response.put("message", "Reservation not found.");
                return ResponseEntity.ok(response);
            }

            // Optionally: Check if the reservation is already cancelled
            String status = jdbcTemplate.queryForObject(
                "SELECT status FROM reservationed WHERE reservation_id = ?",
                String.class,
                reservationId
            );

            if ("Cancelled".equalsIgnoreCase(status)) {
                response.put("success", false);
                response.put("message", "This reservation has already been cancelled.");
                return ResponseEntity.ok(response);
            }

            // Soft delete (recommended): set status to Cancelled
            jdbcTemplate.update(
                "UPDATE reservationed SET status = ?, cancelled_at = NOW() WHERE reservation_id = ?",
                "Cancelled",
                reservationId
            );

           

            response.put("success", true);
            response.put("message", "Reservation deleted (cancelled) successfully. Refund will be processed.");
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error: " + e.getMessage());
        }

        return ResponseEntity.ok(response);
    }
}
