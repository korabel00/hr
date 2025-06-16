# Dating App API Testing Project

This project contains automated tests for a dating app's REST API using Java, RestAssured, JUnit 5, and Allure reporting.

## Project Structure

- `src/test/java/com/qa/challenge/models` - Model classes representing API responses
- `src/test/java/com/qa/challenge/utils` - Utility classes for API testing
- `src/test/java/com/qa/challenge/tests` - Test classes for API endpoints
- `checklist.md` - Test checklist for API testing
- `test-report.md` - Manual test report with findings and bug descriptions

## Prerequisites

- Java 11 or higher
- Maven 3.6.0 or higher
- Allure command-line tool (optional, for enhanced reporting)

## API Under Test

This project tests the following API endpoints:

- GET `/api/test/users?gender={gender}` - Gets a list of user IDs filtered by gender
- GET `/api/test/user/{id}` - Gets details of a specific user by ID

## How to Run Tests

### Running Tests with Maven

To run all tests using Maven:

```bash
mvn clean test
```

### Generating and Viewing Allure Reports

After running the tests, you can generate an Allure report:

```bash
mvn allure:report
```

This will generate the report in `target/site/allure-maven-plugin` directory.

To serve the report in a web browser:

```bash
mvn allure:serve
```

This command will generate the report, start a local web server, and automatically open the report in your default web browser.

## Test Categories

The test suite includes various categories of tests:

1. **Functional Tests** - Verify the API works correctly with valid parameters
2. **Error Handling Tests** - Verify proper error responses for invalid inputs
3. **Data Validation Tests** - Verify data format and content is correct

## Error Handling Approach

This test suite has been designed with a clear distinction between valid and invalid parameters. For invalid inputs, two types of acceptable API responses are tested:

- Option 1: HTTP status code 4xx (client error)
- Option 2: HTTP status code 200 with error details in the response body (success=false, non-zero error code, error message)

## Test Reports

- **Allure Reports**: Detailed test execution reports with request/response details
- `checklist.md`: Complete list of test cases to verify API functionality
- `test-report.md`: Documentation of detected issues and improvement suggestions

## Additional Information

This testing project was designed to thoroughly test a deliberately unstable API, with test cases that are resilient to API instability while still properly identifying and reporting actual issues.
