package com.api.automation.stepdefinitions;

import com.api.automation.clients.PostApiClient;
import com.api.automation.payloads.request.PostRequest;
import com.api.automation.payloads.response.PostListResponse;
import com.api.automation.payloads.response.PostResponse;
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
 * Step definitions for all Post API operations.
 * Maps Gherkin steps (Given/When/Then) to Java methods that call PostApiClient.
 *
 * WHY one class per resource: Keeps step definitions organised by domain.
 *   As the test suite grows, each resource's steps stay isolated and maintainable.
 *
 * State sharing: Uses ScenarioContext (ThreadLocal) to pass Response between steps.
 * Each step either stores data (setResponse) or reads it (getResponse) — never both.
 */
public class PostStepDefinitions {

    private static final Logger LOG = LogManager.getLogger(PostStepDefinitions.class);

    private PostApiClient apiClient;        // Domain client — initialized in setup step
    private PostRequest postRequest;        // Request body — built in preparation steps
    private Integer createdPostId;          // Stores the ID returned after creation (for E2E flow)

    // Row data loaded from Excel for data-driven tests (key=column header, value=cell value)
    private Map<String, String> testData;

    // ── Setup ────────────────────────────────────────────────────────────

    /** Initializes the PostApiClient. Called at the start of every Post scenario. */
    @Given("I set up the post API client")
    public void iSetUpThePostApiClient() {
        apiClient = new PostApiClient();
        LOG.info("Post API client initialized");
    }

    // ── Request Preparation ──────────────────────────────────────────────

    /** Builds a PostRequest POJO from inline Gherkin parameters and stores it in ScenarioContext. */
    @Given("I prepare a post request with title {string} and body {string}")
    public void iPreparePostRequest(String title, String body) {
        postRequest = new PostRequest(title, body, 1);
        ScenarioContext.setRequestBody(postRequest);
        LOG.info("Prepared request: {}", postRequest);
    }

    /**
     * Loads test data from an Excel file row and builds a PostRequest from it.
     * Used for data-driven scenarios: values come from .xlsx instead of Gherkin parameters.
     * @param filePath  classpath path to Excel file (e.g., "testdata/posts.xlsx")
     * @param sheetName sheet name (e.g., "CreatePosts")
     * @param rowIndex  0-based row index (excludes header)
     */
    @Given("I load post test data from Excel file {string} sheet {string} row {int}")
    public void iLoadPostTestDataFromExcel(String filePath, String sheetName, int rowIndex) {
        testData = ExcelReader.readRow(filePath, sheetName, rowIndex);
        String title = testData.get("title");
        String body = testData.get("body");
        postRequest = new PostRequest(title, body, 1);
        ScenarioContext.setRequestBody(postRequest);
        LOG.info("Loaded test data from Excel — title: {}, body: {}", title, body);
    }

    // ── GET Operations ───────────────────────────────────────────────────

    /** Calls GET /posts and stores the response in ScenarioContext. */
    @When("I send a GET request to fetch all posts")
    public void iSendGetRequestToFetchAllPosts() {
        Response response = apiClient.getPosts();
        ScenarioContext.setResponse(response);
    }

    /** Calls GET /posts/{id} for a specific post. Returns 200 or 404. */
    @When("I send a GET request to fetch post with ID {int}")
    public void iSendGetRequestToFetchPostById(int postId) {
        Response response = apiClient.getPostById(postId);
        ScenarioContext.setResponse(response);
    }

    // ── POST Operations ──────────────────────────────────────────────────

    /** Calls POST /posts with the prepared PostRequest body. Expects 201 Created. */
    @When("I send a POST request to create a post")
    public void iSendPostRequestToCreatePost() {
        Response response = apiClient.createPost(postRequest);
        ScenarioContext.setResponse(response);
    }

    // ── PUT Operations ───────────────────────────────────────────────────

    /** Calls PUT /posts/{id} — full update replacing all fields. */
    @When("I send a PUT request to update post with ID {int}")
    public void iSendPutRequestToUpdatePost(int postId) {
        Response response = apiClient.updatePost(postId, postRequest);
        ScenarioContext.setResponse(response);
    }

    // ── PATCH Operations ─────────────────────────────────────────────────

    /** Calls PATCH /posts/{id} — partial update, only changes provided fields. */
    @When("I send a PATCH request to update post with ID {int}")
    public void iSendPatchRequestToUpdatePost(int postId) {
        Response response = apiClient.patchPost(postId, postRequest);
        ScenarioContext.setResponse(response);
    }

    // ── DELETE Operations ────────────────────────────────────────────────

    /** Calls DELETE /posts/{id}. JSONPlaceholder returns 200 with empty body. */
    @When("I send a DELETE request to delete post with ID {int}")
    public void iSendDeleteRequestToDeletePost(int postId) {
        Response response = apiClient.deletePost(postId);
        ScenarioContext.setResponse(response);
    }

    // ── Response Assertions ──────────────────────────────────────────────

    /** Deserializes the JSON array response into List<PostResponse> and asserts it's not empty. */
    @And("the response should contain a list of posts")
    public void theResponseShouldContainAListOfPosts() {
        Response response = ScenarioContext.getResponse();
        // JSONPlaceholder returns a flat JSON array
        List<PostResponse> posts = PostListResponse.fromResponse(response);
        assertNotNull("Posts list should not be null", posts);
        assertFalse("Posts list should not be empty", posts.isEmpty());
        LOG.info("Returned {} posts", posts.size());
    }

    /** Asserts the response JSON contains "id" matching the expected post ID. */
    @And("the response should contain post with ID {int}")
    public void theResponseShouldContainPostWithId(int expectedPostId) {
        ResponseValidator.assertFieldEquals(
                ScenarioContext.getResponse(), "id", String.valueOf(expectedPostId));
    }

    /** Asserts the "title" field in the response matches the expected string. */
    @And("the response should contain the title {string}")
    public void theResponseShouldContainTheTitle(String expectedTitle) {
        ResponseValidator.assertFieldEquals(ScenarioContext.getResponse(), "title", expectedTitle);
    }

    /** Asserts the "body" field in the response matches the expected string. */
    @And("the response should contain the body {string}")
    public void theResponseShouldContainTheBody(String expectedBody) {
        ResponseValidator.assertFieldEquals(ScenarioContext.getResponse(), "body", expectedBody);
    }

    /** Extracts the created post ID from response and saves it for later steps (e.g., update/delete). */
    @And("I save the created post ID")
    public void iSaveTheCreatedPostId() {
        Response response = ScenarioContext.getResponse();
        PostResponse postResponse = JsonUtils.fromResponse(response, PostResponse.class);
        createdPostId = postResponse.getId();
        assertNotNull("Created post ID should not be null", createdPostId);
        LOG.info("Saved created post ID: {}", createdPostId);
    }

    // ── Data-Driven Assertions ───────────────────────────────────────────

    /** Asserts title matches the value loaded from Excel (stored in testData map). */
    @And("the response should contain the title from test data")
    public void theResponseShouldContainTheTitleFromTestData() {
        String expectedTitle = testData.get("title");
        ResponseValidator.assertFieldEquals(ScenarioContext.getResponse(), "title", expectedTitle);
    }

    /** Asserts body matches the value loaded from Excel. */
    @And("the response should contain the body from test data")
    public void theResponseShouldContainTheBodyFromTestData() {
        String expectedBody = testData.get("body");
        ResponseValidator.assertFieldEquals(ScenarioContext.getResponse(), "body", expectedBody);
    }
}
