package com.mutil.transaction.service;

/**
 * Created by WuTing on 2017/12/5.
 */
public interface TestService {
	Boolean saveDatas();

	Boolean queryAuthorsFromHistoryDB();

	Boolean queryActorsFromSakilaDB();

	Boolean saveAuthor();

	Boolean saveActor();
}
