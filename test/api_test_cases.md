# API Test Cases for SCRUM-11693: Apply/Remove Coupon in Cart

## Test Case Summary
- **Feature**: Apply/Remove Coupon in Cart
- **Total Test Cases**: 24
- **API Endpoints Covered**: 3
- **Test Types**: Functional, Validation, Security, Performance, Error Handling

---

## Test Case 1: Apply Valid Coupon to Cart (Happy Path)

**Test Case ID**: TC_COUPON_001

**Endpoint**: POST /api/cart/{cartId}/coupon

**Scenario**: Apply a valid, non-expired coupon to a cart with sufficient cart value

**Preconditions**:
- User is authenticated (logged-in user)
- Cart exists with cartId = 123
- Cart total is $100
- Coupon "SAVE10" exists, valid, not expired, discount = 10%, min cart value = $50

**Test Steps**:
1. Send POST request to /api/cart/123/coupon
2. Include valid JWT token in Authorization header
3. Send request body: {"couponCode": "SAVE10"}
4. Verify response status code
5. Verify response body contains updated cart with applied coupon
6. Verify cart total is recalculated correctly

**Expected Result**:
- HTTP Status: 200 OK
- Response body contains:
  - appliedCoupon: "SAVE10"
  - original total: $100
  - discount: $10
  - final total: $90
- Database updated with applied coupon
- Response time < 500ms

---

## Test Case 2: Apply Coupon with Percentage Discount

**Test Case ID**: TC_COUPON_002

**Endpoint**: POST /api/cart/{cartId}/coupon

**Scenario**: Apply percentage-based discount coupon

**Preconditions**:
- User authenticated
- Cart exists with total $200
- Coupon "PERCENT20" exists with 20% discount

**Test Steps**:
1. POST /api/cart/456/coupon
2. Body: {"couponCode": "PERCENT20"}
3. Verify discount calculation

**Expected Result**:
- HTTP Status: 200 OK
- Discount applied: $40 (20% of $200)
- Final total: $160
- Response time < 500ms

---

## Test Case 3: Apply Coupon with Fixed Amount Discount

**Test Case ID**: TC_COUPON_003

**Endpoint**: POST /api/cart/{cartId}/coupon

**Scenario**: Apply fixed amount discount coupon

**Preconditions**:
- User authenticated
- Cart exists with total $150
- Coupon "FLAT25" exists with $25 fixed discount

**Test Steps**:
1. POST /api/cart/789/coupon
2. Body: {"couponCode": "FLAT25"}
3. Verify discount calculation

**Expected Result**:
- HTTP Status: 200 OK
- Discount applied: $25
- Final total: $125
- Response time < 500ms

---

## Test Case 4: Apply Coupon to Guest User Cart

**Test Case ID**: TC_COUPON_004

**Endpoint**: POST /api/cart/{cartId}/coupon

**Scenario**: Guest user applies coupon to cart

**Preconditions**:
- Guest user (no authentication)
- Cart exists with session-based cartId
- Valid coupon available

**Test Steps**:
1. POST /api/cart/guest_cart_001/coupon
2. No Authorization header
3. Body: {"couponCode": "GUEST10"}

**Expected Result**:
- HTTP Status: 200 OK
- Coupon applied to cart
- Cart returned in response (not saved to DB)
- Response time < 500ms

---

## Test Case 5: Apply Coupon Below Minimum Cart Value

**Test Case ID**: TC_COUPON_005

**Endpoint**: POST /api/cart/{cartId}/coupon

**Scenario**: Attempt to apply coupon when cart total is below minimum required value

**Preconditions**:
- User authenticated
- Cart total: $30
- Coupon "SAVE50" requires minimum cart value of $50

**Test Steps**:
1. POST /api/cart/123/coupon
2. Body: {"couponCode": "SAVE50"}

**Expected Result**:
- HTTP Status: 400 Bad Request
- Error message: "Coupon not applicable. Minimum cart value required: $50"
- Cart remains unchanged
- Response time < 500ms

---

## Test Case 6: Apply Expired Coupon

**Test Case ID**: TC_COUPON_006

**Endpoint**: POST /api/cart/{cartId}/coupon

**Scenario**: Attempt to apply an expired coupon

**Preconditions**:
- User authenticated
- Cart exists
- Coupon "EXPIRED10" has expiry date in the past

**Test Steps**:
1. POST /api/cart/123/coupon
2. Body: {"couponCode": "EXPIRED10"}

**Expected Result**:
- HTTP Status: 400 Bad Request
- Error message: "Coupon code invalid or expired"
- Cart remains unchanged
- Response time < 500ms

