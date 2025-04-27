//package MajorProject.Controllers;
//
//
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.multipart.MultipartFile;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.core.io.ByteArrayResource;
//import org.springframework.core.io.Resource;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//import java.util.Base64;
//
//@Controller
//@RequestMapping("/bank")
//public class BankDetailsController {
//    
//    @Autowired
//    private JdbcTemplate jdbcTemplate;
//    
//    // Admin page to view and manage bank details
//    @GetMapping("/admin")
//    public String adminPage(Model model, @RequestParam(required = false) String message) {
//        try {
//            String sql = "SELECT * FROM bank_details";
//            List<Map<String, Object>> details = jdbcTemplate.queryForList(sql);
//            model.addAttribute("bankDetails", details);
//        } catch (Exception e) {
//            // If there's an error, add an empty list to avoid null pointer
//            model.addAttribute("bankDetails", new ArrayList<>());
//            model.addAttribute("message", "Error loading bank details: " + e.getMessage());
//        }
//        
//        if (message != null) {
//            model.addAttribute("message", message);
//        }
//        
//        return "ABank";
//    }
//    
//    // Add new bank details with QR code image
//    @PostMapping("/add")
//    public String addBankDetails(
//            @RequestParam String bankName,
//            @RequestParam String accountNumber,
//            @RequestParam String ifscCode,
//            @RequestParam String accountHolder,
//            @RequestParam String branchName,
//            @RequestParam MultipartFile qrCodeImage) {
//        
//        try {
//            String sql = "INSERT INTO bank_details (bank_name, account_number, ifsc_code, account_holder, branch_name, qr_code_image, qr_code_filename) VALUES (?, ?, ?, ?, ?, ?, ?)";
//            
//            if (qrCodeImage != null && !qrCodeImage.isEmpty()) {
//                jdbcTemplate.update(
//                    sql, 
//                    bankName,
//                    accountNumber,
//                    ifscCode,
//                    accountHolder,
//                    branchName,
//                    qrCodeImage.getBytes(),
//                    qrCodeImage.getOriginalFilename()
//                );
//                return "redirect:/bank/admin?message=Bank details and QR code added successfully";
//            } else {
//                return "redirect:/bank/admin?message=Error: QR code image is required";
//            }
//        } catch (IOException e) {
//            return "redirect:/bank/admin?message=Error uploading QR code: " + e.getMessage();
//        }
//    }
//    
//    // Update existing bank details
//    @PostMapping("/update")
//    public String updateBankDetails(
//            @RequestParam int id,
//            @RequestParam String bankName,
//            @RequestParam String accountNumber,
//            @RequestParam String ifscCode,
//            @RequestParam String accountHolder,
//            @RequestParam String branchName,
//            @RequestParam(required = false) MultipartFile qrCodeImage) {
//        
//        try {
//            if (qrCodeImage != null && !qrCodeImage.isEmpty()) {
//                String sql = "UPDATE bank_details SET bank_name=?, account_number=?, ifsc_code=?, account_holder=?, branch_name=?, qr_code_image=?, qr_code_filename=? WHERE id=?";
//                jdbcTemplate.update(
//                    sql, 
//                    bankName,
//                    accountNumber,
//                    ifscCode,
//                    accountHolder,
//                    branchName,
//                    qrCodeImage.getBytes(),
//                    qrCodeImage.getOriginalFilename(),
//                    id
//                );
//            } else {
//                String sql = "UPDATE bank_details SET bank_name=?, account_number=?, ifsc_code=?, account_holder=?, branch_name=? WHERE id=?";
//                jdbcTemplate.update(
//                    sql, 
//                    bankName,
//                    accountNumber,
//                    ifscCode,
//                    accountHolder,
//                    branchName,
//                    id
//                );
//            }
//            
//            return "redirect:/bank/admin?message=Details updated successfully";
//        } catch (IOException e) {
//            return "redirect:/bank/admin?message=Error updating QR code: " + e.getMessage();
//        }
//    }
//    
//    // Delete bank details
//    @PostMapping("/delete")
//    public String deleteBankDetails(@RequestParam int id) {
//        try {
//            String sql = "DELETE FROM bank_details WHERE id = ?";
//            jdbcTemplate.update(sql, id);
//            return "redirect:/bank/admin?message=Details deleted successfully";
//        } catch (Exception e) {
//            return "redirect:/bank/admin?message=Error deleting details: " + e.getMessage();
//        }
//    }
//    
//    // User view for bank details
//    @GetMapping("/view")
//    public String viewBankDetails(Model model) {
//        try {
//            String sql = "SELECT * FROM bank_details";
//            List<Map<String, Object>> details = jdbcTemplate.queryForList(sql);
//            
//            // Convert QR code images to Base64 for HTML display
//            for (Map<String, Object> detail : details) {
//                if (detail.get("qr_code_image") != null) {
//                    byte[] qrImageBytes = (byte[]) detail.get("qr_code_image");
//                    String base64Image = Base64.getEncoder().encodeToString(qrImageBytes);
//                    // Create a new map entry for the Base64 image
//                    detail.put("qr_code_base64", base64Image);
//                }
//            }
//            
//            model.addAttribute("bankDetails", details);
//        } catch (Exception e) {
//            // If there's an error, add an empty list to avoid null pointer
//            model.addAttribute("bankDetails", new ArrayList<>());
//        }
//        
//        return "userbankdisplay";
//    }
//    
//    // API to get QR code image
//    @GetMapping("/qrcode/{id}")
//    public ResponseEntity<Resource> getQRCode(@PathVariable int id) {
//        try {
//            String sql = "SELECT qr_code_image, qr_code_filename FROM bank_details WHERE id = ?";
//            Map<String, Object> result = jdbcTemplate.queryForMap(sql, id);
//            
//            if (result != null && result.get("qr_code_image") != null) {
//                byte[] qrCodeImage = (byte[]) result.get("qr_code_image");
//                String filename = (String) result.get("qr_code_filename");
//                
//                ByteArrayResource resource = new ByteArrayResource(qrCodeImage);
//                
//                return ResponseEntity.ok()
//                    .contentLength(qrCodeImage.length)
//                    .contentType(MediaType.parseMediaType("image/png"))
//                    .header("Content-Disposition", "inline; filename=\"" + filename + "\"")
//                    .body(resource);
//            }
//        } catch (Exception e) {
//            // Log the error
//            System.err.println("Error retrieving QR code: " + e.getMessage());
//        }
//        
//        return ResponseEntity.notFound().build();
//    }
//    
//    // Get single bank detail for editing
//    @GetMapping("/detail/{id}")
//    @ResponseBody
//    public Map<String, Object> getBankDetail(@PathVariable int id) {
//        String sql = "SELECT id, bank_name, account_number, ifsc_code, account_holder, branch_name FROM bank_details WHERE id = ?";
//        try {
//            return jdbcTemplate.queryForMap(sql, id);
//        } catch (Exception e) {
//            // Log error and return empty response
//            System.err.println("Error retrieving bank detail: " + e.getMessage());
//            return Map.of("error", "Bank detail not found");
//        }
//    }
//}





