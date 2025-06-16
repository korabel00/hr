# API Testing Checklist - Dating App API

## Functional Requirements Covered by Automation

### 1. Users List Endpoint (/api/test/users)

| ID | Requirement | Priority | Automated Test | Notes |
|----|-------------|----------|----------------|-------|
| F-008 | API should reject invalid gender values | Medium | UsersListApiTest.testGetUsersWithInvalidGender |  |
| F-009 | Response should include success flag | High | Yes | Verified in automated test cases |
| F-010 | Response should include error code | High | Yes | Verified in automated test cases |
| F-011 | Response should include error message when applicable | Medium | Yes | Verified in error test cases |

### 2. User Details Endpoint (/api/test/user/{id})

| ID | Requirement | Priority | Automated Test | Notes |
|----|-------------|----------|----------------|-------|
| F-016 | API should validate numeric user IDs | Medium | UserApiTest.testGetUserByInvalidId |  |
| F-017 | API should validate positive user IDs | Medium | UserApiTest.testGetUserByInvalidId |  |
| F-018 | API should handle large ID values | Low | UserApiTest.testGetUserWithVeryLargeId |  |
| F-019 | Response should include success flag | High | Yes | Verified in automated test cases |
| F-020 | Response should include error code | High | Yes | Verified in automated test cases |
| F-021 | Response should include error message when applicable | Medium | Yes | Verified in error test cases |
| F-023 | Gender values should be consistently formatted | Medium | UserApiTest.testGenderCapitalizationConsistency |  |

## Non-Functional Requirements

| ID | Requirement | Priority | Tested | Notes |
|----|-------------|----------|--------|-------|
| NF-001 | API should respond within acceptable time limits (under 1 second) | High | Partial | Implicitly tested but no specific performance test confirmed |
| NF-002 | API should provide clear error messages | Medium | Yes | Verified in error test cases |
| NF-003 | API should follow RESTful design principles | Medium | Partial | Evaluated through test design, no specific test |
| NF-004 | API should use proper HTTP status codes | Medium | Yes | Verified in all test cases with status code assertions |
| NF-005 | API should have consistent response structure | High | Yes | Test cases: UserApiTest.testUserResponseFieldNames |
| NF-006 | API should validate input parameters | High | Yes | Test cases for invalid parameters confirmed |

## Testing Scope

### In Scope
1. Functional validation of all API endpoints
2. Data validation and error handling
3. Response structure and content validation
4. Compliance with API documentation
5. Basic performance checks (response time)

### Out of Scope
1. Load and stress testing
2. Security testing
3. Authentication and authorization testing


## Test Environment
- Base URL: https://hr-challenge.dev.tapyou.com
- API Documentation: Available via Swagger UI
- Testing Tools: Java 11, RestAssured, JUnit 5
- Test Execution: Maven, Allure Reports
