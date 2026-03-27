package com.api.automation.hooks;

import com.api.automation.utils.ScenarioContext;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Cucumber hooks executed before/after each scenario.
 * Handles logging, context setup, and teardown.
 */
public class Hooks {

    private static final Logger LOG = LogManager.getLogger(Hooks.class);

    @Before
    public void beforeScenario(Scenario scenario) {
        LOG.info("═══════════════════════════════════════════════════════════");
        LOG.info("STARTING SCENARIO: {}", scenario.getName());
        LOG.info("Tags: {}", scenario.getSourceTagNames());
        LOG.info("═══════════════════════════════════════════════════════════");
    }

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
