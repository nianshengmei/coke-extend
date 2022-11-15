package org.needcoke.coke.mp.config;

import com.zaxxer.hikari.HikariDataSource;
import pers.warren.ioc.annotation.Bean;
import pers.warren.ioc.annotation.Configuration;
import pers.warren.ioc.condition.ConditionalOnMissingBean;
import pers.warren.ioc.core.Environment;

import javax.sql.DataSource;

@Configuration
public class MybatisPlusConfig implements Environment {

    private String jdbcUri = getProperty("coke.datasource.url",null);

    private String driverClassName = getProperty("coke.datasource.driver",null);

    private String username = getProperty("coke.datasource.username",null);

    private String password = getProperty("coke.datasource.password",null);

    private long timeout = getProperty("coke.datasource.timeout",10000);

    private boolean autoCommit = getProperty("coke.datasource.autoCommit",true);

    private int maxPoolSize = getProperty("coke.datasource.maxPoolSize",5);

    private long maxLifeTime = getProperty("coke.datasource.maxLifeTime",1);

    @Bean
    @ConditionalOnMissingBean(DataSource.class)
    public HikariDataSource hikariDataSource(){
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(jdbcUri);
        dataSource.setDriverClassName(driverClassName);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        dataSource.setIdleTimeout(timeout);
        dataSource.setAutoCommit(autoCommit);
        dataSource.setMaximumPoolSize(maxPoolSize);
        dataSource.setMinimumIdle(1);
        dataSource.setMaxLifetime(maxLifeTime);
        return dataSource;
    }
}
