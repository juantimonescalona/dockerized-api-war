package emmanage.rest;

import java.net.URI;

import javax.ws.rs.client.WebTarget;

import org.glassfish.jersey.filter.LoggingFilter;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import com.jayway.restassured.RestAssured;

import emmanage.config.JerseyInitializer;
import emmanage.config.SpringConfig;

/**
 * Base class for REST interface testing start an embedded server with REST APIs and 
 * contains methods that let you easily test REST interface using JAX-RS client API or
 * RestAssured. 
 * 
 * @author zavora
 */
@RunWith(org.springframework.test.context.junit4.SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = SpringConfig.class)
@ActiveProfiles("test") // required for proper test setup of base directories
public class RestTestBase implements ApplicationContextAware{
	private JerseyTest jerseyTest;

    @Before
    public void setUp() throws Exception {
    	jerseyTest.setUp();
        // initializes REST assured, so that tested URL can be relative
    	RestAssured.baseURI = uri(null).toString();
    }
    @After
    public void tearDown() throws Exception {
    	jerseyTest.tearDown();
    }
    
    /**
     * Creates a JAX-RS web target whose URI refers to the {@link #getBaseUri() base URI}  of the tested
     * JAX-RS / Jersey application is deployed at, plus the path specified in the {@code path} argument.
     *
     * @param path path in the application or <code>null</code> to return root application path 
     * @return the created JAX-RS web target.
     */
    public WebTarget target(String path){
  		return path==null?jerseyTest.target():jerseyTest.target(path);
    }
    
    /**
     * Gets the URI to the deployed REST API on the specified relative path.
     * 
     * @param path path in the application or <code>null</code> to return root application path 
     * @return URI
     */
    public URI uri(String path){
  		return target(path).getUri();
    }
    
    /**
     * The same as {@link #uri(String) uri(String)}, but return string.
     * 
     * @param path path in the application or <code>null</code> to return root application path 
     * @return string
     */
    public String uriString(String path){
    	return uri(path).toString();
    };

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		ResourceConfig app = new JerseyInitializer()
				.property("contextConfig", applicationContext)
				.register(LoggingFilter.class);
		jerseyTest = new JerseyTest(app) {};
	}
}
