package com.api.automation.runners;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

/**
 * Smoke-only Cucumber test runner.
 * Executes only scenarios tagged with @smoke for quick sanity checks.
 *
 * WHY a separate runner: In CI pipelines, you often need a fast feedback loop.
 *   SmokeTestRunner runs only @smoke tests (seconds) vs TestRunner running all 25+ tests.
 *
 * BENEFIT: Use in PR checks for fast gate, run full suite in nightly builds.
 *
 * Run via: mvn test -Dtest=SmokeTestRunner
 */
@RunWith(Cucumber.class)           // Delegates execution to the Cucumber JUnit runner
@CucumberOptions(
        features = "src/test/resources/features",                   // Root folder for .feature files
        glue = {"com.api.automation.stepdefinitions",                // Step definitions package
                "com.api.automation.hooks"},                         // Hooks package
        tags = "@smoke",                                            // Only run @smoke-tagged scenarios
        plugin = {
                "pretty",                                           // Coloured console output
                "html:target/cucumber-reports/smoke-report.html",   // Separate HTML report for smoke
                "json:target/cucumber-reports/smoke-report.json"    // Separate JSON report for smoke
        },
        monochrome = true                                           // Clean console output
)
public class SmokeTestRunner {
}
