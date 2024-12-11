# HealthSync

HealthSync is a web application that lets doctors and patient connect in appointments.

## Building

To build the application run the following. This will create a war file with everything needed to deploy the application.

```sh
maven clean package
```

## Testing

To test the Spring API run:

```sh
maven test
```

To test the React SPA run the following in the `frontend` directory

```sh
npm run test
```

## Configuration

- The Spring API can be configured in the `webapp/src/main/resources/application.properties` file. 
- The React SPA can be configured in the `frontend/.env` file.

## Documentation

A Swagger documentation can be found in the `webapp/src/main/webapp/static/docs/swagger.yaml` file.

## Test Users

In the [deployed application](http://old-pawserver.it.itba.edu.ar/paw-2023a-02) you can use these test users.

### Doctor

- **email**: <cuini123+10@gmail.com>
- **password**: 1234

### Patient

- **email**: <sballerini+10@itba.edu.ar>
- **password**: 1234
