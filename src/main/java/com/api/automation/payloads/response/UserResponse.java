package com.api.automation.payloads.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Response POJO for a User returned by JSONPlaceholder.
 * Maps from JSON: { "id": 1, "name": "...", "username": "...", "email": "...", ... }
 *
 * @JsonIgnoreProperties(ignoreUnknown=true) — nested objects like "address" and "company"
 *   are ignored since we only need the top-level scalar fields for testing.
 *   BENEFIT: Avoids creating Address/Company inner classes when we don't need that data.
 *   If nested data is needed later, just add the inner class and Jackson maps it automatically.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserResponse {

    private Integer id;         // Server-generated user ID
    private String name;        // Full name (e.g., "Leanne Graham")
    private String username;    // Username handle (e.g., "Bret")
    private String email;       // Email address
    private String phone;       // Phone number
    private String website;     // Personal website

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
