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

## Test Reports

- **Allure Reports**: Detailed test execution reports with request/response details
- `checklist.md`: Complete list of test cases to verify API functionality
- `test-report.md`: Documentation of detected issues and improvement suggestions

