package org.needcoke.coke.mp.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.core.config.GlobalConfig;
import com.baomidou.mybatisplus.core.incrementer.DefaultIdentifierGenerator;
import com.baomidou.mybatisplus.core.injector.DefaultSqlInjector;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.GlobalConfigUtils;
import com.baomidou.mybatisplus.core.toolkit.reflect.GenericTypeUtils;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.ibatis.builder.xml.XMLMapperBuilder;
import org.apache.ibatis.logging.slf4j.Slf4jImpl;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import pers.warren.ioc.annotation.Bean;
import pers.warren.ioc.annotation.Configuration;
import pers.warren.ioc.annotation.Value;
import pers.warren.ioc.condition.ConditionalOnMissingBean;

import javax.sql.DataSource;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

@Configuration
public class MybatisPlusConfig {

    @Value("coke.datasource.url")
    private String jdbcUri ;

    @Value("coke.datasource.driver-class-name")
    private String driverClassName;

    @Value("coke.datasource.username")
    private String username;

    @Value("coke.datasource.password")
    private String password;

    @Value("coke.datasource.timeout:10000")
    private long timeout;

    @Value("coke.datasource.autoCommit:true")
    private boolean autoCommit;

    @Value("coke.datasource.maxPoolSize:5")
    private int maxPoolSize;

    @Value("coke.datasource.maxLifeTime:1800000")
    private long maxLifeTime;

    @Value("coke.datasource.maxPageSize:500")
    private long maxPageSize;

    @Value("coke.datasource.classPath:mapper")
    private String classPath;

    @Value("coke.datasource.packageName")
    private List<String> packageNames;

    @Bean
    @ConditionalOnMissingBean(DataSource.class)
    public DataSource hikariDataSource(){
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

    @Bean
    public MybatisConfiguration mybatisConfiguration(Interceptor initInterceptor,DataSource dataSource) {
        MybatisConfiguration configuration = new MybatisConfiguration();
        initConfiguration(configuration);
        configuration.addInterceptor(initInterceptor);
        //配置日志实现
        configuration.setLogImpl(Slf4jImpl.class);
        GenericTypeUtils.setGenericTypeResolver(new MybatisPlusGenericTypeResolver());
        if (null != packageNames) {
            packageNames.forEach(configuration::addMappers);
        }
        //构建mybatis-plus需要的globalconfig
        GlobalConfig globalConfig = new GlobalConfig();
        //此参数会自动生成实现baseMapper的基础方法映射
        globalConfig.setSqlInjector(new DefaultSqlInjector());
        //设置id生成器
        globalConfig.setIdentifierGenerator(new DefaultIdentifierGenerator());
        //设置超类mapper
        globalConfig.setSuperMapperClass(BaseMapper.class);
        //给configuration注入GlobalConfig里面的配置
        GlobalConfigUtils.setGlobalConfig(configuration, globalConfig);
        //设置数据源
        Environment environment = new Environment("1", new JdbcTransactionFactory(), dataSource);
        configuration.setEnvironment(environment);
        //构建sqlSessionFactory
        return configuration;
    }

    /**
     * 初始化配置
     *
     * @param configuration
     */
    private void initConfiguration(MybatisConfiguration configuration) {
        //开启驼峰大小写转换
        configuration.setMapUnderscoreToCamelCase(true);
        //配置添加数据自动返回数据主键
        configuration.setUseGeneratedKeys(true);
    }

    /**
     * 初始化拦截器
     *
     * @return
     */

    @Bean
    public Interceptor initInterceptor() {
        //创建mybatis-plus插件对象
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        //构建分页插件
        PaginationInnerInterceptor paginationInnerInterceptor = new PaginationInnerInterceptor();

        DbType dbType = driverClassName.toLowerCase(Locale.ROOT).contains("mysql")?DbType.MYSQL:DbType.ORACLE;
        paginationInnerInterceptor.setDbType(dbType);
        paginationInnerInterceptor.setOverflow(true);
        paginationInnerInterceptor.setMaxLimit(maxPageSize);
        interceptor.addInnerInterceptor(paginationInnerInterceptor);
        return interceptor;
    }

    /**
     * 解析mapper.xml文件
     * @param configuration
     * @throws IOException
     */

    @Bean
    public SqlSessionFactory registryMapperXml(MybatisConfiguration configuration) throws IOException {
        SqlSessionFactoryBuilder builder = new SqlSessionFactoryBuilder();
        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
        Enumeration<URL> mapper = contextClassLoader.getResources(classPath);
        while (mapper.hasMoreElements()) {
            URL url = mapper.nextElement();
            if (url.getProtocol().equals("file")) {
                String path = url.getPath();
                File file = new File(path);
                File[] files = file.listFiles();
                for (File f : files) {
                    FileInputStream in = new FileInputStream(f);
                    XMLMapperBuilder xmlMapperBuilder = new XMLMapperBuilder(in, configuration, f.getPath(), configuration.getSqlFragments());
                    xmlMapperBuilder.parse();
                    in.close();
                }
            } else {
                JarURLConnection urlConnection = (JarURLConnection) url.openConnection();
                JarFile jarFile = urlConnection.getJarFile();
                Enumeration<JarEntry> entries = jarFile.entries();
                while (entries.hasMoreElements()) {
                    JarEntry jarEntry = entries.nextElement();
                    if (jarEntry.getName().endsWith(".xml")) {
                        InputStream in = jarFile.getInputStream(jarEntry);
                        XMLMapperBuilder xmlMapperBuilder = new XMLMapperBuilder(in, configuration, jarEntry.getName(), configuration.getSqlFragments());
                        xmlMapperBuilder.parse();
                        in.close();
                    }
                }
            }
        }

        SqlSessionFactory sqlSessionFactory = builder.build(configuration);
        return sqlSessionFactory;
    }

}
