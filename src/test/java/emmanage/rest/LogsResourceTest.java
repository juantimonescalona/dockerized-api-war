package emmanage.rest;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Header;

import emmanage.config.AppDirectories;

// rest assured testing
import static com.jayway.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
		;
		// create requested files with lines containing just the contents
		
		return Arrays.asList(logFile)
				.stream()
				.map(
					(x) -> {
						try{
							return Files.write(
								appDirs.getLogDirectory().resolve(x),
								getFileContent(x).getBytes(StandardCharsets.UTF_8) 
							);
						}catch(Exception e){
							throw new RuntimeException(e);
						}
					})
				.collect(Collectors.toList());
	}

	private String getFileContent(String fileName){
		int iterations = 1000;
		//iterations-times of fileName ;
		return Stream
				.iterate(fileName, (y) -> y)
				.limit(1000)
				.collect(
						() -> new StringBuilder(iterations*fileName.length()), 
						StringBuilder::append, 
						StringBuilder::append)
				.toString(); 
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
	    		body("size", equalTo(getFileContent(fileName).length())).
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
	    		header("Content-Disposition", equalTo("attachment; filename=\""+fileName+"\"")).
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

	@Test
    public void logDataPartialContent() throws IOException{
    	String fileName = "logDataPartialContent.log";
    	String fileContents = getFileContent(fileName);
    	
    	setupLogDirectory(fileName);
		try{
	    	// return the file from the end (in a buffer size)
			String path = "/logs/"+fileName+"/data?offset=-3";
			given().
	    		accept(ContentType.TEXT).
	    	when().
	    		get(path).
	    	then().
	    		statusCode(200).
	    		contentType(ContentType.TEXT).
	    		header("Content-Disposition", isEmptyOrNullString()).
	    		body(equalTo(fileContents.substring(fileContents.length()-3)));
			// validate Link headers
			List<Header> listHeaders = get(path).headers().getList("Link");		
    		assertThat(listHeaders, hasSize(2));
    		assertThat(listHeaders.get(0).getValue(),containsString("data?offset="+(fileContents.length()-3)+">; rel=\"current\""));
    		assertThat(listHeaders.get(1).getValue(),containsString("data?offset="+fileContents.length()+">; rel=\"next\""));

	    	// return the file from the end (with a bigger than a buffer size)
			path = "/logs/"+fileName+"/data?offset=-5000";
			given().
	    		accept(ContentType.TEXT).
	    	when().
	    		get(path).
	    	then().
	    		statusCode(200).
	    		contentType(ContentType.TEXT).
	    		header("Content-Disposition", isEmptyOrNullString()).
	    		body(equalTo(fileContents.substring(fileContents.length()-5000)));
			// validate Link headers
			listHeaders = get(path).headers().getList("Link");		
    		assertThat(listHeaders, hasSize(2));
    		assertThat(listHeaders.get(0).getValue(),containsString("data?offset="+(fileContents.length()-5000)+">; rel=\"current\""));
    		assertThat(listHeaders.get(1).getValue(),containsString("data?offset="+fileContents.length()+">; rel=\"next\""));

	    	// return the file from the beginning, since offset is greater that actual value
			path = "/logs/"+fileName+"/data?offset="+(fileContents.length()+1);
			given().
	    		accept(ContentType.TEXT).
	    	when().
	    		get(path).
	    	then().
	    		statusCode(200).
	    		contentType(ContentType.TEXT).
	    		body(equalTo(fileContents));
		} finally{
			// delete files
			setupLogDirectory();
		}
	}
	@Test
    public void logDataNoContent() throws IOException{
    	String fileName = "logDataNoContent.log";
		String fileContent = getFileContent(fileName);
    	setupLogDirectory(fileName);
		try{
			String path = "/logs/"+fileName+"/data?offset="+fileContent.length();
			// poll from the end of file
	    	when().
	    		get(path).
	    	then().
	    		statusCode(204).
	    		header("Content-Disposition", isEmptyOrNullString()).
	    		body(isEmptyOrNullString());
			// validate Link headers
			List<Header> listHeaders = get(path).headers().getList("Link");		
    		assertThat(listHeaders, hasSize(2));
    		assertThat(listHeaders.get(0).getValue(),containsString("data?offset="+fileContent.length()+">; rel=\"current\""));
    		assertThat(listHeaders.get(1).getValue(),containsString("data?offset="+fileContent.length()+">; rel=\"next\""));
		} finally{
			// delete files
			setupLogDirectory();
		}
	}
}
