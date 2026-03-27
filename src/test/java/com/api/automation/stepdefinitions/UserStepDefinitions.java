package com.api.automation.stepdefinitions;

import com.api.automation.clients.UserApiClient;
import com.api.automation.payloads.request.UserRequest;
import com.api.automation.payloads.response.UserListResponse;
import com.api.automation.payloads.response.UserResponse;
import com.api.automation.utils.*;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Step definitions for all Post API operations (JSONPlaceholder).
 * Uses ScenarioContext (thread-local) to share state between steps,
 * making this safe for parallel execution.
 */
public class UserStepDefinitions {

    private static final Logger LOG = LogManager.getLogger(UserStepDefinitions.class);

    private UserApiClient apiClient;
    private UserRequest postRequest;
    private Integer createdPostId;

    // Data-driven test data loaded from Excel
    private Map<String, String> testData;

    // ── Setup ────────────────────────────────────────────────────────────

    @Given("I set up the API client")
    public void iSetUpTheApiClient() {
        apiClient = new UserApiClient();
        LOG.info("API client initialized");
    }

    // ── Request Preparation ──────────────────────────────────────────────

    @Given("I prepare a post request with title {string} and body {string}")
    public void iPreparePostRequest(String title, String body) {
        postRequest = new UserRequest(title, body, 1);
        ScenarioContext.setRequestBody(postRequest);
        LOG.info("Prepared request: {}", postRequest);
    }

    @Given("I load post test data from Excel file {string} sheet {string} row {int}")
    public void iLoadPostTestDataFromExcel(String filePath, String sheetName, int rowIndex) {
        testData = ExcelReader.readRow(filePath, sheetName, rowIndex);
        String title = testData.get("title");
        String body = testData.get("body");
        postRequest = new UserRequest(title, body, 1);
        ScenarioContext.setRequestBody(postRequest);
        LOG.info("Loaded test data from Excel — title: {}, body: {}", title, body);
    }

    // ── GET Operations ───────────────────────────────────────────────────

    @When("I send a GET request to fetch all posts")
    public void iSendGetRequestToFetchAllPosts() {
        Response response = apiClient.getPosts();
        ScenarioContext.setResponse(response);
    }

    @When("I send a GET request to fetch post with ID {int}")
    public void iSendGetRequestToFetchPostById(int postId) {
        Response response = apiClient.getPostById(postId);
        ScenarioContext.setResponse(response);
    }

    // ── POST Operations ──────────────────────────────────────────────────

    @When("I send a POST request to create a post")
    public void iSendPostRequestToCreatePost() {
        Response response = apiClient.createPost(postRequest);
        ScenarioContext.setResponse(response);
    }

    // ── PUT Operations ───────────────────────────────────────────────────

    @When("I send a PUT request to update post with ID {int}")
    public void iSendPutRequestToUpdatePost(int postId) {
        Response response = apiClient.updatePost(postId, postRequest);
        ScenarioContext.setResponse(response);
    }

    // ── PATCH Operations ─────────────────────────────────────────────────

    @When("I send a PATCH request to update post with ID {int}")
    public void iSendPatchRequestToUpdatePost(int postId) {
        Response response = apiClient.patchPost(postId, postRequest);
        ScenarioContext.setResponse(response);
    }

    // ── DELETE Operations ────────────────────────────────────────────────

    @When("I send a DELETE request to delete post with ID {int}")
    public void iSendDeleteRequestToDeletePost(int postId) {
        Response response = apiClient.deletePost(postId);
        ScenarioContext.setResponse(response);
    }

    // ── Response Assertions ──────────────────────────────────────────────

    @Then("the response status code should be {int}")
    public void theResponseStatusCodeShouldBe(int expectedStatusCode) {
        ResponseValidator.assertStatusCode(ScenarioContext.getResponse(), expectedStatusCode);
    }

    @And("the response should contain a list of posts")
    public void theResponseShouldContainAListOfPosts() {
        Response response = ScenarioContext.getResponse();
        // JSONPlaceholder returns a flat JSON array
        List<UserResponse> posts = UserListResponse.fromResponse(response);
        assertNotNull("Posts list should not be null", posts);
        assertFalse("Posts list should not be empty", posts.isEmpty());
        LOG.info("Returned {} posts", posts.size());
    }

    @And("the response should contain post with ID {int}")
    public void theResponseShouldContainPostWithId(int expectedPostId) {
        ResponseValidator.assertFieldEquals(
                ScenarioContext.getResponse(), "id", String.valueOf(expectedPostId));
    }

    @And("the response should contain field {string} with a non-null value")
    public void theResponseShouldContainFieldWithNonNullValue(String jsonPath) {
        ResponseValidator.assertFieldNotNull(ScenarioContext.getResponse(), jsonPath);
    }

    @And("the response should contain the title {string}")
    public void theResponseShouldContainTheTitle(String expectedTitle) {
        ResponseValidator.assertFieldEquals(ScenarioContext.getResponse(), "title", expectedTitle);
    }

    @And("the response should contain the body {string}")
    public void theResponseShouldContainTheBody(String expectedBody) {
        ResponseValidator.assertFieldEquals(ScenarioContext.getResponse(), "body", expectedBody);
    }

    @And("the response should contain a non-null {string}")
    public void theResponseShouldContainNonNull(String fieldName) {
        ResponseValidator.assertFieldNotNull(ScenarioContext.getResponse(), fieldName);
    }

    @And("the response content type should contain {string}")
    public void theResponseContentTypeShouldContain(String expectedContentType) {
        ResponseValidator.assertContentType(ScenarioContext.getResponse(), expectedContentType);
    }

    @And("I save the created post ID")
    public void iSaveTheCreatedPostId() {
        Response response = ScenarioContext.getResponse();
        UserResponse postResponse = JsonUtils.fromResponse(response, UserResponse.class);
        createdPostId = postResponse.getId();
        assertNotNull("Created post ID should not be null", createdPostId);
        LOG.info("Saved created post ID: {}", createdPostId);
    }

    // ── Data-Driven Assertions ───────────────────────────────────────────

    @And("the response should contain the title from test data")
    public void theResponseShouldContainTheTitleFromTestData() {
        String expectedTitle = testData.get("title");
        ResponseValidator.assertFieldEquals(ScenarioContext.getResponse(), "title", expectedTitle);
    }

    @And("the response should contain the body from test data")
    public void theResponseShouldContainTheBodyFromTestData() {
        String expectedBody = testData.get("body");
        ResponseValidator.assertFieldEquals(ScenarioContext.getResponse(), "body", expectedBody);
    }
}
