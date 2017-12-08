package com.mutil.transaction.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.mutil.transaction.config.DataSource;

/**
 * Created by WuTing on 2017/12/5.
 */
@Service
@DataSource("sakilaDataSource")
public class ActorServiceImpl implements ActorService {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public boolean saveActor() {
		jdbcTemplate.execute(
			"insert into actor(actor_id, first_name, last_name, last_update) values(999, 'wwww', 'baidu', '2017-12-05 "
				+ "00:00:00')");
		//		throw new RuntimeException();//return true;
		return true;
	}
}
