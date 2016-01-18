package emmanage.model;

import java.io.File;

import javax.ws.rs.core.UriInfo;

/**
 * Information about a log file.
 * @author zavora
 */
public class LogInfo extends ResourceSupport{
	public long size;
	
	/** 
	 * Sole constructor used by serializers.
	 */
	public LogInfo(){}

	/** 
	 * Constructor that setup log info for a file.
	 * @param file
	 * @param uriInfo used to construct href
	 * @param isLising is URI info from the listing
	 */
	public LogInfo(File file, UriInfo uriInfo, boolean isListing){
		setId(file.getName());
		size = file.length();
		if (isListing){
			// include file name as well
			setHref(uriInfo.getAbsolutePathBuilder().path(file.getName()).build());
		} else{
			setHref(uriInfo.getAbsolutePathBuilder().build());
		}
	}
}