---

## Test Case 7: Apply Non-Existent Coupon

**Test Case ID**: TC_COUPON_007

**Endpoint**: POST /api/cart/{cartId}/coupon

**Scenario**: Attempt to apply a coupon that doesn't exist

**Preconditions**:
- User authenticated
- Cart exists
- Coupon "INVALID123" does not exist in database

**Test Steps**:
1. POST /api/cart/123/coupon
2. Body: {"couponCode": "INVALID123"}

**Expected Result**:
- HTTP Status: 404 Not Found
- Error message: "Coupon not found"
- Cart remains unchanged
- Response time < 500ms

---

## Test Case 8: Apply Coupon with Blank Code

**Test Case ID**: TC_COUPON_008

**Endpoint**: POST /api/cart/{cartId}/coupon

**Scenario**: Validation test - blank coupon code

**Preconditions**:
- User authenticated
- Cart exists

**Test Steps**:
1. POST /api/cart/123/coupon
2. Body: {"couponCode": ""}

**Expected Result**:
- HTTP Status: 400 Bad Request
- Error message: "Coupon code cannot be blank"
- Validation error returned
- Response time < 500ms

---

## Test Case 9: Apply Coupon with Null Code

**Test Case ID**: TC_COUPON_009

**Endpoint**: POST /api/cart/{cartId}/coupon

**Scenario**: Validation test - null coupon code

**Preconditions**:
- User authenticated
- Cart exists

**Test Steps**:
1. POST /api/cart/123/coupon
2. Body: {"couponCode": null}

**Expected Result**:
- HTTP Status: 400 Bad Request
- Error message: "Coupon code cannot be blank"
- Validation error returned
- Response time < 500ms

---

## Test Case 10: Apply Coupon to Non-Existent Cart

**Test Case ID**: TC_COUPON_010

**Endpoint**: POST /api/cart/{cartId}/coupon

**Scenario**: Attempt to apply coupon to a cart that doesn't exist

**Preconditions**:
- User authenticated
- Cart with cartId = 999999 does not exist
- Valid coupon available

**Test Steps**:
1. POST /api/cart/999999/coupon
2. Body: {"couponCode": "SAVE10"}

**Expected Result**:
- HTTP Status: 404 Not Found
- Error message: "Cart not found"
- Response time < 500ms

---

## Test Case 11: Apply Coupon Without Authentication

**Test Case ID**: TC_COUPON_011

**Endpoint**: POST /api/cart/{cartId}/coupon

**Scenario**: Security test - attempt to apply coupon without authentication token

**Preconditions**:
- No authentication token provided
- Cart exists
- Valid coupon available

**Test Steps**:
1. POST /api/cart/123/coupon
2. No Authorization header
3. Body: {"couponCode": "SAVE10"}

**Expected Result**:
- HTTP Status: 401 Unauthorized
- Error message: "Authentication required"
- Response time < 500ms

---

## Test Case 12: Apply Coupon to Another User's Cart

**Test Case ID**: TC_COUPON_012

**Endpoint**: POST /api/cart/{cartId}/coupon

**Scenario**: Security test - user attempts to apply coupon to another user's cart

**Preconditions**:
- User A authenticated
- Cart belongs to User B
- Valid coupon available

**Test Steps**:
1. POST /api/cart/user_b_cart_id/coupon with User A token
2. Body: {"couponCode": "SAVE10"}

**Expected Result**:
- HTTP Status: 403 Forbidden
- Error message: "Access denied to this cart"
- Response time < 500ms

---

## Test Case 13: Apply Second Coupon When One Already Applied

**Test Case ID**: TC_COUPON_013

**Endpoint**: POST /api/cart/{cartId}/coupon

**Scenario**: Attempt to apply second coupon when one is already applied (non-stackable)

**Preconditions**:
- User authenticated
- Cart already has coupon "SAVE10" applied
- Attempting to apply "SAVE20"

**Test Steps**:
1. POST /api/cart/123/coupon
2. Body: {"couponCode": "SAVE20"}

**Expected Result**:
- HTTP Status: 400 Bad Request
- Error message: "Only one coupon can be applied at a time. Please remove existing coupon first."
- Cart remains with original coupon
- Response time < 500ms

---

## Test Case 14: Apply Product-Specific Coupon to Eligible Cart

**Test Case ID**: TC_COUPON_014

**Endpoint**: POST /api/cart/{cartId}/coupon

**Scenario**: Apply coupon that is restricted to specific products

