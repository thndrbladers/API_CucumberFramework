package com.api.automation.clients;

import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Domain-specific API client for the Comment resource (JSONPlaceholder).
 * All CRUD operations for /comments live here.
 */
public class CommentApiClient {

    private static final Logger LOG = LogManager.getLogger(CommentApiClient.class);
    private static final String COMMENTS_ENDPOINT = "/comments";

    private final ApiClient apiClient;

    public CommentApiClient() {
        this.apiClient = new ApiClient();
    }

    public Response getComments() {
        LOG.info("Fetching all comments");
        return apiClient.get(COMMENTS_ENDPOINT);
    }

    public Response getCommentById(int commentId) {
        LOG.info("Fetching comment by ID: {}", commentId);
        return apiClient.get(COMMENTS_ENDPOINT + "/" + commentId);
    }

    public Response getCommentsByPostId(int postId) {
        LOG.info("Fetching comments for post ID: {}", postId);
        return apiClient.get(COMMENTS_ENDPOINT + "?postId=" + postId);
    }

    public Response createComment(Object requestBody) {
        LOG.info("Creating comment");
        return apiClient.post(COMMENTS_ENDPOINT, requestBody);
    }

    public Response updateComment(int commentId, Object requestBody) {
        LOG.info("Updating comment ID: {}", commentId);
        return apiClient.put(COMMENTS_ENDPOINT + "/" + commentId, requestBody);
    }

    public Response patchComment(int commentId, Object requestBody) {
        LOG.info("Patching comment ID: {}", commentId);
        return apiClient.patch(COMMENTS_ENDPOINT + "/" + commentId, requestBody);
    }

    public Response deleteComment(int commentId) {
        LOG.info("Deleting comment ID: {}", commentId);
        return apiClient.delete(COMMENTS_ENDPOINT + "/" + commentId);
    }
}
