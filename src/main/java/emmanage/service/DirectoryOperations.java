package emmanage.service;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Base service that lists a specific directory (log, extensions, patches).  
 * @author zavora
 */
public interface DirectoryOperations{
	/**
	 * Gets the list of files representing application logs.
	 * @return list of application log files
	 */
	public List<File> getFiles() throws IOException;
	
	/**
	 * Gets file of a particular log.
	 * @param id log id
	 * @return log info
	 * @throw IOException log not found
	 */
	public File getFile(String id) throws IOException;
}
