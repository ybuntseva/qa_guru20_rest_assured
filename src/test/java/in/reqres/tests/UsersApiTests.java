package in.reqres.tests;

import in.reqres.models.UserInfoBodyModel;
import in.reqres.models.UserInfoResponseModel;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class UsersApiTests extends TestBase {

    @Test
    void checkTotalNumberOfUsers() {

        given()
                .log().uri()
                .log().method()
                .log().body()
                .when()
                .get("/users?page=2")
                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .body("total", is(12));
    }

    @Test
    void checkSingleUserInfo() {

        given()
                .log().uri()
                .log().method()
                .log().body()
                .when()
                .get("/users/2")
                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .assertThat()
                .body("data.id", is(2), "data.email", is("janet.weaver@reqres.in"),
                        "data.first_name", is("Janet"), "data.last_name", is("Weaver"),
                        "data.avatar", is("https://reqres.in/img/faces/2-image.jpg"));
    }

    @Test
    void userNotFoundTest() {

        given()
                .log().uri()
                .log().method()
                .log().body()
                .when()
                .get("/users/23")
                .then()
                .log().status()
                .log().body()
                .statusCode(404);
    }

    @Test
    void successfulUserCreationTest() {

//        String inputUserData = "{\"name\": \"morpheus\",\n" + "\"job\": \"leader\"}"; // BAD PRACTICE

        UserInfoBodyModel inputUserData = new UserInfoBodyModel();
        inputUserData.setName("morpheus");
        inputUserData.setJob("leader");

        UserInfoResponseModel userCreationResponse = given()
                .log().uri()
                .log().method()
                .log().body()
                .contentType(JSON)
                .body(inputUserData)
                .when()
                .post("/users")
                .then()
                .log().status()
                .log().body()
                .statusCode(201)
                .extract().as(UserInfoResponseModel.class);

        assertEquals("morpheus", userCreationResponse.getName());
        assertEquals("leader", userCreationResponse.getJob());
    }

    @Test
    void successfullyUserUpdateWithPatch() {

//        String updateUserData = "{\"name\": \"morpheus\",\n" + "\"job\": \"zion resident\"}"; // BAD PRACTICE

        UserInfoBodyModel updateUserData = new UserInfoBodyModel();
        updateUserData.setName("morpheus");
        updateUserData.setJob("leader");

        UserInfoResponseModel userUpdateResponse = given()
                .log().uri()
                .log().method()
                .log().body()
                .contentType(JSON)
                .body(updateUserData)
                .when()
                .patch("/users/2")
                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .extract().as(UserInfoResponseModel.class);

                assertEquals("morpheus", userUpdateResponse.getName());
                assertEquals("leader", userUpdateResponse.getJob());
    }

    @Test
    void successfullyUserUpdateWithPut() {

//        String updateUserData = "{\"name\": \"john\",\n" + "\"job\": \"plumber\"}"; // BAD PRACTICE

        UserInfoBodyModel updateUserData = new UserInfoBodyModel();
        updateUserData.setName("john");
        updateUserData.setJob("plumber");

        UserInfoResponseModel userUpdateResponse = given()
                .log().uri()
                .log().method()
                .log().body()
                .contentType(JSON)
                .body(updateUserData)
                .when()
                .put("/users/2")
                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .extract().as(UserInfoResponseModel.class);

        assertEquals("john", userUpdateResponse.getName());
        assertEquals("plumber", userUpdateResponse.getJob());
    }

    @Test
    void successfullyDeleteUser() {

        given()
                .log().uri()
                .log().method()
                .log().body()
                .when()
                .delete("/users/2")
                .then()
                .log().status()
                .log().body()
                .statusCode(204);
    }
}