package MajorProject.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Base64;

@Controller
@RequestMapping("/bank")
public class BankDetailsController {
    
    @Autowired
    private JdbcTemplate jdbcTemplate;
    
   //  Admin page to view and manage bank details
//    @GetMapping("/admin")
//    public String adminPage(Model model, @RequestParam(required = false) String message) {
//        try {
//            String sql = "SELECT * FROM bank_details";
//            List<Map<String, Object>> details = jdbcTemplate.queryForList(sql);
//            
//            // Debug: Print number of records found
//            System.out.println("Found " + details.size() + " bank details records");
//            
//            // Debug: Print the first record if available
//            if (!details.isEmpty()) {
//                System.out.println("First record: " + details.get(0));
//            }
//            
//            model.addAttribute("bankDetails", details);
//        } catch (Exception e) {
//            // If there's an error, add an empty list to avoid null pointer
//            model.addAttribute("bankDetails", new ArrayList<>());
//            model.addAttribute("message", "Error loading bank details: " + e.getMessage());
//            e.printStackTrace(); // Print stack trace for better debugging
//        }
//        
//        if (message != null) {
//            model.addAttribute("message", message);
//        }
//        
//        return "ABank";
//    }
//    
    @GetMapping("/admin")
    public String adminPage(Model model, 
                            @RequestParam(required = false) String message,
                            @RequestParam(required = false, defaultValue = "A") String bankType) {
        try {
            String sql = "SELECT * FROM bank_details";
            List<Map<String, Object>> details = jdbcTemplate.queryForList(sql);

            // Debug: Print number of records found
            System.out.println("Found " + details.size() + " bank details records");

            // Debug: Print the first record if available
            if (!details.isEmpty()) {
                System.out.println("First record: " + details.get(0));
            }

            model.addAttribute("bankDetails", details);
        } catch (Exception e) {
            // If there's an error, add an empty list to avoid null pointer
            model.addAttribute("bankDetails", new ArrayList<>());
            model.addAttribute("message", "Error loading bank details: " + e.getMessage());
            e.printStackTrace(); // Print stack trace for better debugging
        }

        if (message != null) {
            model.addAttribute("message", message);
        }

        // Return different view based on bankType parameter
        if ("U".equalsIgnoreCase(bankType)) {
            return "UBank";
        } else {
            return "ABank";
        }
    }
    
