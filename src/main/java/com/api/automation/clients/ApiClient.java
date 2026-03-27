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
 * Reusable REST API client built on top of RestAssured.
 * Provides a fluent interface for building and executing HTTP requests.
 * All configuration (base URL, timeouts, logging) is driven by ConfigManager.
 */
public class ApiClient {

    private static final Logger LOG = LogManager.getLogger(ApiClient.class);
    private final RequestSpecification requestSpec;

    public ApiClient() {
        ConfigManager config = ConfigManager.getInstance();
        RequestSpecBuilder builder = new RequestSpecBuilder()
                .setBaseUri(config.getBaseUrl())
                .setContentType(ContentType.JSON)
                .setAccept(ContentType.JSON);

        if (config.shouldLogRequest()) {
            builder.log(LogDetail.ALL);
        }

        this.requestSpec = builder.build();
    }

    // ── GET ──────────────────────────────────────────────────────────────

    public Response get(String endpoint) {
        LOG.info("GET {}", endpoint);
        return given()
                .when()
                .get(endpoint)
                .then()
                .extract().response();
    }

    public Response get(String endpoint, Map<String, ?> queryParams) {
        LOG.info("GET {} with queryParams: {}", endpoint, queryParams);
        return given()
                .queryParams(queryParams)
                .when()
                .get(endpoint)
                .then()
                .extract().response();
    }

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

    public Response post(String endpoint, Object body) {
        LOG.info("POST {}", endpoint);
        return given()
                .body(body)
                .when()
                .post(endpoint)
                .then()
                .extract().response();
    }

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

    public Response delete(String endpoint) {
        LOG.info("DELETE {}", endpoint);
        return given()
                .when()
                .delete(endpoint)
                .then()
                .extract().response();
    }

    // ── Helpers ──────────────────────────────────────────────────────────

    /** Returns a pre-configured RequestSpecification ready for chaining. */
    private RequestSpecification given() {
        ConfigManager config = ConfigManager.getInstance();
        RequestSpecification spec = RestAssured.given().spec(requestSpec);
        if (config.shouldLogResponse()) {
            return spec;
        }
        return spec;
    }

    /** Adds a Bearer token to the next request. */
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
