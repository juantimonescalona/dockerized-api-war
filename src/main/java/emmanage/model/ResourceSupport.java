package emmanage.model;

import java.net.URI;

import javax.ws.rs.core.UriInfo;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Simple resource support (without links).
 * 
 * @author zavora
 */
@JsonInclude(JsonInclude.Include.NON_NULL) 
public class ResourceSupport {
	private String id;
    private URI href;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public URI getHref() {
		return href;
	}
	public void setHref(URI href) {
		this.href = href;
	}
	
	public void setupHref(UriInfo uriInfo){
		setHref(uriInfo.getAbsolutePathBuilder().path(id).build());
	}
}