**Preconditions**:
- User authenticated
- Cart contains product_id = 101 (eligible for coupon)
- Coupon "PRODUCT10" is applicable only to product_id = 101

**Test Steps**:
1. POST /api/cart/123/coupon
2. Body: {"couponCode": "PRODUCT10"}

**Expected Result**:
- HTTP Status: 200 OK
- Coupon applied successfully
- Discount calculated only on eligible product
- Response time < 500ms

---

## Test Case 15: Apply Product-Specific Coupon to Ineligible Cart

**Test Case ID**: TC_COUPON_015

**Endpoint**: POST /api/cart/{cartId}/coupon

**Scenario**: Attempt to apply product-specific coupon to cart without eligible products

**Preconditions**:
- User authenticated
- Cart contains product_id = 202 (not eligible)
- Coupon "PRODUCT10" is applicable only to product_id = 101

**Test Steps**:
1. POST /api/cart/123/coupon
2. Body: {"couponCode": "PRODUCT10"}

**Expected Result**:
- HTTP Status: 400 Bad Request
- Error message: "Coupon not applicable to items in cart"
- Cart remains unchanged
- Response time < 500ms

---

## Test Case 16: Remove Applied Coupon (Happy Path)

**Test Case ID**: TC_COUPON_016

**Endpoint**: DELETE /api/cart/{cartId}/coupon

**Scenario**: Remove an applied coupon from cart

**Preconditions**:
- User authenticated
- Cart has coupon "SAVE10" applied
- Cart total with coupon: $90
- Original cart total: $100

**Test Steps**:
1. Send DELETE request to /api/cart/123/coupon
2. Include valid JWT token
3. Verify response

**Expected Result**:
- HTTP Status: 200 OK
- Response body shows:
  - appliedCoupon: null
  - total reverted to $100
- Database updated
- Response time < 500ms

---

## Test Case 17: Remove Coupon When None Applied

**Test Case ID**: TC_COUPON_017

**Endpoint**: DELETE /api/cart/{cartId}/coupon

**Scenario**: Attempt to remove coupon when no coupon is applied

**Preconditions**:
- User authenticated
- Cart exists with no coupon applied

**Test Steps**:
1. DELETE /api/cart/123/coupon

**Expected Result**:
- HTTP Status: 400 Bad Request
- Error message: "No coupon applied to this cart"
- Response time < 500ms

---

## Test Case 18: Remove Coupon from Non-Existent Cart

**Test Case ID**: TC_COUPON_018

**Endpoint**: DELETE /api/cart/{cartId}/coupon

**Scenario**: Attempt to remove coupon from cart that doesn't exist

**Preconditions**:
- User authenticated
- Cart with cartId = 999999 does not exist

**Test Steps**:
1. DELETE /api/cart/999999/coupon

**Expected Result**:
- HTTP Status: 404 Not Found
- Error message: "Cart not found"
- Response time < 500ms

---

## Test Case 19: Remove Coupon Without Authentication

**Test Case ID**: TC_COUPON_019

**Endpoint**: DELETE /api/cart/{cartId}/coupon

**Scenario**: Security test - remove coupon without authentication

**Preconditions**:
- No authentication token
- Cart exists with applied coupon

**Test Steps**:
1. DELETE /api/cart/123/coupon
2. No Authorization header

**Expected Result**:
- HTTP Status: 401 Unauthorized
- Error message: "Authentication required"
- Response time < 500ms

---

## Test Case 20: Validate Coupon Code (Valid)

**Test Case ID**: TC_COUPON_020

**Endpoint**: POST /api/coupon/validate

**Scenario**: Validate a valid coupon code for a specific cart

**Preconditions**:
- Coupon "SAVE15" exists and is valid
- Cart exists with sufficient value

**Test Steps**:
1. POST /api/coupon/validate
2. Body: {"couponCode": "SAVE15", "cartId": "123"}

**Expected Result**:
- HTTP Status: 200 OK
- Response body:
  - valid: true
  - discountType: "PERCENTAGE"
  - discountValue: 15
  - applicableToCart: true
  - message: "Coupon is valid"
- Response time < 500ms

---

## Test Case 21: Validate Expired Coupon

**Test Case ID**: TC_COUPON_021

**Endpoint**: POST /api/coupon/validate

**Scenario**: Validate an expired coupon

**Preconditions**:
- Coupon "OLDCODE" exists but is expired

**Test Steps**:
1. POST /api/coupon/validate
2. Body: {"couponCode": "OLDCODE", "cartId": "123"}

**Expected Result**:
- HTTP Status: 200 OK
- Response body:
  - valid: false
  - message: "Coupon has expired"
