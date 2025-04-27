package MajorProject.Controllers;

//package securityproject.controllers;
//
//import java.util.HashMap;
//import java.util.Map;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.ResponseBody;
//
//@Controller
//public class LoginController {
//	
//	@GetMapping("/forgotusername")
//	public String ForgotUsername()
//	{
//		return "ForgotUN";
//	}
//	
//	@GetMapping("/forgotpassword")
//	public String ForgotPassword()
//	{
//		return "ForgotPwd";
//	}
//	
//	
//	@Autowired
//	private JdbcTemplate jdbcTemplate;
//	
////	String hardcodedusername = "abc@gmail.com";
////	String hardcodedpassword = "12345";
////	
//	@PostMapping("/loginsubmit")
//	@ResponseBody
//	public Map<String,String> OnLogin(@RequestBody Map<String,String> logindata)
//	{
//		Map<String,String> response = new HashMap<>();
//		String msg = "";
//		
//		String sql = "Select count(*) from user where username=? AND password=?";
//		Integer count = jdbcTemplate.queryForObject(sql, Integer.class,
//				logindata.get("username"), logindata.get("password"));
//		
////		if(logindata.get("username").equals(hardcodedusername) && 
////				logindata.get("password").equals(hardcodedpassword))
////		{
////			msg = "Sucessfully Login";
////		}
////		else
////		{
////			msg = "Incorrect username and password";
////		}
//		
//		
//		if(count>0 && count!=null)
//		{
//			msg = "Sucessfully Login";
//		}
//		else
//		{
//			msg = "Incorrect username and password";
//		}
//		
//		response.put("message", msg);
//		
//		return response;
//	}
//	
//	
//	@PostMapping("/createaccount")
//	@ResponseBody
//	
//	public Map<String,String> Onregister(@RequestBody Map<String,String> createaccount)
//	
//	{
//		
//		Map<String,String> response = new HashMap<>();
//		String msg ="";
//		
//		String sql = "insert into user(username,password,emailid) values(?,?,?)";
//		jdbcTemplate.update(sql,
//		createaccount.get("username"), createaccount.get("password"),createaccount.get("email"));
//		msg = "Successfully createaccount";
//		
//		response.put("message", msg);
//		return response;
//		
//	}
//	@PostMapping("/forgotpassincontroller")
//	@ResponseBody
//	
//	public Map<String,String> Onforgotpass(@RequestBody Map<String,String> forgotpass)
//	
//	{
//		
//		Map<String,String> response = new HashMap<>();
//		String msg ="";
//		
//		String sql = "update user set password= ? where emailid = ?";
//		jdbcTemplate.update(sql,
//		forgotpass.get("password"), forgotpass.get("email"));
//		msg = "Password change successfully";
//		
//		response.put("message", msg);
//		return response;
//		
//	}
//}







import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

//First, add this import at the top of your Java file
import java.net.URLEncoder;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.qrcode.QRCodeWriter;
import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;
import com.warrenstrange.googleauth.GoogleAuthenticatorQRGenerator;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;


@Controller
public class LoginController {
  
  @Autowired
  private JdbcTemplate jdbcTemplate;
  
  @GetMapping("/forgotusername")
  public String ForgotUsername() {
      return "ForgotUN";
  }
  
//  @GetMapping("/OTP")
//	public String GetOtpPa() {
//	    return "otp"; // This will map to Main.html in your templates folder
//	}
  @GetMapping("/ADMINLogin")
	public String GetOtpP() {
	    return "adminlogin"; // This will map to Main.html in your templates folder
	}
  
  @GetMapping("/ADMIN")
 	public String Admin() {
 	    return "Admin"; // This will map to Main.html in your templates folder
 	}
  
  @GetMapping("/CRUD")
	public String CRUD() {
	    return "CRUD"; // This will map to Main.html in your templates folder
	}
  
  @GetMapping("/forgotpassword")
  public String ForgotPassword() {
      return "ForgotPwd";
  }
  
  
  
