package ru.t1.java.starter.config;

import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import ru.t1.java.starter.model.DataSourceErrorLog;
import ru.t1.java.starter.repository.DataSourceErrorLogRepository;

import javax.sql.DataSource;

@Configuration
@EnableJpaRepositories(basePackages = "ru.t1.java.starter.repository") // Укажите пакет с вашими репозиториями
public class MetricsDatabaseConfig {
    @Value("${metrics.datasource.url}")
    private String url;
    @Value("${metrics.datasource.username}")
    private String username;
    @Value("${metrics.datasource.password}")
    private String password;
    @Value("${metrics.jpa.hibernate.ddl-auto}")
    private String ddlAuto;
    @Value("${metrics.jpa.properties.hibernate.dialect}")
    private String dialect;
    @Value("${metrics.jpa.show-sql}")
    private String showSql;
    @Value("${metrics.jpa.properties.hibernate.format_sql}")
    private String formatSql;

    @Bean("dataSourceMetrics")
    public DataSource dataSourceMetrics() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        return dataSource;
    }

    @Bean("entityManagerFactoryMetrics")
    public LocalContainerEntityManagerFactoryBean entityManagerFactoryMetrics(@Qualifier("dataSourceMetrics") DataSource dataSource) {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource);
        em.setPackagesToScan("ru.t1.java.starter.model");

        var properties = new java.util.Properties();
        properties.setProperty("hibernate.dialect", dialect);
        properties.setProperty("hibernate.hbm2ddl.auto", ddlAuto);
        properties.setProperty("hibernate.show_sql", showSql);
        properties.setProperty("hibernate.format_sql", formatSql);

        em.setJpaProperties(properties);

        return em;
    }

    @Bean
    public PlatformTransactionManager transactionManagerMetrics(@Qualifier("entityManagerFactoryMetrics") EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }

    @Bean
    public DataSourceErrorLogRepository dataSourceErrorLogRepository(@Qualifier("entityManagerFactoryMetrics") EntityManagerFactory entityManagerFactory) {
        return new JpaRepositoryFactory(entityManagerFactory.createEntityManager()).getRepository(DataSourceErrorLogRepository.class);
    }
}
