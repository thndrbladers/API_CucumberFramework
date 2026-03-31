package com.api.automation.clients;

import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Domain client for the Comments resource (/comments endpoint).
 * Includes a filter method getCommentsByPostId() for querying comments by parent post.
 *
 * WHY: Separates Comment API knowledge from test logic. Step definitions express
 *      intent ("fetch comments for post 1"), this class handles the how (query params, paths).
 */
public class CommentApiClient {

    private static final Logger LOG = LogManager.getLogger(CommentApiClient.class);

    // Base path for all comment operations
    private static final String COMMENTS_ENDPOINT = "/comments";

    // Generic HTTP client this domain client delegates to
    private final ApiClient apiClient;

    /** Constructor — creates a fresh ApiClient. */
    public CommentApiClient() {
        this.apiClient = new ApiClient();
    }

    /** GET /comments — returns all 500 comments. */
    public Response getComments() {
        LOG.info("Fetching all comments");
        return apiClient.get(COMMENTS_ENDPOINT);
    }

    /** GET /comments/{id} — returns a single comment or 404. */
    public Response getCommentById(int commentId) {
        LOG.info("Fetching comment by ID: {}", commentId);
        return apiClient.get(COMMENTS_ENDPOINT + "/" + commentId);
    }

    /** GET /comments?postId={id} — filters comments belonging to a specific post. */
    public Response getCommentsByPostId(int postId) {
        LOG.info("Fetching comments for post ID: {}", postId);
        return apiClient.get(COMMENTS_ENDPOINT + "?postId=" + postId);
    }

    /** POST /comments — creates a new comment. Returns 201. */
    public Response createComment(Object requestBody) {
        LOG.info("Creating comment");
        return apiClient.post(COMMENTS_ENDPOINT, requestBody);
    }

    /** PUT /comments/{id} — full replacement of a comment. */
    public Response updateComment(int commentId, Object requestBody) {
        LOG.info("Updating comment ID: {}", commentId);
        return apiClient.put(COMMENTS_ENDPOINT + "/" + commentId, requestBody);
    }

    /** PATCH /comments/{id} — partial update of a comment. */
    public Response patchComment(int commentId, Object requestBody) {
        LOG.info("Patching comment ID: {}", commentId);
        return apiClient.patch(COMMENTS_ENDPOINT + "/" + commentId, requestBody);
    }

    /** DELETE /comments/{id} — removes a comment. Returns 200. */
    public Response deleteComment(int commentId) {
        LOG.info("Deleting comment ID: {}", commentId);
        return apiClient.delete(COMMENTS_ENDPOINT + "/" + commentId);
    }
}
