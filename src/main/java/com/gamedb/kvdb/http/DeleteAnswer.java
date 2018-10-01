package com.gamedb.kvdb.http;

public class DeleteAnswer {

	public String table;
	public String key;
	public boolean deleted;
	
	public DeleteAnswer(String table, String key, boolean deleted) {
		this.table = table;
		this.key = key;
		this.deleted = deleted;
	}
	
}
