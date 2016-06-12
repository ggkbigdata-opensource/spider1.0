package com.ggk.spider.datasource;

import java.beans.PropertyVetoException;

import javax.annotation.PreDestroy;

import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.tomcat.jdbc.pool.DataSource;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@Configuration
@EnableTransactionManagement
@EnableConfigurationProperties(DataSourceProperties.class)
@MapperScan("com.ggk.spider.mapper")
public class DataSourceConfig {

    private static final String MYBATIS_CONFIG_PATH = "classpath:mybatis-config.xml";

    @Autowired
    private DataSourceProperties dataSourceProperties;

    private DataSource dataSource;

    @Bean(destroyMethod = "close")
    public DataSource dataSource() {
        dataSource = new DataSource();
        dataSource.setDriverClassName(dataSourceProperties.getDriverClassName());
        dataSource.setUrl(dataSourceProperties.getUrl());
        if (dataSourceProperties.getUsername() != null) {
            dataSource.setUsername(dataSourceProperties.getUsername());
        }
        if (dataSourceProperties.getPassword() != null) {
            dataSource.setPassword(dataSourceProperties.getPassword());
        }
        dataSource.setInitialSize(dataSourceProperties.getInitialSize());
        dataSource.setMaxActive(dataSourceProperties.getMaxActive());
        dataSource.setMaxIdle(dataSourceProperties.getMaxIdle());
        dataSource.setMinIdle(dataSourceProperties.getMinIdle());
        dataSource.setTestOnBorrow(dataSourceProperties.isTestOnBorrow());
        dataSource.setTestOnReturn(dataSourceProperties.isTestOnReturn());
        dataSource.setTestOnConnect(dataSourceProperties.isTestOnConnect());
        dataSource.setTestWhileIdle(dataSourceProperties.isTestWhileIdle());
        dataSource.setLogValidationErrors(dataSourceProperties.isLogValidationErrors());
        dataSource.setValidationQuery(dataSourceProperties.getValidationQuery());
        dataSource.setTimeBetweenEvictionRunsMillis(dataSourceProperties.getTimeBetweenEvictionRunsMillis());
        return dataSource;
    }

    @Bean
    public SqlSessionFactory sqlSessionFactoryBean() throws Exception { //NOSONAR

        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource());
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        sqlSessionFactoryBean.setTypeAliasesPackage("com.ggk.spider.model");
        sqlSessionFactoryBean.setConfigLocation(resolver.getResource(MYBATIS_CONFIG_PATH));
        return sqlSessionFactoryBean.getObject();
    }

    @Bean
    public PlatformTransactionManager transactionManager() throws PropertyVetoException {
        return new DataSourceTransactionManager(dataSource());
    }

    @Bean
    public JdbcTemplate jdbcTemplate() throws PropertyVetoException {
        return new JdbcTemplate(dataSource());
    }
    
    @PreDestroy
    public void close() {
        if (dataSource != null) {
            dataSource.close();
            dataSource = null;
        }
    }
}
