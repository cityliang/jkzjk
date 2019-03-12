package com.huntto.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * Description: 类描述 <br/>
 * Copyright: 版权归浙江创得信息技术有限公司所有<br/>
 * Project_name jkzjk<br/>
 * Package_name com.huntto.config<br/>
 * Author 梁城市<br/>
 * Email city_wangyi@163.com<br/>
 * Create_time 2018/12/9 15:54<br/>
 */
@Configuration
@Profile({ "dev", "prod" })
public class DataSourceConfig {
//    @Autowired
//    private Environment env;
//
//    @Bean
//    public DataSource getDataSource() {
//        DataSource dataSource = new DataSource();
//        dataSource.setUrl(env.getProperty("spring.datasource.url"));
//        dataSource.setUsername(env.getProperty("spring.datasource.username"));
//        dataSource.setPassword(env.getProperty("spring.datasource.password"));
//        dataSource.setDriverClassName(env.getProperty("spring.datasource.driverClassName"));
//        return dataSource;
//    }

	private Logger logger = LoggerFactory.getLogger(DataSourceConfig.class);

	@Value("${spring.datasource.driverClassName}")
	String driverClassName;

	@Value("${spring.datasource.url}")
	String url;

	@Value("${spring.datasource.username}")
	String username;

	@Value("${spring.datasource.password}")
	String password;

	@Bean(initMethod = "init", destroyMethod = "close", name = "dataSource")
	public DruidDataSource ruidDataSourceBean() {
		logger.info("-------------------init alibaba datasource config-------------------------");

		DruidDataSource druidDataSource = new DruidDataSource();

		druidDataSource.setDriverClassName(driverClassName);
		druidDataSource.setUrl(url);
		druidDataSource.setUsername(username);
		druidDataSource.setPassword(password);

		logger.info("-------------------init alibaba datasource config done-------------------------");

		return druidDataSource;
	}
}
