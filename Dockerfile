# Use a Debian-based OpenJDK image as the base image
FROM openjdk:23-jdk-slim

# Set the working directory inside the container
WORKDIR /app

# Install xargs (part of findutils)
RUN apt-get update && apt-get install -y findutils

# Copy the build/install/declaratii-anaf directory to /app
COPY build/install/declaratii-anaf /app

# Expose the port the app runs on
EXPOSE 5000

# Command to run the application
CMD ["/app/bin/declaratii-anaf"]
