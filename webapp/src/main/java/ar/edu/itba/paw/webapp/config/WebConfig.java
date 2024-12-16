package ar.edu.itba.paw.webapp.config;

import java.nio.charset.StandardCharsets;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import javax.persistence.EntityManagerFactory;
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
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.DatabasePopulator;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@EnableScheduling
@EnableTransactionManagement
@ComponentScan(
    basePackages = {
      "ar.edu.itba.paw.webapp.controller",
      "ar.edu.itba.paw.webapp.mapper",
      "ar.edu.itba.paw.services",
      "ar.edu.itba.paw.persistence"
    })
@Configuration
public class WebConfig extends WebMvcConfigurerAdapter {

  @Value("classpath:schema.sql")
  private Resource schemaSql;

  // get properties from env variables
  @Autowired private Environment env;

  @Bean
  public DataSource dataSource() {
    final SimpleDriverDataSource ds = new SimpleDriverDataSource();

    ds.setDriverClass(org.postgresql.Driver.class);
    ds.setUrl(env.getProperty("DATASOURCE_URL"));
    ds.setUsername(env.getProperty("DATASOURCE_USER"));
    ds.setPassword(env.getProperty("DATASOURCE_PASSWORD"));

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
    return populator;
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
      return new CommonsMultipartResolver();
  }

  @Bean
  public PlatformTransactionManager transactionManager(final EntityManagerFactory emf) {
    return new JpaTransactionManager(emf);
  }

  @Bean
  public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
    final LocalContainerEntityManagerFactoryBean factoryBean =
        new LocalContainerEntityManagerFactoryBean();
    // Donde estan las entidades que queremos mapear en la db
    factoryBean.setPackagesToScan("ar.edu.itba.paw.models");

    factoryBean.setDataSource(dataSource());

    // Que implementacion de JPA queremos utilizar
    final JpaVendorAdapter jpaVendorAdapter = new HibernateJpaVendorAdapter();
    factoryBean.setJpaVendorAdapter(jpaVendorAdapter);

    final Properties jpaProperties = new Properties();
    // Dialecto
    jpaProperties.setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQL92Dialect");
    // MUY LIMITADAMENTE va a intentar actualizar la base de datos
    jpaProperties.setProperty("hibernate.hbm2ddl.auto", "update");
    // Este dropea la DB
    // jpaProperties.setProperty("hibernate.hdb2ddl.auto", "create");

    // Imprimime a STDOUT las consultas
    // jpaProperties.setProperty("hibernate.show_sql", "true");
    // jpaProperties.setProperty("format_sql", "true");

    factoryBean.setJpaProperties(jpaProperties);

    return factoryBean;
  }

  @Bean
  public TaskScheduler taskScheduler() {
    ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
    taskScheduler.setPoolSize(10);
    return taskScheduler;
  }
}
