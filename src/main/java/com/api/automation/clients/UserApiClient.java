package com.api.automation.clients;

import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Domain-specific API client for the User resource (JSONPlaceholder).
 * All CRUD operations for /users live here.
 */
public class UserApiClient {

    private static final Logger LOG = LogManager.getLogger(UserApiClient.class);
    private static final String USERS_ENDPOINT = "/users";

    private final ApiClient apiClient;

    public UserApiClient() {
        this.apiClient = new ApiClient();
    }

    public Response getUsers() {
        LOG.info("Fetching all users");
        return apiClient.get(USERS_ENDPOINT);
    }

    public Response getUserById(int userId) {
        LOG.info("Fetching user by ID: {}", userId);
        return apiClient.get(USERS_ENDPOINT + "/" + userId);
    }

    public Response createUser(Object requestBody) {
        LOG.info("Creating user");
        return apiClient.post(USERS_ENDPOINT, requestBody);
    }

    public Response updateUser(int userId, Object requestBody) {
        LOG.info("Updating user ID: {}", userId);
        return apiClient.put(USERS_ENDPOINT + "/" + userId, requestBody);
    }

    public Response patchUser(int userId, Object requestBody) {
        LOG.info("Patching user ID: {}", userId);
        return apiClient.patch(USERS_ENDPOINT + "/" + userId, requestBody);
    }

    public Response deleteUser(int userId) {
        LOG.info("Deleting user ID: {}", userId);
        return apiClient.delete(USERS_ENDPOINT + "/" + userId);
    }
}
