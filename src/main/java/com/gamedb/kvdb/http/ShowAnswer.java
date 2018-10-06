package com.gamedb.kvdb.http;

import org.mapdb.HTreeMap;

public class ShowAnswer {

	public String table;
	public HTreeMap<String, String> content;
	
	public ShowAnswer(String table, HTreeMap<String, String> content) {
		this.table = table;
		this.content = content;
	}

	public ShowAnswer() {
		
	}
	
}
