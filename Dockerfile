# Build the project
FROM maven:3.9.0-eclipse-temurin-8 AS builder

# Set working directory in the container
WORKDIR /app

# Copy everything from the root directory to the container
COPY . .

# Install OpenSSH for key generation
RUN apt-get update && apt-get install -y openssh-client && apt-get clean

# Generate a secure key using OpenSSH
RUN ssh-keygen -t rsa -b 4096 -m PEM -N "" -f ./webapp/src/main/resources/jwtPK

# Accept build arguments for React
ARG PUBLIC_URL
ARG GOOGLE_MAPS_API_KEY

# Set environment variables for React during build
ENV REACT_APP_PUBLIC_URL=/ \
    REACT_APP_API_URL=${PUBLIC_URL%/}/api \
    REACT_APP_GOOGLE_MAPS_API_KEY=$GOOGLE_MAPS_API_KEY

# Build the application using Maven
RUN mvn clean package

# Deploy in Tomcat 8.5.100 image
FROM tomcat:8.5.100-jdk8

# Set working directory in the container
WORKDIR /usr/local/tomcat/webapps/

# Copy the built WAR file from the builder stage
COPY --from=builder /app/webapp/target/webapp.war ./ROOT.war

# Expose the default Tomcat port
EXPOSE 8080

# Start the Tomcat server
CMD ["catalina.sh", "run"]
