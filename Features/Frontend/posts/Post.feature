Feature: Integration Testing for API

  Scenario: Create Blog
    Given User click Create Blog Menu
    When User on Create Blog page, insert new blog with title Oracle OSB with category Enterprise Application Integration and blog testblog
    Then Blog created successfully, user will direct to home page
    And The created blog will be found on that Home Page
