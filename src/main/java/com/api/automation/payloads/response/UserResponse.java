package com.api.automation.payloads.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * POJO for the User response from JSONPlaceholder.
 * Nested objects (address, company) are ignored for simplicity.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserResponse {

    private Integer id;
    private String name;
    private String username;
    private String email;
    private String phone;
    private String website;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    @Override
    public String toString() {
        return "UserResponse{id=" + id + ", name='" + name + "', username='" + username + "', email='" + email + "'}";
    }
}
