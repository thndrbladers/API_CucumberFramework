package com.api.automation.payloads.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Response POJO for a Comment returned by JSONPlaceholder.
 * Maps from JSON: { "id": 1, "postId": 1, "name": "...", "email": "...", "body": "..." }
 *
 * @JsonIgnoreProperties(ignoreUnknown=true) — safely ignores any extra fields the API may return.
 *   BENEFIT: Same forward-compatibility as PostResponse — API changes don't break tests.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CommentResponse {

    private Integer id;         // Server-generated comment ID
    private Integer postId;     // Parent post this comment belongs to
    private String name;        // Commenter display name
    private String email;       // Commenter email
    private String body;        // Comment text content

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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
        return "CommentResponse{id=" + id + ", postId=" + postId + ", name='" + name + "', email='" + email + "'}";
    }
}
