package com.gamedb.kvdb;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.mapdb.HTreeMap;
import org.mapdb.Serializer;

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
	
	public static GetMapAnswer getMap(String table) {
		return new GetMapAnswer(table, db.hashMap(table, Serializer.STRING, Serializer.STRING).createOrOpen());
	}
	
	public static ExistAnswer exist(String table, String key) {
		return new ExistAnswer(table, key, db.hashMap(table, Serializer.STRING, Serializer.STRING).createOrOpen().containsKey(key));
	}
	
	public static PutAnswer put(String table, String key, String value) {
		return new PutAnswer(table, key, value, db.hashMap(table, Serializer.STRING, Serializer.STRING).createOrOpen().put(key, value) == null); 
	}

	public static GetAnswer get(String table, String key) {
		return new GetAnswer(table, db.hashMap(table, Serializer.STRING, Serializer.STRING).createOrOpen().get(key));
	}
	
	public static ClearAnswer clear(String table) {
		HTreeMap<String, String> map = db.hashMap(table, Serializer.STRING, Serializer.STRING).createOrOpen();
		int before = map.size();
		map.clear();
		return new ClearAnswer(table, before - map.size(), map.size());
	}
	
	public static SizeAnswer size(String table) {
		return new SizeAnswer(table, db.hashMap(table, Serializer.STRING, Serializer.STRING).createOrOpen().size());
	}
	
	public static DeleteAnswer delete(String table, String key) {
		return new DeleteAnswer(table, key, db.hashMap(table, Serializer.STRING, Serializer.STRING).createOrOpen().remove(key) != null);
	}
	
}
