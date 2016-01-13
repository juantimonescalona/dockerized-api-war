package emmanage.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackageClasses={emmanage.service.impl.LogsServiceImpl.class})
public class SpringConfig {
	@Bean 
	public AppDirectories appBaseDir(){
		return new AppDirectories();
	}
}
