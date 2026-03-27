@comments @regression
Feature: Comments API - DELETE Operations
  As a QA engineer
  I want to delete comments via the API
  So that I can verify deletion works correctly

  @delete
  Scenario: Delete an existing comment
    Given I set up the comment API client
    When I send a DELETE request to delete comment with ID 1
    Then the response status code should be 200
