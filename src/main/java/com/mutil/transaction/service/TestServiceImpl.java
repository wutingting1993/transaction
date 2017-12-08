package com.mutil.transaction.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.PriorityOrdered;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mutil.transaction.config.DataSource;
import com.mutil.transaction.config.MultiTransactional;
import com.mutil.transaction.config.SpringContextUtil;
import com.mutil.transaction.model.Actor;
import com.mutil.transaction.model.Author;

/**
 * Created by WuTing on 2017/12/5.
 */
@Service
public class TestServiceImpl implements TestService, PriorityOrdered {
	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired
	private ActorService actorService;
	@Autowired
	private AuthorService authorService;

	//	@Autowired
	//	@Qualifier("transactionManager")
	//	private JtaTransactionManager transactionManager;

	@Transactional
	@DataSource("libraryDataSource")
	@Override
	public Boolean queryAuthorsFromHistoryDB() {
		List<Author> res = jdbcTemplate.query("select * from author", (rs, rowNum) -> {
			Author author = new Author();
			author.setId(rs.getString("id"));
			author.setFirst_name(rs.getString("first_name"));
			author.setLast_name(rs.getString("last_name"));
			return author;

		});
		System.out.println(res);
		return true;
	}

	@Transactional
	@DataSource("sakilaDataSource")
	@Override
	public Boolean queryActorsFromSakilaDB() {
		List<Actor> res = jdbcTemplate.query("select * from actor", (rs, rowNum) -> {
			Actor actor = new Actor();
			actor.setActor_id(rs.getInt("actor_id"));
			actor.setFirst_name(rs.getString("first_name"));
			actor.setLast_name(rs.getString("last_name"));
			return actor;

		});
		System.out.println(res);
		return true;
	}

	@MultiTransactional(rollbackFor = RuntimeException.class)
	@Override
	public Boolean saveDatas() {
		authorService.saveAuthor();
		//actorService.saveActor();
		//		UserTransaction tran = transactionManager.getUserTransaction();
		//		try {
		//			tran.begin();
		//			//		getService().saveAuthor();
		//			//		getService().saveActor();
		//			tran.commit();
		//		} catch (Exception e) {
		//			e.printStackTrace();
		//			try {
		//				tran.rollback();
		//			} catch (SystemException e1) {
		//				e1.printStackTrace();
		//			}
		//		}
		return false;
	}

	private TestService getService() {
		return SpringContextUtil.getBean(TestService.class);
	}

	@Override
	public int getOrder() {
		return -20;
	}

	@DataSource("libraryDataSource")
	@Override
	public Boolean saveAuthor() {
		jdbcTemplate.execute("insert into author(id, first_name, last_name) values(999, 'wwww', 'baidu')");

		return true;
	}

	@Override
	@DataSource("sakilaDataSource")
	public Boolean saveActor() {
		jdbcTemplate.execute("insert into actor(actor_id, first_name, last_name, last_update) "
			+ "values(999, 'wwww','baidu', '2017-12-05 00:00:00')");
		//		throw new RuntimeException();
		return true;
	}
}
