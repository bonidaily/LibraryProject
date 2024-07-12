package com.cydeo.steps;

import com.cydeo.utility.DB_Util;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;

public class LiveLabsStepDefs01 {
    String actualCategory;

    @Given("Establish the database connection")
    public void establish_the_database_connection() {
        //DB_Util.createConnection(); =>

    }
    @When("I execute query to find most popular book genre")
    public void i_execute_query_to_find_most_popular_book_genre() {
        String query = "select book_categories.name,count(*) from book_borrow\n" +
                "    join books on book_borrow.book_id = books.id\n" +
                "    join book_categories on books.book_category_id = book_categories.id\n" +
                "group by book_categories.name\n" +
                "order by count(*) desc";
        DB_Util.runQuery(query);
        actualCategory = DB_Util.getFirstRowFirstColumn();
        System.out.println(actualCategory);

    }
    @Then("verify {string} is the most popular book genre.")
    public void verify_is_the_most_popular_book_genre(String expectedCategory) {
        System.out.println(expectedCategory);

        Assert.assertEquals(expectedCategory,actualCategory);

        DB_Util.destroy();

    }

}
