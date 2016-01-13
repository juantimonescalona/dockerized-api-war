package emmanage.config;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Initializes application specific directories. 
 * @author zavora
 */
public class AppDirectories {
	private static final Log log = LogFactory.getLog(AppDirectories.class);
	/** Temporary directory set externally from SpringInitializer for in a web context */
	public static File TEMP_DIRECTORY = null;
	
	private Path baseDirectory; 
	private Path logDirectory; 
	private Path extensionsDirectory; 
	private Path tempDirectory; 

	public AppDirectories(){
		init();
	}
	
	public void init(){
		String dir;
		Path path;

		// baseDirectory
		dir = System.getProperty("em.baseDirectory");
		if (dir != null){
			path = Paths.get(dir);
		} else{
			dir = System.getProperty("jboss.server.base.dir");
			if (dir != null){
				path = Paths.get(dir).resolve("..").resolve("..");
			} else{
				log.warn("using temporary directory for em.basedir");
				path = Paths.get(System.getProperty("java.io.tmpdir"));
				
			}
		}
		
		baseDirectory = path.toAbsolutePath();

		logDirectory = baseDirectory.resolve("log").toAbsolutePath();

		extensionsDirectory = baseDirectory.resolve("extensions").toAbsolutePath();

		dir = System.getProperty("em.tempDirectory");
		if (dir != null){
			path = Paths.get(dir);
		} else{
			if (TEMP_DIRECTORY!=null){
				path = TEMP_DIRECTORY.toPath();
			} else{
				path = Paths.get(System.getProperty("java.io.tmpdir"));
			}
		}
		tempDirectory = path.toAbsolutePath();
		log.info("\nem.baseDirectory: "+baseDirectory+
				 "\nem.logDirectory: "+logDirectory+
		         "\nem.extensionsDirectory: "+extensionsDirectory+
		         "\nem.tempDirectory: "+extensionsDirectory);
	}

	public Path getBaseDirectory() {
		return baseDirectory;
	}

	public Path getLogDirectory() {
		return logDirectory;
	}

	public Path getExtensionsDirectory() {
		return extensionsDirectory;
	}

	public Path getTempDirectory() {
		return tempDirectory;
	}
	
}
