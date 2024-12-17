# **HealthSync**

HealthSync is a web application designed to seamlessly connect doctors and patients through appointments. The application features:
- A **Single Page Application (SPA)** built with React.
- A backend powered by **Jersey** and **Spring**, exposing a RESTful API.

---

## **Configuration**

The application can be customized using environment variables. These variables can be defined in a `.env` file in the root directory. Below is the list of configurable environment variables:

```bash
MAIL_USERNAME=your-google-account-username   # Username for the Google mailing account
MAIL_PASSWORD=your-google-account-password   # Password for the Google mailing account
GOOGLE_MAPS_API_KEY=your-api-key             # API key for Google Maps integration in the React SPA
PUBLIC_URL=http://your-domain-or-ip:port     # Public URL for accessing the application (default: http://localhost:8080)
```


---

## **Running the Application**

A Docker Compose file is provided to build and run the application, including:
1. A **Tomcat** container running the web application.
2. A **PostgreSQL** database container configured for the Tomcat server.

Before starting the application, ensure you have configured the required environment variables. This configuration is **mandatory** for the application to work correctly.

Once the variables are set, start the application by running the following command in the root directory:
```sh
docker compose up -d
```

This will:
- Package the application using Maven.
- Deploy the application to a Tomcat server.
- Spin up the PostgreSQL database.

The application will be accessible at: 
**[http://localhost:8080](http://localhost:8080)** by default. This can be changed in the compose file.

---


## **Swagger API Documentation**

The API documentation is available in the swagger.yaml file located at: `webapp/src/main/webapp/static/docs/swagger.yaml` 

You can use tools like [Swagger Editor](https://editor.swagger.io/) to view the API documentation.
