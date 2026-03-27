package com.api.automation.stepdefinitions;

import com.api.automation.clients.CommentApiClient;
import com.api.automation.payloads.request.CommentRequest;
import com.api.automation.payloads.response.CommentResponse;
import com.api.automation.utils.JsonUtils;
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
 * Step definitions for all Comment API operations (JSONPlaceholder).
 */
public class CommentStepDefinitions {

    private static final Logger LOG = LogManager.getLogger(CommentStepDefinitions.class);

    private CommentApiClient apiClient;
    private CommentRequest commentRequest;

    // ── Setup ────────────────────────────────────────────────────────────

    @Given("I set up the comment API client")
    public void iSetUpTheCommentApiClient() {
        apiClient = new CommentApiClient();
        LOG.info("Comment API client initialized");
    }

    // ── Request Preparation ──────────────────────────────────────────────

    @Given("I prepare a comment request for post ID {int} with name {string} email {string} and body {string}")
    public void iPrepareCommentRequest(int postId, String name, String email, String body) {
        commentRequest = new CommentRequest(postId, name, email, body);
        ScenarioContext.setRequestBody(commentRequest);
        LOG.info("Prepared comment request: {}", commentRequest);
    }

    // ── GET Operations ───────────────────────────────────────────────────

    @When("I send a GET request to fetch all comments")
    public void iSendGetRequestToFetchAllComments() {
        Response response = apiClient.getComments();
        ScenarioContext.setResponse(response);
    }

    @When("I send a GET request to fetch comment with ID {int}")
    public void iSendGetRequestToFetchCommentById(int commentId) {
        Response response = apiClient.getCommentById(commentId);
        ScenarioContext.setResponse(response);
    }

    @When("I send a GET request to fetch comments for post ID {int}")
    public void iSendGetRequestToFetchCommentsByPostId(int postId) {
        Response response = apiClient.getCommentsByPostId(postId);
        ScenarioContext.setResponse(response);
    }

    // ── POST Operations ──────────────────────────────────────────────────

    @When("I send a POST request to create a comment")
    public void iSendPostRequestToCreateComment() {
        Response response = apiClient.createComment(commentRequest);
        ScenarioContext.setResponse(response);
    }

    // ── DELETE Operations ────────────────────────────────────────────────

    @When("I send a DELETE request to delete comment with ID {int}")
    public void iSendDeleteRequestToDeleteComment(int commentId) {
        Response response = apiClient.deleteComment(commentId);
        ScenarioContext.setResponse(response);
    }

    // ── Response Assertions ──────────────────────────────────────────────

    @And("the response should contain a list of comments")
    public void theResponseShouldContainAListOfComments() {
        Response response = ScenarioContext.getResponse();
        List<?> comments = response.jsonPath().getList("$");
        assertNotNull("Comments list should not be null", comments);
        assertFalse("Comments list should not be empty", comments.isEmpty());
        LOG.info("Returned {} comments", comments.size());
    }

    @And("the response should contain comment with ID {int}")
    public void theResponseShouldContainCommentWithId(int expectedId) {
        ResponseValidator.assertFieldEquals(
                ScenarioContext.getResponse(), "id", String.valueOf(expectedId));
    }

    @And("all returned comments should belong to post ID {int}")
    public void allReturnedCommentsShouldBelongToPostId(int expectedPostId) {
        Response response = ScenarioContext.getResponse();
        List<Integer> postIds = response.jsonPath().getList("postId");
        assertNotNull("Post ID list should not be null", postIds);
        for (Integer postId : postIds) {
            assertEquals("Comment postId mismatch", Integer.valueOf(expectedPostId), postId);
        }
        LOG.info("All {} comments belong to post ID {}", postIds.size(), expectedPostId);
    }

    @And("the response should contain the comment name {string}")
    public void theResponseShouldContainTheCommentName(String expectedName) {
        ResponseValidator.assertFieldEquals(ScenarioContext.getResponse(), "name", expectedName);
    }

    @And("the response should contain the comment email {string}")
    public void theResponseShouldContainTheCommentEmail(String expectedEmail) {
        ResponseValidator.assertFieldEquals(ScenarioContext.getResponse(), "email", expectedEmail);
    }
}
