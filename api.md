# API Endpoints
base-url: http://127.0.0.1:8080/paw-2023a-02

| Endpoint                 | GET | POST | DELETE | PUT |
|--------------------------|-----|------|--------|-----|
| [/patients]              |     |      |        |     |
| [/patients/{id}]         | x   |      |        |     |
| [/doctors]               | x   |      |        |     |
| [/doctors/{id}]          | x   |      |        |     |
| [/healthinsurances]      | x   |      |        |     |
| [/healthinsurances/{id}] | x   |      |        |     |
| [/appointments]          | x   |      |        |     |
| [/appointments/{id}]     | x   |      |        |     |

#### /patients
#### /patients/{id}
#### /doctors

- GET
```sh
curl <basr-url>/doctors
```
- POST
```sh
curl -H'Content-Type:application/json' -d'{"email":"paw@email.com"}' <base-url>/doctors
```

#### /doctors/{id}
#### /healthinsurances
#### /healthinsurances/{id}
#### /appointments
#### /appointments/{id}

[/patients]: #patients
[/patients/{id}]: #patientsid
[/doctors]: #doctors
[/doctors/{id}]: #doctorsid
[/healthinsurances]: #healthinsurances
[/healthinsurances/{id}]: #healthinsurancesid
[/appointments]: #appointments
[/appointments/{id}]: #appointmentsid