@e2e @regression
Feature: Cross-Resource E2E Flow - User creates a Post and Comments on it
  As a QA engineer
  I want to verify a cross-resource workflow
  So that I can ensure different API resources work together

  @smoke
  Scenario: Fetch a user, create a post, then add a comment
    # FETCH USER
    Given I set up the user API client
    When I send a GET request to fetch user with ID 1
    Then the response status code should be 200
    And the response should contain field "name" with a non-null value

    # CREATE POST
    Given I set up the post API client
    And I prepare a post request with title "Cross-Resource Test" and body "Testing user-post-comment flow"
    When I send a POST request to create a post
    Then the response status code should be 201
    And the response should contain the title "Cross-Resource Test"

    # ADD COMMENT
    Given I set up the comment API client
    And I prepare a comment request for post ID 1 with name "E2E Feedback" email "e2e@test.com" and body "This is cross-resource"
    When I send a POST request to create a comment
    Then the response status code should be 201
    And the response should contain the comment name "E2E Feedback"
