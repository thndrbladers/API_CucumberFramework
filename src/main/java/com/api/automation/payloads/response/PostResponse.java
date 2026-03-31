package com.api.automation.payloads.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Response POJO for a Post returned by JSONPlaceholder.
 * Maps from JSON: { "id": 1, "title": "...", "body": "...", "userId": 1 }
 *
 * @JsonIgnoreProperties(ignoreUnknown=true) — if the API adds new fields in the future,
 *   deserialization won't break. Only the fields declared below are mapped.
 *   BENEFIT: Forward-compatible — API can evolve without breaking existing tests.
 *   Example: If API adds "createdAt" field tomorrow, this class still works untouched.
 *
 * No-arg constructor + setters are used by Jackson for deserialization.
 * Getters are used by step definitions to read and assert field values.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class PostResponse {

    private Integer id;         // Server-generated post ID
    private String title;       // Post title
    private String body;        // Post content
    private Integer userId;     // Author's user ID

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "PostResponse{id=" + id + ", title='" + title + "', body='" + body + "', userId=" + userId + "}";
    }
}
