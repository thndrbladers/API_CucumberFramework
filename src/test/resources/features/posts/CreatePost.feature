@posts @regression
Feature: Posts API - POST Operations
  As a QA engineer
  I want to create new posts via the API
  So that I can verify post creation works correctly

  @smoke @post
  Scenario: Create a new post with valid data
    Given I set up the post API client
    And I prepare a post request with title "Test Post" and body "This is a test post body"
    When I send a POST request to create a post
    Then the response status code should be 201
    And the response should contain the title "Test Post"
    And the response should contain the body "This is a test post body"
    And the response should contain a non-null "id"

  @post
  Scenario: Create a post with special characters in title
    Given I set up the post API client
    And I prepare a post request with title "Café & Résumé — Test!" and body "Special chars body"
    When I send a POST request to create a post
    Then the response status code should be 201
    And the response should contain the title "Café & Résumé — Test!"

  @post @datadriven
  Scenario: Create posts from Excel test data
    Given I set up the post API client
    And I load post test data from Excel file "testdata/posts.xlsx" sheet "CreatePosts" row 0
    When I send a POST request to create a post
    Then the response status code should be 201
    And the response should contain the title from test data
    And the response should contain the body from test data
