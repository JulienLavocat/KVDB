package com.gamedb.kvdb;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import org.mapdb.DB;
import org.mapdb.DBMaker;

import com.gamedb.kvdb.exceptions.DatabaseNotFoundException;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class Files {

	public static HashMap<UUID, DB> dbs = new HashMap<UUID, DB>();
	public static ArrayList<String> users = new ArrayList<String>();
	
	public static void start() {
		
		Gson gson = new Gson();
		
		try {
			String users = new String(java.nio.file.Files.readAllBytes(Paths.get("users.json")));
			
			ArrayList<String> ids = gson.fromJson(users, new TypeToken<ArrayList<String>>(){}.getType());
			
			for(String id : ids) {
				
				Path p = Paths.get("databases/" + id + ".db");
				File f = p.toAbsolutePath().toFile();
				
				DB db = DBMaker.fileDB(f)
						.closeOnJvmShutdown()
						.transactionEnable()
						.make();
				dbs.put(UUID.fromString(id), db);
				Files.users.add(id);
				
				p = Paths.get("databases/" + id + ".log");
				f = p.toAbsolutePath().toFile();
				if(!f.exists())
					f.createNewFile();
				
			}
			
		} catch (IOException e) {
			Report.report(e);
		}
		
	}
	
	public static DB getDB(UUID id) throws DatabaseNotFoundException{
		DB db = dbs.get(id);
		
		if(db == null)
			throw new DatabaseNotFoundException();
		
		return db;
		
	}

	public static void commit() {
		

		
	}
	
}
