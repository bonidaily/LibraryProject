package com.cydeo.steps;

import com.cydeo.pages.BasePage;
import com.cydeo.pages.LoginPage;
import com.cydeo.utility.BrowserUtil;
import com.cydeo.utility.DB_Util;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;

public class LoginStepDefs extends BasePage {
    LoginPage loginPage = new LoginPage();
    String actualAccountHolder;
    String username;

    @Given("the user logged in  {string} and {string}")
    public void the_user_logged_in_and(String username, String password) {

        this.username = username;

        loginPage.login(username,password);
    }
    @When("user gets username  from user fields")
    public void user_gets_username_from_user_fields() {

        BrowserUtil.waitFor(2);
        actualAccountHolder = accountHolderName.getText();
        System.out.println("actualAccountHolder = " + actualAccountHolder);

    }
    @Then("the username should be same with database")
    public void the_username_should_be_same_with_database() {
        DB_Util.runQuery("select full_name from users where email = '"+username+"'");

        String expectedAccountHolder = DB_Util.getFirstRowFirstColumn();
        //actualAccountHolder = accountHolderName.getText();

        Assert.assertEquals(expectedAccountHolder,actualAccountHolder);
    }

}
