# API Testing Report - Dating App API

## Summary
This report documents the findings from testing the dating app API endpoints as specified in the requirements. The API was tested for functionality, data validation, error handling, and conformance to specifications. Currently, 8 tests are failing, primarily due to server errors (500), improper error handling, and validation issues.

## Testing Environment
- API Base URL: https://hr-challenge.dev.tapyou.com
- Testing tools: Java 11, RestAssured, JUnit 5
- Test execution environment: Windows

## Bugs and Issues

### 1. Users List Endpoint (/api/test/users)

| ID | Severity | Description | Expected Behavior | Actual Behavior | Steps to Reproduce | Automated Test Reference |
|----|----------|-------------|-------------------|-----------------|-------------------|--------------------------|
| BUG-001 | Medium | Response field naming inconsistency | API should use "result" field name as specified in documentation | API uses "idList" field instead of "result" | 1. Send GET request to /api/test/users?gender=male<br>2. Observe response structure | UsersListApiTest: testResponseFieldNames |
| BUG-002 | High | Missing required error handling when gender parameter is not provided | API should return an error when a required parameter is missing | API returns HTTP 200 instead of an appropriate error status like 400 | 1. Send GET request to /api/test/users without gender parameter | UsersListApiTest: testGetUsersWithMissingGender |
| BUG-003 | High | Server error with invalid gender value | API should validate the gender parameter and return 400 Bad Request | API returns HTTP 500 for invalid gender values | 1. Send GET request to /api/test/users?gender=invalid_value | UsersListApiTest: testGetUsersWithInvalidGender (failing, expected 400 but got 500) |
| BUG-008 | High | Server error with valid gender value 'magic' | API should return valid response with HTTP 200 | API returns HTTP 500 for gender=magic | 1. Send GET request to /api/test/users?gender=magic | UsersListApiTest: testUsersEndpointReturns200ForAllValidGenders (failing, expected 200 but got 500) |
| BUG-009 | High | Server error with valid gender value 'McCloud' | API should return valid response with HTTP 200 | API returns HTTP 500 for gender=McCloud | 1. Send GET request to /api/test/users?gender=McCloud | UsersListApiTest: testUsersEndpointReturns200ForAllValidGenders (failing, expected 200 but got 500) |

### 2. User Details Endpoint (/api/test/user/{id})

| ID | Severity | Description | Expected Behavior | Actual Behavior | Steps to Reproduce | Automated Test Reference |
|----|----------|-------------|-------------------|-----------------|-------------------|--------------------------|
| BUG-004 | Medium | Response field naming inconsistency | API should use "result" field name as specified in documentation | API uses "user" field instead of "result" | 1. Send GET request to /api/test/user/{id}<br>2. Observe response structure | UserApiTest: testUserResponseFieldNames |
| BUG-005 | High | Negative user ID is accepted without proper error handling | API should validate that ID is positive, returning an appropriate error | API accepts negative IDs as valid with errorCode=0 | 1. Send GET request to /api/test/user/-1 | UserApiTest: testGetUserByInvalidId (failing, expected non-zero error code but got 0) |
| BUG-006 | High | Inconsistent gender capitalization | API should return gender values with consistent capitalization | Gender values have inconsistent capitalization | 1. Request multiple user details and compare gender field | UserApiTest: testGenderCapitalizationConsistency |
| BUG-007 | High | Very large out-of-range IDs return valid user with errorCode=0 | API should return error for non-existent IDs | API returns success with errorCode=0 for large IDs | 1. Send GET request to /api/test/user/2147483647 | UserApiTest: testGetUserWithVeryLargeId (failing, expected non-zero error code but got 0) |
| BUG-010 | Critical | Server error (500) when trying to access users with specific IDs | API should return user details with HTTP 200 | API returns HTTP 500 for specific user IDs | 1. Request details for user ID 0 | UserApiTest: testGetUserDetailsForAllUserIds (failing with user ID 0, expected 200 but got 500) |
| BUG-011 | High | Required user fields presence test failing | API should return successful response with all required fields | API returns unsuccessful response | 1. Send GET request to /api/test/user/{valid-id} | UserApiTest: testRequiredUserFieldsPresence (failing, response not successful) |
| BUG-012 | High | Response field structure inconsistency | API model should handle field naming variations | Model can't handle field name differences | 1. Verify response structure from user endpoint | UserApiTest: testResponseFieldsMatchSpecification (failing, model incompatible with response) |

## Test Coverage Summary

| Test Type | Total Tests | Passed | Failed | Skipped |
|-----------|-------------|--------|--------|---------|
| Functional Tests | 12 | 7 | 5 | 0 |
| Data Validation Tests | 2 | 1 | 1 | 0 |
| Error Handling Tests | 4 | 2 | 2 | 0 |
| Total | 18 | 10 | 8 | 0 |

## Latest Automated Test Results
From the most recent test run:
```
[ERROR] Failures: 
[ERROR]   UserApiTest.testGetUserByInvalidId:136 FAIL: Error code should not be 0 for invalid parameter (-1) ==> expected: not equal but was: <0>
[ERROR]   UserApiTest.testGetUserDetailsForAllUserIds:319->lambda$testGetUserDetailsForAllUserIds$3:325 Status code should be 200 for user ID 0 ==> expected: <200> but was: <500>
[ERROR]   UserApiTest.testGetUserWithVeryLargeId:238 FAIL: Error code should not be 0 for INVALID out-of-range ID (2147483647) ==> expected: not equal but was: <0>
[ERROR]   UserApiTest.testRequiredUserFieldsPresence:364->lambda$testRequiredUserFieldsPresence$7:365 Response should be successful ==> expected: <true> but was: <false>
[ERROR]   UserApiTest.testResponseFieldsMatchSpecification:471->lambda$testResponseFieldsMatchSpecification$13:473 Our model should handle either 'success' or 'isSuccess' field ==> expected: <true> but was: <false>
[ERROR]   UsersListApiTest.testGetUsersWithInvalidGender:139 FAIL: Should return 400 Bad Request for invalid gender parameter 'invalid_gender', got: 500 ==> expected: <400> but was: <500>
[ERROR]   UsersListApiTest.testUsersEndpointReturns200ForAllValidGenders:185 API should return 200 for gender 'magic', but got: 500 ==> expected: <200> but was: <500>
[ERROR]   UsersListApiTest.testUsersEndpointReturns200ForAllValidGenders:185 API should return 200 for gender 'McCloud', but got: 500 ==> expected: <200> but was: <500>
[INFO]
[ERROR] Tests run: 18, Failures: 8, Errors: 0, Skipped: 0
```
