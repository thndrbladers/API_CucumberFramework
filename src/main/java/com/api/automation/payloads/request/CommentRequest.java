package com.api.automation.payloads.request;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * POJO for Comment create/update request payload (JSONPlaceholder).
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommentRequest {

    private Integer postId;
    private String name;
    private String email;
    private String body;

    public CommentRequest() {
    }

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
