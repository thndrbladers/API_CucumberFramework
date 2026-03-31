package com.api.automation.runners;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

/**
 * Primary Cucumber test runner. Executes ALL scenarios across every feature file.
 *
 * WHY @RunWith(Cucumber.class): Tells JUnit to delegate execution to Cucumber's engine
 *   instead of JUnit's default. Cucumber parses .feature files, matches steps, and runs them.
 *
 * WHY @CucumberOptions: Configures where to find features, which packages contain glue code,
 *   and which report plugins to generate. Without it, Cucumber wouldn't know where to look.
 *
 * Run all tests:       mvn test
 * Run by tag:          mvn test -Dcucumber.filter.tags="@smoke"
 * Run by environment:  mvn test -Denv=dev
 * Run a profile:       mvn test -Psmoke -Pdev
 */
@RunWith(Cucumber.class)           // Delegates execution to the Cucumber JUnit runner
@CucumberOptions(
        features = "src/test/resources/features",                  // Root folder for .feature files (scanned recursively)
        glue = {"com.api.automation.stepdefinitions",               // Package containing step definitions
                "com.api.automation.hooks"},                        // Package containing Before/After hooks
        plugin = {
                "pretty",                                          // Console output with colours
                "html:target/cucumber-reports/cucumber.html",      // HTML report
                "json:target/cucumber-reports/cucumber.json",      // JSON report (CI integration)
                "junit:target/cucumber-reports/cucumber.xml"        // JUnit XML report
        },
        monochrome = true,                                          // Strip ANSI codes for clean console logs
        dryRun = false                                              // false = execute steps; true = validate glue only
)
public class TestRunner {
}
