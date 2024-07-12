package com.cydeo.steps;

import com.cydeo.pages.DashBoardPage;
import com.cydeo.utility.BrowserUtil;
import com.cydeo.utility.DB_Util;
import com.cydeo.utility.Driver;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.Assert;
import org.openqa.selenium.By;

import java.util.LinkedHashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class LibrarianTotalAmounts {
    DashBoardPage dashBoardPage = new DashBoardPage();
    int expectedNrUsers;
    int expectedBooksNr;
    int expectedBorrowedBooksNr;


    @Then("user should see the {int} of users and {string} text.")
    public void user_should_see_the_of_users_and_text(int expectedNrUsers, String expectedUser) {
        this.expectedNrUsers = expectedNrUsers;

        BrowserUtil.waitFor(2);
        int actualNrUser = Integer.parseInt(dashBoardPage.usersNumber.getText());
        String actualUser = dashBoardPage.users.getText();

        BrowserUtil.waitFor(2);

        Assert.assertEquals(expectedNrUsers,actualNrUser);
        Assert.assertEquals(expectedUser,actualUser);

        DB_Util.runQuery("select count(*) from users");
        int firstRowFirstColumn = Integer.parseInt(DB_Util.getFirstRowFirstColumn());

        Assert.assertEquals(actualNrUser,firstRowFirstColumn);

    }

    @Then("user should see the {int} of books and {string} text.")
    public void user_should_see_the_of_books_and_text(int expectedBooksNr, String expectedBooks) {
        this.expectedBooksNr=expectedBooksNr;
        BrowserUtil.waitFor(2);
        int actualBooksNr = Integer.parseInt(dashBoardPage.booksNumber.getText());
        String actualBooks = dashBoardPage.books.getText();
        BrowserUtil.waitFor(2);

        Assert.assertEquals(expectedBooksNr,actualBooksNr);
        Assert.assertEquals(expectedBooks,actualBooks);

        DB_Util.runQuery("select count(*) from books");
        int firstRowFirstColumn = Integer.parseInt(DB_Util.getFirstRowFirstColumn());

        Assert.assertEquals(actualBooksNr,firstRowFirstColumn);
    }

    @Then("user should see the {int} of borrowed books and {string} text.")
    public void user_should_see_the_of_borrowed_books_and_text(int expectedBorrowedBooksNr, String borrowedBooks) {
        this.expectedBorrowedBooksNr=expectedBorrowedBooksNr;
        BrowserUtil.waitFor(2);
        int actualBorrowedBooksNr = Integer.parseInt(dashBoardPage.borrowedBooksNumber.getText());
        String actualborrowedBooks = dashBoardPage.borowedBooks.getText();
        BrowserUtil.waitFor(2);

        Assert.assertEquals(expectedBorrowedBooksNr,actualBorrowedBooksNr);
        Assert.assertEquals(borrowedBooks,actualborrowedBooks);

        DB_Util.runQuery("select count(*) from book_borrow\n" +
                "where is_returned = 0");
        int firstRowFirstColumn = Integer.parseInt(DB_Util.getFirstRowFirstColumn());

        Assert.assertEquals(actualBorrowedBooksNr,firstRowFirstColumn);
    }


}
