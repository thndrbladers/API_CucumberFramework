package com.api.automation.payloads.request;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * POJO for Post create/update request payload (JSONPlaceholder).
 * JsonInclude.NON_NULL ensures only set fields are serialized —
 * useful for PATCH operations where partial updates are needed.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PostRequest {

    private String title;
    private String body;
    private Integer userId;

    public PostRequest() {
    }

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
