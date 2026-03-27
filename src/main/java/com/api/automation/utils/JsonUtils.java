package com.api.automation.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import io.restassured.response.Response;

/**
 * JSON utility for serialization, deserialization, and JsonNode operations.
 * Wraps Jackson ObjectMapper with convenience methods used across step definitions.
 */
public final class JsonUtils {

    private static final ObjectMapper MAPPER = new ObjectMapper()
            .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

    private JsonUtils() {
    }

    /** Serialize a POJO to JSON string. */
    public static String toJson(Object obj) {
        try {
            return MAPPER.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize object to JSON", e);
        }
    }

    /** Serialize a POJO to pretty-printed JSON string. */
    public static String toPrettyJson(Object obj) {
        try {
            return MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize object to pretty JSON", e);
        }
    }

    /** Deserialize a JSON string to a POJO. */
    public static <T> T fromJson(String json, Class<T> clazz) {
        try {
            return MAPPER.readValue(json, clazz);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to deserialize JSON to " + clazz.getSimpleName(), e);
        }
    }

    /** Deserialize a RestAssured Response body to a POJO. */
    public static <T> T fromResponse(Response response, Class<T> clazz) {
        return fromJson(response.getBody().asString(), clazz);
    }

    /** Parse a JSON string into a Jackson JsonNode for dynamic traversal. */
    public static JsonNode toJsonNode(String json) {
        try {
            return MAPPER.readTree(json);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to parse JSON to JsonNode", e);
        }
    }

    /** Extract a specific field value from a JSON string by field path (dot-separated). */
    public static String getFieldValue(String json, String fieldPath) {
        JsonNode node = toJsonNode(json);
        for (String field : fieldPath.split("\\.")) {
            node = node.path(field);
        }
        return node.isMissingNode() ? null : node.asText();
    }

    public static ObjectMapper getMapper() {
        return MAPPER;
    }
}
