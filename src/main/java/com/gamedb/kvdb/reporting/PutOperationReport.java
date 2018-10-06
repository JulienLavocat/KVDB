package com.gamedb.kvdb.reporting;

public class PutOperationReport {

	public long ts;
	public String table;
	public String type = "put";
	public String key;
	public String value;
	public boolean created;
	
	public PutOperationReport(String table, String key, String value, boolean created) {
		
		ts = System.currentTimeMillis();
		this.table = table;
		this.key = key;
		this.value = value;
		this.created = created;
		
	}

	@Override
	public String toString() {
		return "{\"ts\":" + System.currentTimeMillis() + ","
				+ "\"table\":\""+ table + "\","
				+ "\"type\":\"put\","
				+ "\"key\":\"" + key + "\","
				+ "\"value\":\"" + value + "\","
				+ "\"created\":" + created + "}" + "\n";
	}
	
}
