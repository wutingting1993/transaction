package com.mutil.transaction.config;

import javax.sql.DataSource;
import javax.transaction.SystemException;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.jta.JtaTransactionManager;

import com.atomikos.icatch.jta.UserTransactionImp;
import com.atomikos.icatch.jta.UserTransactionManager;
import com.atomikos.jdbc.AtomikosDataSourceBean;

/**
 * Created by WuTing on 2017/12/5.
 */
//@Configuration
@PropertySource("classpath:atomikos.properties")
public class DBConfig {

	@Primary
	@Bean("libraryDataSource")
	@ConfigurationProperties(prefix = "spring.datasource.library")
	public DataSource sakilaDataSource() {
		return DataSourceBuilder.create().type(AtomikosDataSourceBean.class).build();
	}

	@Bean("sakilaDataSource")
	@ConfigurationProperties(prefix = "spring.datasource.sakila")
	public DataSource libraryDataSource() {
		return DataSourceBuilder.create().type(AtomikosDataSourceBean.class).build();
	}

	@Bean("dataSource")
	public DynamicDataSource dataSource() {

		return new DynamicDataSource("libraryDataSource");
	}

	@Bean("jdbcTemplate")
	public JdbcTemplate jdbcTemplate(@Qualifier("dataSource") DynamicDataSource dataSource) {

		return new JdbcTemplate(dataSource);
	}

	@Bean(name = "atomikosTransactionManager", initMethod = "init", destroyMethod = "close")
	public UserTransactionManager atomikosTransactionManager() {
		UserTransactionManager atomikosTransactionManager = new UserTransactionManager();
		atomikosTransactionManager.setForceShutdown(true);
		return atomikosTransactionManager;
	}

	@Bean(name = "atomikosUserTransaction")
	public UserTransactionImp atomikosUserTransaction() {
		UserTransactionImp atomikosUserTransaction = new UserTransactionImp();
		try {
			atomikosUserTransaction.setTransactionTimeout(300);
		} catch (SystemException e) {
			e.printStackTrace();
		}
		return atomikosUserTransaction;
	}

	@Bean(name = "transactionManager")
	public JtaTransactionManager transactionManager(UserTransactionManager atomikosTransactionManager,
		UserTransactionImp atomikosUserTransaction) {
		JtaTransactionManager transactionManager = new JtaTransactionManager();
		transactionManager.setTransactionManager(atomikosTransactionManager);
		transactionManager.setUserTransaction(atomikosUserTransaction);
		transactionManager.setAllowCustomIsolationLevels(true);
		return transactionManager;
	}
}