- Response time < 500ms

---

## Test Case 22: Validate Coupon with Insufficient Cart Value

**Test Case ID**: TC_COUPON_022

**Endpoint**: POST /api/coupon/validate

**Scenario**: Validate coupon when cart value is below minimum

**Preconditions**:
- Coupon requires min cart value $100
- Cart total is $50

**Test Steps**:
1. POST /api/coupon/validate
2. Body: {"couponCode": "BIGDEAL", "cartId": "123"}

**Expected Result**:
- HTTP Status: 200 OK
- Response body:
  - valid: false
  - applicableToCart: false
  - message: "Minimum cart value of $100 required"
- Response time < 500ms

---

## Test Case 23: Validate Non-Existent Coupon

**Test Case ID**: TC_COUPON_023

**Endpoint**: POST /api/coupon/validate

**Scenario**: Validate a coupon code that doesn't exist

**Preconditions**:
- Coupon "FAKE123" does not exist

**Test Steps**:
1. POST /api/coupon/validate
2. Body: {"couponCode": "FAKE123", "cartId": "123"}

**Expected Result**:
- HTTP Status: 404 Not Found
- Error message: "Coupon not found"
- Response time < 500ms

---

## Test Case 24: Concurrent Coupon Application

**Test Case ID**: TC_COUPON_024

**Endpoint**: POST /api/cart/{cartId}/coupon

**Scenario**: Performance test - multiple users applying coupons concurrently

**Preconditions**:
- 100 concurrent users
- Each has valid cart and coupon

**Test Steps**:
1. Simulate 100 concurrent POST requests
2. Each applies different coupon to their cart

**Expected Result**:
- All requests complete successfully
- No race conditions or data corruption
- Average response time < 500ms
- 99th percentile < 1000ms
- System handles 10,000 concurrent users as per NFR

---

## Edge Cases and Additional Scenarios

### Test Case 25: Apply Coupon with Maximum Discount Cap

**Test Case ID**: TC_COUPON_025

**Endpoint**: POST /api/cart/{cartId}/coupon

**Scenario**: Apply coupon when calculated discount exceeds maximum allowed discount

**Preconditions**:
- Cart total: $1000
- Coupon offers 10% discount
- System max discount cap: $50

**Test Steps**:
1. POST /api/cart/123/coupon
2. Body: {"couponCode": "BIG10"}

**Expected Result**:
- HTTP Status: 200 OK
- Calculated discount: $100 (10% of $1000)
- Applied discount: $50 (capped at max)
- Final total: $950
- Response time < 500ms

---

### Test Case 26: Apply Coupon with Special Characters

**Test Case ID**: TC_COUPON_026

**Endpoint**: POST /api/cart/{cartId}/coupon

**Scenario**: Input validation - coupon code with special characters

**Preconditions**:
- User authenticated
- Cart exists

**Test Steps**:
1. POST /api/cart/123/coupon
2. Body: {"couponCode": "SAVE<script>alert('xss')</script>"}

**Expected Result**:
- HTTP Status: 400 Bad Request
- Input sanitized/rejected
- No XSS vulnerability
- Response time < 500ms

---

### Test Case 27: Apply Coupon with Case Sensitivity

**Test Case ID**: TC_COUPON_027

**Endpoint**: POST /api/cart/{cartId}/coupon

**Scenario**: Test case sensitivity of coupon codes

**Preconditions**:
- Coupon "SAVE10" exists (uppercase)
- User tries "save10" (lowercase)

**Test Steps**:
1. POST /api/cart/123/coupon
2. Body: {"couponCode": "save10"}

**Expected Result**:
- Behavior depends on business rule:
  - If case-insensitive: HTTP 200 OK, coupon applied
  - If case-sensitive: HTTP 404 Not Found, "Coupon not found"
- Response time < 500ms

---

### Test Case 28: Remove Coupon for Guest User

**Test Case ID**: TC_COUPON_028

**Endpoint**: DELETE /api/cart/{cartId}/coupon

**Scenario**: Guest user removes applied coupon

**Preconditions**:
- Guest user cart with applied coupon
- Session-based cart identification

**Test Steps**:
1. DELETE /api/cart/guest_cart_001/coupon
2. No Authorization header

**Expected Result**:
- HTTP Status: 200 OK
- Coupon removed
- Cart total reverted
- Updated cart returned in response
- Response time < 500ms

---

### Test Case 29: Apply Coupon with Empty Request Body

**Test Case ID**: TC_COUPON_029

**Endpoint**: POST /api/cart/{cartId}/coupon

