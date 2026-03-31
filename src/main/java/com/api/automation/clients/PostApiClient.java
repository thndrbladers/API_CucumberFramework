package com.api.automation.clients;

import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Domain client for the Posts resource (/posts endpoint).
 * Wraps ApiClient with typed methods so step definitions never deal with raw URLs.
 * If the endpoint path changes, only this class needs updating.
 *
 * WHY: Encapsulates endpoint paths — step defs call postApiClient.createPost(body)
 *      instead of apiClient.post("/posts", body). Cleaner, easier to refactor.
 *
 * BENEFIT: Adding a new post operation (e.g., search) only modifies this class;
 *          no step definition or feature file changes needed.
 */
public class PostApiClient {

    private static final Logger LOG = LogManager.getLogger(PostApiClient.class);

    // Base path for all post operations
    private static final String POSTS_ENDPOINT = "/posts";

    // Generic HTTP client this domain client delegates to
    private final ApiClient apiClient;

    /** Constructor — creates a fresh ApiClient (picks up base URL from config). */
    public PostApiClient() {
        this.apiClient = new ApiClient();
    }

    /** GET /posts — returns all 100 posts as a JSON array. */
    public Response getPosts() {
        LOG.info("Fetching all posts");
        return apiClient.get(POSTS_ENDPOINT);
    }

    /** GET /posts/{id} — returns a single post by ID, or 404 if not found. */
    public Response getPostById(int postId) {
        LOG.info("Fetching post by ID: {}", postId);
        return apiClient.get(POSTS_ENDPOINT + "/" + postId);
    }

    /** POST /posts — creates a new post. Expects a PostRequest body. Returns 201 with created resource. */
    public Response createPost(Object requestBody) {
        LOG.info("Creating post");
        return apiClient.post(POSTS_ENDPOINT, requestBody);
    }

    /** PUT /posts/{id} — full replacement of a post. All fields must be provided. Returns 200. */
    public Response updatePost(int postId, Object requestBody) {
        LOG.info("Updating post ID: {}", postId);
        return apiClient.put(POSTS_ENDPOINT + "/" + postId, requestBody);
    }

    /** PATCH /posts/{id} — partial update. Only provided fields are changed. Returns 200. */
    public Response patchPost(int postId, Object requestBody) {
        LOG.info("Patching post ID: {}", postId);
        return apiClient.patch(POSTS_ENDPOINT + "/" + postId, requestBody);
    }

    /** DELETE /posts/{id} — removes a post. Returns 200 with empty body. */
    public Response deletePost(int postId) {
        LOG.info("Deleting post ID: {}", postId);
        return apiClient.delete(POSTS_ENDPOINT + "/" + postId);
    }
}
