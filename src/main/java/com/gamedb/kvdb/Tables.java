package com.gamedb.kvdb;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.mapdb.HTreeMap;
import org.mapdb.Serializer;

import com.gamedb.kvdb.exceptions.DatabaseNotFoundException;
import com.gamedb.kvdb.http.ClearAnswer;
import com.gamedb.kvdb.http.DeleteAnswer;
import com.gamedb.kvdb.http.ExistAnswer;
import com.gamedb.kvdb.http.GetAnswer;
import com.gamedb.kvdb.http.GetMapAnswer;
import com.gamedb.kvdb.http.PutAnswer;
import com.gamedb.kvdb.http.SizeAnswer;

public class Tables {

	public static DB db;
	
	public static void start() {
		Path p = Paths.get("databases/tables.db");
		File f = p.toAbsolutePath().toFile();
		
		db = DBMaker.fileDB(f)
				.closeOnJvmShutdown()
				.transactionEnable()
				.make();
	}
	
	public static GetMapAnswer getMap(String user, String table) {
		try {
			return new GetMapAnswer(table, Files.getDB(UUID.fromString(user)).hashMap(table, Serializer.STRING, Serializer.STRING).createOrOpen());
		} catch (DatabaseNotFoundException e) {
			return new GetMapAnswer();
		}
	}
	
	public static ExistAnswer exist(String user, String table, String key) {
		try {
			return new ExistAnswer(table, key, Files.getDB(UUID.fromString(user)).hashMap(table, Serializer.STRING, Serializer.STRING).createOrOpen().containsKey(key));
		} catch (DatabaseNotFoundException e) {
			return new ExistAnswer();
		}
	}
	
	public static PutAnswer put(String user, String table, String key, String value) {
		try {
			return new PutAnswer(table, key, value, Files.getDB(UUID.fromString(user)).hashMap(table, Serializer.STRING, Serializer.STRING).createOrOpen().put(key, value) == null);
		} catch (DatabaseNotFoundException e) {
			return new PutAnswer();
		} 
	}

	public static GetAnswer get(String user, String table, String key) {
		try {
			return new GetAnswer(table, Files.getDB(UUID.fromString(user)).hashMap(table, Serializer.STRING, Serializer.STRING).createOrOpen().get(key));
		} catch (DatabaseNotFoundException e) {
			return new GetAnswer();
		}
	}
	
	public static ClearAnswer clear(String user, String table) {
		try {
			HTreeMap<String, String> map = Files.getDB(UUID.fromString(user)).hashMap(table, Serializer.STRING, Serializer.STRING).createOrOpen();
			int before = map.size();
			map.clear();
			return new ClearAnswer(table, before - map.size(), map.size());
		} catch (DatabaseNotFoundException e) {
			return new ClearAnswer();
		}
	}
	
	public static SizeAnswer size(String user, String table) {
		try {
			return new SizeAnswer(table, Files.getDB(UUID.fromString(user)).hashMap(table, Serializer.STRING, Serializer.STRING).createOrOpen().size());
		} catch (DatabaseNotFoundException e) {
			return new SizeAnswer();
		}
	}
	
	public static DeleteAnswer delete(String user, String table, String key) {
		try {
			return new DeleteAnswer(table, key, Files.getDB(UUID.fromString(user)).hashMap(table, Serializer.STRING, Serializer.STRING).createOrOpen().remove(key) != null);
		} catch (DatabaseNotFoundException e) {
			return new DeleteAnswer();
		}
	}
	
}
