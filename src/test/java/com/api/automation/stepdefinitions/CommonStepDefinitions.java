package com.api.automation.stepdefinitions;

import com.api.automation.utils.ResponseValidator;
import com.api.automation.utils.ScenarioContext;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;

/**
 * Common step definitions shared across all resource domains.
 * Contains generic response assertions (status code, field checks, content type)
 * so they don't need to be duplicated in each domain's step definition class.
 * All methods pull the current response from ScenarioContext.
 *
 * WHY: The step "the response status code should be 200" is used in Posts, Comments,
 *   AND Users features. Without this class, it would be duplicated 3 times —
 *   Cucumber would throw a DuplicateStepDefinitionException.
 *
 * BENEFIT: One place for shared assertions. New resources get these steps for free
 *   just by using the same Gherkin phrasing in their feature files.
 */
public class CommonStepDefinitions {

    /** Asserts the HTTP response status code matches the expected value. */
    @Then("the response status code should be {int}")
    public void theResponseStatusCodeShouldBe(int expectedStatusCode) {
        ResponseValidator.assertStatusCode(ScenarioContext.getResponse(), expectedStatusCode);
    }

    /** Asserts a JSON field (by JSONPath) exists and is not null. Used with full path syntax. */
    @And("the response should contain field {string} with a non-null value")
    public void theResponseShouldContainFieldWithNonNullValue(String jsonPath) {
        ResponseValidator.assertFieldNotNull(ScenarioContext.getResponse(), jsonPath);
    }

    /** Shorthand variant — asserts a top-level field is not null. */
    @And("the response should contain a non-null {string}")
    public void theResponseShouldContainNonNull(String fieldName) {
        ResponseValidator.assertFieldNotNull(ScenarioContext.getResponse(), fieldName);
    }

    /** Asserts the Content-Type header contains the expected media type (e.g. "json"). */
    @And("the response content type should contain {string}")
    public void theResponseContentTypeShouldContain(String expectedContentType) {
        ResponseValidator.assertContentType(ScenarioContext.getResponse(), expectedContentType);
    }
}
