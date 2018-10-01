package com.gamedb.kvdb.http;

public class ClearAnswer {

	public String table;
	public int entryRemoved;
	public int entryLeft;
	
	public ClearAnswer(String table, int entryRemoved, int entryLeft) {
		this.table = table;
		this.entryRemoved = entryRemoved;
		this.entryLeft = entryLeft;
	}

	public ClearAnswer() {
		
	}
}
