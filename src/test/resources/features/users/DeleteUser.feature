@users @regression
Feature: Users API - DELETE Operations
  As a QA engineer
  I want to delete users via the API
  So that I can verify deletion works correctly

  @delete
  Scenario: Delete an existing user
    Given I set up the user API client
    When I send a DELETE request to delete user with ID 1
    Then the response status code should be 200
