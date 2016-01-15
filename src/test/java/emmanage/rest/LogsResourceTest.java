package emmanage.rest;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.jayway.restassured.http.ContentType;

import emmanage.config.AppDirectories;

// rest assured testing
import static com.jayway.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class LogsResourceTest extends RestTestBase{
	@Autowired
	AppDirectories appDirs;
	
	private List<Path> setupLogDirectory(String... logFile) throws IOException{
		// cleanup log directory
		Files.newDirectoryStream(appDirs.getLogDirectory()).forEach(
				x -> {
					try{
						Files.delete(x);
					}catch(Exception e){
						throw new RuntimeException(e);
					}
				});
		// create requested files with lines containing just the contents
		return Arrays.asList(logFile)
				.stream()
				.map(
					(x) -> {
						try{
							return Files.write(
								appDirs.getLogDirectory().resolve(x),
								Arrays.asList(x,x,x,x)
							);
						}catch(Exception e){
							throw new RuntimeException(e);
						}
					})
				.collect(Collectors.toList());
	}
	
	@Test
    public void list() throws IOException{
		setupLogDirectory("log-listFiles1.log","log-listFiles1.nolog");

		try{
	    	// test that log contains log1, but not log2
			given().
	    		accept(ContentType.JSON).
	    	when().
	    		get("/logs").
	    	then().
	    		statusCode(200).
	    		contentType(ContentType.JSON).
	    		body("",hasSize(1)).
	    		body("id",hasItem("log-listFiles1.log")).
	    		body("id",not(hasItem("log-listFiles2.nolog"))).
	    		body("find { it.id == 'log-listFiles1.log' }.href", endsWith("/log-listFiles1.log")).
	    		and(); // tail only
		} finally{
			// delete files
			setupLogDirectory();
		}
    }
	@Test
    public void logDescriptor() throws IOException{
    	String fileName = "logDescriptor.log";
    	setupLogDirectory(fileName);
		try{
	    	// test log metadata retrieval
			given().
	    		accept(ContentType.JSON).
	    	when().
	    		get("/logs/"+fileName).
	    	then().
	    		statusCode(200).
	    		contentType(ContentType.JSON).
	    		body("id",equalTo(fileName)).
	    		body("href", endsWith("/"+fileName)).
	    		and(); // tail only

			// test invalid log metadata
			given().
	    		accept(ContentType.JSON).
	    	when().
	    		get("/logs/no"+fileName).
	    	then().
	    		statusCode(404);
		} finally{
			// delete files
			setupLogDirectory();
		}
	}
	@Test
    public void logData() throws IOException{
    	String fileName = "logData.log";
    	setupLogDirectory(fileName);
		try{
	    	// JSON not supported
			given().
	    		accept(ContentType.JSON).
	    	when().
	    		get("/logs/"+fileName+"/data").
	    	then().
	    		statusCode(406);

	    	// TEXT supported
			given().
	    		accept(ContentType.TEXT).
	    	when().
	    		get("/logs/"+fileName+"/data").
	    	then().
	    		statusCode(200).
	    		contentType(ContentType.TEXT).
	    		body(containsString(fileName));

			// test invalid log metadata
			given().
	    		accept(ContentType.TEXT).
	    	when().
	    		get("/logs/no"+fileName+"/data").
	    	then().
	    		statusCode(404);
		} finally{
			// delete files
			setupLogDirectory();
		}
	}

}
