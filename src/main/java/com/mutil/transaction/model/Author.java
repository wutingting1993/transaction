package com.mutil.transaction.model;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by WuTing on 2017/12/5.
 */
@Getter
@Setter
public class Author {
	private String id;
	private String first_name;
	private String last_name;

	@Override
	public String toString() {
		return id + ":" + first_name + ":" + last_name;
	}
}
