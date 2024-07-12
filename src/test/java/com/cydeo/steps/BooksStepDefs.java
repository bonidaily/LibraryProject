package com.cydeo.steps;

import com.cydeo.pages.BookPage;
import com.cydeo.pages.DashBoardPage;
import com.cydeo.utility.BrowserUtil;
import com.cydeo.utility.DB_Util;
import com.cydeo.utility.Driver;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class BooksStepDefs {
    BookPage bookPage=new BookPage();
    List<String> actualCategoryList;


    @When("the user navigates to {string} page")
    public void the_user_navigates_to_page(String moduleName) {
        new DashBoardPage().navigateModule(moduleName);


    }


    @When("the user gets all book categories in webpage")
    public void the_user_gets_all_book_categories_in_webpage() {
        actualCategoryList=BrowserUtil.getAllSelectOptions(bookPage.mainCategoryElement);
        actualCategoryList.remove(0);
        System.out.println("actualCategoryList = " + actualCategoryList);
    }

    @Then("user should be able to see following categories")
    public void user_should_be_able_to_see_following_categories(List<String> expectedCategoryList) {


        Assert.assertEquals(expectedCategoryList, actualCategoryList);

    }


    @When("I open book {string}")
    public void i_open_book(String bookName) {

        System.out.println("bookName = " + bookName);
        BrowserUtil.waitForClickablility(bookPage.search, 5).sendKeys(bookName);
        BrowserUtil.waitForClickablility(bookPage.editBook(bookName), 5).click();

    }

    @Then("verify book categories must match book categories table from db")
    public void verifyBookCategoriesMustMatchBookCategoriesTableFromDb() {
        DB_Util.runQuery("select * from book_categories");
        List<String> expectedCategoriesList = DB_Util.getColumnDataAsList("name");
        System.out.println("expectedCategoriesNames = " + expectedCategoriesList);

        Assert.assertEquals(expectedCategoriesList,actualCategoryList);
    }

    @Then("book information must match the database for {string}")
    public void bookInformationMustMatchTheDatabaseFor(String arg0) {
        String query = "select name,isbn,year,author,description from books where id = 24583";
        DB_Util.runQuery(query);

//        //verify book name
//        BrowserUtil.waitFor(1);
//        String actualBookname = bookPage.bookName.getAttribute("value");
//        String expectedBookName = DB_Util.getCellValue(1,2);
//        System.out.println("actualBookname = " + actualBookname);
//        System.out.println("expectedBookName = " + expectedBookName);
//        Assert.assertEquals(expectedBookName,actualBookname);
//        BrowserUtil.waitFor(2);
//
//        //verify isbn
//        String actualIsbn = Driver.getDriver().findElement(By.xpath("//input[@placeholder='ISBN']")).getAttribute("value");
//        String expectedIsbn = DB_Util.getCellValue(1,3);
//        System.out.println("actualIsbn = " + actualIsbn);
//        System.out.println("expectedIsbn = " + expectedIsbn);
//        Assert.assertEquals(expectedIsbn,actualIsbn);
//
//        //verify year
//        String actualYear = bookPage.year.getAttribute("value");
//        String expectedYear = DB_Util.getCellValue(1,4);
//        System.out.println("actualYear = " + actualYear);
//        System.out.println("expectedYear = " + expectedYear);
//        Assert.assertEquals(expectedYear,actualYear);
//
//        //verify author
//        String actualAuthor = bookPage.author.getAttribute("value");
//        String expectedAuthor = DB_Util.getCellValue(1,5);
//        System.out.println("actualAuthor = " + actualAuthor);
//        System.out.println("expectedAuthor = " + expectedAuthor);
//        Assert.assertEquals(expectedAuthor,actualAuthor);
//
//        //verify book desc
//
//        String actualBookdesc = bookPage.description.getText();
//        String expectedBookdesc = DB_Util.getCellValue(1,7);
//        System.out.println("actualBookCategory = " + actualBookdesc);
//        System.out.println("expectedBookCategory = " + expectedBookdesc);
//        Assert.assertEquals(actualBookdesc,expectedBookdesc);

        Map<String,String>rowMap = DB_Util.getRowMap(1);
        BrowserUtil.waitFor(2);

        String actualBookname = bookPage.bookName.getAttribute("value");
        String expectedBookname = rowMap.get("name");
        Assert.assertEquals(expectedBookname,actualBookname);

        String actualIsbn = Driver.getDriver().findElement(By.xpath("//input[@placeholder='ISBN']")).getAttribute("value");
        String expectedIsbn = rowMap.get("isbn") ;
        Assert.assertEquals(expectedIsbn,actualIsbn);

        String actualYear = bookPage.year.getAttribute("value");
        String expectedYear = rowMap.get("year");
        Assert.assertEquals(expectedYear,actualYear);

        String actualAuthor = bookPage.author.getAttribute("value");
        String expectedAuthor = rowMap.get("author");
        Assert.assertEquals(expectedAuthor,actualAuthor);

        String actualBookdesc = bookPage.description.getText();
        String expectedBookDesc = rowMap.get("description");
        Assert.assertEquals(expectedBookDesc,actualBookdesc);


        DB_Util.destroy();


    }
}
