package com.api.automation.clients;

import com.api.automation.config.ConfigManager;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;

/**
 * Generic HTTP client wrapper around RestAssured.
 * Provides get/post/put/patch/delete methods that domain clients (PostApiClient, etc.) delegate to.
 * Reads base URL, timeouts, and logging settings from ConfigManager — no hardcoded values.
 *
 * WHY: Centralises all HTTP logic in one place. Domain clients delegate here, so adding
 *      auth, retries, or logging changes only touches this class — not every test.
 *
 * BENEFIT: Adding a new resource (e.g., /albums) only requires a small domain client
 *          that calls these methods; no duplicate HTTP boilerplate.
 */
public class ApiClient {

    private static final Logger LOG = LogManager.getLogger(ApiClient.class);

    // Pre-configured request template: base URL, content type, logging — reused for every call.
    // BENEFIT: Built once in the constructor, shared by all HTTP methods → consistent config.
    private final RequestSpecification requestSpec;

    /**
     * Constructor — builds the request spec once from config.
     * Sets base URI (e.g., https://jsonplaceholder.typicode.com), JSON content type,
     * and conditional request logging (controlled by log.request property).
     *
     * WHY RequestSpecBuilder: Separates configuration from execution.
     *   Example flow → new PostApiClient() → new ApiClient() → builder.setBaseUri(url).build()
     *   Every subsequent get()/post() call reuses this spec automatically.
     */
    public ApiClient() {
        ConfigManager config = ConfigManager.getInstance();
        RequestSpecBuilder builder = new RequestSpecBuilder()
                .setBaseUri(config.getBaseUrl())        // From config/{env}.properties → base.url
                .setContentType(ContentType.JSON)       // All requests send JSON
                .setAccept(ContentType.JSON);            // All requests expect JSON back

        if (config.shouldLogRequest()) {
            builder.log(LogDetail.ALL);                  // Logs full request details to console
        }

        this.requestSpec = builder.build();
    }

    // ── GET ──────────────────────────────────────────────────────────────

    /** Simple GET — sends request to endpoint, returns raw Response. */
    public Response get(String endpoint) {
        LOG.info("GET {}", endpoint);
        return given()
                .when()
                .get(endpoint)
                .then()
                .extract().response();
    }

    /**
     * GET with query parameters — e.g., /comments?postId=1.
     * WHY: Query params as a Map keeps URLs clean and handles encoding automatically.
     * Example: apiClient.get("/comments", Map.of("postId", 1))  → GET /comments?postId=1
     */
    public Response get(String endpoint, Map<String, ?> queryParams) {
        LOG.info("GET {} with queryParams: {}", endpoint, queryParams);
        return given()
                .queryParams(queryParams)
                .when()
                .get(endpoint)
                .then()
                .extract().response();
    }

    /** GET with both query parameters and custom headers. */
    public Response get(String endpoint, Map<String, ?> queryParams, Map<String, String> headers) {
        LOG.info("GET {} with queryParams: {} headers: {}", endpoint, queryParams, headers);
        return given()
                .queryParams(queryParams)
                .headers(headers)
                .when()
                .get(endpoint)
                .then()
                .extract().response();
    }

    // ── POST ─────────────────────────────────────────────────────────────

    /**
     * POST — sends body (POJO) as JSON to endpoint. Jackson serializes it automatically.
     * WHY Object body: Accepts any POJO (PostRequest, CommentRequest, etc.) —
     *   RestAssured + Jackson convert it to JSON, so callers never build raw JSON strings.
     */
    public Response post(String endpoint, Object body) {
        LOG.info("POST {}", endpoint);
        return given()
                .body(body)          // RestAssured serializes the POJO to JSON via Jackson
                .when()
                .post(endpoint)
                .then()
                .extract().response();
    }

    /** POST with custom headers — used when APIs require extra headers (e.g., X-Request-Id). */
    public Response post(String endpoint, Object body, Map<String, String> headers) {
        LOG.info("POST {} with headers: {}", endpoint, headers);
        return given()
                .headers(headers)
                .body(body)
                .when()
                .post(endpoint)
                .then()
                .extract().response();
    }

    // ── PUT ──────────────────────────────────────────────────────────────

    /** PUT — full resource replacement. Sends entire updated body to endpoint. */
    public Response put(String endpoint, Object body) {
        LOG.info("PUT {}", endpoint);
        return given()
                .body(body)
                .when()
                .put(endpoint)
                .then()
                .extract().response();
    }

    // ── PATCH ────────────────────────────────────────────────────────────

    /**
     * PATCH — partial update. Only fields present in body are modified on server.
     * WHY separate from PUT: PUT replaces the entire resource; PATCH updates only the fields you send.
     * Works with @JsonInclude(NON_NULL) — only non-null fields appear in the JSON body.
     */
    public Response patch(String endpoint, Object body) {
        LOG.info("PATCH {}", endpoint);
        return given()
                .body(body)
                .when()
                .patch(endpoint)
                .then()
                .extract().response();
    }

    // ── DELETE ───────────────────────────────────────────────────────────

    /** DELETE — removes resource at endpoint. No request body needed. */
    public Response delete(String endpoint) {
        LOG.info("DELETE {}", endpoint);
        return given()
                .when()
                .delete(endpoint)
                .then()
                .extract().response();
    }

    // ── Helpers ──────────────────────────────────────────────────────────

    /**
     * Creates a new request from the pre-built spec.
     * Every HTTP method above calls this to get a fresh, configured request.
     *
     * WHY: RestAssured.given().spec(requestSpec) clones the spec into a new request,
     *      so each call is independent — adding headers/params in one call won't leak into others.
     */
    private RequestSpecification given() {
        ConfigManager config = ConfigManager.getInstance();
        RequestSpecification spec = RestAssured.given().spec(requestSpec);
        if (config.shouldLogResponse()) {
            return spec;
        }
        return spec;
    }

    /**
     * Adds a Bearer token header for authenticated endpoints.
     * Token is read from config (auth.token property). Returns 'this' for method chaining.
     * Usage: apiClient.withAuth().get("/protected-resource")
     */
    public ApiClient withAuth() {
        String token = ConfigManager.getInstance().getAuthToken();
        requestSpec.header("Authorization", "Bearer " + token);
        return this;
    }

    /** Adds custom headers to the next request. */
    public ApiClient withHeaders(Map<String, String> headers) {
        requestSpec.headers(headers);
        return this;
    }
}
