# MyProject - Cart Coupon Management System

## Overview
SpringBoot application for managing cart coupons - apply, remove, and validate coupons.

## Features
- Apply coupon to cart
- Remove coupon from cart
- Validate coupon code
- Automatic discount calculation
- Support for percentage and fixed amount discounts
- Minimum cart value validation
- Coupon expiry validation

## Technology Stack
- Java 21
- Spring Boot 3.5.9
- Spring Data JPA
- H2 Database (in-memory)
- Lombok
- MapStruct
- JUnit 5
- Mockito
- JaCoCo (code coverage)
- Swagger/OpenAPI

## Prerequisites
- JDK 21
- Maven 3.6+

## Build and Run

### Build the project
```bash
mvn clean install
```

### Run the application
```bash
mvn spring-boot:run
```

The application will start on `http://localhost:8080/api`

### Run tests
```bash
mvn test
```

### Generate code coverage report
```bash
mvn jacoco:report
```
Report will be available at `target/site/jacoco/index.html`

## API Endpoints

### 1. Apply Coupon to Cart
**POST** `/api/cart/{cartId}/coupon`

**Request Body:**
```json
{
  "couponCode": "SAVE20"
}
```

**Response:**
```json
{
  "id": "1",
  "userId": "user123",
  "items": [...],
  "total": 104.0,
  "originalTotal": 130.0,
  "appliedCoupon": "SAVE20",
  "discountAmount": 26.0
}
```

### 2. Remove Coupon from Cart
**DELETE** `/api/cart/{cartId}/coupon`

**Response:** 204 No Content

### 3. Validate Coupon
**POST** `/api/coupon/validate`

**Request Body:**
```json
{
  "couponCode": "SAVE20",
  "cartId": "1"
}
```

**Response:**
```json
{
  "valid": true,
  "couponCode": "SAVE20",
  "discountType": "PERCENTAGE",
  "discountValue": 20.0,
  "minCartValue": 50.0,
  "expiryDate": "2026-02-08",
  "message": "Coupon is valid and applicable",
  "applicableItems": []
}
```

## API Documentation

Swagger UI is available at: `http://localhost:8080/api/swagger-ui.html`

OpenAPI JSON: `http://localhost:8080/api/api-docs`

## Sample Data

The application initializes with sample data:

### Coupons:
- **SAVE20**: 20% discount, min cart value: $50, expires in 30 days
- **FLAT10**: $10 fixed discount, min cart value: $30, expires in 60 days
- **EXPIRED**: 15% discount (already expired)

### Cart:
- Cart ID: 1
- User ID: user123
- Items:
  - Product prod001: 2 x $50 = $100
  - Product prod002: 1 x $30 = $30
- Total: $130

## H2 Console

H2 database console is available at: `http://localhost:8080/api/h2-console`

**Connection details:**
- JDBC URL: `jdbc:h2:mem:myproject`
- Username: `sa`
- Password: (empty)

## CORS Configuration

CORS is configured to allow requests from:
- `http://localhost:4200` (Angular development server)

## Security

Basic security is configured. All API endpoints are currently accessible without authentication for development purposes.

## Project Structure

```
code/
├── src/
│   ├── main/
│   │   ├── java/com/myproject/
│   │   │   ├── controllers/          # REST controllers
│   │   │   ├── models/
│   │   │   │   ├── dtos/            # Data Transfer Objects
│   │   │   │   ├── entities/        # JPA entities
│   │   │   │   └── repositories/    # JPA repositories
│   │   │   ├── services/            # Service interfaces and implementations
│   │   │   ├── config/              # Configuration classes
│   │   │   ├── exceptions/          # Custom exceptions
│   │   │   └── MyprojectApplication.java
│   │   └── resources/
│   │       └── application.properties
│   └── test/                        # Unit and integration tests
└── pom.xml
```

## Error Handling

The application uses global exception handling with standardized error responses:

```json
{
  "timestamp": "2026-01-08T12:00:00",
  "traceId": "abc123xyz",
  "errorCode": "COUPON_EXPIRED",
  "message": "Coupon code invalid or expired",
  "details": []
}
```

## Testing

The project includes comprehensive unit tests for:
- Services (CartService, CouponService, CartCouponService)
- Controllers (CartCouponController, CouponController)
- Code coverage reports via JaCoCo

## CI/CD

GitHub Actions workflow is configured in `.github/workflows/build.yml` for:
- Building the application
- Running tests
- Generating coverage reports
- Uploading artifacts

## License

This project is part of the SCRUM-11693 user story implementation.
