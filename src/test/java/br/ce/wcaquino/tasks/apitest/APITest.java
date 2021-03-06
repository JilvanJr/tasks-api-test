package br.ce.wcaquino.tasks.apitest;

import org.hamcrest.CoreMatchers;
import org.junit.BeforeClass;
import org.junit.Test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;

public class APITest {
	
	@BeforeClass
	public static void setup() {
		RestAssured.baseURI = "http://localhost:8001/tasks-backend";
	}

	@Test
	public void deveRetornarTarefas() {
		RestAssured.given()
			.when()
			// Fazendo requisição tipo get para url
				.get("/todo")
			.then()
				.statusCode(200)
			;
	}
	
	@Test
	public void deveAdicionarTarefaComSucesso() {
		RestAssured.given()
				.body("{\"task\":\"Teste via API\",\"dueDate\":\"2021-06-06\"}")
				.contentType(ContentType.JSON)
			.when()
				.post("/todo")
			.then()
				.statusCode(201)
			;
	}
	
	@Test
	public void naoDeveAdicionarTarefaInvalida() {
		RestAssured.given()
				.body("{\"task\":\"Teste via API\",\"dueDate\":\"2020-06-03\"}")
				.contentType(ContentType.JSON)
			.when()
				.post("/todo")
			.then()
				.statusCode(400)
				.body("message", CoreMatchers.is("Due date must not be in past"))
			;
	}
	
	@Test
	public void deveRemoverTarefaComSucesso() {
		Integer id = RestAssured.given()
				.body("{\"task\":\"Teste para remocao\",\"dueDate\":\"2021-12-06\"}")
				.contentType(ContentType.JSON)
			.when()
				.post("/todo")
			.then()
				.statusCode(201)
				.extract().path("id")
			;
		
		System.out.println(id);
		
		RestAssured.given()
		.when()
			.delete("/todo/"+id)
		.then()
		.statusCode(204)
		;
	}
}
