# Test Suite for SCRUM-11693: Apply/Remove Coupon in Cart

## Overview

This directory contains comprehensive test artifacts for the coupon application and removal feature in the shopping cart API.

## Directory Structure

```
test/
├── README.md                          # This file
├── api_test_cases.md                  # Detailed test case documentation
├── postman/
│   ├── collection.json                # Postman collection with 14 API tests
│   └── environment.json               # Postman environment variables
└── reports/
    └── execution_report.md            # Test execution report and results
```

## Test Artifacts

### 1. Test Case Documentation (`api_test_cases.md`)

**Purpose**: Comprehensive test case documentation covering all scenarios

**Contents**:
- 30 detailed test cases
- Test case IDs, scenarios, preconditions, steps, and expected results
- Coverage matrix mapping requirements to test cases
- Test data requirements
- Traceability matrix

**Categories Covered**:
- Happy path scenarios (4 tests)
- Validation tests (6 tests)
- Business logic tests (5 tests)
- Error handling tests (4 tests)
- Security tests (3 tests)
- Performance tests (1 test)
- Edge cases (7 tests)

### 2. Postman Collection (`postman/collection.json`)

**Purpose**: Executable API test collection for automated testing

**Contents**:
- 14 API test requests organized in folders
- Pre-request scripts for test setup
- Test scripts with assertions
- Dynamic variable chaining
- Global test scripts for common validations

**Test Groups**:
1. **Apply Coupon Tests** (9 tests)
   - Happy path scenarios
   - Validation failures
   - Security tests
   - Business rule violations

2. **Remove Coupon Tests** (4 tests)
   - Successful removal
   - Error scenarios
   - Security tests

3. **Validate Coupon Tests** (4 tests)
   - Valid coupon validation
   - Expired coupon
   - Insufficient cart value
   - Non-existent coupon

4. **Security Tests** (2 tests)
   - Unauthorized access
   - XSS prevention

5. **Performance Tests** (1 test)
   - Concurrent user simulation

### 3. Postman Environment (`postman/environment.json`)

**Purpose**: Environment configuration for test execution

