
Feature: User module
  As a librarian, I should be able to see ACTIVE/INACTIVE user count
  to manage library


  Scenario: Librarian should able to ACTIVE/INACTIVE user count
    Given the user logged in as "librarian"
    And the user navigates to "Users" page
    When the user gets "ACTIVE" user count
    And the "ACTIVE" user count should be equal database
    When the user gets "INACTIVE" user count
    And the "INACTIVE" user count should be equal database

   @db
  Scenario Outline: Librarian should able to ACTIVE/INACTIVE user count
    Given the user logged in as "librarian"
    And the user navigates to "Books" page
    When the user gets "<category>" book count
    Then the "<category>" book count should be equal with database

    Examples:
      |category|
      | Action and Adventure    |
      | Anthology               |
      | Classic                 |
      | Comic and Graphic Novel |
      | Crime and Detective     |
      | Drama                   |
      | Fable                   |
      | Fairy Tale              |
      | Fan-Fiction             |
      | Fantasy                 |
      | Historical Fiction      |
      | Horror                  |
      | Science Fiction         |
      | Biography/Autobiography |
      | Humor                   |
      | Romance                 |
      | Short Story             |
      | Essay                   |
      | Memoir                  |
      | Poetry                  |

