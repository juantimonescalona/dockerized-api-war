package emmanage.config;

import java.io.File;

import javax.servlet.ServletContextEvent;

import org.springframework.web.context.ContextLoaderListener;

/**
 * Initializes Spring configuration in a web application.
 * @author zavora
 */
public class SpringInitializer extends ContextLoaderListener{

	@Override
	public void contextInitialized(ServletContextEvent event) {
		AppDirectories.TEMP_DIRECTORY = (File)event.getServletContext().getAttribute("javax.servlet.context.tempdir");
		super.contextInitialized(event);
	}

// !!! The approach with org.springframework.web.WebApplicationInitializer failed:
//	@Override
//	public void onStartup(ServletContext servletContext) throws ServletException {
//		AppDirectories.TEMP_DIRECTORY = (File)servletContext.getAttribute("javax.servlet.context.tempdir");
//
//		// Create the 'root' Spring application context
//	    AnnotationConfigWebApplicationContext rootContext =  new AnnotationConfigWebApplicationContext();
//	    rootContext.register(SpringConfig.class);
//
//	    // Manage the lifecycle of the root application context
//	    servletContext.addListener(new ContextLoaderListener(rootContext));		
//	}
}
