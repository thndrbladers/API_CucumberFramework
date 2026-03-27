package com.api.automation.payloads.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * POJO for the Comment response from JSONPlaceholder.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CommentResponse {

    private Integer id;
    private Integer postId;
    private String name;
    private String email;
    private String body;

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
