package com.gamedb.kvdb.http;

import org.mapdb.HTreeMap;

public class GetMapAnswer {

	public String table;
	public HTreeMap<String, String> content;
	
	public GetMapAnswer(String table, HTreeMap<String, String> content) {
		this.table = table;
		this.content = content;
	}

	public GetMapAnswer() {
		
	}
	
}
