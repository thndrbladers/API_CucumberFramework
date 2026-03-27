package com.api.automation.stepdefinitions;

import com.api.automation.clients.UserApiClient;
import com.api.automation.payloads.request.UserRequest;
import com.api.automation.utils.ResponseValidator;
import com.api.automation.utils.ScenarioContext;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Step definitions for all User API operations (JSONPlaceholder).
 */
public class UserStepDefinitions {

    private static final Logger LOG = LogManager.getLogger(UserStepDefinitions.class);

    private UserApiClient apiClient;
    private UserRequest userRequest;

    // ── Setup ────────────────────────────────────────────────────────────

    @Given("I set up the user API client")
    public void iSetUpTheUserApiClient() {
        apiClient = new UserApiClient();
        LOG.info("User API client initialized");
    }

    // ── Request Preparation ──────────────────────────────────────────────

    @Given("I prepare a user request with name {string} username {string} and email {string}")
    public void iPrepareUserRequest(String name, String username, String email) {
        userRequest = new UserRequest(name, username, email);
        ScenarioContext.setRequestBody(userRequest);
        LOG.info("Prepared user request: name={}, username={}, email={}", name, username, email);
    }

    // ── GET Operations ───────────────────────────────────────────────────

    @When("I send a GET request to fetch all users")
    public void iSendGetRequestToFetchAllUsers() {
        Response response = apiClient.getUsers();
        ScenarioContext.setResponse(response);
    }

    @When("I send a GET request to fetch user with ID {int}")
    public void iSendGetRequestToFetchUserById(int userId) {
        Response response = apiClient.getUserById(userId);
        ScenarioContext.setResponse(response);
    }

    // ── POST Operations ──────────────────────────────────────────────────

    @When("I send a POST request to create a user")
    public void iSendPostRequestToCreateUser() {
        Response response = apiClient.createUser(userRequest);
        ScenarioContext.setResponse(response);
    }

    // ── PUT Operations ───────────────────────────────────────────────────

    @When("I send a PUT request to update user with ID {int}")
    public void iSendPutRequestToUpdateUser(int userId) {
        Response response = apiClient.updateUser(userId, userRequest);
        ScenarioContext.setResponse(response);
    }

    // ── DELETE Operations ────────────────────────────────────────────────

    @When("I send a DELETE request to delete user with ID {int}")
    public void iSendDeleteRequestToDeleteUser(int userId) {
        Response response = apiClient.deleteUser(userId);
        ScenarioContext.setResponse(response);
    }

    // ── Response Assertions ──────────────────────────────────────────────

    @And("the response should contain a list of users")
    public void theResponseShouldContainAListOfUsers() {
        Response response = ScenarioContext.getResponse();
        List<?> users = response.jsonPath().getList("$");
        assertNotNull("Users list should not be null", users);
        assertFalse("Users list should not be empty", users.isEmpty());
        LOG.info("Returned {} users", users.size());
    }

    @And("the response should contain user with ID {int}")
    public void theResponseShouldContainUserWithId(int expectedId) {
        ResponseValidator.assertFieldEquals(
                ScenarioContext.getResponse(), "id", String.valueOf(expectedId));
    }

    @And("the response should contain the user name {string}")
    public void theResponseShouldContainTheUserName(String expectedName) {
        ResponseValidator.assertFieldEquals(ScenarioContext.getResponse(), "name", expectedName);
    }

    @And("the response should contain the user email {string}")
    public void theResponseShouldContainTheUserEmail(String expectedEmail) {
        ResponseValidator.assertFieldEquals(ScenarioContext.getResponse(), "email", expectedEmail);
    }
}
