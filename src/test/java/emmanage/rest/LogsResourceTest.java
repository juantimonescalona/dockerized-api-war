package emmanage.rest;

import org.junit.Test;

// rest assured testing
import static com.jayway.restassured.RestAssured.*;
//import static com.jayway.restassured.matcher.RestAssuredMatchers.*;
//import static org.hamcrest.Matchers.*;

public class LogsResourceTest extends RestTestBase{
	// TODO implement tests properly as an example

	@Test
    public void listFiles(){
    	System.out.println(uri("/logs"));
    	get(uri("/logs")).then().statusCode(200);
    	System.out.println("TODO3");
    }
    @Test
    public void getValidFileDescriptor(){
    	System.out.println("TODO");
    }
    @Test
    public void getInvalidFileDescriptor(){
    	System.out.println("TODO4");
    }
    @Test
    public void getValidFileContents(){
    	System.out.println("TODO5");
    }
    @Test
    public void getInvalidFileContents(){
    	System.out.println("TODO6");
    }
}
