@posts @regression
Feature: Posts API - GET Operations
  As a QA engineer
  I want to retrieve post information via the API
  So that I can verify the post data is returned correctly

  @smoke @get
  Scenario: Get list of all posts
    Given I set up the post API client
    When I send a GET request to fetch all posts
    Then the response status code should be 200
    And the response should contain a list of posts
    And the response content type should contain "application/json"

  @get
  Scenario: Get a single post by valid ID
    Given I set up the post API client
    When I send a GET request to fetch post with ID 1
    Then the response status code should be 200
    And the response should contain post with ID 1
    And the response should contain field "title" with a non-null value

  @get @negative
  Scenario: Get a post with non-existent ID
    Given I set up the post API client
    When I send a GET request to fetch post with ID 9999
    Then the response status code should be 404
