package com.api.automation.utils;

import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Thread-safe state holder for sharing data between Cucumber step definitions.
 *
 * Problem: Cucumber steps are separate Java methods, but they need to share data.
 *   e.g., Step 1 makes a POST call, Step 3 checks the response from that call.
 *
 * Solution: ThreadLocal variables store per-thread state. Each test thread gets
 *   its own copy, making this safe for parallel execution.
 *
 * WHY ThreadLocal instead of instance variables:
 *   - Cucumber creates ONE instance of each step def class per scenario
 *   - But steps span MULTIPLE classes (Post, Common, etc.)
 *   - ThreadLocal is accessible from ANY class without dependency injection
 *
 * BENEFIT: Any step definition can call ScenarioContext.getResponse() to access
 *   the last API response, regardless of which class made the HTTP call.
 *
 * Lifecycle: @Before hook initializes → steps read/write → @After hook calls clear().
 */
public final class ScenarioContext {

    private static final Logger LOG = LogManager.getLogger(ScenarioContext.class);

    // ThreadLocal ensures each parallel test thread has its own isolated state.
    // WHY ThreadLocal: Regular static fields would cause data races in parallel execution —
    //   one thread's response would overwrite another's. ThreadLocal prevents this.
    private static final ThreadLocal<Response> CURRENT_RESPONSE = new ThreadLocal<>();     // Last API response
    private static final ThreadLocal<Object> CURRENT_REQUEST_BODY = new ThreadLocal<>();   // Last request body sent
    private static final ThreadLocal<Integer> CURRENT_STATUS_CODE = new ThreadLocal<>();    // Last HTTP status code

    /** Private constructor — all methods are static. */
    private ScenarioContext() {
    }

    /** Stores the API response and extracts its status code. Called after every API call in step defs. */
    public static void setResponse(Response response) {
        CURRENT_RESPONSE.set(response);
        CURRENT_STATUS_CODE.set(response.getStatusCode());
        LOG.debug("Stored response with status: {}", response.getStatusCode());
    }

    /** Retrieves the stored response. Used by assertion steps (e.g., "status code should be 200"). */
    public static Response getResponse() {
        return CURRENT_RESPONSE.get();
    }

    /** Stores the request body POJO. Used by steps that need to reference what was sent. */
    public static void setRequestBody(Object body) {
        CURRENT_REQUEST_BODY.set(body);
    }

    /** Retrieves the stored request body. */
    public static Object getRequestBody() {
        return CURRENT_REQUEST_BODY.get();
    }

    /** Returns the last response's HTTP status code, or 0 if no response stored yet. */
    public static int getStatusCode() {
        Integer code = CURRENT_STATUS_CODE.get();
        return code != null ? code : 0;
    }

    /**
     * Removes all stored state. MUST be called in @After hook.
     * Without this, ThreadLocal values leak when threads are reused in a thread pool.
     *
     * WHY: Thread pools recycle threads. If clear() isn't called, the next test running
     *   on the same thread would see stale data from the previous test → false passes/failures.
     */
    public static void clear() {
        CURRENT_RESPONSE.remove();
        CURRENT_REQUEST_BODY.remove();
        CURRENT_STATUS_CODE.remove();
        LOG.debug("Scenario context cleared");
    }
}
