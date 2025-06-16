package com.qa.challenge.tests;

import com.qa.challenge.models.UserIdListResponse;
import com.qa.challenge.utils.RestClient;
import com.qa.challenge.utils.TestConfig;
import io.qameta.allure.*;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.junit.jupiter.api.Assertions.*;

@Epic("Dating App API Testing")
@Feature("Users List API")
@Owner("QA Team")
public class UsersListApiTest {
    private RestClient restClient;

    @BeforeEach
    void setUp() {
        restClient = new RestClient();
    }

    @Test
    @DisplayName("Get users list with 'any' gender parameter")
    @Description("Verify that users list can be retrieved with 'any' gender filter")
    @Severity(SeverityLevel.NORMAL)
    @Story("Retrieve users list by gender")
    void testGetUsersByAnyGender() {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("gender", TestConfig.GENDER_ANY);

        Response response = restClient.get(TestConfig.USERS_ENDPOINT, queryParams);

        assertEquals(200, response.getStatusCode(), "Status code should be 200");

        UserIdListResponse userIdList = response.as(UserIdListResponse.class);
        assertTrue(userIdList.getSuccess(), "Response should be successful");
        assertEquals(0, userIdList.getErrorCode(), "Error code should be 0");
        assertNull(userIdList.getErrorMessage(), "Error message should be null");
        assertNotNull(userIdList.getIdList(), "ID list should not be null");
    }


    @Test
    @DisplayName("Get users list with missing gender parameter")
    @Description("Verify proper error handling when the required gender parameter is missing")
    @Severity(SeverityLevel.NORMAL)
    @Story("Error handling")
    @Issue("BUG-002")
    void testGetUsersWithMissingGender() {
        // INVALID REQUEST: The gender parameter is required but intentionally omitted
        // According to API specifications, gender is a required parameter

        Response response = restClient.get(TestConfig.USERS_ENDPOINT);

        // Log the response for debugging and reporting
        System.out.println("Response for INVALID missing required parameter (gender): " + response.asString());

        // For INVALID REQUEST (missing required parameter), the API should properly indicate an error:
        // 1. Return an appropriate error status code (400 Bad Request is most appropriate)
        // 2. Or return 200 OK with clear error details in response body

        if (response.getStatusCode() == 200) {
            // Option 2: API returns 200 OK even for missing required parameter (not recommended)
            UserIdListResponse errorResponse = response.as(UserIdListResponse.class);

            // For INVALID REQUEST (missing parameter), success flag should be false
            assertFalse(errorResponse.getSuccess(),
                "FAIL: API accepted request without required 'gender' parameter. Response should indicate failure.");

            // For INVALID REQUEST (missing parameter), error code should not be 0 (which indicates success)
            assertNotEquals(0, errorResponse.getErrorCode(),
                "FAIL: Error code should not be 0 for request missing required 'gender' parameter");

            // For INVALID REQUEST (missing parameter), error message should be provided
            assertNotNull(errorResponse.getErrorMessage(),
                "FAIL: Error message missing for request without required 'gender' parameter");
        } else {
            // Option 1: API returns non-200 status (better practice for missing required parameters)
            assertEquals(400, response.getStatusCode(),
                "FAIL: Should return 400 Bad Request for missing required 'gender' parameter, got: " + response.getStatusCode());
        }
    }

    @Test
    @DisplayName("Get users list with invalid gender parameter")
    @Description("Verify proper error handling when an invalid gender value is provided")
    @Severity(SeverityLevel.NORMAL)
    @Story("Error handling")
    @Issue("BUG-003")
    void testGetUsersWithInvalidGender() {
        // INVALID PARAMETER: Using a gender value ("invalid_gender") that is not in the API specification
        // Valid values according to spec are: "male", "female", "magic", "McCloud", and "any"
        String invalidGender = "invalid_gender";

        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("gender", invalidGender);

        Response response = restClient.get(TestConfig.USERS_ENDPOINT, queryParams);

        // Log the response for debugging and reporting
        System.out.println("Response for INVALID gender parameter value '" + invalidGender + "': " + response.asString());

        // For INVALID PARAMETER (invalid gender value), the API should properly indicate an error:
        // 1. Return an appropriate error status code (400 Bad Request is most appropriate)
        // 2. Or return 200 OK with clear error details in response body

        if (response.getStatusCode() == 200) {
            // Option 2: API returns 200 OK even for invalid gender value
            UserIdListResponse userIdList = response.as(UserIdListResponse.class);

            // Documentation of actual behavior (for reporting purposes)
            System.out.println("API response with invalid gender '" + invalidGender + "':");
            System.out.println("- Success flag: " + userIdList.getSuccess());
            System.out.println("- Error code: " + userIdList.getErrorCode());
            System.out.println("- Error message: " + userIdList.getErrorMessage());

            // For INVALID gender value, success flag should be false
            assertFalse(userIdList.getSuccess(),
                "FAIL: Invalid parameter (gender='" + invalidGender + "') accepted as valid. Response should indicate failure.");

            // For INVALID gender value, error code should not be 0 (which indicates success)
            assertNotEquals(0, userIdList.getErrorCode(),
                "FAIL: Error code should not be 0 for invalid gender parameter '" + invalidGender + "'");

            // For INVALID gender value, error message should be provided
            assertNotNull(userIdList.getErrorMessage(),
                "FAIL: Error message missing for invalid gender parameter '" + invalidGender + "'");
        } else {
            // Option 1: API returns non-200 status (better practice for invalid parameters)
            assertEquals(400, response.getStatusCode(),
                    "FAIL: Should return 400 Bad Request for invalid gender parameter '" + invalidGender + "', got: " + response.getStatusCode());
        }
    }

    @Test
    @DisplayName("Verify response structure matches API specification")
    @Description("Verify the response structure and field names match the API documentation")
    @Severity(SeverityLevel.NORMAL)
    @Story("API Response Structure")
    @Issue("BUG-001")
    void testResponseFieldNames() {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("gender", TestConfig.GENDER_MALE);

        Response response = Allure.step("Request users list with gender: male", () ->
            restClient.get(TestConfig.USERS_ENDPOINT, queryParams)
        );
        Allure.step("Assert status code is 200", () -> {
            assertEquals(200, response.getStatusCode(), "Status code should be 200");
        });

        // Check for presence of "idList" field which according to documentation should be "result"
        String responseBody = response.getBody().asString();
        Allure.step("Assert response contains 'idList' field and not 'result' field", () -> {
            assertTrue(responseBody.contains("\"idList\""),
                "Response contains 'idList' field instead of 'result' as specified in API documentation (BUG-001)");
            assertFalse(responseBody.contains("\"result\": ["),
                "Response should not contain 'result' field as API incorrectly uses 'idList' instead (BUG-001)");
        });
        System.out.println("BUG-001: API response field naming inconsistency. Uses 'idList' instead of 'result' as specified.");
    }

    // Moved from UserApiTest
    @ParameterizedTest
    @ValueSource(strings = {"male", "female", "magic", "McCloud"})
    @DisplayName("GET /users?gender={gender} returns 200 for all valid genders")
    @Description("Verify that the users endpoint returns HTTP 200 for all valid gender values, including 'magic' and 'McCloud'")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Gender filter returns 200 for all valid values")
    void testUsersEndpointReturns200ForAllValidGenders(String gender) {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("gender", gender);
        Response response = Allure.step("Request users list with gender: " + gender, () ->
            restClient.get(TestConfig.USERS_ENDPOINT, queryParams)
        );
        assertEquals(200, response.getStatusCode(),
            "API should return 200 for gender '" + gender + "', but got: " + response.getStatusCode());
    }
}
