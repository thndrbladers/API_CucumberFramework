package com.api.automation.utils;

import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static org.junit.Assert.*;

/**
 * Centralized response validation utility.
 * Provides assertion helpers that produce clear failure messages
 * and log the response details for debugging.
 */
public final class ResponseValidator {

    private static final Logger LOG = LogManager.getLogger(ResponseValidator.class);

    private ResponseValidator() {
    }

    /** Assert the HTTP status code matches expected. */
    public static void assertStatusCode(Response response, int expectedStatusCode) {
        int actual = response.getStatusCode();
        LOG.info("Asserting status code: expected={}, actual={}", expectedStatusCode, actual);
        assertEquals("Unexpected HTTP status code", expectedStatusCode, actual);
    }

    /** Assert the response body contains a specific field with the expected value. */
    public static void assertFieldEquals(Response response, String jsonPath, String expectedValue) {
        String actual = response.jsonPath().getString(jsonPath);
        LOG.info("Asserting field '{}': expected='{}', actual='{}'", jsonPath, expectedValue, actual);
        assertEquals("Mismatch for JSON field: " + jsonPath, expectedValue, actual);
    }

    /** Assert the response body contains a specific field with a non-null value. */
    public static void assertFieldNotNull(Response response, String jsonPath) {
        Object actual = response.jsonPath().get(jsonPath);
        LOG.info("Asserting field '{}' is not null: actual='{}'", jsonPath, actual);
        assertNotNull("Expected non-null value for JSON field: " + jsonPath, actual);
    }

    /** Assert the response body contains a field whose value contains the expected substring. */
    public static void assertFieldContains(Response response, String jsonPath, String expectedSubstring) {
        String actual = response.jsonPath().getString(jsonPath);
        LOG.info("Asserting field '{}' contains '{}': actual='{}'", jsonPath, expectedSubstring, actual);
        assertNotNull("Field '" + jsonPath + "' is null", actual);
        assertTrue("Field '" + jsonPath + "' does not contain '" + expectedSubstring + "'",
                actual.contains(expectedSubstring));
    }

    /** Assert the response time is below a threshold (in milliseconds). */
    public static void assertResponseTimeBelow(Response response, long maxMillis) {
        long actual = response.getTime();
        LOG.info("Asserting response time: max={}ms, actual={}ms", maxMillis, actual);
        assertTrue("Response time " + actual + "ms exceeds max " + maxMillis + "ms", actual <= maxMillis);
    }

    /** Assert the response body contains a list of at least N items at the given JSON path. */
    public static void assertListSizeAtLeast(Response response, String jsonPath, int minSize) {
        int size = response.jsonPath().getList(jsonPath).size();
        LOG.info("Asserting list '{}' size: min={}, actual={}", jsonPath, minSize, size);
        assertTrue("List '" + jsonPath + "' has " + size + " items, expected at least " + minSize, size >= minSize);
    }

    /** Assert the content-type header matches expected. */
    public static void assertContentType(Response response, String expectedContentType) {
        String actual = response.getContentType();
        LOG.info("Asserting content-type: expected='{}', actual='{}'", expectedContentType, actual);
        assertTrue("Content-Type mismatch", actual != null && actual.contains(expectedContentType));
    }
}
