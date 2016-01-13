package emmanage.service.impl;

import java.nio.file.Path;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import emmanage.config.AppDirectories;
import emmanage.service.LogsService;

@Service
public class LogsServiceImpl extends DirectoryOperationsImpl implements LogsService{
	@Autowired
	AppDirectories appDirectories;

	@Override
	protected Path getDirectory() {
		return appDirectories.getLogDirectory();
	}

	@Override
	protected String getGlobFilter() {
		return "*.log";
	}
}
