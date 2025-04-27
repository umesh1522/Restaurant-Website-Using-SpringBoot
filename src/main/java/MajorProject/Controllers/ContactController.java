package MajorProject.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin // Enables frontend to access API from different origin
public class ContactController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // POST: Submit contact form data
    @PostMapping("/submitContactForm")
    @ResponseBody
    public Map<String, String> submitContactForm(@RequestBody Map<String, String> contactData) {
        Map<String, String> response = new HashMap<>();
        String msg = "";

        try {
            String sql = "INSERT INTO contact_messages (name, email, subject, message, submission_date) VALUES (?, ?, ?, ?, NOW())";

            int rowsAffected = jdbcTemplate.update(
                    sql,
                    contactData.get("name"),
                    contactData.get("email"),
                    contactData.get("subject"),
                    contactData.get("message")
            );

            msg = rowsAffected > 0 ? "Message sent successfully" : "Failed to send message";
        } catch (Exception e) {
            msg = "Error: " + e.getMessage();
        }

        response.put("message", msg);
        return response;
    }

    // GET: Retrieve all contact messages
    @GetMapping("/allContacts")
    @ResponseBody
    public List<Map<String, Object>> getAllContacts() {
        String sql = "SELECT * FROM contact_messages ORDER BY submission_date DESC";
        return jdbcTemplate.queryForList(sql);
    }
    
 // Add this method to your ContactController

 // GET: Retrieve contact messages by read status (read/unread)
 @GetMapping("/contactsByReadStatus")
 @ResponseBody
 public List<Map<String, Object>> getContactsByReadStatus(@RequestParam boolean isRead) {
     String sql = "SELECT * FROM contact_messages WHERE is_read = ? ORDER BY submission_date DESC";
     return jdbcTemplate.queryForList(sql, isRead);
 }

 
 
//Add this method to your ContactController

//PUT: Mark a message as read
@PutMapping("/markAsRead/{id}")
public Map<String, String> markAsRead(@PathVariable int id) {
  Map<String, String> response = new HashMap<>();
  try {
      String sql = "UPDATE contact_messages SET is_read = TRUE WHERE id = ?";
      int rowsAffected = jdbcTemplate.update(sql, id);

      response.put("message", rowsAffected > 0 ? "Message marked as read" : "Failed to mark as read");
  } catch (Exception e) {
      response.put("message", "Error: " + e.getMessage());
  }
  return response;
}


			
//Add this method to your ContactController

//DELETE: Delete a contact message
@DeleteMapping("/deleteContact/{id}")
public Map<String, String> deleteContact(@PathVariable int id) {
 Map<String, String> response = new HashMap<>();
 try {
     String sql = "DELETE FROM contact_messages WHERE id = ?";
     int rowsAffected = jdbcTemplate.update(sql, id);

     response.put("message", rowsAffected > 0 ? "Message deleted successfully" : "Failed to delete message");
 } catch (Exception e) {
     response.put("message", "Error: " + e.getMessage());
 }
 return response;
}

}
