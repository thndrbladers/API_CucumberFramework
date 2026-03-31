package com.api.automation.utils;

import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static org.junit.Assert.*;

/**
 * Centralized assertion library for API response validation.
 * Every assertion logs what it's checking (expected vs actual) before asserting,
 * so failures produce clear, debuggable output in both console and log files.
 *
 * WHY centralised: Without this, assertions would be scattered across every step
 *   definition with inconsistent messages and no logging. This class ensures:
 *   1. Consistent failure messages ("Unexpected HTTP status code", "Mismatch for JSON field")
 *   2. Automatic logging of expected vs actual before every assert
 *   3. One place to add new assertion types as the framework grows
 *
 * BENEFIT: Step definitions become one-liners:
 *   ResponseValidator.assertStatusCode(response, 200);
 *   ResponseValidator.assertFieldEquals(response, "title", "My Post");
 *
 * All methods are static. Private constructor prevents instantiation.
 */
public final class ResponseValidator {

    private static final Logger LOG = LogManager.getLogger(ResponseValidator.class);

    private ResponseValidator() {
    }

    /** Asserts HTTP status code matches expected. Fails with "Unexpected HTTP status code" message. */
    public static void assertStatusCode(Response response, int expectedStatusCode) {
        int actual = response.getStatusCode();
        LOG.info("Asserting status code: expected={}, actual={}", expectedStatusCode, actual);
        assertEquals("Unexpected HTTP status code", expectedStatusCode, actual);
    }

    /** Asserts a JSON field equals the expected string value. Uses JsonPath to extract the field. */
    public static void assertFieldEquals(Response response, String jsonPath, String expectedValue) {
        String actual = response.jsonPath().getString(jsonPath);
        LOG.info("Asserting field '{}': expected='{}', actual='{}'", jsonPath, expectedValue, actual);
        assertEquals("Mismatch for JSON field: " + jsonPath, expectedValue, actual);
    }

    /** Asserts a JSON field exists and is not null. */
    public static void assertFieldNotNull(Response response, String jsonPath) {
        Object actual = response.jsonPath().get(jsonPath);
        LOG.info("Asserting field '{}' is not null: actual='{}'", jsonPath, actual);
        assertNotNull("Expected non-null value for JSON field: " + jsonPath, actual);
    }

    /** Asserts a JSON field's value contains the expected substring. */
    public static void assertFieldContains(Response response, String jsonPath, String expectedSubstring) {
        String actual = response.jsonPath().getString(jsonPath);
        LOG.info("Asserting field '{}' contains '{}': actual='{}'", jsonPath, expectedSubstring, actual);
        assertNotNull("Field '" + jsonPath + "' is null", actual);
        assertTrue("Field '" + jsonPath + "' does not contain '" + expectedSubstring + "'",
                actual.contains(expectedSubstring));
    }

    /** Asserts response time is under a threshold. Useful for performance checks. */
    public static void assertResponseTimeBelow(Response response, long maxMillis) {
        long actual = response.getTime();
        LOG.info("Asserting response time: max={}ms, actual={}ms", maxMillis, actual);
        assertTrue("Response time " + actual + "ms exceeds max " + maxMillis + "ms", actual <= maxMillis);
    }

    /** Asserts a JSON array at the given path has at least minSize elements. */
    public static void assertListSizeAtLeast(Response response, String jsonPath, int minSize) {
        int size = response.jsonPath().getList(jsonPath).size();
        LOG.info("Asserting list '{}' size: min={}, actual={}", jsonPath, minSize, size);
        assertTrue("List '" + jsonPath + "' has " + size + " items, expected at least " + minSize, size >= minSize);
    }

    /** Asserts the Content-Type header contains the expected string (e.g., "application/json"). */
    public static void assertContentType(Response response, String expectedContentType) {
        String actual = response.getContentType();
        LOG.info("Asserting content-type: expected='{}', actual='{}'", expectedContentType, actual);
        assertTrue("Content-Type mismatch", actual != null && actual.contains(expectedContentType));
    }
}
