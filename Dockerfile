FROM eclipse-temurin:17-jdk-alpine

# Set working directory
WORKDIR /app

# Copy the JAR file into the image
COPY target/*.jar app.jar

# Create upload directory
RUN mkdir -p /app/uploads && chmod 777 /app/uploads

# Set the timezone
ENV TZ=Asia/Kolkata

# Set environment variables
ENV SPRING_DATASOURCE_URL=jdbc:mysql://mysql-db:3306/hotel
ENV SPRING_DATASOURCE_DRIVER_CLASS_NAME=com.mysql.cj.jdbc.Driver

# Expose the port the app runs on
EXPOSE 8080

# Command to run the application
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
