package com.qa.challenge.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.qameta.allure.Allure;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.config.ObjectMapperConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class RestClient {
    private static final Logger logger = LoggerFactory.getLogger(RestClient.class);
    private final RequestSpecification requestSpec;

    public RestClient() {
        // Configure Jackson for handling Java 8 date/time types
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        // Add custom deserializer for LocalDateTime if needed
        RestAssured.config = RestAssuredConfig.config()
                .objectMapperConfig(ObjectMapperConfig.objectMapperConfig()
                .jackson2ObjectMapperFactory((type, s) -> objectMapper));

        RequestSpecBuilder builder = new RequestSpecBuilder()
                .setBaseUri(TestConfig.BASE_URL)
                .setContentType(ContentType.JSON)
                .setConfig(RestAssured.config)
                .log(LogDetail.ALL)
                .addFilter(new AllureRestAssured()); // Add Allure reporting filter

        requestSpec = RestAssured.given().spec(builder.build());
    }

    public Response get(String endpoint) {
        return requestSpec.when().get(endpoint);
    }

    public Response get(String endpoint, Map<String, Object> queryParams) {
        return requestSpec.queryParams(queryParams).when().get(endpoint);
    }

    public Response get(String endpoint, Map<String, Object> queryParams, Map<String, Object> pathParams) {
        return requestSpec.queryParams(queryParams).pathParams(pathParams).when().get(endpoint);
    }

    public Response get(String endpoint, Map<String, Object> pathParams, boolean isPathParam) {
        // Log the actual path parameter values being used
        logger.info("Making GET request to {} with path parameters: {}", endpoint, pathParams);

        // Substitute path parameter placeholders in the endpoint
        String finalEndpoint = endpoint;
        for (Map.Entry<String, Object> entry : pathParams.entrySet()) {
            String placeholder = "{" + entry.getKey() + "}";
            finalEndpoint = finalEndpoint.replace(placeholder, String.valueOf(entry.getValue()));
            logger.info("Replaced {} with {} in endpoint", placeholder, entry.getValue());
        }

        logger.info("Final endpoint URL: {}", finalEndpoint);
        return requestSpec.when().get(finalEndpoint);
    }
}
