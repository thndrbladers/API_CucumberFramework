@e2e @regression
Feature: Posts API - End-to-End CRUD Flow
  As a QA engineer
  I want to perform a complete CRUD lifecycle on posts
  So that I can verify the entire post management workflow

  @smoke
  Scenario: Complete post lifecycle - Create, Read, Update, Delete
    # CREATE
    Given I set up the API client
    And I prepare a post request with title "E2E Test Post" and body "E2E test body content"
    When I send a POST request to create a post
    Then the response status code should be 201
    And the response should contain the title "E2E Test Post"
    And I save the created post ID

    # READ
    When I send a GET request to fetch post with ID 1
    Then the response status code should be 200
    And the response should contain field "title" with a non-null value

    # UPDATE
    Given I prepare a post request with title "E2E Updated Post" and body "Updated body"
    When I send a PUT request to update post with ID 1
    Then the response status code should be 200
    And the response should contain the title "E2E Updated Post"

    # DELETE
    When I send a DELETE request to delete post with ID 1
    Then the response status code should be 200
