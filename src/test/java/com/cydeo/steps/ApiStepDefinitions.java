package com.cydeo.steps;

import com.cydeo.pages.BookPage;
import com.cydeo.pages.LoginPage;
import com.cydeo.pages.UsersPage;
import com.cydeo.utility.*;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class ApiStepDefinitions {
    public Logger log = LogManager.getLogger(this.getClass());
    RequestSpecification givenPart = RestAssured.given().log().uri();
    Response response;
    ValidatableResponse thenPart;
    String path;
    String userOrBookName;

    @Given("I logged Library api as a {string}")
    public void i_logged_library_api_as_a(String user) {
        //adding the credentials for authorization in header
        givenPart.header("x-library-token", LibraryUtils.getTokenByRole(user));


    }

    @Given("Accept header is {string}")
    public void accept_header_is(String acceptContent) {
        //adding the content type that we want to recieve
        givenPart.accept(acceptContent);

    }

    @When("I send GET request to {string} endpoint")
    public void iSendGETRequestToEndpoint(String endpoint) {
        //sending get request to the endpoint and storing to response
        response = givenPart.when().get(LibraryUtils.baseUrl + endpoint);

        //create thenPart from response
        thenPart = response.then();

    }

    @Then("status code should be {int}")
    public void status_code_should_be(int statusCode) {
        //verify status code
        thenPart.statusCode(statusCode);

        log.info("Status Code " + response.statusCode());
    }

    @Then("Response Content type is {string}")
    public void response_content_type_is(String contentType) {
        //verify contentType
        thenPart.contentType(contentType);

        log.info("Content type" + response.contentType());

    }

    @And("each {string} field should not be null")
    public void eachFieldShouldNotBeNull(String field) {
        // verify each string should not be null
        thenPart.body(field,everyItem(notNullValue()));
    }

    @Then("{string} field should not be null")
    public void field_should_not_be_null(String field) {
        //verify that field is not null
        thenPart.body(field,(notNullValue()));
    }

    @Given("Path param is {string}")
    public void path_param_is(String pathParam) {
        //store pathParam in class variable to access it later
        path = pathParam;

        //adding the pathparam to givenPart
        givenPart.pathParam("id", pathParam);

        log.info("Path Param " + path);
    }

    @Then("{string} field should be same with path param")
    public void field_should_be_same_with_path_param(String id) {
        //verify that that id is same as path
        thenPart.body(id, is(path));

    }

    @Then("following fields should not be null")
    public void following_fields_should_not_be_null(List<String> fields) {
        //get a List of String from requirement and do assertion for each of them with for each loop
        //to verify that each of them is not null
        for (String each : fields) {
            thenPart.body(each, is(notNullValue()));
        }

    }

    @And("Request Content Type header is {string}")
    public void requestContentTypeHeaderIs(String reqContentType) {

        //adding the content type that we are sending to givenPart
        givenPart.given().contentType(reqContentType);

    }

    Map<String, Object> body = new HashMap<>();

    @Given("I create a random {string} as request body")
    public void i_create_a_random_as_request_body(String randomOption) {
        //save map in body - class level

        body = LibraryUtils.getRandomMap(randomOption);

        //save the name of the book or user for later usage
        userOrBookName = (String) body.get("name");

        //givenPart = given().spec(givenPart).formParams(body);
        givenPart.given().formParams(body);
    }


    @When("I send POST request to {string} endpoint")
    public void iSendPOSTRequestToEndpoint(String endPoint) {
        //sending post request and storing to response
        response = givenPart.given().when()
                .post(LibraryUtils.baseUrl + endPoint);

        //get thenPart from the response above
        thenPart = response.then();
    }

    @And("the field value for {string} path should be equal to {string}")
    public void theFieldValueForPathShouldBeEqualTo(String actualMessage, String expectedMessage) {

        //Assert.assertEquals(response.path(path), message);
        thenPart.body(actualMessage,equalTo(expectedMessage));

    }


    @And("I logged in Library UI as {string}")
    public void iLoggedInLibraryUIAs(String userType) {
        //create loginPage object to access the method to log in
        LoginPage loginPage = new LoginPage();
        //login to ui with userType
        loginPage.login(userType);

    }


    @And("I navigate to {string} page")
    public void iNavigateToPage(String books) {
        //create bookPage object to access the method of this class
        BookPage bookPage = new BookPage();
        //navigate to books page
        bookPage.navigateModule(books);
    }


    @And("UI, Database and API created book information must match")
    public void uiDatabaseAndAPICreatedBookInformationMustMatch() {

        BookPage bookPage = new BookPage();
        //save id
        int id = Integer.parseInt(response.path("book_id"));

        //GET THE BOOK INFO FROM DATABASE
        String query = "select * from books where id = " + id;
        DB_Util.runQuery(query);

        Map<String, String> bookInfoDB = DB_Util.getRowMap(1);
        log.info(bookInfoDB);
        String actualBookNameDb = bookInfoDB.get("name");
        String actualIsbnDb = bookInfoDB.get("isbn");
        String actualYearDb = (bookInfoDB.get("year"));
        String actualAuthorDb = bookInfoDB.get("author");

        //GET THE DATA FROM UI
        BrowserUtil.waitForVisibility(bookPage.search, 2).sendKeys(userOrBookName);
        BrowserUtil.waitFor(2);

        String actualBookIsbnUi = Driver.getDriver().findElement(By.xpath("(//tbody//td)[2]")).getText();
        BrowserUtil.waitFor(1);
        String actualBookNameUi = Driver.getDriver().findElement(By.xpath("(//tbody//td)[3]")).getText();
        BrowserUtil.waitFor(1);
        String actualBookAuthorUi = Driver.getDriver().findElement(By.xpath("(//tbody//td)[4]")).getText();
        BrowserUtil.waitFor(1);
        String actualBookYearUi = Driver.getDriver().findElement(By.xpath("(//tbody//td)[6]")).getText();



        //GET THE BOOK INFO FROM API RESPONSE
        Map<String, String> bookInfoApi = LibraryUtils.returnSingleInfoApi("book",id);

        log.info(bookInfoApi);
        String expectedBookNameApi = bookInfoApi.get("name");
        String expectedIsbnApi = bookInfoApi.get("isbn");
        String expectedYearApi = bookInfoApi.get("year");
        String expectedAuthorApi = bookInfoApi.get("author");

        //Assert UI VS DB
        Assert.assertEquals(actualBookNameUi, actualBookNameDb);
        Assert.assertEquals(actualBookIsbnUi, actualIsbnDb);
        Assert.assertEquals(actualBookYearUi, actualYearDb);
        Assert.assertEquals(actualBookAuthorUi, actualAuthorDb);

        //Assert Api vs UI
        Assert.assertEquals(expectedBookNameApi, actualBookNameUi);
        Assert.assertEquals(expectedIsbnApi, actualBookIsbnUi);
        Assert.assertEquals(expectedYearApi, actualBookYearUi);
        Assert.assertEquals(expectedAuthorApi, actualBookAuthorUi);

        //Assert Api vs DB
        Assert.assertEquals(expectedBookNameApi, actualBookNameDb);
        Assert.assertEquals(expectedIsbnApi, actualIsbnDb);
        Assert.assertEquals(expectedYearApi, actualYearDb);
        Assert.assertEquals(expectedAuthorApi, actualAuthorDb);


    }

    @And("created user information should match with Database")
    public void createdUserInformationShouldMatchWithDatabase() {
        //save user_id
        int id = Integer.parseInt(response.path("user_id"));
        //write query to retrieve data fromDB
        String query = "select * from users where id = " + id;
        DB_Util.runQuery(query);
        Map<String, String> userInfoDB = DB_Util.getRowMap(1);
        log.info(userInfoDB);

        //using returnSingleInfoApi method created in LibraryUtils to return user or book info
        Map<String, String> userInfoApi = LibraryUtils.returnSingleInfoApi("user",id);
        log.info(userInfoApi);

        //Assert API expected To DB ACTUAL
        Assert.assertEquals(userInfoApi.get("full_name"), userInfoDB.get("full_name"));
        Assert.assertEquals(userInfoApi.get("email"), userInfoDB.get("email"));
        Assert.assertEquals(userInfoApi.get("user_group_id"), userInfoDB.get("user_group_id"));
        Assert.assertEquals(userInfoApi.get("status"), userInfoDB.get("status"));
        Assert.assertEquals(userInfoApi.get("start_date"), userInfoDB.get("start_date"));
        Assert.assertEquals(userInfoApi.get("end_date"), userInfoDB.get("end_date"));
        Assert.assertEquals(userInfoApi.get("address"), userInfoDB.get("address"));

        //store the email from the retrieved info from Api
        email = String.valueOf(userInfoApi.get("email"));
        //if we get the password from API is hash value producing by MD5(message-digest algorithm)
        //instead we get from the body that we send
        password = String.valueOf(body.get("password"));

    }

    String email;
    String password;

    @And("created user should be able to login Library UI")
    public void createdUserShouldBeAbleToLoginLibraryUI() {

        LoginPage loginPage = new LoginPage();
        //we pass email and password credentials that we retrieved above and saved in class level
        loginPage.login(email, password);
        BrowserUtil.waitFor(1);

        //verify that when user log in , user see Library as title
        String expectedTitle = "Library";
        String actualTitle = Driver.getDriver().getTitle();
        Assert.assertEquals(expectedTitle, actualTitle);

    }

    @And("created user name should appear in Dashboard Page")
    public void createdUserNameShouldAppearInDashboardPage() {
        //go to userPage
        UsersPage usersPage = new UsersPage();
        usersPage.users.click();

        //and send the email that we retrieved above to search for user
        BrowserUtil.waitForVisibility(usersPage.searchField, 5);
        usersPage.searchField.sendKeys(email);

        //create a dynamic xpath to verify that when we search user by email, that email is displayed with user credentials
        WebElement userDisplayed = Driver.getDriver().findElement(By.xpath("//tbody//td[.='" + email + "']"));
        BrowserUtil.waitForVisibility(userDisplayed, 5);

        Assert.assertTrue(userDisplayed.isDisplayed());

    }

    @Given("I logged Library api with credentials {string} and {string}")
    public void iLoggedLibraryApiWithCredentialsAnd(String email, String password) {
        //assigning the email and password with the values that we have in requirements
        this.email = email;
        this.password = password;

    //login with given email and password
    givenPart.header(" x-library-token",LibraryUtils.getTokenByCred(email,password));

    }

    @And("I send token information as request body")
    public void iSendTokenInformationAsRequestBody() {
        //adding body info to givenPart
        givenPart.when()
                .formParam("token",LibraryUtils.getTokenByCred(email,password));
    }


}
