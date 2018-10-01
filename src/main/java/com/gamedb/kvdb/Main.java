package com.gamedb.kvdb;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {

	public static void main(String[] args) throws Exception {

		Tables.start();
		
		Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(new Runnable() {

			@Override
			public void run() {
				
				synchronized (Tables.db) {
					Tables.db.commit();
				}
				
			}
			
		}, 2, 2, TimeUnit.SECONDS);
		
		new Router();

	}

}