**Scenario**: Validation test - empty request body

**Preconditions**:
- User authenticated
- Cart exists

**Test Steps**:
1. POST /api/cart/123/coupon
2. Body: {}

**Expected Result**:
- HTTP Status: 400 Bad Request
- Error message: "Coupon code is required"
- Response time < 500ms

---

### Test Case 30: Apply Coupon with Malformed JSON

**Test Case ID**: TC_COUPON_030

**Endpoint**: POST /api/cart/{cartId}/coupon

**Scenario**: Validation test - malformed JSON in request body

**Preconditions**:
- User authenticated
- Cart exists

**Test Steps**:
1. POST /api/cart/123/coupon
2. Body: {"couponCode": "SAVE10",}

**Expected Result**:
- HTTP Status: 400 Bad Request
- Error message: "Invalid JSON format"
- Response time < 500ms

---

## Test Execution Summary

### Coverage Matrix

| Category | Test Cases | Coverage |
|----------|-----------|----------|
| Happy Path | 4 | Apply valid coupon, percentage discount, fixed discount, guest user |
| Validation | 6 | Blank code, null code, empty body, malformed JSON, special chars, case sensitivity |
| Business Logic | 5 | Min cart value, expired coupon, product-specific, max discount cap, non-stackable |
| Error Handling | 4 | Non-existent coupon, non-existent cart, no coupon applied, invalid cart |
| Security | 3 | No authentication, unauthorized access, XSS prevention |
| Performance | 1 | Concurrent users |
| Remove Coupon | 3 | Happy path, no coupon applied, guest user |

**Total Test Cases**: 30

---

## Test Data Requirements

### Coupons
- SAVE10: 10% discount, min $50, expires 2026-12-31
- SAVE20: 20% discount, min $100, expires 2026-12-31
- PERCENT20: 20% discount, no min, expires 2026-12-31
- FLAT25: $25 fixed discount, min $50, expires 2026-12-31
- GUEST10: 10% discount for guest users, no min, expires 2026-12-31
- SAVE50: 50% discount, min $50, expires 2026-12-31
- EXPIRED10: 10% discount, expired on 2025-01-01
- BIGDEAL: 15% discount, min $100, expires 2026-12-31
- PRODUCT10: 10% discount, applicable to product_id = 101, expires 2026-12-31
- BIG10: 10% discount, no min, max discount $50, expires 2026-12-31

### Carts
- Cart 123: User A, total $100, no coupon
- Cart 456: User A, total $200, no coupon
- Cart 789: User A, total $150, no coupon
- guest_cart_001: Guest user, total $80, no coupon

### Users
- User A: userId = 1, authenticated
- User B: userId = 2, authenticated
- Guest User: No userId, session-based

---

## Automation Notes

- All test cases can be automated using Postman/Newman
- Use environment variables for base URL, tokens, cartIds
- Implement pre-request scripts for test data setup
- Use test scripts for assertions
- Chain requests using collection variables
- Performance tests can use Postman monitors or JMeter

---

## Test Execution Prerequisites

1. **Environment Setup**:
   - SpringBoot application running on localhost:8080 or test environment
   - MySQL database with test data loaded
   - Valid JWT tokens for authenticated users

2. **Test Data**:
   - Database seeded with test coupons, carts, and users
   - Test data script provided separately

3. **Tools**:
   - Postman for manual testing
   - Newman for automated execution
   - JMeter for performance testing (optional)

---

## Traceability Matrix

| Requirement | Test Cases |
|-------------|------------|
| Apply valid coupon | TC_COUPON_001, TC_COUPON_002, TC_COUPON_003, TC_COUPON_004 |
| Coupon validation | TC_COUPON_005, TC_COUPON_006, TC_COUPON_007, TC_COUPON_020, TC_COUPON_021, TC_COUPON_022, TC_COUPON_023 |
| Remove coupon | TC_COUPON_016, TC_COUPON_017, TC_COUPON_018, TC_COUPON_028 |
| Security | TC_COUPON_011, TC_COUPON_012, TC_COUPON_019, TC_COUPON_026 |
| Input validation | TC_COUPON_008, TC_COUPON_009, TC_COUPON_029, TC_COUPON_030 |
| Business rules | TC_COUPON_013, TC_COUPON_014, TC_COUPON_015, TC_COUPON_025 |
| Performance | TC_COUPON_024 |
| Edge cases | TC_COUPON_010, TC_COUPON_027 |

---

**Document Version**: 1.0

**Last Updated**: 2026-01-08

**Author**: QA Engineer

**Status**: Ready for Execution