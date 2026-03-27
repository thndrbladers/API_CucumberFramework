@posts @regression
Feature: Posts API - PUT/PATCH Update Operations
  As a QA engineer
  I want to update existing posts via the API
  So that I can verify update functionality works correctly

  @smoke @put
  Scenario: Update a post with PUT request
    Given I set up the API client
    And I prepare a post request with title "Updated Title" and body "Updated body content"
    When I send a PUT request to update post with ID 1
    Then the response status code should be 200
    And the response should contain the title "Updated Title"
    And the response should contain the body "Updated body content"

  @patch
  Scenario: Partially update a post with PATCH request
    Given I set up the API client
    And I prepare a post request with title "Patched Title" and body "Patched body"
    When I send a PATCH request to update post with ID 1
    Then the response status code should be 200
    And the response should contain the title "Patched Title"
