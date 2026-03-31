package com.api.automation.payloads.request;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Request body POJO for creating/updating a Post.
 * Maps to JSON: { "title": "...", "body": "...", "userId": 1 }
 *
 * WHY POJO instead of raw JSON strings: Type-safe, IDE-autocomplete, compile-time checks.
 *   PostRequest req = new PostRequest("Title", "Body", 1);
 *   vs. String json = "{\"title\":\"Title\"}"  ← error-prone, no compile check.
 *
 * @JsonInclude(NON_NULL) — null fields are excluded from JSON output.
 *   BENEFIT: Makes PATCH requests work correctly — only set fields are sent.
 *   Example: new PostRequest(); req.setTitle("New");  → {"title":"New"} (body & userId omitted)
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PostRequest {

    private String title;       // Post title
    private String body;        // Post content
    private Integer userId;     // ID of the user who authored the post

    /** No-arg constructor — required by Jackson for deserialization (JSON → POJO). */
    public PostRequest() {
    }

    /**
     * Convenience constructor for step definitions to create a request in one line.
     * Example: new PostRequest("Java Guide", "Intro to Java", 1)
     */
    public PostRequest(String title, String body, int userId) {
        this.title = title;
        this.body = body;
        this.userId = userId;
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
        return "PostRequest{title='" + title + "', body='" + body + "', userId=" + userId + "}";
    }
}
