package com.cydeo.steps;

import com.cydeo.pages.BookPage;
import com.cydeo.pages.UsersPage;
import com.cydeo.utility.BrowserUtil;
import com.cydeo.utility.DB_Util;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;
import org.openqa.selenium.support.ui.Select;

public class LiveSessionStepDefs {

    String actualUserCount;
    UsersPage usersPage = new UsersPage();
    @When("the user gets {string} user count")
    public void the_user_gets_user_count(String status) {
        Select select = new Select(usersPage.userStatusDropdown);
        select.selectByVisibleText(status);

        BrowserUtil.waitFor(2);

        String userDetails = usersPage.userCount.getText();
        actualUserCount =  usersPage.getCount(userDetails);
        System.out.println(status + "userCount = " + actualUserCount);

    }
    @When("the {string} user count should be equal database")
    public void the_user_count_should_be_equal_database(String status) {
        DB_Util.createConnection();
        DB_Util.runQuery("select count(*)\n" +
                "from users\n" +
                "where status = '"+status+"' and user_group_id!= 1");

        BrowserUtil.waitFor(2);
        String expectedUserCount = DB_Util.getFirstRowFirstColumn();
        System.out.println("expectedUserCount = " + expectedUserCount);

        Assert.assertEquals(expectedUserCount,actualUserCount);

    }
    BookPage bookPage = new BookPage();
    String actualBookCount;

    //02
    @When("the user gets {string} book count")
    public void the_user_gets_book_count(String categoryName) {

    BrowserUtil.selectByVisibleText(bookPage.mainCategoryElement,categoryName);
    BrowserUtil.waitFor(1);

        String bookDetails = bookPage.bookCount.getText();
        actualBookCount = bookPage.getCount(bookDetails);

        System.out.println(actualBookCount);

    }
    @Then("the {string} book count should be equal with database")
    public void the_book_count_should_be_equal_with_database(String categoryName) {

        DB_Util.createConnection();
        String query = "select count(*)\n" +
                "from books b join book_categories bc on bc.id = b.book_category_id\n" +
                "where bc.name = '"+categoryName+"'";

        DB_Util.runQuery(query);
        String expectedBookCount = DB_Util.getFirstRowFirstColumn();

        Assert.assertEquals(expectedBookCount,actualBookCount);
    }

}
