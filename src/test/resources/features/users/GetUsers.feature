@users @regression
Feature: Users API - GET Operations
  As a QA engineer
  I want to retrieve user information via the API
  So that I can verify user data is returned correctly

  @smoke @get
  Scenario: Get list of all users
    Given I set up the user API client
    When I send a GET request to fetch all users
    Then the response status code should be 200
    And the response should contain a list of users
    And the response content type should contain "application/json"

  @get
  Scenario: Get a single user by valid ID
    Given I set up the user API client
    When I send a GET request to fetch user with ID 1
    Then the response status code should be 200
    And the response should contain user with ID 1
    And the response should contain field "name" with a non-null value
    And the response should contain field "email" with a non-null value

  @get @negative
  Scenario: Get a user with non-existent ID
    Given I set up the user API client
    When I send a GET request to fetch user with ID 9999
    Then the response status code should be 404
