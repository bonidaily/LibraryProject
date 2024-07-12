package com.cydeo.steps;

import com.cydeo.pages.DashBoardPage;
import com.cydeo.pages.LoginPage;
import com.cydeo.utility.BrowserUtil;
import com.cydeo.utility.ConfigurationReader;
import com.cydeo.utility.DB_Util;
import com.google.common.base.Strings;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;

public class DashboardStepDefs {
    String actualUserNumbers;
    String actualBookNumbers;
    String actualBorrowedBookNumbers;
    LoginPage loginPage = new LoginPage();
    DashBoardPage dashBoardPage = new DashBoardPage();


    @Given("the user logged in as {string}")
    public void the_user_logged_in_as(String user) {
        loginPage.login(user);
        BrowserUtil.waitFor(4);
    }

    @When("user gets all information from modules")
    public void user_gets_all_information_from_modules() {

        actualUserNumbers = dashBoardPage.usersNumber.getText();
        System.out.println("actualUserNumbers = " + actualUserNumbers);
        actualBookNumbers = dashBoardPage.booksNumber.getText();
        System.out.println("actualBookNumbers = " + actualBookNumbers);
        actualBorrowedBookNumbers = dashBoardPage.borrowedBooksNumber.getText();
        System.out.println("actualBorrowedBookNumbers = " + actualBorrowedBookNumbers);

    }

    @Then("the informations should be same with database")
    public void the_informations_should_be_same_with_database() {
        String url = "jdbc:mysql://34.230.35.214:3306/library2";
        String username = "library2_client";
        String password = "6s2LQQTjBcGFfDhY";

        //DB_Util.createConnection(url, username, password); - will be created by hooks

        //USERS
        DB_Util.runQuery("select count(*) from users");

        String expectedUserNumbers = DB_Util.getFirstRowFirstColumn();

        System.out.println("actualUserNumbers = " + actualUserNumbers);
        System.out.println("expectedUserNumbersStr = " + expectedUserNumbers);
        Assert.assertEquals(expectedUserNumbers, actualUserNumbers);

        //BOOKS
        DB_Util.runQuery("select count(*) from books");

        String expectedBooksNumbers = DB_Util.getFirstRowFirstColumn();

        System.out.println("expectedBooksNumbers = " + expectedBooksNumbers);
        System.out.println("actualBorrowedBookNumbers = " + actualBookNumbers);

        Assert.assertEquals(expectedBooksNumbers, actualBookNumbers);


        //Borrowed books
        DB_Util.runQuery("select count(*) from book_borrow where is_returned = 0");

        String expectedBorrowedBooksNumbers = DB_Util.getFirstRowFirstColumn();

        System.out.println("actualBorrowedBooksNumbers = " + actualBorrowedBookNumbers);
        System.out.println("expectedBorrowedBooksNumbers = " + expectedBorrowedBooksNumbers);

        Assert.assertEquals(expectedBorrowedBooksNumbers, actualBorrowedBookNumbers);

        //DB_Util.destroy(); will be closed by hooks @db

    }
}
