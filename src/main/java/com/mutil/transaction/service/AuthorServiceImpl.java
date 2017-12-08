package com.mutil.transaction.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.mutil.transaction.config.DataSource;

/**
 * Created by WuTing on 2017/12/5.
 */
@Service
@DataSource("libraryDataSource")
public class AuthorServiceImpl implements AuthorService {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public boolean saveAuthor() {
		jdbcTemplate.execute("insert into author(id, first_name, last_name) values(999, 'wwww', 'baidu')");
		return true;
	}
}
