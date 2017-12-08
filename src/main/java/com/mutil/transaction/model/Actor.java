package com.mutil.transaction.model;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by WuTing on 2017/12/5.
 */
@Getter
@Setter
public class Actor {
	private Integer actor_id;
	private String first_name;
	private String last_name;
	private String last_update;

	@Override
	public String toString() {
		return actor_id + ":" + first_name + ":" + last_name;
	}
}
