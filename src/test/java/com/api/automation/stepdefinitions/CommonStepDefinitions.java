package com.api.automation.stepdefinitions;

import com.api.automation.utils.ResponseValidator;
import com.api.automation.utils.ScenarioContext;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;

/**
 * Common step definitions shared across all resource domains.
 * Contains generic response assertions that are reusable.
 */
public class CommonStepDefinitions {

    @Then("the response status code should be {int}")
    public void theResponseStatusCodeShouldBe(int expectedStatusCode) {
        ResponseValidator.assertStatusCode(ScenarioContext.getResponse(), expectedStatusCode);
    }

    @And("the response should contain field {string} with a non-null value")
    public void theResponseShouldContainFieldWithNonNullValue(String jsonPath) {
        ResponseValidator.assertFieldNotNull(ScenarioContext.getResponse(), jsonPath);
    }

    @And("the response should contain a non-null {string}")
    public void theResponseShouldContainNonNull(String fieldName) {
        ResponseValidator.assertFieldNotNull(ScenarioContext.getResponse(), fieldName);
    }

    @And("the response content type should contain {string}")
    public void theResponseContentTypeShouldContain(String expectedContentType) {
        ResponseValidator.assertContentType(ScenarioContext.getResponse(), expectedContentType);
    }
}
