package com.api.automation.payloads.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * POJO for the Post response from JSONPlaceholder.
 * Used for both create (POST → 201) and update (PUT → 200) responses.
 * JsonIgnoreProperties(ignoreUnknown) ensures forward compatibility.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserResponse {

    private Integer id;
    private String title;
    private String body;
    private Integer userId;

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
        return "UserResponse{id=" + id + ", title='" + title + "', body='" + body + "', userId=" + userId + "}";
    }
}
