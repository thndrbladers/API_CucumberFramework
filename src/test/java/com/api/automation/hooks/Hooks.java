package com.api.automation.hooks;

import com.api.automation.utils.ScenarioContext;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Cucumber hooks executed before and after every scenario.
 * Handles logging, failure diagnostics, and ThreadLocal cleanup.
 *
 * WHY hooks instead of putting this logic in step definitions:
 *   - Runs automatically for EVERY scenario — no risk of forgetting cleanup
 *   - Separates cross-cutting concerns (logging, cleanup) from business logic (API calls)
 *
 * BENEFIT: When a test fails, the @After hook attaches the API response body
 *   directly into the Cucumber HTML report — you can debug without re-running.
 */
public class Hooks {

    private static final Logger LOG = LogManager.getLogger(Hooks.class);

    /**
     * Runs before each scenario. Logs the scenario name and tags for traceability.
     * BENEFIT: In CI logs you can immediately see which scenario is running,
     *   making it easy to trace failures back to a specific test.
     * @param scenario Cucumber-injected object containing scenario metadata
     */
    @Before
    public void beforeScenario(Scenario scenario) {
        LOG.info("═══════════════════════════════════════════════════════════");
        LOG.info("STARTING SCENARIO: {}", scenario.getName());
        LOG.info("Tags: {}", scenario.getSourceTagNames());
        LOG.info("═══════════════════════════════════════════════════════════");
    }

    /**
     * Runs after each scenario.
     * On failure: attaches the last API response body to the Cucumber report.
     * Always: logs the final status and clears ScenarioContext to prevent ThreadLocal leaks.
     *
     * WHY clear() is critical: Without it, the next scenario on the same thread
     *   would inherit stale response data → false passes or confusing failures.
     */
    @After
    public void afterScenario(Scenario scenario) {
        // Attach response body to report on failure for debugging
        if (scenario.isFailed() && ScenarioContext.getResponse() != null) {
            String responseBody = ScenarioContext.getResponse().getBody().asPrettyString();
            scenario.attach(responseBody.getBytes(), "application/json", "Response Body");
            LOG.error("Scenario FAILED — Response body attached to report");
        }

        LOG.info("═══════════════════════════════════════════════════════════");
        LOG.info("FINISHED SCENARIO: {} — Status: {}", scenario.getName(), scenario.getStatus());
        LOG.info("═══════════════════════════════════════════════════════════");

        // Clear thread-local context to prevent leaks in parallel execution
        ScenarioContext.clear();
    }
}
