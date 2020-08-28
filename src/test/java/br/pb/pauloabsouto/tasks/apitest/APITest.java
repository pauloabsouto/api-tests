package br.pb.pauloabsouto.tasks.apitest;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.hamcrest.CoreMatchers;
import org.junit.BeforeClass;
import org.junit.Test;

public class APITest {

    @BeforeClass
    public static void setup(){
        RestAssured.baseURI = "http://localhost:8001/tasks-backend";
    }

    @Test
    public void taskResponseTest(){
        RestAssured.given()
                .when()
                    .get("/todo")
                .then()
                    .statusCode(200);
    }

    @Test
    public void addTaskTest(){
        RestAssured.given()
                    .body("{ \"task\": \"API Test\", \"dueDate\": \"2020-12-30\" }")
                    .contentType(ContentType.JSON)
                .when()
                    .post("/todo")
                .then()
                    .statusCode(201);
    }

    @Test
    public void addInvalidTaskTest(){
        RestAssured.given()
                    .body("{ \"task\": \"API Test\", \"dueDate\": \"2013-12-30\" }")
                    .contentType(ContentType.JSON)
                .when()
                    .post("/todo")
                .then()
                    .log().all()
                    .statusCode(400)
                .body("message", CoreMatchers.is("Due date must not be in past"));
    }

    @Test
    public void removeTask(){
        Integer id = RestAssured.given()
                .body("{ \"task\": \"API Test\", \"dueDate\": \"2020-12-30\" }")
                    .contentType(ContentType.JSON)
                .when()
                    .post("/todo")
                .then()
                    .statusCode(201)
                    .extract().path("id");
        System.out.println(id);

        RestAssured.given()
                .when()
                    .delete("/todo/"+id)
                .then()
                    .statusCode(204);
    }


}
