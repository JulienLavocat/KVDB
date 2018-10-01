package com.gamedb.kvdb.http;

public class ExistAnswer {

	public String table;
	public String key;
	public boolean exist;
	
	public ExistAnswer(String table, String key, boolean exist) {
		this.table = table;
		this.key = key;
		this.exist = exist;
	}
	
}
