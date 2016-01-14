package emmanage.config;

import java.nio.file.Paths;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@ComponentScan(basePackageClasses={emmanage.service.impl.LogsServiceImpl.class})
public class SpringConfig {
	@Bean
	@Profile("default")
	public AppDirectories appBaseDir(){
		return new AppDirectories();
	}
	@Bean
	@Profile("test")
	public AppDirectories appBaseDirForTests(){
		String baseDir = Paths
				.get(System.getProperty("java.io.tmpdir"))
				.resolve("em.baseDirectory")
				.toFile().getAbsolutePath();
		return new AppDirectories(baseDir);
	}
}
