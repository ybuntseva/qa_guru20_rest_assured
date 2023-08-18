package in.reqres.tests;

import in.reqres.models.SingleUserResponseModel;
import in.reqres.models.UserInfoBodyModel;
import in.reqres.models.UserInfoResponseModel;
import in.reqres.models.UserListInfoModel;
import org.junit.jupiter.api.Test;

import static in.reqres.specs.UsersSpec.*;
import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class UsersApiTests {

    @Test
    void checkTotalNumberOfUsers() {

        UserListInfoModel listOfUsersResponse = step("Send a request to get the list of users", () -> given(usersRequestSpec)
                .when()
                .get("/users?page=2")
                .then()
                .spec(usersListResponseSpec)
                .extract().as(UserListInfoModel.class));

        step("Check response", () ->
        assertEquals(12, listOfUsersResponse.getTotal()));
    }

    @Test
    void checkSingleUserInfo() {

        SingleUserResponseModel singleUserInfo = step("Send a request to check the user's info", () -> given(usersRequestSpec)
                .when()
                .get("/users/2")
                .then()
                .spec(usersResponseSpec)
                .body(matchesJsonSchemaInClasspath("schemas/singleUser-response-schema.json"))
                .extract().as(SingleUserResponseModel.class));

       step("Check response", () -> {
           assertEquals(2, singleUserInfo.getData().getId());
           assertEquals("janet.weaver@reqres.in", singleUserInfo.getData().getEmail());
           assertEquals("Janet", singleUserInfo.getData().getFirst_name());
           assertEquals("Weaver", singleUserInfo.getData().getLast_name());
           assertEquals("https://reqres.in/img/faces/2-image.jpg", singleUserInfo.getData().getAvatar());
       });
       }

    @Test
    void userNotFoundTest() {

        step("Send a request of non-existent user", () -> given(usersRequestSpec)
                .when()
                .get("/users/23")
                .then()
                .spec(userNotFoundResponseSpec));
    }

    @Test
    void successfulUserCreationTest() {

//        String inputUserData = "{\"name\": \"morpheus\",\n" + "\"job\": \"leader\"}"; // BAD PRACTICE

        UserInfoBodyModel inputUserData = new UserInfoBodyModel();
        inputUserData.setName("morpheus");
        inputUserData.setJob("leader");

        UserInfoResponseModel userCreationResponse = step("Send a request to create a user", () -> given(usersRequestSpec)
                .body(inputUserData)
                .when()
                .post("/users")
                .then()
                .spec(userCreationResponseSpec)
                .extract().as(UserInfoResponseModel.class));

        step("Check response", () -> {
        assertEquals("morpheus", userCreationResponse.getName());
        assertEquals("leader", userCreationResponse.getJob());
    });
    }

    @Test
    void successfullyUserUpdateWithPatch() {

//        String updateUserData = "{\"name\": \"morpheus\",\n" + "\"job\": \"zion resident\"}"; // BAD PRACTICE

        UserInfoBodyModel updateUserData = new UserInfoBodyModel();
        updateUserData.setName("morpheus");
        updateUserData.setJob("leader");

        UserInfoResponseModel userUpdateResponse = step("Send a request to update the user's info with PATCH", () -> given(usersRequestSpec)
                .body(updateUserData)
                .when()
                .patch("/users/2")
                .then()
                .spec(userUpdateResponseSpec)
                .extract().as(UserInfoResponseModel.class));

        step("Check response", () ->
                assertAll(
                        () -> assertEquals("morpheus", userUpdateResponse.getName()),
                        () -> assertEquals("leader", userUpdateResponse.getJob())
                ));
    }

    @Test
    void successfullyUserUpdateWithPut() {

//        String updateUserData = "{\"name\": \"john\",\n" + "\"job\": \"plumber\"}"; // BAD PRACTICE

        UserInfoBodyModel updateUserData = new UserInfoBodyModel();
        updateUserData.setName("john");
        updateUserData.setJob("plumber");

        UserInfoResponseModel userUpdateResponse = step("Send a request to update the user's info with PUT", () -> given(usersRequestSpec)
                .body(updateUserData)
                .when()
                .put("/users/2")
                .then()
                .spec(userUpdateResponseSpec)
                .extract().as(UserInfoResponseModel.class));

        step("Check response", () ->
                assertAll(
                        () -> assertEquals("john", userUpdateResponse.getName()),
                        () -> assertEquals("plumber", userUpdateResponse.getJob())
                ));
    }

    @Test
    void successfullyDeleteUser() {

        step("Send a request to delete a user", () -> given(usersRequestSpec)
                .when()
                .delete("/users/2")
                .then()
                .spec(userDeletedResponseSpec));
    }
}