**Variables**:
- `baseUrl`: API base URL (default: http://localhost:8080)
- `authToken`: JWT authentication token
- `cartId`: Default cart ID for testing
- `lowValueCartId`: Cart with low total for min value tests
- `highValueCartId`: Cart with high total for max discount tests
- `eligibleProductCartId`: Cart with eligible products
- `ineligibleProductCartId`: Cart with ineligible products
- `otherUserCartId`: Another user's cart for security tests
- `appliedCartId`: Cart with applied coupon (set dynamically)
- `validCouponCode`: Valid coupon code
- `expiredCouponCode`: Expired coupon code
- `invalidCouponCode`: Non-existent coupon code

### 4. Execution Report (`reports/execution_report.md`)

**Purpose**: Test execution results and analysis

**Contents**:
- Executive summary
- Overall statistics
- Endpoint-wise coverage
- Test category breakdown
- Detailed test results
- Performance metrics
- Error distribution
- Test data summary
- Recommendations
- CI/CD integration guide

## Quick Start

### Prerequisites

1. **Application Running**:
   ```bash
   cd code/
   mvn spring-boot:run
   ```

2. **Database Setup**:
   - MySQL running on localhost:3306
   - Database `ecommerce` created
   - Test data loaded (see execution_report.md for SQL script)

3. **Authentication**:
   - Obtain valid JWT token for User A
   - Update `authToken` in environment.json

### Running Tests

#### Option 1: Using Postman GUI

1. Open Postman
2. Import collection: `postman/collection.json`
3. Import environment: `postman/environment.json`
4. Select the imported environment
5. Update `authToken` variable with valid JWT
6. Click "Run Collection" in Collection Runner
7. Review results

#### Option 2: Using Newman CLI

```bash
# Install Newman
npm install -g newman newman-reporter-htmlextra

# Run tests
newman run test/postman/collection.json \n  -e test/postman/environment.json \n  --reporters cli,htmlextra \n  --reporter-htmlextra-export test/reports/newman-report.html

# View HTML report
open test/reports/newman-report.html
```

#### Option 3: Using Maven (if integrated)

```bash
mvn clean test -Dtest=CouponApiTest
```

### Updating Environment Variables

Edit `postman/environment.json` to update:

```json
{
  "key": "baseUrl",
  "value": "https://your-test-environment.com",
  "enabled": true
},
{
  "key": "authToken",
  "value": "your-actual-jwt-token",
  "enabled": true
}
```

## Test Coverage Summary

### API Endpoints Tested

| Endpoint | Method | Test Cases | Coverage |
|----------|--------|-----------|----------|
| /api/cart/{cartId}/coupon | POST | 9 | 100% |
| /api/cart/{cartId}/coupon | DELETE | 4 | 100% |
| /api/coupon/validate | POST | 4 | 100% |

### Test Type Distribution

- **Functional Tests**: 40% (12 tests)
- **Validation Tests**: 20% (6 tests)
- **Security Tests**: 10% (3 tests)
- **Error Handling**: 13% (4 tests)
- **Business Logic**: 17% (5 tests)

### Requirements Traceability

All functional and non-functional requirements from SCRUM-11693 are covered:

✅ Apply coupon to cart
✅ Remove coupon from cart
✅ Validate coupon code
✅ Coupon expiry validation
✅ Minimum cart value check
✅ Product-specific coupons
✅ Non-stackable coupons
✅ Authentication/Authorization
✅ Input validation
✅ Performance (< 500ms)
✅ Security (HTTPS, encryption, XSS prevention)
✅ Concurrent user handling

## Test Data Management

### Test Data Location

Test data is defined in:
1. `api_test_cases.md` - Test data requirements section
2. `execution_report.md` - Database seed script
3. `postman/environment.json` - Runtime variables

### Test Data Reset

Before each test run, ensure:
1. Database is in clean state
2. Test coupons are loaded
3. Test carts are created
4. No coupons are pre-applied to test carts

## Continuous Integration

### GitHub Actions Integration

Add to `.github/workflows/api-tests.yml`:

```yaml
name: API Tests

on:
  push:
    branches: [ main, develop ]
  pull_request:
    branches: [ main ]

jobs:
  api-tests:
    runs-on: ubuntu-latest
    
    services:
      mysql:
        image: mysql:8.0
        env:
          MYSQL_ROOT_PASSWORD: root
          MYSQL_DATABASE: ecommerce
        ports:
          - 3306:3306
        options: >-
          --health-cmd="mysqladmin ping"
          --health-interval=10s
          --health-timeout=5s
          --health-retries=3
    
    steps:
      - uses: actions/checkout@v2
      
      - name: Set up JDK 21
        uses: actions/setup-java@v2
        with:
          java-version: '21'
          distribution: 'temurin'
          
      - name: Build with Maven
        run: mvn clean package -DskipTests
        
      - name: Start Application
        run: |
          java -jar target/*.jar &
          sleep 30
          
      - name: Setup Node.js
        uses: actions/setup-node@v2
        with:
          node-version: '16'
          
      - name: Install Newman
        run: npm install -g newman newman-reporter-htmlextra
        
      - name: Run API Tests
        run: |
          newman run test/postman/collection.json \n            -e test/postman/environment.json \n            --reporters cli,junit,htmlextra \n            --reporter-junit-export test/reports/junit-report.xml \n            --reporter-htmlextra-export test/reports/newman-report.html
            
      - name: Publish Test Results
        uses: EnricoMi/publish-unit-test-result-action@v1
        if: always()
        with:
          files: test/reports/junit-report.xml
          
      - name: Upload Test Report
        uses: actions/upload-artifact@v2
        if: always()
        with:
          name: test-reports
          path: test/reports/
```

## Troubleshooting

### Common Issues

#### 1. Authentication Failures (401)
**Cause**: Invalid or expired JWT token
**Solution**: 
- Obtain fresh JWT token from login endpoint
- Update `authToken` in environment.json

#### 2. Connection Refused
**Cause**: Application not running
**Solution**:
- Start SpringBoot application: `mvn spring-boot:run`
- Verify application is listening on port 8080

#### 3. Test Data Not Found (404)
**Cause**: Database not seeded with test data
**Solution**:
- Run database seed script from execution_report.md
- Verify data exists: `SELECT * FROM coupon;`

#### 4. Newman Command Not Found
**Cause**: Newman not installed
**Solution**:
```bash
npm install -g newman
```

## Maintenance

### Updating Tests

1. **Add New Test Case**:
   - Document in `api_test_cases.md`
   - Add request to `postman/collection.json`
   - Update coverage matrix

2. **Modify Existing Test**:
   - Update test case documentation
   - Update Postman request/assertions
   - Re-run tests to verify

3. **Update Test Data**:
   - Modify seed script in execution_report.md
   - Update environment variables
   - Reset database and re-run tests

### Version Control

- All test artifacts are version controlled in Git
- Follow semantic versioning for test suite releases
- Tag releases: `test-suite-v1.0.0`

## Support

For questions or issues:
1. Review test case documentation
2. Check execution report for troubleshooting
3. Contact QA team
4. Create issue in project tracker

## License

Internal use only - Proprietary

---

**Last Updated**: 2026-01-08

**Version**: 1.0.0

**Status**: Ready for Execution