    // User view for bank details
    @GetMapping("/view")
    public String viewBankDetails(Model model) {
        try {
            String sql = "SELECT * FROM bank_details";
            List<Map<String, Object>> details = jdbcTemplate.queryForList(sql);
            
            // Debug: Print number of records found
            System.out.println("Found " + details.size() + " bank details records for user view");
            
            // Convert QR code images to Base64 for HTML display
            for (Map<String, Object> detail : details) {
                if (detail.get("qr_code_image") != null) {
                    byte[] qrImageBytes = (byte[]) detail.get("qr_code_image");
                    String base64Image = Base64.getEncoder().encodeToString(qrImageBytes);
                    // Create a new map entry for the Base64 image
                    detail.put("qr_code_base64", base64Image);
                }
            }
            
            model.addAttribute("bankDetails", details);
        } catch (Exception e) {
            // If there's an error, add an empty list to avoid null pointer
            model.addAttribute("bankDetails", new ArrayList<>());
            model.addAttribute("message", "Error loading bank details: " + e.getMessage());
            e.printStackTrace(); // Print stack trace for better debugging
        }
        
        return "UBank";
    }
    
    // Keep all other methods as they are
    // Add new bank details with QR code image
    @PostMapping("/add")
    public String addBankDetails(
            @RequestParam String bankName,
            @RequestParam String accountNumber,
            @RequestParam String ifscCode,
            @RequestParam String accountHolder,
            @RequestParam String branchName,
            @RequestParam MultipartFile qrCodeImage) {
        
        try {
            String sql = "INSERT INTO bank_details (bank_name, account_number, ifsc_code, account_holder, branch_name, qr_code_image, qr_code_filename) VALUES (?, ?, ?, ?, ?, ?, ?)";
            
            if (qrCodeImage != null && !qrCodeImage.isEmpty()) {
                jdbcTemplate.update(
                    sql, 
                    bankName,
                    accountNumber,
                    ifscCode,
                    accountHolder,
                    branchName,
                    qrCodeImage.getBytes(),
                    qrCodeImage.getOriginalFilename()
                );
                return "redirect:/bank/admin?message=Bank details and QR code added successfully";
            } else {
                return "redirect:/bank/admin?message=Error: QR code image is required";
            }
        } catch (IOException e) {
            e.printStackTrace(); // Add better error logging
            return "redirect:/bank/admin?message=Error uploading QR code: " + e.getMessage();
        }
    }
    
