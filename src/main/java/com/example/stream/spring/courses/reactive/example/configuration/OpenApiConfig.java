package com.example.stream.spring.courses.reactive.example.configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

/**
 * OpenAPI configuration for the Course Reactive API.
 */
@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Course Reactive API",
                version = "1.0.0",
                description = "API documentation for the Course Reactive application, providing endpoints for managing courses and instructors.",
                contact = @Contact(
                        name = "Your Name",
                        email = "your.email@example.com",
                        url = "https://github.com/faspeee/course-reactive"
                ),
                license = @License(
                        name = "MIT License",
                        url = "https://opensource.org/licenses/MIT"
                )
        ),
        servers = {
                @Server(
                        url = "http://localhost:8585",
                        description = "Local development server"
                )
        }
)
public class OpenApiConfig {
    // This class is used to configure OpenAPI metadata for the application.
}
