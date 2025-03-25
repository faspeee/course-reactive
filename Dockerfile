# Stage 1: Build the application using GraalVM
FROM ghcr.io/graalvm/graalvm-ce:latest AS build

# Set the working directory
WORKDIR /app

# Install Native Image component
RUN gu install native-image

# Copy the project files
COPY . .

# Build the application
RUN ./mvnw package -Pnative -DskipTests

# Stage 2: Create a minimal image with the native executable
FROM alpine:latest

# Set the working directory
WORKDIR /app

# Install necessary dependencies
RUN apk add --no-cache libc6-compat

# Copy the native executable from the build stage
COPY --from=build /app/target/course-reactive .

# Expose the application port
EXPOSE 8080

# Run the application
CMD ["./course-reactive"]
