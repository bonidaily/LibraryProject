package com.cydeo.utility;

import com.github.javafaker.Faker;
import io.cucumber.java.it.Ma;
import io.restassured.http.ContentType;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class LibraryUtils {

    public static final String baseUrl = ConfigurationReader.getProperty("baseUrl");

    public static String getTokenByRole(String role) {

        String email = "";
        String password = "";

        switch (role) {
            case "librarian":
                email = ConfigurationReader.getProperty("librarian_username");
                password = ConfigurationReader.getProperty("librarian_password");
        }

        Map<String, String> userInfo = new HashMap<>();
        userInfo.put("email", email);
        userInfo.put("password", password);

        return given()
                .contentType(ContentType.URLENC)
                .formParams(userInfo)
                .when()
                .post(baseUrl + "/login")
                .then()
                .statusCode(200).extract().response().path("token");

    }

    public static String getTokenByCred(String email,String password){

        return given()
                .contentType(ContentType.URLENC)
                .formParam("email",email)
                .formParam("password",password)
                .when()
                .post(baseUrl + "/login")
                .then()
                .statusCode(200).extract().response().path("token");

    }

    public static Map<String, Object> getRandomBookMap() {

        Faker faker = new Faker();
        Map<String, Object> bookMap = new LinkedHashMap<>();
        String randomBookName = faker.book().title() + faker.number().numberBetween(33, 99);
        bookMap.put("name", randomBookName);
        bookMap.put("isbn", faker.code().isbn13());
        bookMap.put("year", faker.number().numberBetween(1000, 2021));
        bookMap.put("author", faker.book().author());
        bookMap.put("book_category_id", faker.number().numberBetween(1, 20));  // in library app valid category_id is 1-20
        bookMap.put("description", faker.chuckNorris().fact());

        return bookMap;
    }

    public static Map<String, Object> getRandomUserMap() {

        Faker faker = new Faker();
        Map<String, Object> userMap = new LinkedHashMap<>();
        String fullName = faker.name().fullName();
        String email = fullName.substring(0, fullName.indexOf(" ")) + faker.number().numberBetween(10, 100)  + "@library";
        System.out.println(email);
        userMap.put("full_name", fullName);
        userMap.put("email", email);
        userMap.put("password", "libraryUser");
        // 2 is librarian as role
        userMap.put("user_group_id", 2);
        userMap.put("status", "ACTIVE");
        userMap.put("start_date", "2023-03-11");
        userMap.put("end_date", "2024-03-11");
        userMap.put("address", faker.address().cityName());

        return userMap;
    }

    public static Map<String, Object> getRandomMap(String option) {

        switch (option) {
            case "user":
                return getRandomUserMap();
            case "book":
                return getRandomBookMap();
            default:
                return null;
        }
    }

    public static Map<String,String> returnSingleInfoApi(String option,int id){

        return given().accept(ContentType.JSON)
                .header("x-library-token", LibraryUtils.getTokenByRole("librarian"))
                .pathParam("id", id)
                .when()
                .get(LibraryUtils.baseUrl + "/get_"+option+"_by_id/{id}")
                .then()
                .statusCode(200).extract().jsonPath().getMap("");

    }

}
