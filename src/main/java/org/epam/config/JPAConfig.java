package org.epam.config;

import java.util.Properties;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@PropertySource("classpath:application.properties")
@EnableTransactionManagement
public class JPAConfig {

  @Value("${jdbc.driverClassName}")
  private String driverClassName;

  @Value("${jdbc.url}")
  private String url;

  @Value("${jdbc.user}")
  private String username;

  @Value("${jdbc.pass}")
  private String password;

  @Value("${hibernate.hbm2ddl.auto}")
  private String hbm2ddlAuto;

  @Value("${hibernate.dialect}")
  private String dialect;

  @Bean
  public LocalContainerEntityManagerFactoryBean entityManagerFactory() {

    final LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
    em.setDataSource(dataSource());
    em.setPackagesToScan("org.epam.entity");
    final JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
    em.setJpaVendorAdapter(vendorAdapter);
    em.setJpaProperties(additionalProperties());

    return em;
  }

  @Profile("dev")
  @Bean
  public DataSource dataSource() {

    final DriverManagerDataSource dataSource = new DriverManagerDataSource();
    dataSource.setDriverClassName(driverClassName);
    dataSource.setUrl(url);
    dataSource.setUsername(username);
    dataSource.setPassword(password);

    return dataSource;
  }

  @Bean
  public PlatformTransactionManager transactionManager() {

    final JpaTransactionManager transactionManager = new JpaTransactionManager();
    transactionManager.setEntityManagerFactory(entityManagerFactory().getObject());

    return transactionManager;
  }

  @Bean
  public static PersistenceExceptionTranslationPostProcessor exceptionTranslation() {

    return new PersistenceExceptionTranslationPostProcessor();
  }

  final Properties additionalProperties() {

    final Properties hibernateProperties = new Properties();
    hibernateProperties.setProperty("hibernate.hbm2ddl.auto", hbm2ddlAuto);
    hibernateProperties.setProperty("hibernate.dialect", dialect);
    hibernateProperties.setProperty("hibernate.cache.use_second_level_cache", "false");
    return hibernateProperties;
  }

  @Bean
  public DataSourceInitializer dataSourceInitializer(DataSource dataSource) {

    DataSourceInitializer initializer = new DataSourceInitializer();
    initializer.setDataSource(dataSource);
    initializer.setDatabasePopulator(databasePopulator());
    return initializer;
  }

  private ResourceDatabasePopulator databasePopulator() {

    ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
    populator.addScript(
        new ClassPathResource("fillDb.sql"));
    return populator;
  }
}
