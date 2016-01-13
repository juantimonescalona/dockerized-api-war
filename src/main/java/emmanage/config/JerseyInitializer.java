package emmanage.config;

import java.util.Arrays;
import java.util.List;

import javax.ws.rs.ApplicationPath;

import org.glassfish.jersey.server.ResourceConfig;

import io.swagger.jaxrs.config.BeanConfig;

/**
 * Initializes Jersey2 REST providers. 
 * @author zavora
 */
@ApplicationPath("/v1")
public class JerseyInitializer extends ResourceConfig {
	public JerseyInitializer(){
		List<String> restResources = Arrays.asList("emmanage.rest");

		// setup REST resources and providers
		packages(false,restResources.toArray(new String[restResources.size()]));
		// register swagger resource, serializer
		register(io.swagger.jaxrs.listing.ApiListingResource.class);
        register(io.swagger.jaxrs.listing.SwaggerSerializers.class);
		
        // initialize swagger
        BeanConfig beanConfig = new BeanConfig();
        beanConfig.setResourcePackage(String.join(",", restResources));
        beanConfig.setPrettyPrint(true);
        beanConfig.setScan(true);
        // resource/SwaggerDefinitionProvider initializes Swagger root meta data
        // resource/SwaggerBaseUrlFilter initializes swagger schemes, host and basePath
    }
	
}