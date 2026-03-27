package com.api.automation.runners;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

/**
 * Runner for smoke tests only.
 * Executes scenarios tagged with @smoke.
 */
@RunWith(Cucumber.class)
@CucumberOptions(
        features = "src/test/resources/features",
        glue = {"com.api.automation.stepdefinitions", "com.api.automation.hooks"},
        tags = "@smoke",
        plugin = {
                "pretty",
                "html:target/cucumber-reports/smoke-report.html",
                "json:target/cucumber-reports/smoke-report.json"
        },
        monochrome = true
)
public class SmokeTestRunner {
}
