package com.api.automation.payloads.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.response.Response;

import java.util.List;

/**
 * Helper class for deserializing a JSON array of posts.
 * JSONPlaceholder's GET /posts returns a top-level array [{ ... }, { ... }],
 * not an object wrapper, so we need TypeReference<List<PostResponse>> to parse it.
 *
 * WHY TypeReference: Java's type erasure removes List<PostResponse> at runtime.
 *   TypeReference preserves the generic type so Jackson knows to deserialize each
 *   array element as PostResponse, not a plain Map.
 *
 * BENEFIT: Gives you a typed List<PostResponse> instead of List<Object>,
 *   so you can safely call postList.get(0).getTitle() with IDE autocomplete.
 *
 * Example:
 *   List<PostResponse> posts = PostListResponse.fromResponse(response);
 *   posts.forEach(p -> System.out.println(p.getTitle()));
 */
public class PostListResponse {

    // Shared ObjectMapper instance for JSON parsing
    private static final ObjectMapper MAPPER = new ObjectMapper();

    /**
     * Parses the response body (raw JSON array) into a typed List<PostResponse>.
     * Used in step definitions: List<PostResponse> posts = PostListResponse.fromResponse(response);
     */
    public static List<PostResponse> fromResponse(Response response) {
        try {
            return MAPPER.readValue(
                    response.getBody().asString(),
                    new TypeReference<List<PostResponse>>() {}
            );
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse posts list response", e);
        }
    }
}
