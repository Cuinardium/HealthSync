package ar.edu.itba.paw.webapp.config;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.DatabasePopulator;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

@EnableScheduling
@EnableTransactionManagement
@EnableWebMvc
@ComponentScan(
  basePackages = {
    "ar.edu.itba.paw.webapp.controller",
    "ar.edu.itba.paw.services",
    "ar.edu.itba.paw.persistence"
  }
)
@Configuration
@PropertySource("classpath:application.properties")
public class WebConfig extends WebMvcConfigurerAdapter {

  @Value("classpath:schema.sql")
  private Resource schemaSql;

  @Value("classpath:insertDefaults.sql")
  private Resource insertDefaultsSql;

  // get properties from application.properties
  @Autowired private Environment env;

  @Bean
  public ViewResolver viewResolver() {
    InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();

    viewResolver.setViewClass(JstlView.class);
    viewResolver.setPrefix("/WEB-INF/jsp/");
    viewResolver.setSuffix(".jsp");

    return viewResolver;
  }

  @Bean
  public DataSource dataSource() {
    final SimpleDriverDataSource ds = new SimpleDriverDataSource();

    ds.setDriverClass(org.postgresql.Driver.class);
    ds.setUrl(env.getProperty("datasource.url"));
    ds.setUsername(env.getProperty("datasource.user"));
    ds.setPassword(env.getProperty("datasource.password"));

    return ds;
  }

  @Bean
  public DataSourceInitializer dataSourceInitializer(final DataSource ds) {
    final DataSourceInitializer dsi = new DataSourceInitializer();

    dsi.setDataSource(ds);
    dsi.setDatabasePopulator(databasePopulator());

    return dsi;
  }

  private DatabasePopulator databasePopulator() {
    final ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
    populator.addScript(schemaSql);
    populator.addScript(insertDefaultsSql);
    return populator;
  }

  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {
    super.addResourceHandlers(registry);

    registry.addResourceHandler("/css/**").addResourceLocations("/css/");
    registry.addResourceHandler("/js/**").addResourceLocations("/js/");
    registry.addResourceHandler("/icons/**").addResourceLocations("/icons/");
    registry.addResourceHandler("/img/**").addResourceLocations("/img/");
  }

  @Bean
  public MessageSource messageSource() {
    final ReloadableResourceBundleMessageSource ms = new ReloadableResourceBundleMessageSource();

    ms.setCacheSeconds((int) TimeUnit.MINUTES.toSeconds(5));
    ms.setBasename("classpath:i18n/messages");
    ms.setDefaultEncoding(StandardCharsets.UTF_8.name());

    return ms;
  }

  @Bean
  public MultipartResolver multipartResolver() {
    CommonsMultipartResolver resolver = new CommonsMultipartResolver();
    resolver.setMaxUploadSize(1024 * 1024 * 10);
    return resolver;
  }

  @Bean
  public PlatformTransactionManager transactionManager(final DataSource ds) {
    return new DataSourceTransactionManager(ds);
  }
}
