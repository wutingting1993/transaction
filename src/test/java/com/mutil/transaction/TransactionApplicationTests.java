package com.mutil.transaction;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.mutil.transaction.service.ActorService;
import com.mutil.transaction.service.AuthorService;
import com.mutil.transaction.service.TestService;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class TransactionApplicationTests {

	@Autowired
	private TestService testService;

	@Autowired
	private ActorService actorService;

	@Autowired
	private AuthorService authorService;

	@Test
	public void queryAuthorsFromHistoryDB() {
		testService.queryAuthorsFromHistoryDB();
	}

	@Test
	public void queryActorsFromSakilaDB() {
		testService.queryActorsFromSakilaDB();
	}

	@Test
	public void saveAuthor() {
		//testService.saveAuthor();
		testService.queryAuthorsFromHistoryDB();
	}

	@Test
	public void saveActor() {
		//testService.saveActor();
		testService.queryActorsFromSakilaDB();
	}

	@Test
	public void queryAll() {
		testService.queryActorsFromSakilaDB();
		testService.queryAuthorsFromHistoryDB();
	}
	@Test
	public void saveDatas() {
		testService.saveDatas();
	}

	@Test
	public void saveAuthor2() {
		authorService.saveAuthor();
	}
}
