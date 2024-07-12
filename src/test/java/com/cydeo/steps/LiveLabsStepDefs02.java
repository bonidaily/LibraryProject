package com.cydeo.steps;

import com.cydeo.pages.BookPage;
import com.cydeo.pages.LoginPage;
import com.cydeo.utility.BrowserUtil;
import com.cydeo.utility.DB_Util;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;

import java.util.Map;

public class LiveLabsStepDefs02 {
    LoginPage loginPage = new LoginPage();
    BookPage bookPage = new BookPage();

    @Given("the {string} on the home page")
    public void the_on_the_home_page(String user) {

        loginPage.login(user);

    }
    @When("the librarian click to add book")
    public void the_librarian_click_to_add_book() {
        bookPage.addBook.click();

    }
    @When("the librarian enter book name {string}")
    public void the_librarian_enter_book_name(String bookName) {
        BrowserUtil.waitFor(1);

        bookPage.bookName.sendKeys(bookName);

    }
    @When("the librarian enter ISBN {string}")
    public void the_librarian_enter_isbn(String isbn) {

        bookPage.isbn.sendKeys(isbn);

    }
    @When("the librarian enter year {string}")
    public void the_librarian_enter_year(String year) {

        bookPage.year.sendKeys(year);

    }
    @When("the librarian enter author {string}")
    public void the_librarian_enter_author(String author) {

        bookPage.author.sendKeys(author);

    }
    @When("the librarian choose the book category {string}")
    public void the_librarian_choose_the_book_category(String bookCategory) {

        BrowserUtil.selectByVisibleText(bookPage.bookCategory,bookCategory);

    }
    @When("the librarian click to save changes")
    public void the_librarian_click_to_save_changes() {
       bookPage.saveChanges.click();
    }
    @Then("verify {string} message is displayed")
    public void verify_message_is_displayed(String expectedMessage) {

        BrowserUtil.waitFor(1);

        Assert.assertEquals(expectedMessage,bookPage.toastMessage.getText());

    }
    @Then("verify {string} information must match with DB")
    public void verify_information_must_match_with_db(String expectedBookName) {

        String query = "select distinct name,author,books.isbn\n" +
                "from books\n" +
                "where books.name = '"+expectedBookName+"';";

        DB_Util.createConnection();
        DB_Util.runQuery(query);

//        Map<String,String> rowMap = DB_Util.getRowMap(1);
//        String actualBookName = rowMap.get("name");

        String actualBookName = DB_Util.getCellValue(1,1);

        Assert.assertEquals(expectedBookName,actualBookName);






    }
}
