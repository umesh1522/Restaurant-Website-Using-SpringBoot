package MajorProject.Controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin // Enables frontend to access API from different origin
public class FeedbackController {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    // POST: Submit feedback form data
    @PostMapping("/submitFeedback")
    @ResponseBody
    public Map<String, String> submitFeedback(@RequestBody Map<String, String> feedbackData) {
        Map<String, String> response = new HashMap<>();
        String msg = "";
        try {
            String sql = "INSERT INTO hotel_feedback (guest_name, email, room_number, stay_date, rating, " +
                         "cleanliness_rating, service_rating, amenities_rating, comments, read_at, submission_date) " +
                         "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, NOW())";
            
            int rowsAffected = jdbcTemplate.update(
                    sql,
                    feedbackData.get("guestName"),
                    feedbackData.get("email"),
                    feedbackData.get("roomNumber"),
                    feedbackData.get("stayDate"),
                    Integer.parseInt(feedbackData.get("overallRating")),
                    Integer.parseInt(feedbackData.get("cleanlinessRating")),
                    Integer.parseInt(feedbackData.get("serviceRating")),
                    Integer.parseInt(feedbackData.get("amenitiesRating")),
                    feedbackData.get("comments"),
                    0 // Initially set as unread (0)
            );
            msg = rowsAffected > 0 ? "Feedback submitted successfully" : "Failed to submit feedback";
        } catch (Exception e) {
            msg = "Error: " + e.getMessage();
        }
        response.put("message", msg);
        return response;
    }

    // GET: Retrieve all feedback entries
    @GetMapping("/allFeedback")
    @ResponseBody
    public List<Map<String, Object>> getAllFeedback() {
        String sql = "SELECT * FROM hotel_feedback ORDER BY submission_date DESC";
        return jdbcTemplate.queryForList(sql);
    }
    
    // GET: Retrieve feedback by read status (read/unread)
    @GetMapping("/feedbackByReadStatus")
    @ResponseBody
    public List<Map<String, Object>> getFeedbackByReadStatus(@RequestParam boolean isRead) {
        String sql = "SELECT * FROM hotel_feedback WHERE read_at = ? ORDER BY submission_date DESC";
        return jdbcTemplate.queryForList(sql, isRead ? 1 : 0);
    }
    
    // PUT: Mark a feedback as read
    @PutMapping("/markFeedbackAsRead/{id}")
    public Map<String, String> markFeedbackAsRead(@PathVariable int id) {
        Map<String, String> response = new HashMap<>();
        try {
            String sql = "UPDATE hotel_feedback SET read_at = 1 WHERE id = ?";
            int rowsAffected = jdbcTemplate.update(sql, id);
            response.put("message", rowsAffected > 0 ? "Feedback marked as read" : "Failed to mark as read");
        } catch (Exception e) {
            response.put("message", "Error: " + e.getMessage());
        }
        return response;
    }
    
    // PUT: Mark a feedback as unread
    @PutMapping("/markFeedbackAsUnread/{id}")
    public Map<String, String> markFeedbackAsUnread(@PathVariable int id) {
        Map<String, String> response = new HashMap<>();
        try {
            String sql = "UPDATE hotel_feedback SET read_at = 0 WHERE id = ?";
            int rowsAffected = jdbcTemplate.update(sql, id);
            response.put("message", rowsAffected > 0 ? "Feedback marked as unread" : "Failed to mark as unread");
        } catch (Exception e) {
            response.put("message", "Error: " + e.getMessage());
        }
        return response;
    }
    
    // DELETE: Delete a feedback entry
    @DeleteMapping("/deleteFeedback/{id}")
    public Map<String, String> deleteFeedback(@PathVariable int id) {
        Map<String, String> response = new HashMap<>();
        try {
            String sql = "DELETE FROM hotel_feedback WHERE id = ?";
            int rowsAffected = jdbcTemplate.update(sql, id);
            response.put("message", rowsAffected > 0 ? "Feedback deleted successfully" : "Failed to delete feedback");
        } catch (Exception e) {
            response.put("message", "Error: " + e.getMessage());
        }
        return response;
    }
    
    // GET: Get feedback statistics
    @GetMapping("/feedbackStats")
    @ResponseBody
    public Map<String, Object> getFeedbackStats() {
        Map<String, Object> stats = new HashMap<>();
        
        // Get total count
        Integer totalCount = jdbcTemplate.queryForObject(
            "SELECT COUNT(*) FROM hotel_feedback", Integer.class);
        stats.put("totalFeedbacks", totalCount);
        
        // Get average ratings
        Double avgOverallRating = jdbcTemplate.queryForObject(
            "SELECT AVG(rating) FROM hotel_feedback", Double.class);
        stats.put("avgOverallRating", avgOverallRating);
        
        Double avgCleanlinessRating = jdbcTemplate.queryForObject(
            "SELECT AVG(cleanliness_rating) FROM hotel_feedback", Double.class);
        stats.put("avgCleanlinessRating", avgCleanlinessRating);
        
        Double avgServiceRating = jdbcTemplate.queryForObject(
            "SELECT AVG(service_rating) FROM hotel_feedback", Double.class);
        stats.put("avgServiceRating", avgServiceRating);
        
        Double avgAmenitiesRating = jdbcTemplate.queryForObject(
            "SELECT AVG(amenities_rating) FROM hotel_feedback", Double.class);
        stats.put("avgAmenitiesRating", avgAmenitiesRating);
        
        // Get unread count
        Integer unreadCount = jdbcTemplate.queryForObject(
            "SELECT COUNT(*) FROM hotel_feedback WHERE read_at = 0", Integer.class);
        stats.put("unreadFeedbacks", unreadCount);
        
        return stats;
    }
}