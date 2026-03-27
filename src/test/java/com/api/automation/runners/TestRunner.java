package com.api.automation.runners;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

/**
 * Primary Cucumber test runner.
 *
 * Run all tests:       mvn test
 * Run by tag:          mvn test -Dcucumber.filter.tags="@smoke"
 * Run by environment:  mvn test -Denv=dev
 * Run a profile:       mvn test -Psmoke -Pdev
 */
@RunWith(Cucumber.class)
@CucumberOptions(
        features = "src/test/resources/features",
        glue = {"com.api.automation.stepdefinitions", "com.api.automation.hooks"},
        plugin = {
                "pretty",
                "html:target/cucumber-reports/cucumber.html",
                "json:target/cucumber-reports/cucumber.json",
                "junit:target/cucumber-reports/cucumber.xml"
        },
        monochrome = true,
        dryRun = false
)
public class TestRunner {
}
