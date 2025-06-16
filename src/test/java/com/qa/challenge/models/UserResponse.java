package com.qa.challenge.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserResponse {
    private boolean isSuccess;
    private int errorCode;
    private String errorMessage;
    private User user;

    // Some APIs return 'success' instead of 'isSuccess'
    private boolean success;

    // Custom getter to handle both isSuccess and success fields
    public boolean getSuccess() {
        return isSuccess || success;
    }
}
