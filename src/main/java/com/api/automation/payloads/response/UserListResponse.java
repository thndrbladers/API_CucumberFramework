package com.api.automation.payloads.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.response.Response;

import java.util.List;

/**
 * Helper for deserializing a flat JSON array of posts from JSONPlaceholder.
 * JSONPlaceholder returns GET /posts as a top-level array (not wrapped in an object).
 */
public class UserListResponse {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    /**
     * Deserializes the response body (JSON array) into a list of UserResponse objects.
     */
    public static List<UserResponse> fromResponse(Response response) {
        try {
            return MAPPER.readValue(
                    response.getBody().asString(),
                    new TypeReference<List<UserResponse>>() {}
            );
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse posts list response", e);
        }
    }
}
