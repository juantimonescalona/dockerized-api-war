package emmanage.config;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.annotation.Nullable;

import org.apache.commons.io.FileUtils;
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

	/**
	 * Sole constructor used by Spring.
	 */
	public AppDirectories(){
		init(null);
	}
	
	/**
	 * Intended for tests to create application directories at the specified path.
	 * @param root
	 */
	public AppDirectories(String root){
		init(root);
	}

	/**
	 * Initializes paths using an optional base directory.
	 * @param base base directory
	 */
	public void init(@Nullable String root){
		String dir;
		Path path;

		if (root!=null){
			File rootFile = new File(root);
			if (rootFile.exists()){
				try {
					FileUtils.deleteDirectory(rootFile);
				} catch (IOException e) {
					throw new RuntimeException("Unable to delete temporary directory!",e);
				}
			}
			dir = root;
			// create directories
			new File(root,"log").mkdirs();
			new File(root,"extensions").mkdirs();
			new File(root,"temp").mkdirs();
		} else{
			dir = System.getProperty("em.baseDirectory");
		}
		// baseDirectory
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

		logDirectory = baseDirectory.resolve("log");

		extensionsDirectory = baseDirectory.resolve("extensions");

		dir = System.getProperty("em.tempDirectory");
		if (root!=null){
			path = baseDirectory.resolve("temp");
		}else if (dir != null){
			path = Paths.get(dir);
		} else{
			if (TEMP_DIRECTORY!=null){
				path = TEMP_DIRECTORY.toPath();
			} else{
				path = Paths.get(System.getProperty("java.io.tmpdir"));
			}
		}
		tempDirectory = path;
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
