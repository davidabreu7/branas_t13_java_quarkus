package com.branas.api.controllers;

import com.branas.domain.DTO.AccountInput;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.branas.utils.TestValues.VALID_CPF;
import static com.branas.utils.TestValues.VALID_NAME;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

@QuarkusTest
class AccountControllerTest {

    String VALID_EMAIL;
    @BeforeEach
    void setUp() {
        VALID_EMAIL = "john.doe%d@gmail.com".formatted(System.currentTimeMillis());
    }

    @Test
    void signupShouldCreateAccount() {
        AccountInput validPassenger = AccountInput.builder()
                .isPassenger(true)
                .name(VALID_NAME.value())
                .email(VALID_EMAIL)
                .cpf(VALID_CPF.value())
                .build();

        Response response = given()
                .contentType(ContentType.JSON)
                .accept(ContentType.TEXT)
                .body(validPassenger)
        .when().post("/api/signup");

        given()
                .accept(ContentType.JSON)
        .when()
                .get("/api/account/%s".formatted(response.getBody().asString()))
        .then()
                .statusCode(200)
                .body("accountId", is(response.getBody().asString()))
                .body("passenger", is(validPassenger.isPassenger()))
                .body("email", is(validPassenger.email()))
                .body("name", is(validPassenger.name()));
    }
}