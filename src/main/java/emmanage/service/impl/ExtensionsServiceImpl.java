package emmanage.service.impl;

import java.nio.file.Path;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import emmanage.config.AppDirectories;
import emmanage.service.ExtensionsService;

@Service
public class ExtensionsServiceImpl extends DirectoryOperationsImpl implements ExtensionsService{
	@Autowired
	AppDirectories appDirectories;

	@Override
	protected Path getDirectory() {
		return appDirectories.getExtensionsDirectory();
	}

	@Override
	protected String getGlobFilter() {
		return "*.jar";
	}
}
