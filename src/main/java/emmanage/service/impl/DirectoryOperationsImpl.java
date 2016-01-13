package emmanage.service.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import emmanage.service.DirectoryOperations;

/**
 * File manipulation operations on filtered directory files. 
 * @author zavora
 */
public abstract class DirectoryOperationsImpl implements DirectoryOperations{
	/**
	 * Returns directory to read files from.
	 * @return
	 */
	protected abstract Path getDirectory();
	
	/**
	 * Returns glob filter of managed files.
	 * @return
	 */
	protected abstract String getGlobFilter();

	@Override
	public List<File> getFiles() throws IOException {
        try(DirectoryStream<Path> ds = Files.newDirectoryStream(getDirectory(),getGlobFilter())){
        	Stream<Path> stream = StreamSupport.stream(ds.spliterator(), false);
        	return stream.sorted().map(x -> x.toFile()).collect(Collectors.toList());
        }
	}

	@Override
	public File getFile(String id) throws IOException {
		File f = new File(getDirectory().toFile(),id);
		if (!f.exists() || !f.isFile()) throw new FileNotFoundException(id);
		return f;
	}
}
