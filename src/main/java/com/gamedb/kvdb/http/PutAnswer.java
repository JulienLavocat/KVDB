package com.gamedb.kvdb.http;

public class PutAnswer {

	public String table;
	public String key;
	public String value;
	public boolean created;
	
	public PutAnswer(String table, String key, String value, boolean created) {
		this.table = table;
		this.key = key;
		this.value = value;
		this.created = created;
	}

	public PutAnswer() {
		
	}
	
}
