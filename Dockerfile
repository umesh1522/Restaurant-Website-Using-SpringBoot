FROM eclipse-temurin:17-jdk-alpine

# Set working directory
WORKDIR /app

# Copy the JAR file into the image
COPY target/*.jar app.jar

# Create upload directory and give full permissions
RUN mkdir -p /app/uploads && chmod -R 777 /app/uploads

# Set timezone
ENV TZ=Asia/Kolkata

# Set Spring environment variables (you can override these in Render dashboard too)
ENV SPRING_DATASOURCE_URL=jdbc:mysql://mysql-db:3306/hotel
ENV SPRING_DATASOURCE_DRIVER_CLASS_NAME=com.mysql.cj.jdbc.Driver

# Expose application port
EXPOSE 8080

# Run the Spring Boot application
ENTRYPOINT ["java", "-jar", "app.jar"]

