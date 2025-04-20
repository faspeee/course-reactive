package com.example.stream.spring.courses.reactive.example.configuration;

import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.DefaultErrorAttributes;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.ServerRequest;

import java.util.Map;

/**
 * {@code ConfigurationError} customizes the global error attributes for WebFlux.
 * <p>
 * By default, Spring Boot omits the exception message from the response body in production.
 * This configuration ensures that the exception message is included in error responses,
 * which can be useful for debugging or providing meaningful client feedback.
 * </p>
 *
 * <p>
 * This customization affects all {@code @RestControllerAdvice} and functional error handling.
 * </p>
 */
@Configuration
public class ConfigurationError {

    /**
     * Overrides the default {@link ErrorAttributes} to include the {@code "message"} field
     * in error responses.
     *
     * @return a customized {@link ErrorAttributes} bean
     */
    @Bean
    public ErrorAttributes errorAttributes() {
        return new DefaultErrorAttributes() {
            @Override
            public Map<String, Object> getErrorAttributes(ServerRequest request, ErrorAttributeOptions options) {
                // Include the exception's message in the response payload
                return super.getErrorAttributes(request, options.including(ErrorAttributeOptions.Include.MESSAGE));
            }
        };
    }
}
