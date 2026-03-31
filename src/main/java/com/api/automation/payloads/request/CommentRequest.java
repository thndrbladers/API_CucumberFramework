package com.api.automation.payloads.request;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Request body POJO for creating/updating a Comment.
 * Maps to JSON: { "postId": 1, "name": "...", "email": "...", "body": "..." }
 *
 * @JsonInclude(NON_NULL) — null fields excluded from serialization.
 *   BENEFIT: Same as PostRequest — enables partial updates via PATCH.
 *
 * WHY: Encapsulates comment data in a typed object. Step definitions build this POJO,
 *   then CommentApiClient passes it to ApiClient.post() which serializes it to JSON.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommentRequest {

    private Integer postId;     // ID of the parent post this comment belongs to
    private String name;        // Commenter display name
    private String email;       // Commenter email address
    private String body;        // Comment text content

    /** No-arg constructor — required by Jackson. */
    public CommentRequest() {
    }

    /** Convenience constructor with all required fields. */
    public CommentRequest(int postId, String name, String email, String body) {
        this.postId = postId;
        this.name = name;
        this.email = email;
        this.body = body;
    }

    public Integer getPostId() {
        return postId;
    }

    public void setPostId(Integer postId) {
        this.postId = postId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return "CommentRequest{postId=" + postId + ", name='" + name + "', email='" + email + "', body='" + body + "'}";
    }
}
