package testCases;

import org.testng.Assert;
import org.testng.annotations.Test;

import io.restassured.response.Response;

//specify the imported packages, 2 static packages from rest assured
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import java.util.HashMap;

public class TC_VideoGameAPI {
	
	@Test(priority=1)
	public void test_GetAllVideoGames() {
		
		//start with the given session, we do not have any prerequisite
		given()
		
		.when()//inside when section we need to send the GET request
			.get("http://localhost:8080/app/videogames")
		
		.then()//in here you can do validation, validating status code 200
			.statusCode(200);
		
	}
	
	@Test(priority=2)
	public void test_AddNewVideoGame() {
		//since we are going to do a POST request we need to have some data to specify the data we will create a HashMap variable, inside
		//this variable we will maintain the data, according to the document, the body and model schema have 6 values we need to pass, like
		//id,name,releaseDate etc, in our data variable we will add key and value pairs only.
		
		HashMap data= new HashMap();
		data.put("id","100");
		data.put("name", "Spider-Man");
		data.put("releaseDate", "2022-03-19T17:56:50.481Z");
		data.put("reviewScore", "5");
		data.put("category", "Adventure");
		data.put("rating", "Universal");
		//now we need to pass the data along the POST request, and will create a new record in DB
		
		Response res=
		//in given we specify the prerequisites, the content type which is xml or json type, and specify the data 
		given()
			.contentType("application/json")//here we specify content type as json format
			.body(data)
			
		//now send the request
		.when()
			.post("http://localhost:8080/app/videogames")//inside you need to specify the POST url
		.then()//can perform some validations like status code 200, and log the body to check if it was created and view in console window
			.statusCode(200)
			.log().body()//whatever data we are sending in hashmap will be displayed in console window
			.extract().response();//will verify response body that as per documentation it should say "status": "Record Added Successfully". We have to save the
			//response in a variable, in here we cannot put assert command directly. This method will return the response, that we can store
			//in a variable created above, Response rs=. So whatever POST request we are sending (ULR) that will give you some response, and we
			//are validating some status code and printing the body, and we are storing the response inside this rs variable, all those things
			//should be part of response (rs), now we need to get the body from response, will be res.asSrting() because that is in json format
			//and if you want to check the text in some of the responses we need to convert that into strings format, that can be saved
			//in a string variable called jsonString, now the response is in string format, we can do assertions.
		String jsonString= res.asString();
		//in actual will go jsonString.contains and the message we are expecting which is "Record Added Successfully" and in expecting
		//we are expecting to be true, statement will return true or false, if it returns true it will match with the expected true and assertion
		//will pass
		Assert.assertEquals(jsonString.contains("Record Added Successfully"),true);
			
	}
	@Test(priority=3)
	public void test_getVideoGame() {//Get request that will retrieve single game record. Verify this time with xml
		
		given()
			.when()
				.get("http://localhost:8080/app/videogames/100")
			.then()//Validating  status code 200 and verify body, after sending the request there is response body, by default it is in xml, we can verify the id within the body
			//or name etc..
				.statusCode(200)
				.log().body()
				.body("videoGame.id", equalTo("100"))// in here you can pass arguments and the body you are expeting. Id is present in the video game parent tag, if you want to 
				//access id you need to first specify the parent tag.
				.body("videoGame.name", equalTo("Spider-Man"));
				
		
	}
	//Put will update existing record, similar to post request.
	@Test(priority=4)
	public void test_updateVideoGame() {
		//we need to specify the data we are going to update for that we can copy the hashmap from put request will change name to Pacman, review score to 4
		HashMap data= new HashMap();
		data.put("id","100");
		data.put("name", "Pacman");
		data.put("releaseDate", "2022-03-19T17:56:50.481Z");
		data.put("reviewScore", "4");
		data.put("category", "Adventure");
		data.put("rating", "Universal");
		
		// in given we need to specify 2 things, content type and the body and specify data
			given()
				.contentType("application/json")
				.body(data)
		//send the request, with the put request.
			.when()
				.put("http://localhost:8080/app/videogames/100")
		//what are we expecting out of this request?
			.then()
				.statusCode(200)
					//print the updated data in console window
				.log().body()
				.body("videoGame.id",equalTo("100"))
				.body("videoGame.name", equalTo("Pacman"))
				.body("videoGame.reviewScore", equalTo("4"));
					
	}
	//delete request, requires video game id, needs to be passed along with the request
	@Test(priority=5)
	public void test_deleteVideoGame() throws InterruptedException {
		Response res=
		//we dont have anything in given
		given()
		.when()//inside when, we need to send the delete request
			.delete("http://localhost:8080/app/videogames/100")
			//perform validations
		.then()
			.statusCode(200)
			.log().body()
			//verify message, to check this message we need to save it inside a variable
			.extract().response(); //we will store this response in variable response rs= which will now contain a body, then conbert that response into string format
			Thread.sleep(3000);
			String jsonString=res.asString();//res.asString() will convert the response as a string format, and save it in a string variable
			//now you can apply assertions
			Assert.assertEquals(jsonString.contains("Record Deleted Successfully"), true);
			
		
		
	}
	
	

 }
