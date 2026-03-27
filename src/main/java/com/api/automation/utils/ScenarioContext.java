package com.api.automation.utils;

import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Thread-safe context holder for sharing state between step definitions within a scenario.
 * Uses ThreadLocal to ensure parallel execution safety.
 */
public final class ScenarioContext {

    private static final Logger LOG = LogManager.getLogger(ScenarioContext.class);

    private static final ThreadLocal<Response> CURRENT_RESPONSE = new ThreadLocal<>();
    private static final ThreadLocal<Object> CURRENT_REQUEST_BODY = new ThreadLocal<>();
    private static final ThreadLocal<Integer> CURRENT_STATUS_CODE = new ThreadLocal<>();

    private ScenarioContext() {
    }

    public static void setResponse(Response response) {
        CURRENT_RESPONSE.set(response);
        CURRENT_STATUS_CODE.set(response.getStatusCode());
        LOG.debug("Stored response with status: {}", response.getStatusCode());
    }

    public static Response getResponse() {
        return CURRENT_RESPONSE.get();
    }

    public static void setRequestBody(Object body) {
        CURRENT_REQUEST_BODY.set(body);
    }

    public static Object getRequestBody() {
        return CURRENT_REQUEST_BODY.get();
    }

    public static int getStatusCode() {
        Integer code = CURRENT_STATUS_CODE.get();
        return code != null ? code : 0;
    }

    /** Must be called in @After hook to prevent memory leaks during parallel execution. */
    public static void clear() {
        CURRENT_RESPONSE.remove();
        CURRENT_REQUEST_BODY.remove();
        CURRENT_STATUS_CODE.remove();
        LOG.debug("Scenario context cleared");
    }
}
