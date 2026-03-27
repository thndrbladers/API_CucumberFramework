@comments @regression
Feature: Comments API - POST Operations
  As a QA engineer
  I want to create new comments via the API
  So that I can verify comment creation works correctly

  @smoke @post
  Scenario: Create a new comment with valid data
    Given I set up the comment API client
    And I prepare a comment request for post ID 1 with name "Test Comment" email "test@example.com" and body "This is a test comment"
    When I send a POST request to create a comment
    Then the response status code should be 201
    And the response should contain the comment name "Test Comment"
    And the response should contain the comment email "test@example.com"
    And the response should contain a non-null "id"

  @post
  Scenario: Create a comment with special characters
    Given I set up the comment API client
    And I prepare a comment request for post ID 1 with name "Réview & Feedback!" email "user@café.com" and body "Great article — très bien!"
    When I send a POST request to create a comment
    Then the response status code should be 201
    And the response should contain the comment name "Réview & Feedback!"
