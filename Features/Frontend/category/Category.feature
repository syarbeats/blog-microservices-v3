Feature: Integration Testing for API

  Scenario: Create New Category
    Given User Click menu Create Blog Category
    When User has login, he will direct to create blog category page
    Then User insert Category Name Microcontroller and Description Description1
    And After successfully create new category, user will direct to Category List page
    And Category Name Microcontroller and Description Description1 will exist in that Category List Page.
