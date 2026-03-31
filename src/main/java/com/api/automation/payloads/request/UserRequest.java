package com.api.automation.payloads.request;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Request body POJO for creating/updating a User.
 * Maps to JSON: { "name": "...", "username": "...", "email": "...", "phone": "...", "website": "..." }
 *
 * @JsonInclude(NON_NULL) — null fields excluded. The 3-arg constructor leaves phone/website null
 *   so they won't appear in the JSON unless explicitly set via setters.
 *
 * BENEFIT: The 3-arg constructor makes test code concise for common cases:
 *   new UserRequest("John", "john123", "john@test.com")  → only 3 fields in JSON.
 *   If phone/website are needed: call setPhone()/setWebsite() after construction.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserRequest {

    private String name;        // User full name
    private String username;    // Unique username handle
    private String email;       // User email address
    private String phone;       // Optional — phone number
    private String website;     // Optional — personal website URL

    /** No-arg constructor — required by Jackson. */
    public UserRequest() {
    }

    /** Constructor with required fields only. Phone and website default to null (excluded from JSON). */
    public UserRequest(String name, String username, String email) {
        this.name = name;
        this.username = username;
        this.email = email;
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
        return "UserRequest{name='" + name + "', username='" + username + "', email='" + email + "'}";
    }
}
