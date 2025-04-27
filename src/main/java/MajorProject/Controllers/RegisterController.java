//package MajorProject.Controllers;
////
////import java.awt.image.BufferedImage;
////import java.io.ByteArrayOutputStream;
////import java.io.IOException;
////import java.io.UnsupportedEncodingException;
////import java.util.Base64;
////import java.util.HashMap;
////import java.util.Map;
////
////import javax.imageio.ImageIO;
////
////import org.springframework.web.bind.annotation.PostMapping;
////import org.springframework.web.bind.annotation.RequestBody;
////import org.springframework.web.bind.annotation.ResponseBody;
////
////import com.google.zxing.BarcodeFormat;
////import com.google.zxing.WriterException;
////import com.google.zxing.client.j2se.MatrixToImageWriter;
////import com.google.zxing.qrcode.QRCodeWriter;
////import com.warrenstrange.googleauth.GoogleAuthenticator;
////import com.warrenstrange.googleauth.GoogleAuthenticatorKey;
////import com.warrenstrange.googleauth.GoogleAuthenticatorQRGenerator;
////
////public class RegisterController {
////	
////	
////	 @PostMapping("/createaccount")
////	    @ResponseBody
////	    public Map<String, String> OnRegister(@RequestBody Map<String, String> createAccount) throws UnsupportedEncodingException {
////	        Map<String, String> response = new HashMap<>();
////	        String msg=" ";
////	        GoogleAuthenticator gAuth = new GoogleAuthenticator();
////	        GoogleAuthenticatorKey key = gAuth.createCredentials();
////
////	        String secretkey = key.getKey();
////
////	        String sql = "INSERT INTO user (username, password, emailid,secretkey) VALUES (?,?, ?, ?)";
//////	        jdbcTemplate.update(sql,
////	            createAccount.get("username"),
////	            createAccount.get("password"),
////	            createAccount.get("email"),secretkey);
////
////	        // Generate QR CODE URL
////	        String url = GoogleAuthenticatorQRGenerator.getOtpAuthTotpURL("SpringPrject", createAccount.get("username"), key);
////	        
////	        String base64Image = null;
////			try
////			{
////				QRCodeWriter qrCodeWriter = new QRCodeWriter();
////		        var bitMatrix = qrCodeWriter.encode(url, BarcodeFormat.QR_CODE, 250, 250);
////		        BufferedImage qrImage = MatrixToImageWriter.toBufferedImage(bitMatrix);
////		        ByteArrayOutputStream baos = new ByteArrayOutputStream();
////		        ImageIO.write(qrImage, "png", baos);
////		        base64Image = Base64.getEncoder().encodeToString(baos.toByteArray());
////			}
////			catch(WriterException | IOException e)
////			{
////				e.printStackTrace();
////			}
////
////	        msg="Successfully created account";
////	        response.put("message", msg);
////	        response.put("qrCodeUrl", base64Image); // Send the Google Chart API URL, not the OTP URL
////	        return response;
////	    }
////	    
////	 
////
////
////
////}
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.dao.EmptyResultDataAccessException;
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.*;
//
//import jakarta.servlet.http.HttpSession;
//import java.util.HashMap;
//import java.util.Map;
//
//@Controller
//public class RegisterController {
//
//    @Autowired
//    private JdbcTemplate jdbcTemplate;
//    
//    @GetMapping("/login")
//    public String loginPage() {
//        return "login";
//    }
//    
//    @PostMapping("/login-auth")
//    @ResponseBody
//    public Map<String, String> authenticateUser(@RequestBody Map<String, String> loginRequest, HttpSession session) {
//        Map<String, String> response = new HashMap<>();
//        
//        try {
//            String username = loginRequest.get("username");
//            String password = loginRequest.get("password");
//            
//            // Check if input is email or username
//            String sql;
//            if (username.contains("@")) {
//                sql = "SELECT id, username FROM normaluser WHERE emailid = ? AND password = ?";
//            } else {
//                sql = "SELECT id, username FROM normaluser WHERE username = ? AND password = ?";
//            }
//            
//            Map<String, Object> user;
//            try {
//                user = jdbcTemplate.queryForMap(sql, username, password);
//            } catch (EmptyResultDataAccessException e) {
//                response.put("status", "error");
//                response.put("message", "Invalid username or password");
//                return response;
//            }
//            
//            // User authenticated, store in session
//            session.setAttribute("userId", user.get("id"));
//            session.setAttribute("username", user.get("username"));
//            
//            response.put("status", "success");
//            response.put("message", "Login successful");
//            response.put("redirect", "/dashboard");
//        } catch (Exception e) {
//            response.put("status", "error");
//            response.put("message", "An error occurred: " + e.getMessage());
//            e.printStackTrace();
//        }
//        
//        return response;
//    }
//    
//    
//    private void sendOtpByEmail(String email, String otp) {
//        // Print OTP to console for testing
//        System.out.println("\n------------------------------------");
//        System.out.println("OTP for " + email + ": " + otp);
//        System.out.println("------------------------------------\n");
//        
//        // For now, we're skipping actual email sending
//        // In a production environment, you would uncomment this code
//        /*
//        try {
//            SimpleMailMessage message = new SimpleMailMessage();
//            message.setTo(email);
//            message.setSubject("Your OTP for Login Verification");
//            message.setText("Your OTP code is: " + otp + "\n\nThis code will expire in 10 minutes.");
//            emailSender.send(message);
//        } catch (Exception e) {
//            System.err.println("Failed to send email: " + e.getMessage());
//            e.printStackTrace();
//        }
//        */
//    }
//    
//    @GetMapping("/logout")
//    public String logout(HttpSession session) {
//        session.invalidate();
//        return "redirect:/login";
//    }
//    
//    // Simple dashboard page (protected route)
//    @GetMapping("/dashboard")
//    public String dashboard(HttpSession session) {
//        // Check if user is logged in
//        if (session.getAttribute("userId") == null) {
//            return "redirect:/login";
//        }
//        return "dashboard";
//    }
//}






//genrate 6 digit otp
