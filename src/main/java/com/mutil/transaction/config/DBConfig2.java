package com.mutil.transaction.config;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

/**
 * Created by WuTing on 2017/12/5.
 */
@Configuration
@PropertySource("classpath:db.properties")
@Import(DataSourceRegister.class)
public class DBConfig2 {

	@Bean("jdbcTemplate")
	public JdbcTemplate jdbcTemplate(DynamicDataSource dataSource) {

		return new JdbcTemplate(dataSource);
	}

	@Bean("jdbcReadTemplate")
	public JdbcTemplate jdbcReadTemplate(DynamicDataSource dataSource) {
		return new JdbcTemplate(dataSource);
	}

	@Bean("transactionManager")
	public DataSourceTransactionManager transactionManager(DataSource dataSource) {
		return new DataSourceTransactionManager(dataSource);
	}
}