  @PostMapping("/Adminloginsubmit")
  @ResponseBody
  public Map<String, String> OnAdminLogin(@RequestBody Map<String, String> logindata) {
      Map<String, String> response = new HashMap<>();
      String msg = "";
      
      String sql = "SELECT COUNT(*) FROM normaladminusers WHERE username=? AND password=?";
      Integer count = jdbcTemplate.queryForObject(sql, Integer.class,
              logindata.get("username"), logindata.get("password"));
      
      if (count != null && count > 0) {
          msg = "Successfully Logged In";
      } else {
          msg = "Incorrect username and password";
      }
      
      response.put("message", msg);
      return response;
  }
  
  
  @PostMapping("/loginsubmit")
  @ResponseBody
  public Map<String, String> OnLogin(@RequestBody Map<String, String> logindata) {
      Map<String, String> response = new HashMap<>();
      String msg = "";
      
      String sql = "SELECT COUNT(*) FROM normal WHERE username=? AND password=?";
      Integer count = jdbcTemplate.queryForObject(sql, Integer.class,
              logindata.get("username"), logindata.get("password"));
      
      if (count != null && count > 0) {
          msg = "Successfully Logged In";
      } else {
          msg = "Incorrect username and password";
      }
      
      response.put("message", msg);
      return response;
  }
  
//  @PostMapping("/createaccount")
//  @ResponseBody
//  public Map<String, String> OnRegister(@RequestBody Map<String, String> createAccount) {
//      Map<String, String> response = new HashMap<>();
//      
//      String sql = "INSERT INTO user (username, password, emailid) VALUES (?, ?, ?)";
//      jdbcTemplate.update(sql,
//              createAccount.get("username"),
//              createAccount.get("password"),
//              createAccount.get("email"));
//      
//      response.put("message", "Successfully created account");
//      return response;
//  }
  

  @PostMapping("/createaccount")
  @ResponseBody
  public Map<String, String> OnRegister(@RequestBody Map<String, String> createAccount) throws UnsupportedEncodingException {
      Map<String, String> response = new HashMap<>();
      String msg=" ";
      GoogleAuthenticator gAuth = new GoogleAuthenticator();
      GoogleAuthenticatorKey key = gAuth.createCredentials();

      String secretkey = key.getKey();

      String sql = "INSERT INTO normal (username, password, emailid,secretkey) VALUES (?,?, ?, ?)";
      jdbcTemplate.update(sql,
          createAccount.get("username"),
          createAccount.get("password"),
          createAccount.get("email"),secretkey);

      // Generate QR CODE URL
      String url = GoogleAuthenticatorQRGenerator.getOtpAuthTotpURL("SpringPrject", createAccount.get("username"), key);
      
      String base64Image = null;
		try
		{
			QRCodeWriter qrCodeWriter = new QRCodeWriter();
	        var bitMatrix = qrCodeWriter.encode(url, BarcodeFormat.QR_CODE, 250, 250);
	        BufferedImage qrImage = MatrixToImageWriter.toBufferedImage(bitMatrix);
	        ByteArrayOutputStream baos = new ByteArrayOutputStream();
	        ImageIO.write(qrImage, "png", baos);
	        base64Image = Base64.getEncoder().encodeToString(baos.toByteArray());
		}
		catch(WriterException | IOException e)
		{
			e.printStackTrace();
		}

      msg="Successfully created account";
      response.put("message", msg);
      response.put("qrCodeUrl", base64Image); // Send the Google Chart API URL, not the OTP URL
      return response;
  }
  




  
  
  @PostMapping("/forgotpassincontroller")
  @ResponseBody
  public Map<String, String> OnForgotPass(@RequestBody Map<String, String> forgotPass) {
      Map<String, String> response = new HashMap<>();
      
      String sql = "UPDATE normal SET password=? WHERE emailid=?";
      jdbcTemplate.update(sql,
              forgotPass.get("password"),
              forgotPass.get("email"));
      
      response.put("message", "Password changed successfully");
      return response;
  }
  
  @PostMapping("/forgotusernamecontroller")
  @ResponseBody
  public Map<String, String> OnForgotUsername(@RequestBody Map<String, String> forgotUsername) {
      Map<String, String> response = new HashMap<>();
      
      String sql = "SELECT username FROM normal WHERE emailid = ?";
      try {
          String username = jdbcTemplate.queryForObject(sql, String.class, forgotUsername.get("email"));
          response.put("message", "Your username is: " + username);
      } catch (Exception e) {
          response.put("message", "Email not found in our records.");
      }

      return response;
  }
  
  @PostMapping("/logout")
  @ResponseBody
  public Map<String, String> OnLogout(@RequestBody Map<String, String> logout) {
      Map<String, String> response = new HashMap<>();
      response.put("message", "Successfully logged out");
      return response;
  }
  
  @PostMapping("/validateotp")
  @ResponseBody
  public Map<String, String> Validateotp(@RequestBody Map<String, String> logindata) {
      Map<String, String> response = new HashMap<>();
      String msg = "";
      
      String sql = "SELECT secretkey FROM normal WHERE username=?";
      String secretkey = jdbcTemplate.queryForObject(sql, String.class,
              logindata.get("username"));
      
      //verify otp using googleauth
      GoogleAuthenticator gauth = new GoogleAuthenticator();
     // GoogleAuthenticatorKey key = gAuth.createCredentials();
      boolean isvalid= gauth.authorize(secretkey,
      		Integer.parseInt(logindata.get("otp")));
     
      if(isvalid)
      {
      	msg="Successfully verified";
      }
      else {
      	msg="Successfully not verified";
      }
      response.put("ValidOtp", msg);
      return response;
  }
  
  
  

}


