@comments @regression
Feature: Comments API - GET Operations
  As a QA engineer
  I want to retrieve comment information via the API
  So that I can verify comment data is returned correctly

  @smoke @get
  Scenario: Get list of all comments
    Given I set up the comment API client
    When I send a GET request to fetch all comments
    Then the response status code should be 200
    And the response should contain a list of comments
    And the response content type should contain "application/json"

  @get
  Scenario: Get a single comment by valid ID
    Given I set up the comment API client
    When I send a GET request to fetch comment with ID 1
    Then the response status code should be 200
    And the response should contain comment with ID 1
    And the response should contain field "email" with a non-null value

  @get
  Scenario: Get comments filtered by post ID
    Given I set up the comment API client
    When I send a GET request to fetch comments for post ID 1
    Then the response status code should be 200
    And the response should contain a list of comments
    And all returned comments should belong to post ID 1

  @get @negative
  Scenario: Get a comment with non-existent ID
    Given I set up the comment API client
    When I send a GET request to fetch comment with ID 9999
    Then the response status code should be 404
