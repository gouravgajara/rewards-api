# Reward Service API

Spring Boot REST API for calculating customer reward points based on transactions.

## Tech Stack

- Java 17
- Spring Boot
- Spring Data JPA
- H2 Database
- Maven
- JUnit 5
- Mockito

## Project Structure

```
src/main/java/com/retail/rewards
│── controller
│── dto
│── entity
│── exception
│── repository
│── service
│   └── impl
```

## H2 Database

H2 console:

http://localhost:8080/h2-console

JDBC URL:

```
jdbc:h2:mem:rewardsdb
```

## Run Application

```bash
mvn spring-boot:run
```

## API Endpoints

### Get All Rewards

```http
GET /api/rewards
```

### Sample Response

```json
[
  {
    "customerId": 101,
    "customerName": "John",
    "monthlyRewards": [
      {
        "month": "May",
        "points": 90
      }
    ],
    "totalRewards": 90
  }
]
```

### Get Rewards By Customer Id

```http
GET /api/rewards/101
```

### Sample Response

```json
{
  "customerId": 101,
  "customerName": "John",
  "monthlyRewards": [
    {
      "month": "May",
      "points": 90
    }
  ],
  "totalRewards": 90
}
```

### Customer Not Found Response

```json
{
  "message": "Customer not found with id: 999"
}
```

## Test Coverage

Included:

- Unit Tests
- Controller Tests
- Integration Tests
- Positive and Negative Scenarios

## Reward Calculation Logic

- Amount <= 50 → 0 points
- Amount between 51 and 100 → 1 point per dollar over 50
- Amount > 100 → 2 points per dollar over 100 + 50 points

Example:

```text
Transaction Amount = 120
Reward Points = 90
```
