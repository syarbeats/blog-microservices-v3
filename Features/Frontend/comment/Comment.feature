Feature: Integration Testing for API

  Scenario: Create Blog Comment
    Given User is on Blog Home page
    When User click one blog with title Linux Tutorial
    Then User will be directed to the blog page of Linux Tutorial
    And On that page, user send comment Test Comment 2
    Then Comment Test Comment 2 will be displayed on Blog Comment section
