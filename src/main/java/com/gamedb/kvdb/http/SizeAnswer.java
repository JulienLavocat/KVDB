package com.gamedb.kvdb.http;

public class SizeAnswer {

	public String table;
	public int size;

	public SizeAnswer(String table, int size) {
		this.table = table;
		this.size = size;
	}

}
