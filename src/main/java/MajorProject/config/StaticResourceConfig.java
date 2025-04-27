//package MajorProject.config;
//
//
//
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
//import java.nio.file.Paths;
//
//@Configuration
//public class StaticResourceConfig implements WebMvcConfigurer {
//
//    @Override
//    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        // Serve files from "uploads/payments" as "/uploads/payments/**"
//        String uploadPath = Paths.get("uploads").toAbsolutePath().toUri().toString();
//        registry.addResourceHandler("/uploads/**")
//                .addResourceLocations(uploadPath);
//    }
//}
package MajorProject.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class StaticResourceConfig implements WebMvcConfigurer {

    // Define a consistent upload directory for the entire application
    public static final Path UPLOAD_BASE_DIR = Paths.get("uploads");

    // Constructor to ensure the base upload directory exists
    public StaticResourceConfig() {
        try {
            if (!Files.exists(UPLOAD_BASE_DIR)) {
                Files.createDirectories(UPLOAD_BASE_DIR);
                System.out.println("Created base upload directory at: " + UPLOAD_BASE_DIR.toAbsolutePath());
            }
        } catch (IOException e) {
            System.err.println("Failed to create base upload directory: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Get the absolute path to serve files from
        String uploadPath = UPLOAD_BASE_DIR.toAbsolutePath().normalize().toUri().toString();
        
        // Configure the resource handler
        registry.addResourceHandler("/uploads/**")
               .addResourceLocations(uploadPath);
        
        System.out.println("Serving static resources from: " + uploadPath);
    }
}
