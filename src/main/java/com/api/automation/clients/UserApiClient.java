package com.api.automation.clients;

import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Domain-specific API client for the Post resource (JSONPlaceholder).
 * Encapsulates endpoints and provides typed methods for post operations.
 * This pattern keeps endpoint URLs out of step definitions.
 */
public class UserApiClient {

    private static final Logger LOG = LogManager.getLogger(UserApiClient.class);
    private static final String POSTS_ENDPOINT = "/posts";

    private final ApiClient apiClient;

    public UserApiClient() {
        this.apiClient = new ApiClient();
    }

    public Response getPosts() {
        LOG.info("Fetching all posts");
        return apiClient.get(POSTS_ENDPOINT);
    }

    public Response getPostById(int postId) {
        LOG.info("Fetching post by ID: {}", postId);
        return apiClient.get(POSTS_ENDPOINT + "/" + postId);
    }

    public Response createPost(Object requestBody) {
        LOG.info("Creating post");
        return apiClient.post(POSTS_ENDPOINT, requestBody);
    }

    public Response updatePost(int postId, Object requestBody) {
        LOG.info("Updating post ID: {}", postId);
        return apiClient.put(POSTS_ENDPOINT + "/" + postId, requestBody);
    }

    public Response patchPost(int postId, Object requestBody) {
        LOG.info("Patching post ID: {}", postId);
        return apiClient.patch(POSTS_ENDPOINT + "/" + postId, requestBody);
    }

    public Response deletePost(int postId) {
        LOG.info("Deleting post ID: {}", postId);
        return apiClient.delete(POSTS_ENDPOINT + "/" + postId);
    }
}
