package MajorProject.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@RestController
public class UserController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // Show all users from 'normaladminusers' table
    @GetMapping("/getAllUsers")
    @ResponseBody
    public List<Map<String, Object>> getAllUsers() {
        String sql = "SELECT id, username, emailid FROM normaladminusers";
        return jdbcTemplate.query(sql, (rs, rowNum) -> mapUserRow(rs));
    }

//    // Show all users from 'normal' table
//    @GetMapping("/getAllUser")
//    @ResponseBody
//    public List<Map<String, Object>> getAllUser() {
//        String sql = "SELECT id, username, emailid FROM normal";
//        return jdbcTemplate.query(sql, (rs, rowNum) -> mapUserRow(rs));
//    }

    // Mapping method to avoid duplication
    private Map<String, Object> mapUserRow(ResultSet rs) throws SQLException {
        Map<String, Object> row = new HashMap<>();
        row.put("id", rs.getInt("id"));
        row.put("username", rs.getString("username"));
        row.put("emailid", rs.getString("emailid"));
        return row;
    }

    // Add a new user to the 'normal' table
    @PostMapping("/add-user")
    @ResponseBody
    public Map<String, String> OnRegister(@RequestBody Map<String, String> createAccount) {
        Map<String, String> response = new HashMap<>();
        
        // Generate or assign a secret key (you may generate it dynamically if needed)
        String secretkey = UUID.randomUUID().toString();  // Or use a fixed value
        
        String sql = "INSERT INTO normaladminusers (username, password, emailid, secretkey) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(sql,
                createAccount.get("username"),
                createAccount.get("password"),
                createAccount.get("email"),
                secretkey);
        
        response.put("message", "Successfully created account");
        return response;
    }


    @PutMapping("/updateUser/{id}")
    @ResponseBody
    public Map<String, String> updateUser(@PathVariable int id, @RequestBody Map<String, String> payload) {
        String sql = "UPDATE normaladminusers SET username = ?, emailid = ?, password = ? WHERE id = ?";
        int rows = jdbcTemplate.update(sql, payload.get("username"), payload.get("email"), payload.get("password"), id);

        Map<String, String> response = new HashMap<>();
        response.put("message", rows > 0 ? "User updated successfully" : "User not found");
        return response;
    }



    @DeleteMapping("/deleteUser/{id}")
    @ResponseBody
    public Map<String, String> deleteUser(@PathVariable int id) {
        String sql = "DELETE FROM normaladminusers WHERE id = ?";
        int rowsAffected = jdbcTemplate.update(sql, id);

        Map<String, String> response = new HashMap<>();
        response.put("message", rowsAffected > 0 ? "User deleted successfully" : "User not found");
        return response;
    }
    
    
    
  
}
