package guiatv.conf.mvc;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

//public class WebInit implements WebApplicationInitializer {
//	
//	private static Logger logger = Logger.getLogger("debugLog");
//	
//	
//	@Override
//	public void onStartup(ServletContext container) throws ServletException {
////		container.setInitParameter("spring.profiles.default",
////				"{CatalogRestControllerTests, PersistenceTests, dev, default}");
//		// Creates the root application context
//		AnnotationConfigWebApplicationContext appContext = new AnnotationConfigWebApplicationContext();
//		// Registers the application configuration with the root context
//		appContext.register(WebConfig.class);
//		// Creates the Spring Container shared by all Servlets and Filters
//		container.addListener(new ContextLoaderListener(appContext));
//		
//	}
//
//}

public class WebInit extends
		AbstractAnnotationConfigDispatcherServletInitializer {

	@Override
	protected Class<?>[] getRootConfigClasses() {
		return null;
	}

	@Override
	protected Class<?>[] getServletConfigClasses() {
		return new Class[] { WebConfig.class };
	}

	@Override
	protected String[] getServletMappings() {
		return new String[] { "/" };
	}

}