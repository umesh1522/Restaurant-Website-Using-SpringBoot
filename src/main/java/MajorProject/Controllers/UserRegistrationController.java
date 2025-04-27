//package MajorProject.Controllers;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.mail.SimpleMailMessage;
//import org.springframework.mail.javamail.JavaMailSender;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.*;
//
//import java.security.SecureRandom;
//import java.util.HashMap;
//import java.util.Map;
//
//@Controller
//public class UserRegistrationController {
//
//    @Autowired
//    private JdbcTemplate jdbcTemplate;
//    
//    @Autowired
//   private JavaMailSender emailSender;
//    
//    // Serve the registration page
////    @GetMapping("/register")
////    public String registrationPage() {
////        return "register";
////    }
//    
//    @PostMapping("/createaccount")
//    @ResponseBody
//    public Map<String, String> createAccount(@RequestBody Map<String, String> createAccountRequest) {
//        Map<String, String> response = new HashMap<>();
//        
//        try {
//            // Generate a 6-digit OTP
//            String otp = generateOTP();
//            
//            // Store user info in database including secretkey
//            String sql = "INSERT INTO normaluser (username, password, emailid, secretkey, otp) VALUES (?, ?, ?, ?, ?)";
//            jdbcTemplate.update(sql,
//                    createAccountRequest.get("username"),
//                    createAccountRequest.get("password"),
//                    createAccountRequest.get("email"),
//                    otp,  // You can use the OTP as the secretkey for now
//                    otp);
//            
//            // Print OTP to console (for testing without email)
//            System.out.println("OTP for " + createAccountRequest.get("email") + ": " + otp);
//            
//            response.put("status", "success");
//            response.put("message", "Account created successfully. OTP: " + otp);
//        } catch (Exception e) {
//            response.put("status", "error");
//            response.put("message", "Failed to create account: " + e.getMessage());
//            e.printStackTrace();
//        }
//        
//        return response;
//    }
//    // Verify OTP endpoint
//    @PostMapping("/verifyotp")
//    @ResponseBody
//    public Map<String, String> verifyOTP(@RequestBody Map<String, String> verifyRequest) {
//        Map<String, String> response = new HashMap<>();
//        
//        try {
//            // Get the OTP from database for this email
//            String sql = "SELECT otp FROM normaluser WHERE emailid = ?";
//            String storedOtp = jdbcTemplate.queryForObject(sql, String.class, verifyRequest.get("email"));
//            
//            if (storedOtp != null && storedOtp.equals(verifyRequest.get("otp"))) {
//                // OTP verified, update user status
//                String updateSql = "UPDATE normaluser SET verified = true WHERE emailid = ?";
//                jdbcTemplate.update(updateSql, verifyRequest.get("email"));
//                
//                response.put("status", "success");
//                response.put("message", "OTP verified successfully. Your account is now active.");
//            } else {
//                response.put("status", "error");
//                response.put("message", "Invalid OTP. Please try again.");
//            }
//        } catch (Exception e) {
//            response.put("status", "error");
//            response.put("message", "Verification failed: " + e.getMessage());
//            e.printStackTrace();
//        }
//        
//        return response;
//    }
//    
//    // Generate a random 6-digit OTP
//    private String generateOTP() {
//        SecureRandom random = new SecureRandom();
//        int otp = 100000 + random.nextInt(900000);
//        return String.valueOf(otp);
//    }
//    
//    // Send OTP via email
//    private void sendOtpByEmail(String email, String otp) {
//    	 System.out.println("OTP for " + email + ": " + otp);
//        SimpleMailMessage message = new SimpleMailMessage();
//        message.setTo(email);
//        message.setSubject("Your OTP for Account Verification");
//        message.setText("Your OTP code is: " + otp + "\n\nThis code will expire in 10 minutes.");
//        emailSender.send(message);
//    }
//}
//
//  
//       <div class="search-profile">
//    <div class="search-bar">
//        <i class="fas fa-search"></i>
//        <input type="text" id="searchInput" placeholder="Search...">
//    </div>
//</div>
//<div id="results"></div>
//<script>
//  document.getElementById("searchInput").addEventListener("input", function () {
//    const query = this.value.trim();
//    if (query.length === 0) return;
//
//    fetch(`http://localhost:8080/api/search?query=${encodeURIComponent(query)}`)
//      .then(res => res.json())
//      .then(data => {
//        const resultsDiv = document.getElementById("results");
//        resultsDiv.innerHTML = data.map(item =>
//          `<div>${item.name} - ${item.role}</div>` // customize for your data
//        ).join("");
//      });
//  });
//</script>







