package com.api.automation.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import io.restassured.response.Response;

/**
 * JSON utility class wrapping Jackson ObjectMapper.
 * Provides one-liner methods for serialization (POJO → JSON) and
 * deserialization (JSON → POJO) used across step definitions and validators.
 *
 * WHY: Hides Jackson boilerplate (try/catch on JsonProcessingException)
 *   behind clean one-liner calls. Without this, every conversion would need 5+ lines.
 *
 * BENEFIT: Test code stays readable:
 *   String json = JsonUtils.toJson(postRequest);         // instead of mapper.writeValueAsString(...)
 *   PostResponse post = JsonUtils.fromResponse(resp, PostResponse.class);
 *
 * All methods are static — no instantiation needed. Private constructor enforces this.
 */
public final class JsonUtils {

    // Shared ObjectMapper — thread-safe, reused across all calls.
    // FAIL_ON_EMPTY_BEANS=false prevents errors when serializing POJOs with no fields set.
    // WHY shared: Creating a new ObjectMapper per call is expensive; one instance is enough.
    private static final ObjectMapper MAPPER = new ObjectMapper()
            .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

    /** Private constructor — prevents instantiation (utility class pattern). */
    private JsonUtils() {
    }

    /** Converts a POJO to a compact JSON string. e.g., PostRequest → {"title":"...","body":"..."} */
    public static String toJson(Object obj) {
        try {
            return MAPPER.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize object to JSON", e);
        }
    }

    /** Converts a POJO to a pretty-printed JSON string (indented, multi-line). Good for logging. */
    public static String toPrettyJson(Object obj) {
        try {
            return MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize object to pretty JSON", e);
        }
    }

    /** Converts a JSON string to a POJO of the given class. e.g., fromJson(json, PostResponse.class). */
    public static <T> T fromJson(String json, Class<T> clazz) {
        try {
            return MAPPER.readValue(json, clazz);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to deserialize JSON to " + clazz.getSimpleName(), e);
        }
    }

    /** Converts a RestAssured Response body directly to a POJO. Extracts body string internally. */
    public static <T> T fromResponse(Response response, Class<T> clazz) {
        return fromJson(response.getBody().asString(), clazz);
    }

    /** Parses JSON string into a Jackson JsonNode tree for dynamic field access without a POJO.
     *  WHY: Useful when you need to check one field without creating a full response class.
     *  Example: JsonNode node = JsonUtils.toJsonNode(json); node.get("id").asInt();
     */
    public static JsonNode toJsonNode(String json) {
        try {
            return MAPPER.readTree(json);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to parse JSON to JsonNode", e);
        }
    }

    /**
     * Extracts a nested field value using dot-separated path.
     * WHY: Avoids chaining multiple .get() calls manually.
     * Example: getFieldValue(json, "address.city") traverses {"address":{"city":"NYC"}} → "NYC".
     * Returns null if the path doesn't exist.
     */
    public static String getFieldValue(String json, String fieldPath) {
        JsonNode node = toJsonNode(json);
        for (String field : fieldPath.split("\\.")) {
            node = node.path(field);
        }
        return node.isMissingNode() ? null : node.asText();
    }

    /** Exposes the shared ObjectMapper for advanced use (e.g., TypeReference deserialization). */
    public static ObjectMapper getMapper() {
        return MAPPER;
    }
}
