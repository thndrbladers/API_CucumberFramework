@users @regression
Feature: Users API - POST Operations
  As a QA engineer
  I want to create new users via the API
  So that I can verify user creation works correctly

  @smoke @post
  Scenario: Create a new user with valid data
    Given I set up the user API client
    And I prepare a user request with name "John Doe" username "johndoe" and email "john@example.com"
    When I send a POST request to create a user
    Then the response status code should be 201
    And the response should contain the user name "John Doe"
    And the response should contain the user email "john@example.com"
    And the response should contain a non-null "id"

  @put
  Scenario: Update a user with PUT request
    Given I set up the user API client
    And I prepare a user request with name "Jane Updated" username "janeupdated" and email "jane@updated.com"
    When I send a PUT request to update user with ID 1
    Then the response status code should be 200
    And the response should contain the user name "Jane Updated"
