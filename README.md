# Farmco Connect

Spring Boot REST API for managing Rwanda’s agricultural data with a complete administrative location hierarchy (Province → District → Sector → Cell → Village), farmers, harvests, payments, and user assignments.

## Features
- Full Rwanda location hierarchy with parent/child validation
- Users saved at **village** level (parents provide cell/sector/district/province)
- Farmers linked to locations
- Harvests auto-create payments
- One-to-one, one-to-many, and many-to-many relationships
- Pagination and sorting for users and farmers
- Province (or any level) user filtering

## Tech Stack
- Java 21
- Spring Boot
- Spring Data JPA
- PostgreSQL

## Project Structure
```
farmco_connect/
  backend/
    src/main/java/com/farmco/farmco_connect/
      controller/
      model/
      repository/
      service/
    src/main/resources/
    postman/
```

## Prerequisites
- Java 21
- Maven (or Maven Wrapper)
- PostgreSQL

## Configuration
Edit database connection in:
`backend/src/main/resources/application.properties`

Example:
```
spring.datasource.url=jdbc:postgresql://localhost:5432/farmcoDB
spring.datasource.username=postgres
spring.datasource.password=YOUR_PASSWORD
```

## Run the Application
```powershell
cd c:\Users\KARLIE\Desktop\java\mavenapp\farmco_connect\backend
./mvnw spring-boot:run
```

API base URL: `http://localhost:8080`

## Core API Endpoints

### Location Hierarchy
- `POST /api/locations/save`
- `GET /api/locations/provinces`
- `GET /api/locations/districts?provinceId=...`
- `GET /api/locations/sectors?districtId=...`
- `GET /api/locations/cells?sectorId=...`
- `GET /api/locations/villages?cellId=...`

### Users
- `POST /api/users/save?village={villageId|code|name}`
- `GET /api/users/by-province?province=KGL`
- `GET /api/users/by-location?value=Gasabo`
- `GET /api/users/paged?page=0&size=5&sortBy=username&direction=asc`

### Farmers, Harvests, Payments
- `POST /api/farmers/save?locationId={villageId}`
- `GET /api/farmers/paged?page=0&size=5&sortBy=name&direction=asc`
- `POST /api/harvests/save?farmerId=...`
- `PUT /api/payments/withdraw?harvestId=...`

## Postman & Testing
Postman collection and environment are available in:
`backend/postman/`

To generate a single HTML test report with Newman:
```powershell
npm install -g newman newman-reporter-htmlextra
newman run .\postman\FarmcoConnect_Practical_Exam.postman_collection.json `
  -e .\postman\FarmcoConnect_Local.postman_environment.json `
  -r cli,htmlextra,json `
  --reporter-htmlextra-export .\postman\Farmco_Test_Report.html `
  --reporter-json-export .\postman\Farmco_Test_Report.json
```

## Notes
- Always create locations in order: Province → District → Sector → Cell → Village.
- Users must be saved using **village** only.
- Province-level filtering works through the parent chain.

## License
For academic use.
