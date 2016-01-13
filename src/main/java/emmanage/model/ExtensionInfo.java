package emmanage.model;

import java.io.File;

import javax.ws.rs.core.UriInfo;

/**
 * Contains information about extension
 * @author zavora
 */
public class ExtensionInfo extends ResourceSupport{
	/** 
	 * Sole constructor used by serializers.
	 */
	public ExtensionInfo(){}

	/** 
	 * Constructor that setup id and href.
	 * @param file
	 * @param uriInfo used to construct href
	 */
	public ExtensionInfo(File file, UriInfo uriInfo){
		setId(file.getName());
		setHref(uriInfo.getAbsolutePathBuilder().path(file.getName()).build());
	}

}