    // Update existing bank details
    @PostMapping("/update")
    public String updateBankDetails(
            @RequestParam int id,
            @RequestParam String bankName,
            @RequestParam String accountNumber,
            @RequestParam String ifscCode,
            @RequestParam String accountHolder,
            @RequestParam String branchName,
            @RequestParam(required = false) MultipartFile qrCodeImage) {
        
        try {
            if (qrCodeImage != null && !qrCodeImage.isEmpty()) {
                String sql = "UPDATE bank_details SET bank_name=?, account_number=?, ifsc_code=?, account_holder=?, branch_name=?, qr_code_image=?, qr_code_filename=? WHERE id=?";
                jdbcTemplate.update(
                    sql, 
                    bankName,
                    accountNumber,
                    ifscCode,
                    accountHolder,
                    branchName,
                    qrCodeImage.getBytes(),
                    qrCodeImage.getOriginalFilename(),
                    id
                );
            } else {
                String sql = "UPDATE bank_details SET bank_name=?, account_number=?, ifsc_code=?, account_holder=?, branch_name=? WHERE id=?";
                jdbcTemplate.update(
                    sql, 
                    bankName,
                    accountNumber,
                    ifscCode,
                    accountHolder,
                    branchName,
                    id
                );
            }
            
            return "redirect:/bank/admin?message=Details updated successfully";
        } catch (IOException e) {
            e.printStackTrace(); // Add better error logging
            return "redirect:/bank/admin?message=Error updating QR code: " + e.getMessage();
        }
    }
    
    // Delete bank details
    @PostMapping("/delete")
    public String deleteBankDetails(@RequestParam int id) {
        try {
            String sql = "DELETE FROM bank_details WHERE id = ?";
            jdbcTemplate.update(sql, id);
            return "redirect:/bank/admin?message=Details deleted successfully";
        } catch (Exception e) {
            e.printStackTrace(); // Add better error logging
            return "redirect:/bank/admin?message=Error deleting details: " + e.getMessage();
        }
    }
    
    // API to get QR code image
    @GetMapping("/qrcode/{id}")
    public ResponseEntity<Resource> getQRCode(@PathVariable int id) {
        try {
            String sql = "SELECT qr_code_image, qr_code_filename FROM bank_details WHERE id = ?";
            Map<String, Object> result = jdbcTemplate.queryForMap(sql, id);
            
            if (result != null && result.get("qr_code_image") != null) {
                byte[] qrCodeImage = (byte[]) result.get("qr_code_image");
                String filename = (String) result.get("qr_code_filename");
                
                ByteArrayResource resource = new ByteArrayResource(qrCodeImage);
                
                return ResponseEntity.ok()
                    .contentLength(qrCodeImage.length)
                    .contentType(MediaType.parseMediaType("image/png"))
                    .header("Content-Disposition", "inline; filename=\"" + filename + "\"")
                    .body(resource);
            }
        } catch (Exception e) {
            // Log the error
            System.err.println("Error retrieving QR code: " + e.getMessage());
            e.printStackTrace();
        }
        
        return ResponseEntity.notFound().build();
    }
    
    // Get single bank detail for editing
    @GetMapping("/detail/{id}")
    @ResponseBody
    public Map<String, Object> getBankDetail(@PathVariable int id) {
        String sql = "SELECT id, bank_name, account_number, ifsc_code, account_holder, branch_name FROM bank_details WHERE id = ?";
        try {
            return jdbcTemplate.queryForMap(sql, id);
        } catch (Exception e) {
            // Log error and return empty response
            System.err.println("Error retrieving bank detail: " + e.getMessage());
            e.printStackTrace();
            return Map.of("error", "Bank detail not found");
        }
    }
}