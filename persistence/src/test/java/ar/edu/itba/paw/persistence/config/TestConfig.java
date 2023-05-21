package ar.edu.itba.paw.persistence.config;

import java.util.Properties;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.DatabasePopulator;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@ComponentScan("ar.edu.itba.paw.persistence")
@Configuration
@EnableTransactionManagement
public class TestConfig {

  @Value("classpath:hsqldb.sql")
  private Resource hsqldbSql;

  @Value("classpath:schema.sql")
  private Resource schemaSql;

  @Value("classpath:testInserts.sql")
  private Resource testInsertsSql;

  @Bean
  public DataSource dataSource() {
    final SimpleDriverDataSource ds = new SimpleDriverDataSource();

    ds.setDriverClass(org.hsqldb.jdbc.JDBCDriver.class);
    ds.setUrl("jdbc:hsqldb:mem:paw");
    ds.setUsername("ha");
    ds.setPassword("");

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
    populator.addScript(hsqldbSql);
    populator.addScript(schemaSql);
    populator.addScript(testInsertsSql);
    return populator;
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
    final HibernateJpaVendorAdapter jpaVendorAdapter = new HibernateJpaVendorAdapter();
    factoryBean.setJpaVendorAdapter(jpaVendorAdapter);

    final Properties jpaProperties = new Properties();
    // Dialecto
    jpaProperties.setProperty("hibernate.dialect", "org.hibernate.dialect.HSQLDialect");
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
}
