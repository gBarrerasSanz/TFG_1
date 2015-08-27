package guiatv.conf.mvc;

import guiatv.catalog.restcontroller.CatalogRestController;

import java.util.List;

import org.codehaus.jackson.map.ser.impl.JsonSerializerMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;
import org.springframework.context.annotation.Import;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.integration.support.json.Jackson2JsonObjectMapper;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import com.fasterxml.jackson.core.JsonFactory.Feature;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

@Configuration
@EnableWebMvc
@ComponentScan({"guiatv"})
public class WebConfig extends WebMvcConfigurerAdapter {
 
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(converter());
    }
    
    @Bean
    public MappingJackson2HttpMessageConverter converter() {
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setObjectMapper(springHateoasMapper());
        return converter;
    }
    
    private static final String SPRING_HATEOAS_OBJECT_MAPPER = "_halObjectMapper";
    
    @Autowired
    @Qualifier(SPRING_HATEOAS_OBJECT_MAPPER)
    private ObjectMapper springHateoasObjectMapper;


    @Bean(name = SPRING_HATEOAS_OBJECT_MAPPER)
    ObjectMapper springHateoasMapper() {
    	springHateoasObjectMapper.configure(DeserializationFeature.UNWRAP_SINGLE_VALUE_ARRAYS, true);
        return springHateoasObjectMapper;
    }
    
    /**
     * THYMELEAF CONFIGURATION
     */
    
  @Override
  public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
      configurer.enable();
  }
  
  @Override
  public void addViewControllers(ViewControllerRegistry registry) {
//	  registry.addViewController("/staticweb").setViewName("staticweb");
	  registry.addViewController("/publisherCatalog").setViewName("programmes_catalog");
	  registry.addViewController("/publisherCatalog/").setViewName("programmes_catalog");
	  registry.addViewController("/programmes_catalog.html").setViewName("programmes_catalog");
//	  registry.addViewController("/adminChannels").setViewName("adminChannels");
//      registry.addViewController("/ml/home").setViewName("home");
//      registry.addViewController("/ml/").setViewName("home");
//      registry.addViewController("/home").setViewName("home");
  }
    
    @Bean
    ServletContextTemplateResolver templateResolver() {
        ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver();
        templateResolver.setPrefix("/WEB-INF/templates/");
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode("HTML5");
        templateResolver.setOrder(1);
        return templateResolver;
        
    }

    @Bean
    SpringTemplateEngine templateEngine() {
        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        templateEngine.setTemplateResolver(templateResolver());
        return templateEngine;
    }

    @Bean
    ThymeleafViewResolver viewResolver() {
        ThymeleafViewResolver resolver = new ThymeleafViewResolver();
        resolver.setOrder(1);
        resolver.setTemplateEngine(templateEngine());
        resolver.setViewNames(new String[] { "*" });
        resolver.setCache(false);
        return resolver;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
    	registry.addResourceHandler("static/**").addResourceLocations("/WEB-INF/static/");
//    	registry.addResourceHandler("/resources/**").addResourceLocations("/resources/");
    }
    
 // Only needed if we are using @Value and ${...} when referencing properties
 	@Bean
 	public static PropertySourcesPlaceholderConfigurer properties() {
 		PropertySourcesPlaceholderConfigurer propertySources = new PropertySourcesPlaceholderConfigurer();
 		Resource[] resources = new ClassPathResource[] { 
 				new ClassPathResource("application.properties") };
 		propertySources.setLocations(resources);
 		propertySources.setIgnoreUnresolvablePlaceholders(true);
 		return propertySources;
 	}
 	
 	// Provides internationalization of messages
 	@Bean
 	public ResourceBundleMessageSource messageSource() {
 		ResourceBundleMessageSource source = new ResourceBundleMessageSource();
 		source.setBasename("messages");
 		return source;
 	}
}
