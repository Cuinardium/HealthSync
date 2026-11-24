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

# Deploy in Tomcat 8.5.100 image (hardened: non-root, pre-expanded WAR, no sample apps)
FROM tomcat:8.5.100-jdk8

RUN rm -rf /usr/local/tomcat/webapps/* /usr/local/tomcat/webapps.dist \
 && groupadd -g 10001 tomcat \
 && useradd -u 10001 -g tomcat -M -d /usr/local/tomcat -s /usr/sbin/nologin tomcat

# Expand the WAR at build time so the root filesystem can be read-only at runtime
COPY --from=builder /app/webapp/target/webapp.war /tmp/ROOT.war
RUN mkdir -p /usr/local/tomcat/webapps/ROOT /usr/local/tomcat/webapps/logs /usr/local/tomcat/conf/Catalina/localhost \
 && cd /usr/local/tomcat/webapps/ROOT && jar xf /tmp/ROOT.war && rm /tmp/ROOT.war \
 && chown -R tomcat:tomcat /usr/local/tomcat/logs /usr/local/tomcat/work /usr/local/tomcat/temp \
      /usr/local/tomcat/webapps/logs /usr/local/tomcat/conf/Catalina

# logback writes to ./logs relative to this directory
WORKDIR /usr/local/tomcat/webapps/
USER 10001:10001

EXPOSE 8080
CMD ["catalina.sh", "run"]
