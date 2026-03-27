@posts @regression
Feature: Posts API - DELETE Operations
  As a QA engineer
  I want to delete posts via the API
  So that I can verify deletion works correctly

  @smoke @delete
  Scenario: Delete an existing post
    Given I set up the API client
    When I send a DELETE request to delete post with ID 1
    Then the response status code should be 200

  @delete
  Scenario: Delete another post
    Given I set up the API client
    When I send a DELETE request to delete post with ID 50
    Then the response status code should be 200
