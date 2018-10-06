package com.gamedb.kvdb;

import io.sentry.Sentry;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.UUID;

import com.gamedb.kvdb.http.ClearAnswer;
import com.gamedb.kvdb.http.DeleteAnswer;
import com.gamedb.kvdb.http.ExistAnswer;
import com.gamedb.kvdb.http.GetAnswer;
import com.gamedb.kvdb.http.PutAnswer;
import com.gamedb.kvdb.http.QueryAnswer;
import com.gamedb.kvdb.http.ShowAnswer;
import com.gamedb.kvdb.http.SizeAnswer;
import com.gamedb.kvdb.reporting.PutOperationReport;
import com.google.gson.Gson;

public class Report {

	public static Gson gson = new Gson();

	public static void start() {

		Sentry.init("https://4582e2643292402ea595c57b8ee55901@sentry.io/1291761");

	}

	public static void report(Exception e) {
		Sentry.capture(e);
	}

	public static void reportPutOperation(UUID id, PutAnswer a) {

		try {
			Files.write(Paths.get("databases", id.toString()+".log"),
					("{\"ts\":" + System.currentTimeMillis() + ","
							+ "\"table\":\""+ a.table + "\","
							+ "\"type\":\"PUT\","
							+ "\"key\":\"" + a.key + "\","
							+ "\"value\":\"" + a.value + "\","
							+ "\"created\":" + a.created + "}" + "\n")
					.getBytes(), StandardOpenOption.APPEND);
		} catch (IOException e) {
			report(e);
		}

	}

	public static void reportGetOperation(UUID id, GetAnswer a) {
		
		try {
			Files.write(Paths.get("databases", id.toString()+".log"),
					("{\"ts\":" + System.currentTimeMillis() + ","
							+ "\"table\":\""+ a.table + "\","
							+ "\"type\":\"GET\","
							+ "\"value\":\"" + a.value + "\"}" + "\n")
					.getBytes(), StandardOpenOption.APPEND);
		} catch (IOException e) {
			report(e);
		}

	}

	public static void reportClearOperation(UUID id, ClearAnswer a) {
		try {
			Files.write(Paths.get("databases", id.toString()+".log"),
					("{\"ts\":" + System.currentTimeMillis() + ","
							+ "\"table\":\""+ a.table + "\","
							+ "\"type\":\"CLEAR\","
							+ "\"entryLeft\":\"" + a.entryLeft + "\","
							+ "\"entryRemoved\":\"" + a.entryRemoved + "\"}" + "\n")
					.getBytes(), StandardOpenOption.APPEND);
		} catch (IOException e) {
			report(e);
		}
	}
	
	public static void reportDeleteOperation(UUID id, DeleteAnswer a) {

		try {
			Files.write(Paths.get("databases", id.toString()+".log"),
					("{\"ts\":" + System.currentTimeMillis() + ","
							+ "\"table\":\""+ a.table + "\","
							+ "\"type\":\"DELETE\","
							+ "\"key\":\"" + a.key + "\","
							+ "\"deleted\":\"" + a.deleted + "\"}" + "\n")
					.getBytes(), StandardOpenOption.APPEND);
		} catch (IOException e) {
			report(e);
		}

	}
	
	public static void reportSizeOperation(UUID id, SizeAnswer a) {

		try {
			Files.write(Paths.get("databases", id.toString()+".log"),
					("{\"ts\":" + System.currentTimeMillis() + ","
							+ "\"table\":\""+ a.table + "\","
							+ "\"type\":\"SIZE\","
							+ "\"size\":\"" + a.size + "\"}" + "\n")
					.getBytes(), StandardOpenOption.APPEND);
		} catch (IOException e) {
			report(e);
		}

	}
	
	public static void reportExistOperation(UUID id, ExistAnswer a) {

		try {
			Files.write(Paths.get("databases", id.toString()+".log"),
					("{\"ts\":" + System.currentTimeMillis() + ","
							+ "\"table\":\""+ a.table + "\","
							+ "\"type\":\"EXIST\","
							+ "\"key\":\"" + a.key + "\","
							+ "\"exist\":\"" + a.exist + "\"}" + "\n")
					.getBytes(), StandardOpenOption.APPEND);
		} catch (IOException e) {
			report(e);
		}

	}
	
	public static void reportShowOperation(UUID id, ShowAnswer a) {

		try {
			Files.write(Paths.get("databases", id.toString()+".log"),
					("{\"ts\":" + System.currentTimeMillis() + ","
							+ "\"table\":\""+ a.table + "\","
							+ "\"type\":\"SHOW\"}" + "\n")
					.getBytes(), StandardOpenOption.APPEND);
		} catch (IOException e) {
			report(e);
		}

	}
	
	public static void reportQueryOperation(UUID id, QueryAnswer a) {
		
		try {
			Files.write(Paths.get("databases", id.toString()+".log"),
					("{\"ts\":" + System.currentTimeMillis() + ","
							+ "\"table\":\""+ a.table + "\","
							+ "\"type\":\"QUERY\","
							+ "\"query\":\"" + a.query + "\","
							+ "\"done\":\"" + a.done + "\","
							+ "\"resultCount\":\"" + a.resultCount + "\"}" + "\n")
					.getBytes(), StandardOpenOption.APPEND);
		} catch (IOException e) {
			report(e);
		}
		
	}
	
}
