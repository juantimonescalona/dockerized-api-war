package emmanage.model;

import java.io.File;

import javax.ws.rs.core.UriInfo;

/**
 * Information about a log file.
 * @author zavora
 */
public class LogInfo extends ResourceSupport{
	/** 
	 * Sole constructor used by serializers.
	 */
	public LogInfo(){}

	/** 
	 * Constructor that setup log info for a file.
	 * @param file
	 * @param uriInfo used to construct href
	 */
	public LogInfo(File file, UriInfo uriInfo){
		setId(file.getName());
		setHref(uriInfo.getAbsolutePathBuilder().path(file.getName()).build());
	}
}
