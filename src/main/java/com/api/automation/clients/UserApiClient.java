package com.api.automation.clients;

import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Domain client for the Users resource (/users endpoint).
 * Wraps ApiClient with typed methods for user CRUD operations.
 *
 * WHY: Same pattern as PostApiClient/CommentApiClient — keeps all /users paths
 *      in one file. Follows the "one domain, one client" convention for maintainability.
 */
public class UserApiClient {

    private static final Logger LOG = LogManager.getLogger(UserApiClient.class);

    // Base path for all user operations
    private static final String USERS_ENDPOINT = "/users";

    // Generic HTTP client this domain client delegates to
    private final ApiClient apiClient;

    /** Constructor — creates a fresh ApiClient. */
    public UserApiClient() {
        this.apiClient = new ApiClient();
    }

    /** GET /users — returns all 10 users. */
    public Response getUsers() {
        LOG.info("Fetching all users");
        return apiClient.get(USERS_ENDPOINT);
    }

    /** GET /users/{id} — returns a single user or 404. */
    public Response getUserById(int userId) {
        LOG.info("Fetching user by ID: {}", userId);
        return apiClient.get(USERS_ENDPOINT + "/" + userId);
    }

    /** POST /users — creates a new user. Returns 201. */
    public Response createUser(Object requestBody) {
        LOG.info("Creating user");
        return apiClient.post(USERS_ENDPOINT, requestBody);
    }

    /** PUT /users/{id} — full replacement of a user. */
    public Response updateUser(int userId, Object requestBody) {
        LOG.info("Updating user ID: {}", userId);
        return apiClient.put(USERS_ENDPOINT + "/" + userId, requestBody);
    }

    /** PATCH /users/{id} — partial update of a user. */
    public Response patchUser(int userId, Object requestBody) {
        LOG.info("Patching user ID: {}", userId);
        return apiClient.patch(USERS_ENDPOINT + "/" + userId, requestBody);
    }

    /** DELETE /users/{id} — removes a user. Returns 200. */
    public Response deleteUser(int userId) {
        LOG.info("Deleting user ID: {}", userId);
        return apiClient.delete(USERS_ENDPOINT + "/" + userId);
    }
}
