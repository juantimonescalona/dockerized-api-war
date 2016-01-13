package emmanage.rest;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;

import io.swagger.models.Scheme;
import io.swagger.models.Swagger;

/**
 * Changes returned swagger to point to local APIs URL. 
 */
@Provider
public class SwaggerBaseUrlFilter implements ContainerResponseFilter{
	private static List<String> IGNORED_PROPS = Arrays.asList("basePath","host","schemes");
	private List<PropertyDescriptor> props;

	
	public SwaggerBaseUrlFilter(){
		try {
			BeanInfo beanInfo = Introspector.getBeanInfo(Swagger.class);
			PropertyDescriptor[] pds = beanInfo.getPropertyDescriptors();
			this.props = new ArrayList<>();
			for(PropertyDescriptor pd: pds){
				if (IGNORED_PROPS.contains(pd.getName())) continue;
				if (pd.getReadMethod()!=null && pd.getWriteMethod()!=null){
					props.add(pd);
				}
			}
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext)
			throws IOException {
		if(responseContext.getEntity() instanceof Swagger){
			Swagger orig = (Swagger)responseContext.getEntity();

			// clone swagger
			Swagger swagger = new Swagger();
			try {
				for(PropertyDescriptor pd: props){
						pd.getWriteMethod().invoke(swagger, pd.getReadMethod().invoke(orig));
				}
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
			// setup host, base path, schemes in the clone according to request
 			URI uri = requestContext.getUriInfo().getBaseUri();
			String basePath = uri.getPath();
			if (basePath.endsWith("/")) basePath = basePath.substring(0, basePath.length()-1);
			swagger.scheme(Scheme.forValue(uri.getScheme()))
			       .basePath(basePath)
			       .host(uri.getPort()!=-1?(uri.getHost()+":"+uri.getPort()):uri.getHost());
			responseContext.setEntity(swagger);
		}
	}
}
